package io.lombocska.bear.common;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.Value;

import java.beans.ConstructorProperties;
import java.util.UUID;

@Value
public class BookDeletedEvent extends BearEvent {

    @JsonCreator
    @ConstructorProperties( {"aggregationId"})
    public BookDeletedEvent(final UUID aggregationId) {
        super(aggregationId, BearEventType.BOOK_DELETED);
    }

}
