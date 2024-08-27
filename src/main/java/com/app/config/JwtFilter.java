package com.app.config;

import com.app.dto.UserDTO;
import com.app.service.UserService;
import com.app.util.Token;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.LinkedHashMap;

@Slf4j
@Component
public class JwtFilter extends OncePerRequestFilter {

    @Autowired
    private Token token;

    @Autowired
    private UserService userService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        // 토큰 검사 로직
        log.info("jwt filter");
        String authorization = request.getHeader("Authorization");
        if(authorization != null){
            log.info("Authorization: {}", authorization);
            if(token.isValidToken(authorization)){
                log.info("token valid");
                Claims claims = token.getToken(authorization);
                log.info("claims: {}", claims);
                LinkedHashMap<String, String> user = (LinkedHashMap<String, String>) claims.get("audience");
                log.info("user: {}", user);
                UserDetails userDetails = userService.loadUserByUsername(user.get("userNm"));
                UsernamePasswordAuthenticationToken userToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                SecurityContextHolder.getContext().setAuthentication(userToken);
            }
        }
        filterChain.doFilter(request, response);
    }
}
