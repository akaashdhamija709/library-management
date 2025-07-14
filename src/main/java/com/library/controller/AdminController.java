package com.library.controller;

import com.library.model.Book;
import com.library.model.Issue;
import com.library.repository.IssueRepository;
import com.library.service.BookService;
import com.library.service.IssueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
@PreAuthorize("hasRole('ADMIN')")
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private IssueRepository issueRepository;

    @Autowired
    private IssueService issueService;

    @Autowired
    private BookService bookService;

    @GetMapping("/returns")
    public String returnDashboard(Model model) {
        List<Issue> pending = issueRepository.findByReturnDateIsNull();
        model.addAttribute("issues", pending);
        return "admin-returns";
    }

    @GetMapping("/returns/confirm/{id}")
    public String confirmReturn(@PathVariable Long id, Model model) {
        Issue issue = issueRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid id"));
        double penalty = issueService.calculate(
                issue.getExpectedReturnDate(),
                LocalDate.now()
        );
        model.addAttribute("issue", issue);
        model.addAttribute("penalty", String.format("%.2f", penalty));
        return "return-confirm";
    }

    @PostMapping("/returns/confirm/{id}")
    public String completeReturn(@PathVariable Long id,
                                 RedirectAttributes redirectAttrs) {
        Issue issue = issueRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid id"));
        double penalty = issueService.completeReturn(issue);
        redirectAttrs.addFlashAttribute("message", "Return processed, amount received: $%.2f" + penalty);
        return "redirect:/admin/returns";
    }

    @GetMapping("/books/new")
    public String showCreateBookForm(Model model) {
        model.addAttribute("book", new Book());
        return "admin-book-form";
    }

    @PostMapping("/books/save")
    public String saveBook(@ModelAttribute Book book, RedirectAttributes ra) {
        book.setAvailable("Y");
        bookService.saveBook(book);
        ra.addFlashAttribute("message", "New book added successfully.");
        return "redirect:/admin/books";
    }

    @GetMapping("/books")
    public String adminBookList(Model model) {
        List<Book> books = bookService.getAllBooks();
        List<Issue> currentIssues = issueRepository.findByReturnDateIsNull();

        Map<Long, Issue> issueMap = currentIssues.stream()
                .collect(Collectors.toMap(i -> i.getBook().getId(), i -> i));

        model.addAttribute("books", books);
        model.addAttribute("issueMap", issueMap);
        return "admin-book-list";
    }

}
