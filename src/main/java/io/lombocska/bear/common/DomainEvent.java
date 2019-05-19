package io.lombocska.bear.common;

import java.time.LocalDateTime;
import java.util.UUID;

public class DomainEvent<T> {

    private UUID aggregationId;

    private String source;

    private LocalDateTime createdAt;

    private T payload;

}
