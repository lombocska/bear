package io.lombocska.bear.domain.service;

import io.lombocska.bear.domain.entity.Category;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
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
