//package com.library.controller;
//
//import com.library.model.User;
//import com.library.service.UserService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.ui.Model;
//import org.springframework.web.bind.annotation.*;
//
//@RestController
//public class UserController {
//
//    @Autowired
//    UserService userService;
//
//    @GetMapping("/users/register")
//    public String showForm(Model model) {
//        model.addAttribute("user", new User());
//        return "user-form";
//    }
//
//    @PostMapping("/users/register")
//    public String register(@ModelAttribute User user) {
//        userService.saveUser(user);
//        return "redirect:/books";
//    }
//}
