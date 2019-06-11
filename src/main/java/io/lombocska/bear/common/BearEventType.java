package io.lombocska.bear.common;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.CaseFormat;

public enum BearEventType {
    @JsonProperty("BookCreated")
    BOOK_CREATED,
    @JsonProperty("BookDeleted")
    BOOK_DELETED;

    @Override
    public String toString() {
        return CaseFormat.UPPER_UNDERSCORE.to(CaseFormat.UPPER_CAMEL, name());
    }
}
