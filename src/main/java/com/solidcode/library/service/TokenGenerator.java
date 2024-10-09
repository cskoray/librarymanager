package com.solidcode.library.service;

import com.solidcode.library.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TokenGenerator implements CommandLineRunner {

    private final JwtUtil jwtUtil;

    @Override
    public void run(String... args) {

        String token = jwtUtil.generateToken("hardcodedUser");
        System.out.println("Generated JWT token: " + token);
    }
}
