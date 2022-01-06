package trading.engine.event;

public enum OrderType {
    MARKET("Market"), LIMIT("Limit");

    private final String name;

    OrderType(String s) {
        name = s;
    }

    @Override
    public String toString() {
        return name;
    }
}
