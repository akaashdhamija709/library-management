package com.library.controller;

import com.library.model.Issue;
import com.library.service.IssueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/issues")
public class IssueController {

    @Autowired
    private IssueService issueService;

    /*@PostMapping("/issue")
    public Issue issue(@RequestParam Long bookId, @RequestParam Long userId) {
        return issueService.issueBook(bookId, userId);
    }*/

    /*@PostMapping("/return")
    public Issue returnBook(@RequestParam Long issueId) {
        return issueService.markReturned(issueId);
    }*/

}
