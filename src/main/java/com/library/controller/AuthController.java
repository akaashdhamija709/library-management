package com.library.controller;

import com.library.service.UserService;
import com.library.model.User;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.ui.Model;

@Controller
public class AuthController {

    @Autowired
    private UserService userService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping("/login")
    public String showLoginPage() {
        return "login";
    }

    /*@PostMapping("/login")
    public String login(@RequestParam String username,
                        @RequestParam String password,
                        Model model,
                        HttpSession session,
                        @ModelAttribute("redirectAfterLogin") String redirectAfterLogin) {
        if (userService.authenticate(username, password)) {
            session.setAttribute("loggedInUser", username);
            if (redirectAfterLogin != null && !redirectAfterLogin.isEmpty()) {
                return "redirect:" + redirectAfterLogin;
            }
            return "redirect:/books";
        } else {
            model.addAttribute("error", "Invalid credentials");
            return "login";
        }
    }*/

    @GetMapping("/signup")
    public String showSignupPage(Model model) {
        model.addAttribute("user", new User());
        return "signup";
    }

    @PostMapping("/signup")
    public String signup(@ModelAttribute User user,
                         @RequestParam("confirmPassword") String confirmPassword,
                         Model model) {
        if (!user.getPassword().equals(confirmPassword)) {
            model.addAttribute("passwordError", "Passwords do not match");
            return "signup";
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userService.saveUser(user);
        return "redirect:/login";
    }

    @PostMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/login";
    }
}