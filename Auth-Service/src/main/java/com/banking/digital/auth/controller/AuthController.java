package com.banking.digital.auth.controller;

import com.banking.digital.auth.config.JwtUtil;
import com.banking.digital.auth.dto.AuthResponseDto;
import com.banking.digital.auth.dto.LoginRequestDto;
import com.banking.digital.auth.dto.RegisterRequestDto;
import com.banking.digital.auth.entity.User;
import com.banking.digital.auth.repository.UserRepository;
import com.banking.digital.auth.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody RegisterRequestDto request) {
        return ResponseEntity.ok(authService.register(request));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponseDto> login(@RequestBody LoginRequestDto request) {

        String token = authService.login(request);

        return ResponseEntity.ok(new AuthResponseDto(token, "Login Successful"));
    }

    @GetMapping("/validate")
    public ResponseEntity<String> validateToken(@RequestParam String token) {
        return ResponseEntity.ok("Token is valid");
    }

}
