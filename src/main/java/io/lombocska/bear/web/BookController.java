package io.lombocska.bear.web;

import io.lombocska.bear.domain.BookFacade;
import io.lombocska.bear.domain.dto.BookDTO;
import io.lombocska.bear.domain.service.CreateBookCommand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping(value = "/api")
public class BookController {

    private BookFacade bookFacade;

    @Autowired
    public BookController(BookFacade bookFacade) {
        this.bookFacade = bookFacade;
    }

    @GetMapping
    public List<BookDTO> index() {
        return bookFacade.findAll();
    }

    @GetMapping(value = "/{bookId}")
    public BookDTO show(@PathVariable("bookId") UUID bookId) {
        return bookFacade.findOne(bookId);
    }


    @PostMapping
    public BookDTO create(@Valid @RequestBody CreateBookCommand createBookCommand) {
        return bookFacade.create(createBookCommand);
    }

}
