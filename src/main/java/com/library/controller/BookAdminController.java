package com.library.controller;

import com.library.model.Book;
import com.library.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Optional;

@Controller
@RequestMapping("/admin/books")
public class BookAdminController {

    @Autowired
    private BookService bookService;

    @GetMapping("/edit/{id}")
    public String editBook(@PathVariable("id") Long id, Model model) {
        Book book = bookService.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid book ID: " + id));
        model.addAttribute("book", book);
        return "edit-book"; // Thymeleaf template for editing
    }

    @PostMapping("/edit/{id}")
    public String updateBook(@PathVariable("id") Long id,
                             @ModelAttribute Book book,
                             RedirectAttributes redirectAttrs) {
        book.setId(id);
        bookService.save(book);
        redirectAttrs.addFlashAttribute("success", "Book updated successfully");  // ← flash attribute
        return "redirect:/admin/books";
    }

    @GetMapping("/delete/{id}")
    public String deleteBook(@PathVariable("id") Long id) {
        bookService.deleteById(id);
        return "redirect:/admin/books";
    }
}
