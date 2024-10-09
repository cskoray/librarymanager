package com.solidcode.library.security;

import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.stereotype.Component;

import java.util.Date;

import static io.jsonwebtoken.Jwts.builder;
import static io.jsonwebtoken.Jwts.parser;

@Component
public class JwtUtil {

    private final String SECRET_KEY = "super safe secret key";

    public String generateToken(String username) {

        return builder().setSubject(username).setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 60))
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY).compact();
    }

    public boolean validateToken(String token, String username) {

        return parser().setSigningKey(SECRET_KEY).parseClaimsJws(token).getBody().getSubject()
                .equals(username) && !isExpired(token);
    }

    private boolean isExpired(String token) {

        return parser().setSigningKey(SECRET_KEY).parseClaimsJws(token).getBody().getExpiration().before(new Date());
    }
}
