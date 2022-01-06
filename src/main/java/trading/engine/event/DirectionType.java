package trading.engine.event;

public enum DirectionType {
    LONG("Long"), SHORT("Short");

    private final String name;

    DirectionType(String s) {
        name = s;
    }

    @Override
    public String toString() {
        return name;
    }
}
