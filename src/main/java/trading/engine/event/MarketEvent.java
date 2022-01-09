package trading.engine.event;

import java.time.LocalDate;

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
        return "MarketEvent{" +
                "eventType=" + eventType +
                '}';
    }
}
