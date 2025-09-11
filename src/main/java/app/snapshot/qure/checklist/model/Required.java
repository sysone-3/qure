package app.snapshot.qure.checklist.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum Required {
    Y("Y"),
    N("N");

    private final String value;

    Required(String value) {
        this.value = value;
    }

    @JsonCreator
    public static Required fromValue(String value) {
        for (Required status : Required.values()) {
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