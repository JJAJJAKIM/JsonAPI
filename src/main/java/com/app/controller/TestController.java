package com.app.controller;

import com.app.util.Token;
import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.annotations.Select;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class TestController {

    @GetMapping("/test")
    public Object test(Authentication auth) {
        return auth.getPrincipal();
    }

    @Autowired
    private Token token;

    @GetMapping("/token")
    public String token(Authentication auth) {
        log.info("Auth : {}", auth.getPrincipal());
        return token.setToken(auth);
    }

    @GetMapping("/token/{token}")
    public Claims token(@PathVariable("token") String jwt) {
       if(token.isValidToken(jwt)){
           return token.getToken(jwt);
       }
       return null;
    }


}
