package com.app.controller;

import com.app.util.Token;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
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
}
