package io.lombocska.bear.domain.repository;

import io.lombocska.bear.domain.entity.Book;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface BookRepository extends JpaRepository<Book, UUID> {

}
