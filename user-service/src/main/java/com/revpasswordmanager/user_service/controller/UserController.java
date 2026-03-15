package com.revpasswordmanager.user_service.controller;

import com.revpasswordmanager.user_service.dto.LoginRequest;
import com.revpasswordmanager.user_service.dto.RecoverPasswordRequest;
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

    @PutMapping("/change-master-password")
    public Map<String,String> changeMasterPassword(
            @RequestParam Long userId,
            @RequestParam String currentPassword,
            @RequestParam String newPassword){

        userService.changeMasterPassword(userId,currentPassword,newPassword);

        return Map.of("message","Master password updated successfully");
    }

    @PostMapping("/recover-password")
    public Map<String,String> recoverPassword(@RequestBody RecoverPasswordRequest request){

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found"));

        if(!passwordEncoder.matches(request.getAnswer1(),user.getSecurityAnswer1()) ||
                !passwordEncoder.matches(request.getAnswer2(),user.getSecurityAnswer2()) ||
                !passwordEncoder.matches(request.getAnswer3(),user.getSecurityAnswer3())){

            throw new RuntimeException("Security answers incorrect");
        }

        user.setMasterPassword(passwordEncoder.encode(request.getNewPassword()));
        userRepository.save(user);

        return Map.of("message","Master password reset successful");
    }

    @GetMapping("/security-questions/{email}")
    public Map<String,String> getSecurityQuestions(@PathVariable String email){

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return Map.of(
                "question1", user.getSecurityQuestion1(),
                "question2", user.getSecurityQuestion2(),
                "question3", user.getSecurityQuestion3()
        );
    }

    @PutMapping("/update-security-answers")
    public Map<String,String> updateSecurityAnswers(
            @RequestParam Long userId,
            @RequestParam String masterPassword,
            @RequestParam String answer1,
            @RequestParam String answer2,
            @RequestParam String answer3){

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if(!passwordEncoder.matches(masterPassword,user.getMasterPassword())){
            throw new RuntimeException("Master password incorrect");
        }

        user.setSecurityAnswer1(passwordEncoder.encode(answer1));
        user.setSecurityAnswer2(passwordEncoder.encode(answer2));
        user.setSecurityAnswer3(passwordEncoder.encode(answer3));

        userRepository.save(user);

        return Map.of("message","Security answers updated");
    }

}