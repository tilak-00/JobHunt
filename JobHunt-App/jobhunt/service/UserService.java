package com.example.jobhunt.service;

import com.example.jobhunt.dto.SignupRequest;
import com.example.jobhunt.entity.User;
import com.example.jobhunt.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepository userRepository,
                       PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public String registerUser(SignupRequest request) {
        if (!request.getPassword().equals(request.getConfirmPassword())) {
            return "❌ Passwords do not match.";
        }

        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            return "❌ Email already registered.";
        }

        if (userRepository.findByMobileNumber(request.getMobileNumber()).isPresent()) {
            return "❌ Mobile number already registered.";
        }

        User user = new User();
        user.setFullName(request.getFullName());
        user.setEmail(request.getEmail());
        user.setMobileNumber(request.getMobileNumber());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setProvider("LOCAL");

        userRepository.save(user);

        return "✅ Signup successful.";
    }
}
