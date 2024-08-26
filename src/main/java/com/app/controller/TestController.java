package com.app.controller;

import com.app.util.Token;
import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.annotations.Select;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@Slf4j
@CrossOrigin(origins = {"http://127.0.0.1:5500", "http://localhost:5500"})
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
//        log.info("Auth : {}", auth.getPrincipal());
        return token.setToken(auth);
    }

    @PostMapping("/token")
    public Claims getToken(@RequestParam("token") String jwt){
        if(token.isValidToken(jwt)){
            return token.getToken(jwt);
        }
        return null;
    }

    @GetMapping("/token/{token}")
    public Claims token(@PathVariable("token") String jwt) {
       if(token.isValidToken(jwt)){
           return token.getToken(jwt);
       }
       return null;
    }


}
