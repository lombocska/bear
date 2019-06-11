package io.lombocska.bear.domain.service;

import io.lombocska.bear.common.BearDomainEvent;
import io.lombocska.bear.common.BookCreatedEvent;
import io.lombocska.bear.domain.entity.Book;

public class BookEvent {

    public static BearDomainEvent<BookCreatedEvent> created(Book book) {
        var bookCreated = new BookCreatedEvent(book.getId(), book.getCreatedAt());

        return BearDomainEvent.<BookCreatedEvent>builder()
                .aggregationId(bookCreated.getAggregationId().toString())
                .createdAt(bookCreated.getCreatedAt())
                .payload(bookCreated)
                .source("BEAR")
                .type(bookCreated.getEventType().toString())
                .build();

    }

}
