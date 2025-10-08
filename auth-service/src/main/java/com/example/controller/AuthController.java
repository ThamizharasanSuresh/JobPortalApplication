package com.example.controller;


import com.example.dto.AuthResponse;
import com.example.dto.LoginRequest;
import com.example.dto.RegisterRequest;
import com.example.repository.UserRepository;
import com.example.service.AuthService;
import com.sharepersistence.entity.User;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UserRepository userRepository;
    private final AuthService authService;


    public AuthController(AuthService authService,UserRepository userRepository) {
        this.authService = authService;
        this.userRepository=userRepository;
    }

    @PostMapping("/register")
    public AuthResponse register(@RequestBody RegisterRequest request) {
        return authService.register(request);
    }

    @PostMapping("/login")
    public AuthResponse login(@RequestBody LoginRequest request) {
        return authService.login(request);
    }

    @GetMapping("/users/{id}")
    public User getUserById(@PathVariable Long id){
        return userRepository.findById(id).orElseThrow();
    }
}

