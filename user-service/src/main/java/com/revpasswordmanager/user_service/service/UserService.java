package com.revpasswordmanager.user_service.service;

import com.revpasswordmanager.user_service.entity.User;
import com.revpasswordmanager.user_service.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public User registerUser(User user) {

        if(user.getSecurityAnswer1()==null ||
                user.getSecurityAnswer2()==null ||
                user.getSecurityAnswer3()==null){
            throw new RuntimeException("Minimum 3 security questions required");
        }

        // encode master password
        user.setMasterPassword(
                passwordEncoder.encode(user.getMasterPassword())
        );

        // encode security answers
        user.setSecurityAnswer1(passwordEncoder.encode(user.getSecurityAnswer1()));
        user.setSecurityAnswer2(passwordEncoder.encode(user.getSecurityAnswer2()));
        user.setSecurityAnswer3(passwordEncoder.encode(user.getSecurityAnswer3()));

        return userRepository.save(user);
    }

    public User getUserById(Long id){
        return userRepository.findById(id).orElse(null);
    }

    public void changeMasterPassword(Long userId, String currentPassword, String newPassword){

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if(!passwordEncoder.matches(currentPassword, user.getMasterPassword())){
            throw new RuntimeException("Current password incorrect");
        }

        user.setMasterPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
    }

}