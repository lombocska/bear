package io.lombocska.bear.domain.dto;

import io.lombocska.bear.domain.entity.Book;
import io.lombocska.bear.domain.entity.Category;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import java.time.Instant;
import java.util.UUID;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BookDTO {

    @NonNull
    private UUID id;

    @NonNull
    private String title;

    @NonNull
    private Instant createdAt;

    @NonNull
    private Long sold;

    @NonNull
    private String writer;

    @NonNull
    private Category category;

    @NonNull
    private String description;

    public static BookDTO create(Book book) {
        return BookDTO.builder()
                .id(book.getId())
                .title(book.getTitle())
                .createdAt(book.getCreatedAt())
                .sold(book.getSold())
                .writer(book.getWriter().getLastName())
                .category(book.getCategory())
                .description(book.getDescription())
                .build();
    }

}
