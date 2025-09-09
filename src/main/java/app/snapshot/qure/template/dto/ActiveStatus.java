package app.snapshot.qure.template.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum ActiveStatus {
    Y("Y"),
    N("N");

    private final String value;

    ActiveStatus(String value) {
        this.value = value;
    }

    @JsonCreator
    public static ActiveStatus fromValue(String value) {
        for (ActiveStatus status : ActiveStatus.values()) {
            if (status.value.equals(value)) {
                return status;
            }
        }
        return null;
    }

    @JsonValue
    public String getValue() {
        return value;
    }
}