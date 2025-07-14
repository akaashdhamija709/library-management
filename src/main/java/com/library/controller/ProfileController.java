package com.library.controller;

import com.library.model.Issue;
import com.library.model.User;
import com.library.repository.IssueRepository;
import com.library.repository.UserRepository;
import com.library.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.util.List;

@Controller
@RequestMapping("/profile")
@PreAuthorize("isAuthenticated()")
public class ProfileController {

    @Autowired
    private IssueRepository issueRepository;
    @Autowired
    private UserRepository userRepo;
    @Autowired
    private UserService userService;

    @GetMapping
    public String showProfile(Model model, Principal principal) {
        User user = userService.findByUsername(principal.getName()).orElseThrow();
        List<Issue> issued = issueRepository.findByUserAndReturnDateIsNull(user);
        List<Issue> history = issueRepository.findByUserAndReturnDateIsNotNull(user);
        model.addAttribute("issued", issued);
        model.addAttribute("history", history);
        model.addAttribute("user", user);
        return "profile";
    }


    @PostMapping("/update")
    public String updateProfile(@ModelAttribute("user") @Valid User formUser,
                                BindingResult result,
                                Principal principal,
                                RedirectAttributes attrs) {
        if (result.hasErrors()) {
            return "profile";
        }

        // Fetch the existing user — ensures correct ID is included
        User current = userService.findByUsername(principal.getName())
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        // Copy allowed fields
        current.setFirst_name(formUser.getFirst_name());
        current.setLast_name(formUser.getLast_name());
        current.setEmail(formUser.getEmail());
        current.setAge(formUser.getAge());

        userService.updateProfile(current);
        attrs.addFlashAttribute("success", "Profile updated ✅");
        return "redirect:/profile";
    }


}

