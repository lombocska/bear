package io.lombocska.bear.domain;

import io.lombocska.bear.domain.dto.BookDTO;
import io.lombocska.bear.domain.service.BookService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BookFacade {

    private BookService bookService;

    public BookFacade(BookService bookService) {
        this.bookService = bookService;
    }

    public List<BookDTO> findAll() {
        return (bookService.findAll());
    }

}
