package trading.engine.event;

public class MarketEvent implements Event{

    private final EventType eventType;

    public MarketEvent() {
        this.eventType = EventType.MARKET;
    }

    @Override
    public EventType getEventType() {
        return eventType;
    }

    @Override
    public String toString() {
        return "MarketEvent{}";
    }

}
