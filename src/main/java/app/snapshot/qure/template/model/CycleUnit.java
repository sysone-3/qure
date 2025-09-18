package app.snapshot.qure.template.model;
// 작성자: 최이서

public enum CycleUnit {
    DAY("DAY"),
    WEEK("WEEK"),
    MONTH("MONTH"),
    YEAR("YEAR");

    private final String value;

    CycleUnit(String value) {
        this.value = value;
    }

    public String getValue() { return value; }
}
