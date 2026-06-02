package com.example.logichain.service;

import com.example.logichain.dto.LoginRequest;
import com.example.logichain.dto.RegisterRequest;
import com.example.logichain.model.User;
import com.example.logichain.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtService jwtService){
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    public String register(RegisterRequest request){
        User user = new User();
        user.setUsername(request.getUsername());
        user.setpassword(passwordEncoder.encode(request.getPassword()));

        user.setRole("USER");
        userRepository.save(user);

        return "User registered successfully";
    }

    public String login(LoginRequest request){

        User user = userRepository
                .findByUsername(request.getUsername())
                .orElseThrow(()-> new RuntimeException("User not found"));
        boolean matches = passwordEncoder.matches(
                request.getPassword(),
                user.getpassword()
        );
        if(!matches){
            throw new RuntimeException("Invalid Password");
        }

        return jwtService.generateToken(user.getUsername(), user.getRole());
    }

}
