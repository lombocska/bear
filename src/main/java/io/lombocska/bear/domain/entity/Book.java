package io.lombocska.bear.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Entity
@Table
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @NonNull
    private String title;

    @NonNull
    private LocalDateTime createdAt;

    @NonNull
    private Long sold;

    @ManyToOne(fetch = FetchType.LAZY, optional = false, targetEntity = Writer.class)
    @JoinColumn(name = "writer_id")
    private Writer writer;

    @NonNull
    @Enumerated(value = EnumType.STRING)
    private Category category;

    @NonNull
    private String description;

}
