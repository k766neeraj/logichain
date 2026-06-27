package com.example.logichain.service;

import com.example.logichain.dto.LoginRequest;
import com.example.logichain.dto.RegisterRequest;
import com.example.logichain.model.AuditAction;
import com.example.logichain.model.User;
import com.example.logichain.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuditLogService auditLogService;

    public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtService jwtService,
                       AuditLogService auditLogService){
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.auditLogService = auditLogService;
    }

    public String register(RegisterRequest request){
        User user = new User();
        user.setUsername(request.getUsername());
        user.setpassword(passwordEncoder.encode(request.getPassword()));

        user.setRole("USER");
        userRepository.save(user);

        auditLogService.logAction(
                AuditAction.CREATE_USER,
                "USER",
                user.getId()
        );

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

        auditLogService.logAction(
                AuditAction.LOGIN,
                "USER",
                user.getId()
        );

        return jwtService.generateToken(user.getUsername(), user.getRole());
    }

}
