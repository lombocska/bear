package io.lombocska.bear.domain;

import io.lombocska.bear.domain.dto.BookDTO;
import io.lombocska.bear.domain.service.BookService;
import io.lombocska.bear.domain.service.CreateBookCommand;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class BookFacade {

    private BookService bookService;

    public BookFacade(BookService bookService) {
        this.bookService = bookService;
    }

    public List<BookDTO> findAll() {
        return (bookService.findAll());
    }

    public BookDTO findOne(UUID bookId) {
        return bookService.findOne(bookId);
    }

    public BookDTO create(CreateBookCommand createBookCommand) {
        return bookService.create(createBookCommand);
    }

}
