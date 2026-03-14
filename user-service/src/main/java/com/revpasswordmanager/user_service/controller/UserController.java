package com.revpasswordmanager.user_service.controller;

import com.revpasswordmanager.user_service.dto.LoginRequest;
import com.revpasswordmanager.user_service.entity.User;
import com.revpasswordmanager.user_service.security.JwtUtil;
import com.revpasswordmanager.user_service.service.UserService;
import com.revpasswordmanager.user_service.repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping("/register")
    public User register(@RequestBody User user) {
        return userService.registerUser(user);
    }

    @PostMapping("/login")
    public Map<String,String> login(@RequestBody LoginRequest request){

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found"));

        if(!passwordEncoder.matches(request.getPassword(),
                user.getMasterPassword())){

            throw new RuntimeException("Invalid password");
        }

        String token = JwtUtil.generateToken(user.getEmail());

        return Map.of("token", token);
    }

    @GetMapping("/{id}")
    public User getUserById(@PathVariable Long id){
        return userService.getUserById(id);
    }
}