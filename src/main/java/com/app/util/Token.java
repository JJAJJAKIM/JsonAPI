package com.app.util;

import io.jsonwebtoken.Jwt;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
public class Token {

    public String setToken() {

        JwtBuilder token = Jwts.builder()
                .header().add(getHeader()).and()
                .claims(setClaims())
                .signWith(getSecretKey())
        ;
        log.info("Token : {}",token.compact());
        return token.compact();
    }

    private Map<String, String> getHeader() {
        Map<String, String> header = new HashMap<>();
        header.put("alg", "HS256");
        header.put("typ", "JWT");
        return header;
    }

    private SecretKey getSecretKey() {
        return Jwts.SIG.HS256.key().build();
    }

    private Map<String, ?> setClaims() {
        Map<String, Object> claims = new HashMap<>();

        Map<String, Object> user = new HashMap<>();
        user.put("userNm", "사용자");

        claims.put("issuer", "JsonAPI");
        claims.put("subject", "User");
        claims.put("audience", user);
        claims.put("iat", Calendar.getInstance().getTime().getTime());
        claims.put("expiration", getDate(1));
        return claims;
    }

    private Date getDate(int i){
        Calendar date = Calendar.getInstance();
        date.add(Calendar.MINUTE, i);
        return date.getTime();
    }
}
