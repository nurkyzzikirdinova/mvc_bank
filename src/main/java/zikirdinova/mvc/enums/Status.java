package zikirdinova.mvc.enums;

public enum Status {
    CREATED("СОЗДАН"),
    ISSUED("ВЫДАН");

    private final String description;

    Status(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
