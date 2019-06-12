package io.lombocska.bear.utils.model;

import io.lombocska.bear.domain.entity.Book;
import io.lombocska.bear.domain.entity.Category;
import io.lombocska.bear.domain.entity.Writer;
import io.lombocska.bear.domain.service.CreateBookCommand;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Slf4j
public class TestModelBuilder {

    public static Writer build(List<Book> books) {
        return Writer.builder()
                .lastName("FantasyLastName")
                .firstName("FirstName")
                .birthDate(LocalDate.of(1992, 06, 22))
                .books(books)
                .build();
    }

    public static CreateBookCommand build(Writer writer) {
        return CreateBookCommand.builder()
                .title("TestBookTitle")
                .createdAt(LocalDateTime.of(2019, 06, 22, 06, 22))
                .sold(1000L)
                .writer(writer.getId())
                .category(Category.CRIMI)
                .description("lorem ipsum test description")
                .build();
    }

}
