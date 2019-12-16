package msadaka.controllers;

import msadaka.models.Employee;
import msadaka.models.User;
import msadaka.repositories.EmployeeRepository; 
import msadaka.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import javax.websocket.server.PathParam;
import java.text.SimpleDateFormat;

@Controller
public class EmployeeController {
    Iterable<Employee> employees;
    Employee employee;
    @Autowired
    private UserService userService;

    @Value("${spring.queries.roles-query}")
    private String rolesQuery;
    @Value("${employee.identifier.prefix}")
    private String empPrefix;

    @PostMapping(value="/all-employees" ,produces = { "application/json" },headers = "Accept=application/json")
    public String allemployees(/*@RequestBody Request request*/) {
              return "employees";
    }

        @GetMapping("/employees")
        public String employees( Model model) {
            employees=employeeDao.findAll();
            try{
                Authentication auth = SecurityContextHolder.getContext().getAuthentication();
                model.addAttribute("userName", auth.getName());
                model.addAttribute("adminMessage","Content Available Only for Users with Admin Role");}
            catch(Exception e)
            {}
            model.addAttribute("employees",employees);
            model.addAttribute("user", new User());
          //  model.addAttribute("employee", new Employee());
          model.addAttribute("employee", new Employee());


            try{

                System.out.println("Employees: "+ (employees==null?"No Employees":employees.toString()));
            }
            catch(Exception e)
            {e.printStackTrace();}

            return "employees";
        }
    @GetMapping("profile")
    public String profile(@RequestParam(name="id") Integer id, Model model) {
        employee=employeeDao.findEmployeeById(id);
        model.addAttribute("employee",employee);

        try{

            System.out.println("Employee: "+ (employee==null?"Not Found":employee.toString()));
        }
        catch(Exception e)
        {e.printStackTrace();}


        return "profile";
    }
    @GetMapping("edit-profile")
    public String editprofile(@RequestParam(name="id") Integer id, Model model) {
        employee=employeeDao.findEmployeeById(id);
        model.addAttribute("date_format", "dd/MM/yyyy");
      //  SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
       // employee.setDob(simpleDateFormat.format(employee.getDob()));
        model.addAttribute("employee",employee);

        try{

            System.out.println("Employee: "+ (employee==null?"Not Found":employee.toString()));
        }
        catch(Exception e)
        {e.printStackTrace();}
        return "edit-profile";
    }
    @PostMapping(value = "/newemployee")
    public String createNewEmployee(@RequestBody(required = false) @Valid Employee employee, BindingResult bindingResult,Model model) {

        System.out.println(employee.toString());

        User userExists = userService.findByEmailOrUsername(employee.getUser().getEmail(),employee.getUser().getUsername());

        if (userExists != null) {
            if (userExists.getEmail()!=null) {
                if(userExists.getEmail().equalsIgnoreCase(employee.getUser().getEmail())) {
                    bindingResult
                            .rejectValue("user.email", "error.user",
                                    "There is already a user registered with the email  provided");
                }
            }
            if (userExists.getUsername()!=null) {
                if(userExists.getUsername().equalsIgnoreCase(employee.getUser().getUsername()) && employee.getUser().getUsername()!="")
                {bindingResult
                        .rejectValue("user.username", "error.user",
                                "There is already a user registered with the Username provided");}
            }

        }
        if (bindingResult.hasErrors()) {

            model.addAttribute("successMessage", bindingResult.getFieldError());
            model.addAttribute("newemployee", employee);
        } else {
            String name=employee.getUser().getSurName()+", "+employee.getUser().getFirstName()+" "+employee.getUser().getLastName();
            employee.getUser().setName(name);
            employee.getUser().setActive(1);

            userService.saveUser(employee.getUser());
            employee.setName(name);
            employee=employeeDao.save(employee);
            if(employee==null)  // In Case the employee object creation failed but the user was already created
            {
                userService.deleteUser(userService.findByEmailOrUsername(employee.getUser().getEmail(),employee.getUser().getUsername()));
            }
            employee.setEmpNo(empPrefix+String.format("%04d",employee.getUser().getId()));  //String.format("%04d", number);
            employeeDao.save(employee);

            model.addAttribute("successMessage", "Employee has been registered successfully");
            model.addAttribute("employee", new Employee());
        }
        return "employees :: modal-body";

    }

    @PostMapping(value = "/searchemps")
    public String SearchEmployees(@RequestBody(required = false) @Valid Employee employee, BindingResult bindingResult,Model model) {

        employees=employeeDao.findEmployeesByEmpNoIgnoreCaseContainingAndNameIgnoreCaseContaining(employee.getEmpNo(),employee.getName());
        System.out.println("Found Employees: "+employees);
            model.addAttribute("successMessage", "Employee has been found successfully");
            model.addAttribute("employees", employees);

        return "employees :: staff-grid-row";

    }


    @Autowired
    private EmployeeRepository employeeDao;

}
