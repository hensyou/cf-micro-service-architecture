package com.cloudnativecoffee.market.controllers;

import com.cloudnativecoffee.market.model.Login;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class LoginController {
    private static final String ADMIN = "admin";

    @GetMapping("/")
    public String loginAction(Model model) {
        model.addAttribute("login", new Login());
        return "login";
    }

    @PostMapping("/")
    public String checkLogin(@ModelAttribute Login login) {
        if(!(ADMIN.equals(login.getUsername()) && ADMIN.equals(login.getPassword())))
            return "redirect:/";
        return "redirect:/home";
    }

}
