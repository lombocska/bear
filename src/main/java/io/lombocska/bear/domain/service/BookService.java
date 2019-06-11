package io.lombocska.bear.domain.service;

import io.lombocska.bear.domain.dto.BookDTO;
import io.lombocska.bear.domain.entity.Book;
import io.lombocska.bear.domain.repository.BookRepository;
import io.lombocska.bear.util.dispatcher.KafkaDispatcher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional("transactionManager")
public class BookService {

    private final BookRepository bookRepository;
    private final KafkaDispatcher kafkaDispatcher;
    private final BookFactory factory;

    @Autowired
    public BookService(BookRepository bookRepository, KafkaDispatcher kafkaDispatcher, BookFactory factory) {
        this.bookRepository = bookRepository;
        this.kafkaDispatcher = kafkaDispatcher;
        this.factory = factory;
    }

    public List<BookDTO> findAll() {
        return toDTO(bookRepository.findAll());
    }

    public BookDTO findOne(UUID bookId) {
        return BookDTO.create(bookRepository.findOneById(bookId));
    }

    public BookDTO create(CreateBookCommand createBookCommand) {
        var book = factory.build(createBookCommand);
        var saved = bookRepository.save(book);
        var event = BookEvent.created(saved);
        kafkaDispatcher.send(event);
        return BookDTO.create(saved);
    }

    private List<BookDTO> toDTO(List<Book> books) {
        return books.stream()
                .map(BookDTO::create)
                .collect(Collectors.toList());
    }

}
