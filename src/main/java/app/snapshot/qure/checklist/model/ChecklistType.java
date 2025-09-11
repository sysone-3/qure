package app.snapshot.qure.checklist.model;

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

    public static ChecklistType fromFormType(String formType) {
        return switch (formType) {
            case "BOOLEAN" -> BOOL;
            case "NUMBER"  -> NUM;
            case "PHOTO"   -> IMAGE;
            default        -> TEXT;
        };
    }
}

