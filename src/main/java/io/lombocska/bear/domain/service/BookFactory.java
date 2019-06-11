package io.lombocska.bear.domain.service;

import io.lombocska.bear.domain.entity.Book;
import io.lombocska.bear.domain.entity.Writer;
import io.lombocska.bear.domain.repository.WriterRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;

@Service
public class BookFactory {

    private final WriterRepository writerRepository;

    @Autowired
    public BookFactory(WriterRepository writerRepository) {
        this.writerRepository = writerRepository;
    }

    public Book build(CreateBookCommand createBookCommand) {
        Optional<Writer> writer = writerRepository.findById(createBookCommand.getWriter());
        var now = Instant.now();
        return Book.builder()
                .category(createBookCommand.getCategory())
                .createdAt(now)
                .description(createBookCommand.getDescription())
                .sold(createBookCommand.getSold())
                .title(createBookCommand.getTitle())
                .writer(writer.get())
                .build();

    }

}
