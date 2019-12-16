package msadaka.controllers;

import msadaka.beans.PostBean;
import msadaka.models.User;
import msadaka.services.UserService;
import msadaka.utils.WebClient;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;

@RestController
public class PayBillsController {
    @Value("${quantum.api.url}")
    private String quantumurl;

    public static JSONObject data = new JSONObject();


    @PostMapping("/getPayBills")
    //@Produces({MediaType.APPLICATION_JSON})
    public String getPayBills()
    {
        String response="[]";
        try
        {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            User user = userService.findUserByUsername(auth.getName());
            System.out.println("user.getEmail() >> "+user.getEmail());
            WebClient client=new WebClient();
            //public String webRequest(String url,String data,String post_get,String params)
            response=client.webRequest(quantumurl+"/churches/getPayBills?userEmail="+user.getEmail(),"","POST","");
            System.out.println("getPayBills().response >> "+response);
        }
        catch(Exception e){e.printStackTrace();}

        return  response ;

    }


    @PostMapping("/newPayBill")
    //@Produces({MediaType.APPLICATION_JSON})
    public String newPayBill(@RequestBody PostBean bean)
    {
        String response="[]";
        HashMap<String, String> map=bean.getMap();
        try
        {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            User user = userService.findUserByUsername(auth.getName());
            System.out.println("user.getEmail() >> "+user.getEmail());
            WebClient client=new WebClient();
            data.put("id",map.get("id"));
            data.put("name",map.get("name"));
            data.put("altName",map.get("altName"));


            data.put("payBill",map.get("payBill"));
            //data.put("payBill","361751");

            data.put("shortCode",map.get("shortCode"));
            //data.put("shortCode","361751");

            data.put("userEmail",user.getEmail());
            data.put("mpesaAppkey","Sd5l3hiCcwA5lqxugGA50yN2j9nBr1zA");
            data.put("mpesaAppsecret","3FyAQdGhJTexg2yh");
            data.put("mpesaPasskey",map.get("mpesaPasskey"));
            //832fe8ede696958c2276e505529dba6031d0bf64fab82e89d1a7091ee5085b76
            //public String webRequest(String url,String data,String post_get,String params)
            response=client.webRequest(quantumurl+"/churches/createChurch",data.toString(),"POST","");
            response=response.replace("'","\"");
            System.out.println("newPayBill().response >> "+response);
        }
        catch(Exception e){e.printStackTrace();}

        return  response  ;

    }

    @PostMapping("/deletePayBill")
    //@Produces({MediaType.APPLICATION_JSON})
    public String deletePayBill(@RequestBody PostBean bean)
    {
        String response="[]";
        HashMap<String, String> map=bean.getMap();
        try
        {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            User user = userService.findUserByUsername(auth.getName());
            System.out.println("user.getEmail() >> "+user.getEmail());
            WebClient client=new WebClient();
            data.put("id",map.get("id"));
            data.put("userEmail",user.getEmail());

            response=client.webRequest(quantumurl+"/churches/deleteChurch",data.toString(),"POST","");
            response=response.replace("'","\"");

            System.out.println("deletePayBill().response >> "+response);
        }
        catch(Exception e){e.printStackTrace();}

        return  response  ;

    }


    @Autowired
    private UserService userService;
}
