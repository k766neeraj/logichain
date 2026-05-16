package com.example.logichain.service;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import io.jsonwebtoken.security.Keys;
import java.security.Key;

import java.util.Date;

@Service
public class JwtService {

    @Value("${jwt.secret}")
    private String secretkey;

    public String generateToken(String username){
        Key key = Keys.hmacShaKeyFor(secretkey.getBytes());
        System.out.println("secret_key"+secretkey);
        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis()+1000*60*60))
                .signWith(
                        key,
                        SignatureAlgorithm.HS256
                ).compact();
    }
}
