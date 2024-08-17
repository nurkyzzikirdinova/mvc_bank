package zikirdinova.mvc.enums;

public enum Currency {
    USD("Доллар"),
    EUR("Евро"),
    RUB("Рубль"),
    KGS("Сом");

    private final String description;

    Currency(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
