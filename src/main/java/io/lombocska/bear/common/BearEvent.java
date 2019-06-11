package io.lombocska.bear.common;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.UUID;


@Getter
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.EXISTING_PROPERTY, property = "eventType")
@JsonSubTypes( {
        @JsonSubTypes.Type(name = "BookCreated", value = BookCreatedEvent.class),
        @JsonSubTypes.Type(name = "BookDeleted", value = BookDeletedEvent.class)
})
@AllArgsConstructor
public abstract class BearEvent {

    protected UUID aggregationId;
    protected BearEventType eventType;

}
