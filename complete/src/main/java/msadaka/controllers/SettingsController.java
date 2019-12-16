package msadaka.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class SettingsController {

    @GetMapping("settings")
    public String settings(Model model) {

              return "settings";
    }

    @GetMapping("roles-permissions")
    public String rolesPermissions(Model model) {

        return "roles-permissions";
    }
}
