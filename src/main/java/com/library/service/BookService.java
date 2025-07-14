package com.library.service;

import com.library.model.Book;
import com.library.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class BookService {

    @Autowired
    private BookRepository bookRepository;

    public List<Book> getAllBooks() {
        return bookRepository.findAll();
    }

    public Optional<Book> getBookById(Long id) {
        return bookRepository.findById(id);
    }

    public Book saveBook(Book book) {
        return bookRepository.save(book);
    }

    public void deleteBook(Long id) {
        bookRepository.deleteById(id);
    }

    public List<Book> findBooks(String title, String author) {
        if ((title == null || title.isEmpty()) && (author == null || author.isEmpty())) {
            return getAllBooks();
        }
        return getAllBooks().stream()
                .filter(book -> (title == null || title.isEmpty() || book.getTitle().toLowerCase().contains(title.toLowerCase())))
                .filter(book -> (author == null || author.isEmpty() || book.getAuthor().toLowerCase().contains(author.toLowerCase())))
                .collect(Collectors.toList());
    }

    public Optional<Book> findById(Long id) {
        return Optional.ofNullable(bookRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid book Id:" + id)));
    }

    public void save(Book book) {
        if (book != null) {
            bookRepository.save(book);
        }
    }

    public void deleteById(Long id) {
        if (id != null) {
            bookRepository.deleteById(id);
        }
    }
}
