package trading.engine.event;

import java.time.LocalDate;

public class MarketEvent implements Event{

    private final EventType eventType;
    private final LocalDate timeStamp;

    public MarketEvent(LocalDate timeStamp) {
        this.eventType = EventType.MARKET;
        this.timeStamp = timeStamp;
    }

    @Override
    public EventType getEventType() {
        return eventType;
    }

    public LocalDate getTimeStamp() {
        return timeStamp;
    }

    @Override
    public String toString() {
        return "MarketEvent{" +
                "eventType=" + eventType +
                ", timeStamp=" + timeStamp +
                '}';
    }
}
