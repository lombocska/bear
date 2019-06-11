package io.lombocska.bear.domain.repository;

import io.lombocska.bear.domain.entity.Writer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface WriterRepository extends JpaRepository<Writer, UUID> {

//    Writer findOneByIdOrFail(UUID writerId);

}
