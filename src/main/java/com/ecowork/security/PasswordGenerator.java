package com.ecowork.security;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

public class PasswordGenerator {

    public static void main(String[] args) {

        PasswordEncoder encoder = new BCryptPasswordEncoder();

        String rawPassword = "admin123";

        String hashed = encoder.encode(rawPassword);

        System.out.println("Senha original: " + rawPassword);
        System.out.println("BCrypt gerado: " + hashed);
    }
}