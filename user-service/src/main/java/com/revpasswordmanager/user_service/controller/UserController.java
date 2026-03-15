package com.revpasswordmanager.user_service.controller;

import com.revpasswordmanager.user_service.dto.LoginRequest;
import com.revpasswordmanager.user_service.dto.OtpRequest;
import com.revpasswordmanager.user_service.entity.User;
import com.revpasswordmanager.user_service.security.JwtUtil;
import com.revpasswordmanager.user_service.service.EmailService;
import com.revpasswordmanager.user_service.service.UserService;
import com.revpasswordmanager.user_service.repository.UserRepository;

import com.revpasswordmanager.user_service.util.OtpUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
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

    @Autowired
    private EmailService emailService;

    @PostMapping("/register")
    public User register(@RequestBody User user) {
        return userService.registerUser(user);
    }

    @PostMapping("/login")
    public Map<String,Object> login(@RequestBody LoginRequest request){

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found"));

        if(!passwordEncoder.matches(request.getPassword(),
                user.getMasterPassword())){

            throw new RuntimeException("Invalid password");
        }

        // OTP flow
        if(user.isTwoFactorEnabled()){

            String otp = OtpUtil.generateOtp();

            user.setOtp(otp);
            user.setOtpExpiry(LocalDateTime.now().plusMinutes(5));

            userRepository.save(user);

            System.out.println("OTP for user " + user.getEmail() + " : " + otp);

            return Map.of(
                    "message","OTP sent to console",
                    "userId", user.getId()
            );
        }

        // normal login
        String token = JwtUtil.generateToken(user.getEmail(), user.getId());

        return Map.of(
                "token", token,
                "userId", user.getId()
        );
    }

    @GetMapping("/{id}")
    public User getUserById(@PathVariable Long id){
        return userService.getUserById(id);
    }

    @PostMapping("/verify-otp")
    public Map<String,String> verifyOtp(@RequestBody OtpRequest request){

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found"));

        if(user.getOtp() == null){
            throw new RuntimeException("OTP not generated");
        }

        if(!user.getOtp().equals(request.getOtp())){
            throw new RuntimeException("Invalid OTP");
        }

        if(user.getOtpExpiry().isBefore(LocalDateTime.now())){
            throw new RuntimeException("OTP expired");
        }

        user.setOtp(null);
        userRepository.save(user);

        String token = JwtUtil.generateToken(user.getEmail(), user.getId());

        return Map.of(
                "token", token,
                "userId", user.getId().toString()
        );
    }
}