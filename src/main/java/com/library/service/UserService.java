package com.library.service;

import com.library.model.User;
import com.library.repository.BookRepository;
import com.library.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;


    public boolean authenticate(String username, String password) {
        Optional<User> user = userRepository.findByUsername(username);
        return user != null && user.get().getPassword().equals(password);
    }

    public void saveUser(User user) {
        userRepository.save(user);
    }

    public User registerUser(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public User updateProfile(User updated) {
        // Optionally fetch existing user, update only allowed fields
        return userRepository.save(updated);
    }

}
