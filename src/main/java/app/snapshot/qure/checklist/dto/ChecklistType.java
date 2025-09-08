package app.snapshot.qure.checklist.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum ChecklistType {
    BOOL("BOOL"),
    NUM("NUM"),
    TEXT("TEXT"),
    IMAGE("IMAGE");

    private final String value;

    ChecklistType(String value) {
        this.value = value;
    }

    @JsonValue
    public String getValue() {
        return value;
    }

    @JsonCreator
    public static ChecklistType fromValue(String value) {
        for (ChecklistType type : values()) {
            if (type.value.equalsIgnoreCase(value)) {
                return type;
            }
        }
        throw new IllegalArgumentException("Unknown checklist type: " + value);
    }
}
