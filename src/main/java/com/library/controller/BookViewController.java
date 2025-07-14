package com.library.controller;

import com.library.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/books")
public class BookViewController {

    @Autowired
    private BookService bookService;

    @GetMapping
    public String listBooks(@RequestParam(required = false) String title,
                            @RequestParam(required = false) String author,
                            Model model) {
        model.addAttribute("books", bookService.findBooks(title, author));
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            String username = authentication.getName();
            model.addAttribute("firstName", username);
        }
        return "book-list";
    }

    @GetMapping("/issue/{bookId}")
    public String showIssuePage(@PathVariable Long bookId, Model model, RedirectAttributes redirectAttributes) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            redirectAttributes.addFlashAttribute("redirectAfterLogin", "/books/issue/" + bookId);
            return "redirect:/login";
        }
        // Add book and rental options to model
        model.addAttribute("bookId", bookId);
        model.addAttribute("rentalPeriods", List.of(7, 14, 30, 90, 180));
        return "issue-book";
    }
}