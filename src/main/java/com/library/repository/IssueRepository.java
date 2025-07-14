package com.library.repository;

import com.library.model.Issue;
import com.library.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface IssueRepository extends JpaRepository<Issue, Long> {
        @Query("SELECT i FROM Issue i WHERE i.returnDate IS NULL")
        List<Issue> findAllCurrentlyLoaned();

        List<Issue> findByReturnDateIsNull();

        List<Issue> findByUserAndReturnDateIsNull(User user);
        List<Issue> findByUserAndReturnDateIsNotNull(User user);

        List<Issue> findByUser(User user);



}
