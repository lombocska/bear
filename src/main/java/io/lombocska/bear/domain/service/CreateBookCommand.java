package io.lombocska.bear.domain.service;

import io.lombocska.bear.domain.entity.Category;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
public class CreateBookCommand {

    @NonNull
    private String title;

    @NonNull
    private LocalDateTime createdAt;

    @NonNull
    private Long sold;

    @NonNull
    private UUID writer;

    @NonNull
    private Category category;

    @NonNull
    private String description;

}
