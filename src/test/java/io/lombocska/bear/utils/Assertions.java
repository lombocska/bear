package io.lombocska.bear.utils;

import io.lombocska.bear.common.BearDomainEvent;
import io.lombocska.bear.common.BearEventType;
import io.lombocska.bear.common.BookCreatedEvent;
import io.lombocska.bear.domain.entity.Book;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class Assertions {

    public static void assertSavedBook(BearDomainEvent<BookCreatedEvent> bearDomainEvent, Book book) {
        assertThat("BEAR", is(bearDomainEvent.getSource()));
        assertThat(BearEventType.BOOK_CREATED.toString(), is(bearDomainEvent.getType()));
        assertThat(book.getId().toString(), is(bearDomainEvent.getAggregationId()));
        var createdEvent = bearDomainEvent.getPayload();
        assertThat(book.getCreatedAt(), is(createdEvent.getCreatedAt()));
    }

}
