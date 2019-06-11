package io.lombocska.bear.common;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.Value;

import java.beans.ConstructorProperties;
import java.time.Instant;
import java.util.UUID;

@Value
public class BookCreatedEvent extends BearEvent {

    private Instant createdAt;

    @JsonCreator
    @ConstructorProperties( {"aggregationId", "createdAt"})
    public BookCreatedEvent(final UUID aggregateId, Instant createdAt) {
        super(aggregateId, BearEventType.BOOK_CREATED);
        this.createdAt = createdAt;
    }

}
