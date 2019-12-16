package msadaka.controllers;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import msadaka.dto.PasswordDto;
import msadaka.error.InvalidOldPasswordException;
import msadaka.models.PasswordResetToken;
import msadaka.models.User;
import msadaka.repositories.EmployeeRepository;
import msadaka.repositories.PasswordResetTokenRepository;
import msadaka.services.ISecurityUserService;
import msadaka.services.UserService;
import msadaka.utils.GenericResponse;
import msadaka.utils.WebClient;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import java.text.NumberFormat;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@Controller
public class LoginController  extends HandlerInterceptorAdapter {

    @Autowired
    private UserService userService;


    @Value("${footer}")
    private String footer;

    @Value("${quantum.api.url}")
    private String quantumurl;

    @Value("${config.default.cur1}")
    private String cur1;

    @Value("${config.default.cur2}")
    private String cur2;

    @Value("${support.email}")
    private String supportmail;

    @Autowired
    private JavaMailSender mailSender;
    @Autowired
    private ISecurityUserService securityUserService;

    @Autowired
    private MessageSource messages;

    @Autowired
    private AuthenticationManager authenticationManager;

    private static final Logger logger = Logger.getLogger( LoginController.class.getName() );

    @RequestMapping(value="/login", method = RequestMethod.GET)
    public ModelAndView login(){
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("pages-login");
        return modelAndView;
    }

    @RequestMapping(value="/emailError", method = RequestMethod.GET)
    public ModelAndView emailError(){
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("emailError");
        return modelAndView;
    }




    @RequestMapping(value="/registration", method = RequestMethod.GET)
    public ModelAndView registration(){
        ModelAndView modelAndView = new ModelAndView();
        User user = new User();
        modelAndView.addObject("user", user);
        modelAndView.setViewName("pages-register");
        return modelAndView;
    }
    @RequestMapping(value="/updatePassword", method = RequestMethod.GET)
    public ModelAndView updatePassword(){
        ModelAndView modelAndView = new ModelAndView();

        modelAndView.setViewName("updatePassword");
        return modelAndView;
    }



    @RequestMapping(value="/pages-recoverpw", method = RequestMethod.GET)
    public ModelAndView pagesrecoverpw() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("pages-recoverpw");
        return modelAndView;
    }

    // Reset password
    @RequestMapping(value = "/user/resetPassword", method = RequestMethod.POST)
    @ResponseBody
    public GenericResponse resetPassword(final HttpServletRequest request, @RequestParam("email") final String userEmail) {
        final User user = userService.findUserByEmail(userEmail);
        logger.log(Level.INFO,"user >> : "+user.toString());
        if (user != null) {
            try {
                logger.info("user Found!");
                final String token = UUID.randomUUID().toString();
                userService.createPasswordResetTokenForUser(user, token);
                mailSender.send(constructResetTokenEmail(getAppUrl(request), request.getLocale(), token, user));
            }
            catch(Exception e)
            {
                logger.log(Level.SEVERE,"ERROR: "+e.getMessage());

                e.printStackTrace();
            }
        }
        return new GenericResponse(messages.getMessage("message.resetPasswordEmail", null, request.getLocale()));
    }
    @RequestMapping(value = "/user/changePassword", method = RequestMethod.GET)
    public String showChangePasswordPage(final Locale locale, final Model model, @RequestParam("id") final long id, @RequestParam("token") final String token) {
        final String result = securityUserService.validatePasswordResetToken(id, token);
        if (result != null) {
            model.addAttribute("message", messages.getMessage("auth.message." + result, null, locale));
            return "redirect:/login?lang=" + locale.getLanguage();
        }
        return "redirect:/updatePassword?lang=" + locale.getLanguage();
    }

    @RequestMapping(value = "/registration", method = RequestMethod.POST)
    public ModelAndView createNewUser(@Valid User user, BindingResult bindingResult) {
        ModelAndView modelAndView = new ModelAndView();
        User userExists = userService.findUserByEmail(user.getUsername());
        user.setActive(1);
        String name=user.getSurName()+", "+user.getFirstName()+" "+user.getLastName();
        user.setName(name);
        if (userExists != null) {
            bindingResult
                    .rejectValue("username", "error.user",
                            "There is already a user registered with the username provided");
        }
        if (bindingResult.hasErrors()) {
            modelAndView.setViewName("registration");
        } else {
            userService.saveUser(user);
            modelAndView.addObject("successMessage", "User has been registered successfully");
            modelAndView.addObject("user", new User());
            modelAndView.setViewName("pages-register");

        }
        return modelAndView;
    }

    @RequestMapping(value={"/", "/home"}, method = RequestMethod.GET)
    public ModelAndView index(){
        ModelAndView modelAndView = new ModelAndView();
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        JSONObject data=new JSONObject();

       try{
         // System.out.println(SecurityContextHolder.getContext().getAuthentication().getPrincipal());
           User user = userService.findUserByUsername(auth.getName());
          System.out.println("User Email>> "+ user.getEmail());
          data.put("userEmail",user.getEmail());
           modelAndView.addObject("userName",user.getFirstName());
        modelAndView.addObject("adminMessage","Content Available Only for Users with Admin Role");}
        catch(Exception e)
        {e.printStackTrace();}


        System.out.println("data.toString() >> "+data.toString());

       //Call APIS
        WebClient client=new WebClient();
        String todays_sales="0.0";
        String todays_txns="0";
        String monthly_sales="0.0";
        String forever_sales="0.0";



        try{
            todays_txns= client.webRequest(quantumurl+"/payments/getTodaysCountForEmail",data.toString(),"POST","");
            if(todays_txns.equalsIgnoreCase("null")) todays_txns="0";

        }
        catch(Exception e)
        {e.printStackTrace();}

        //Todays Sales for Email
        try{
           todays_sales=client.webRequest(quantumurl+"/payments/getTodaysTotalForEmail",data.toString(),"POST","");
           System.out.println("todays_sales >> "+todays_sales);
            if(todays_sales.equalsIgnoreCase("null")) todays_sales="0.0";
        }
        catch(Exception e)
        {e.printStackTrace();}

        //Monthly Sales for Email
        try{
            monthly_sales=client.webRequest(quantumurl+"/payments/getThisMonthTotalForEmail",data.toString(),"POST","");
            System.out.println("monthly_sales >> "+monthly_sales);
            if(monthly_sales.equalsIgnoreCase("null")) monthly_sales="0.0";
        }
        catch(Exception e)
        {e.printStackTrace();}

        //Forever Sales for Email
        try{
            forever_sales=client.webRequest(quantumurl+"/payments/getForeverTotalForEmail",data.toString(),"POST","");
            System.out.println("forever_sales >> "+forever_sales);
            if(forever_sales.equalsIgnoreCase("null")) forever_sales="0.0";
        }
        catch(Exception e)
        {e.printStackTrace();}



      /*  try{


                Locale kes = new Locale(cur1, cur2);
                NumberFormat kenyanFormat = NumberFormat.getCurrencyInstance(kes);
                Double num=Double.parseDouble(todays_sales);
                todays_sales=kenyanFormat.format(num);

            Double num1=Double.parseDouble(forever_sales);
            forever_sales=kenyanFormat.format(num1);

            Double num2=Double.parseDouble(monthly_sales);
            monthly_sales=kenyanFormat.format(num2);

               // System.out.println("Kenyan: " + kenyanFormat.format(num));


        }
catch (Exception e)
{e.printStackTrace();}
*/

    //    modelAndView.addObject("totalemps",""+employeeDao.count());

        modelAndView.addObject("todays_sales",todays_sales);
        modelAndView.addObject("todays_txns",todays_txns);
        modelAndView.addObject("monthly_sales",monthly_sales);
        modelAndView.addObject("forever_sales",forever_sales);
        modelAndView.addObject("footer",footer);

        modelAndView.setViewName("index");
        return modelAndView;
    }

    private SimpleMailMessage constructResetTokenEmail(
            String contextPath, Locale locale, String token, User user) {
        String url = contextPath + "/user/changePassword?id=" +
                user.getId() + "&token=" + token;
        String message = messages.getMessage("message.resetPassword",
                null, locale);
        return constructEmail("Reset Password", message + " \r\n" + url, user);
    }

    @RequestMapping(value = "/user/savePassword", method = RequestMethod.POST)
    @ResponseBody
    public GenericResponse savePassword(Locale locale,
                                        @Valid  @RequestBody PasswordDto passwordDto) {
       // logger.log(Level.SEVERE, "passwordDto> "+passwordDto.getNewPassword());
//try {
    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    logger.info("auth.getPrincipal() >> " + auth.getPrincipal().toString());
    User user =
            (User) auth.getPrincipal();

    userService.changeUserPassword(user, passwordDto.getNewPassword());
    logger.log(Level.INFO, new GenericResponse(
            messages.getMessage("message.resetPasswordSuc", null, locale)).toString());
    return new GenericResponse(
            messages.getMessage("message.resetPasswordSuc", null, locale));
/*}
catch(Exception e) {
    logger.log(Level.SEVERE,new GenericResponse(
            messages.getMessage(e.getMessage(), null, locale)).getMessage());

    e.printStackTrace();
    logger.log(Level.INFO, new GenericResponse(
            messages.getMessage(e.getMessage(), null, locale)).toString());
    return new GenericResponse(
            messages.getMessage(e.getMessage(), null, locale));
}*/

    }

    // change user password
    @RequestMapping(value = "/user/updatePassword", method = RequestMethod.POST)
    @ResponseBody
    public GenericResponse changeUserPassword(final Locale locale, @Valid @RequestBody PasswordDto passwordDto) {
        final User user = userService.findUserByEmail(((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getEmail());
        if (!userService.checkIfValidOldPassword(user, passwordDto.getOldPassword())) {
           throw new InvalidOldPasswordException();
        }
        userService.changeUserPassword(user, passwordDto.getNewPassword());
        return new GenericResponse(messages.getMessage("message.updatePasswordSuc", null, locale));
    }
/*
    @RequestMapping(value = "/user/update/2fa", method = RequestMethod.POST)
    @ResponseBody
    public GenericResponse modifyUser2FA(@RequestParam("use2FA") final boolean use2FA) throws UnsupportedEncodingException {
        final User user = userService.updateUser2FA(use2FA);
        if (use2FA) {
            return new GenericResponse(userService.generateQRUrl(user));
        }
        return null;
    }

*/


    private SimpleMailMessage constructEmail(String subject, String body,
                                             User user) {
        SimpleMailMessage email = new SimpleMailMessage();
        email.setSubject(subject);
        email.setText(body);
        email.setTo(user.getEmail());
        email.setFrom(supportmail);
        return email;
    }

    public String validatePasswordResetToken(long id, String token) {
        PasswordResetToken passToken =
                passwordResetTokenRepository.findByToken(token);
        if ((passToken == null) || (passToken.getUser()
                .getId() != id)) {
            return "invalidToken";
        }

        Calendar cal = Calendar.getInstance();
        if ((passToken.getExpiryDate()
                .getTime() - cal.getTime()
                .getTime()) <= 0) {
            return "expired";
        }

        User user = passToken.getUser();
        Authentication auth = new UsernamePasswordAuthenticationToken(
                user, null, Arrays.asList(
                new SimpleGrantedAuthority("CHANGE_PASSWORD_PRIVILEGE")));
        SecurityContextHolder.getContext().setAuthentication(auth);
        return null;
    }

    public void authWithHttpServletRequest(HttpServletRequest request, String username, String password) {
        try {
            request.login(username,password);
        } catch (ServletException e) {

        }
    }

    public void authWithAuthManager(HttpServletRequest request, String username, String password) {
        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(username, password);
        authToken.setDetails(new WebAuthenticationDetails(request));
        Authentication authentication = authenticationManager.authenticate(authToken);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        // request.getSession().setAttribute(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY, SecurityContextHolder.getContext());
    }

    public void authWithoutPassword(User user) {
       /* List<Privilege> privileges = user.getRoles().stream().map(role -> role.getPrivileges()).flatMap(list -> list.stream()).distinct().collect(Collectors.toList());
        List<GrantedAuthority> authorities = privileges.stream().map(p -> new SimpleGrantedAuthority(p.getName())).collect(Collectors.toList());

        Authentication authentication = new UsernamePasswordAuthenticationToken(user, null, authorities);

        SecurityContextHolder.getContext().setAuthentication(authentication);*/
    }


    @Autowired
    private EmployeeRepository employeeDao;

    @Autowired
    private PasswordResetTokenRepository passwordResetTokenRepository;

    private String getAppUrl(HttpServletRequest request) {
        return "https://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath();
    }

}