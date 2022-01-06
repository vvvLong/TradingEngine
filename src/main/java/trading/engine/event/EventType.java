package trading.engine.event;

public enum EventType {
    MARKET("Market"), SIGNAL("Signal"), ORDER("Order"), FILL("Fill");

    private final String name;

    EventType(String s) {
        name = s;
    }

    public String getName() {
        return name;
    }

}
