package com.app.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

//@PreAuthorize("hasRole('DEV')")
@Controller
public class ViewController {

    @GetMapping("/")
    public String home() {
        return "home";
    }

    @ResponseBody
    @GetMapping("/admin")
    public String admin(Authentication auth) {
        return auth.getName();
    }
    @GetMapping("/login")
    public String login() {
        return "login";
    }

//    @ResponseBody
//    @GetMapping("/logout")
//    public String logout() {
//        return "logout";
//    }
}
