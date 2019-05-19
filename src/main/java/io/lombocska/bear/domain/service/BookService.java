package io.lombocska.bear.domain.service;

import io.lombocska.bear.domain.dto.BookDTO;
import io.lombocska.bear.domain.entity.Book;
import io.lombocska.bear.domain.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class BookService {

    private BookRepository bookRepository;

    @Autowired
    public BookService(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    public List<BookDTO> findAll() {
        return toDTO(bookRepository.findAll());
    }

    private List<BookDTO> toDTO(List<Book> books) {
        return books.stream()
                .map(BookDTO::create)
                .collect(Collectors.toList());
    }

}
