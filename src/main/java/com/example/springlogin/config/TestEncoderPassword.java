package com.example.springlogin.config;

import java.util.Scanner;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class TestEncoderPassword {
    public static void main(String[] args) {
        BCryptPasswordEncoder b = new BCryptPasswordEncoder();
        System.out.print("Raw password: ");
        String rawP = new Scanner(System.in).nextLine();
        System.out.println(b.encode(rawP));
    }
}
