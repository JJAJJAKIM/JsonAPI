package com.app.controller;

import com.app.config.WebSecurity;
import com.app.dto.TokenDTO;
import com.app.dto.UserDTO;
import com.app.mapper.AuthMapper;
import com.app.util.Token;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.annotations.Select;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.token.TokenService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.thymeleaf.spring6.processor.SpringActionTagProcessor;

import java.util.Map;

@Slf4j
@CrossOrigin(origins = {"http://127.0.0.1:5500", "http://localhost:5500"})
@RestController
public class TestController {

    @GetMapping("/test")
    public Object test(Authentication auth) {
        return auth.getPrincipal();
    }

    @Autowired
    private AuthMapper authMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;


    @PostMapping("/jsLogin")
    public TokenDTO jsLogin(@RequestParam Map<String,String> params) {
        log.info("params: {}", params);
        String userPwd = params.get("userPwd");
        boolean state = false;
        String jwt = null;

        if(userPwd != null){
            UserDTO userDto = authMapper.findbyUserNm(params.get("userNm")).orElseThrow((()-> new RuntimeException("그런 사람 없다")));

            if( passwordEncoder.matches(userPwd, userDto.getUserPwd()) ){
                log.info("로그인 성공");
                state = true;
                jwt = token.setToken(userDto);
            }
        }
        return TokenDTO.builder().state(state).token(jwt).build();
    }

    @Autowired
    private Token token;

    @GetMapping("/token")
    public String token(Authentication auth) {
//        log.info("Auth : {}", auth.getPrincipal());
        return token.setToken(auth);
    }

    @PostMapping("/getUser")
    public String getUser(HttpServletRequest req) {
        return check(req);
    }

    private String check(HttpServletRequest req){
        String authorization = req.getHeader("Authorization");
        log.info("Authorization: {}", authorization);
        if(token.isValidToken(authorization)){
            Claims claims = token.getToken(authorization);
            Map<String, String> user = (Map<String, String>) claims.get("audience");
//            log.info("claims: {}", user.get("userNm"));
            return user.get("userNm");
        }
        return null;
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
