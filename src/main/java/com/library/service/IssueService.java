package com.library.service;

import com.library.model.Book;
import com.library.model.User;
import com.library.model.Issue;
import com.library.repository.BookRepository;
import com.library.repository.IssueRepository;
import com.library.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;

@Service
public class IssueService {

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private IssueRepository issueRepository;

    @Transactional
    public Optional<Book> issueBook(Long bookId, String username, int periodDays) {
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new EntityNotFoundException("Book not found"));
        if (!"Y".equalsIgnoreCase(book.getAvailable())) {
            throw new IllegalStateException("Book is not available");
        }

        User user = userRepository.findByUsername(username).
                orElseThrow(() -> new EntityNotFoundException("User not found"));

        LocalDate today = LocalDate.now();
        LocalDate expectedReturnDate = today.plusDays(periodDays);

        Issue issue = new Issue();
        issue.setBook(book);
        issue.setUser(user);
        issue.setLoanDate(today.toString());
        issue.setExpectedReturnDate(expectedReturnDate.toString());
        issue.setReturnDate(null);
        book.setAvailable("N");

        issueRepository.save(issue);
        bookRepository.save(book);

        return Optional.of(book);
    }

    @Transactional
    public double completeReturn(Issue issue) {
        issue.setReturnDate(LocalDate.now().toString());
        double penalty = calculate(issue.getExpectedReturnDate(), LocalDate.now());
        Book book = issue.getBook();
        book.setAvailable("Y");
        issueRepository.save(issue);
        bookRepository.save(book);
        return penalty;
    }

    public double calculate(String expectedReturn, LocalDate actualReturn) {
        LocalDate due = LocalDate.parse(expectedReturn, DateTimeFormatter.ISO_DATE);
        long daysLate = ChronoUnit.DAYS.between(due, actualReturn);
        if (daysLate <= 0) return 0;
        return daysLate * 1.50;  // example: $1.50/day late
    }

    public List<Issue> findLatestIssuesForBooks(List<Long> bookId) {
        return issueRepository.findAllCurrentlyLoaned();
    }
}
