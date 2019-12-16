package msadaka.controllers;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class ReportsController {
    @Value("${quantum.api.url}")
    private String quantumurl;

    @Value("${footer}")
    private String footer;

    @GetMapping("txnreports")
    public String txnreports (Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        try{
            System.out.println(SecurityContextHolder.getContext().getAuthentication().getPrincipal());
            model.addAttribute("userName",auth.getName()); }
        catch(Exception e)
        {}
        model.addAttribute("apiUrl",quantumurl);
        return "txnreports";
    }
    @GetMapping("transactions")
    public String transactions (Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        try{
            System.out.println(SecurityContextHolder.getContext().getAuthentication().getPrincipal());
            model.addAttribute("userName",auth.getName());
            model.addAttribute("footer",footer);
        }
        catch(Exception e)
        {e.printStackTrace();}
        model.addAttribute("apiUrl",quantumurl);
        return "tables-datatable";
    }


}
