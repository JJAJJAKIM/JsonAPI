package com.app.config;

import com.app.util.Token;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j
@Component
public class JwtFilter extends OncePerRequestFilter {

    @Autowired
    private Token token;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        // 토큰 검사 로직
        log.info("jwt filter");
        String authorization = request.getHeader("Authorization");
        if(authorization != null){
            log.info("Authorization: {}", authorization);
            if(token.isValidToken(authorization)){
                log.info("token valid");
            }
        }
        filterChain.doFilter(request, response);
    }
}
