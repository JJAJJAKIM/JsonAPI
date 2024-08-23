package com.app.controller;

import com.app.mapper.AuthMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Map;

//@PreAuthorize("hasRole('DEV')")
@Slf4j
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

    @Autowired
    private AuthMapper authMapper;
    @Autowired
    private PasswordEncoder pwEncoder;

    @GetMapping("/sign")
    public String sign() {
        return "sign";
    }
    @PostMapping("/sign")
    public String sign(@RequestParam Map<String, String> paramMap) {
        log.info("New User : {} ", paramMap );
        if (paramMap != null){
            String userPwd = paramMap.get("userPwd");
            if (userPwd != null) {
               userPwd =  pwEncoder.encode(paramMap.get("userPwd"));
               paramMap.put("userPwd", userPwd );
            }
            int state = authMapper.saveUser(paramMap);

            if (state == 1){
               log.info ("User : {}", paramMap);
                return "redirect:/login";
            }
        }
        return "redirect:/sign";
    }

}
