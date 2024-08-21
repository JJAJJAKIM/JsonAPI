package com.app.controller;

import com.app.util.Token;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

    @GetMapping("/test")
    public String test() {
        return "test";
    }

    @Autowired
    private Token token;

    @GetMapping("/token")
    public String token() {
        return token.setToken();
    }

    @GetMapping("/token/{token}")
    public Claims token(@PathVariable("token") String jwt) {
       if(token.isValidToken(jwt)){
           return token.getToken(jwt);
       }
       return null;
    }
}
