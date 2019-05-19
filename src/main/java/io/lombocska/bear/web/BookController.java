package io.lombocska.bear.web;

import io.lombocska.bear.domain.BookFacade;
import io.lombocska.bear.domain.dto.BookDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value = "/api")
public class BookController {

    private BookFacade bookFacade;

    @Autowired
    public BookController(BookFacade bookFacade) {
        this.bookFacade = bookFacade;
    }

    @GetMapping
    public List<BookDTO> listAllBooks() {
        return bookFacade.findAll();
    }
}
