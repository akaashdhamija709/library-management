package com.library.controller;

import com.library.model.Book;
import com.library.service.IssueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.ArrayList;
import java.util.Optional;

@Controller
public class IssueBookController {

    @Autowired
    private IssueService issueService;

    @PostMapping("/books/issue/submit")
    public String doProceedToPayment(
            @RequestParam Long bookId,
            @RequestParam int rentalPeriod,
            @AuthenticationPrincipal UserDetails userDetails,
            RedirectAttributes redirectAttrs) {

        if (userDetails == null) {
            redirectAttrs.addFlashAttribute("redirectAfterLogin", "/books/issue/" + bookId);
            return "redirect:/login";
        }
        String username = userDetails.getUsername();
        Optional<Book> book = issueService.issueBook(bookId, username, rentalPeriod);

        redirectAttrs.addFlashAttribute("issuedBookTitle", book.get().getTitle());
        redirectAttrs.addFlashAttribute("issuedRentalPeriod", rentalPeriod);

        // Redirect back to issue page to show modal
        return "redirect:/books/issue/" + bookId;
    }

}
