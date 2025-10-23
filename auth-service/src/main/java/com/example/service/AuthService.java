package com.example.service;


import com.example.Kafka.ProducerService;
import com.example.dto.AuthResponse;
import com.example.dto.LoginRequest;
import com.example.dto.RegisterRequest;
import com.example.repository.UserRepository;
import com.sharepersistence.entity.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;
    private final ProducerService producerService;

    public AuthService(UserRepository userRepository, JwtUtil jwtUtil, PasswordEncoder passwordEncoder, ProducerService producerService) {
        this.userRepository = userRepository;
        this.jwtUtil = jwtUtil;
        this.passwordEncoder = passwordEncoder;
        this.producerService = producerService;
    }

    public User register(RegisterRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email already registered");
        }

        User user = new User();
        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole(request.getRole());
        user.setEnabled(true);

        userRepository.save(user);
        if(user.getRole().equals("APPLICANT")) {
            producerService.sendUserRegisteredEvent("user-registered","New user registered: ApplicantName is " + user.getUsername());
        }
        else{
            producerService.sendUserRegisteredEvent("company-registered","New company registered by " + user.getUsername());
        }
        return user;
    }

    public AuthResponse login(LoginRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new RuntimeException("Invalid Password!!");
        }

        String token = jwtUtil.generateToken(user.getUsername(), user.getRole());
        Long userId = user.getId();


        return new AuthResponse(userId,token);
    }
}
