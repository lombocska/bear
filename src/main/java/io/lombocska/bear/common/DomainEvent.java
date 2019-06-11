package io.lombocska.bear.common;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;

import java.time.Instant;

@Getter
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class DomainEvent<T> {

    @NonNull
    private String aggregationId;

    @NonNull
    private String source;

    @NonNull
    private Instant createdAt;

    @NonNull
    private T payload;

    @NonNull
    private String type;

    protected DomainEvent(
            String source,
            String aggregateId,
            String type,
            T payload) {
        this.type = requireNotBlank(type, "type cannot be blank");
        this.source = requireNotBlank(source, "source cannot be blank");
        this.createdAt = Instant.now();
        this.payload = requireNotNull(payload, "payload cannot be null");
        this.aggregationId = aggregateId;
    }

    protected DomainEvent() {
    }

    private static String requireNotBlank(String validated, String message) {
        if (null == validated || validated.isEmpty()) {
            throw new IllegalArgumentException(message);
        }
        return validated;
    }

    private static <T> T requireNotNull(T validated, String message) {
        if (null == validated) {
            throw new IllegalArgumentException(message);
        }
        return validated;
    }

}
