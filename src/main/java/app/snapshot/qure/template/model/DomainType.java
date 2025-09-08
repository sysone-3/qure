package app.snapshot.qure.template.model;

public enum DomainType {
    CLEANING("CLEANING"),
    PATROL("PATROL"),
    FIRE("FIRE");

    private final String value;

    DomainType(String value) {
        this.value = value;
    }

    public String getValue() { return value; }
}
