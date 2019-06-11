package io.lombocska.bear.common;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.NoArgsConstructor;

import java.time.Instant;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class BearDomainEvent<T extends BearEvent> extends DomainEvent<T> {

    @Builder
    public BearDomainEvent(final String aggregationId,
                           final String type,
                           final T payload,
                           final String source,
                           final Instant createdAt) {
        super(aggregationId, source, createdAt, payload, type);
    }

    @JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.EXTERNAL_PROPERTY, property = "type")
    @JsonSubTypes( {
            @JsonSubTypes.Type(name = "RuleCreated", value = BookCreatedEvent.class),
            @JsonSubTypes.Type(name = "RuleDeleted", value = BookDeletedEvent.class)
    })
    @Override
    public T getPayload() {
        return super.getPayload();
    }

}
