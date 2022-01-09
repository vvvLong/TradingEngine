package trading.engine.event;

import java.time.LocalDate;
import java.util.Objects;

public class MarketEvent implements Event{

    private final EventType eventType;
    private final LocalDate timestamp;
    private final String symbol;

    public MarketEvent(LocalDate timestamp, String symbol) {
        this.eventType = EventType.MARKET;
        this.timestamp = timestamp;
        this.symbol = symbol;
    }

    @Override
    public LocalDate getTimestamp() {
        return timestamp;
    }

    @Override
    public String getSymbol() {
        return symbol;
    }

    @Override
    public EventType getEventType() {
        return eventType;
    }

    @Override
    public String toString() {
        return "MarketEvent{" +
                "eventType=" + eventType +
                ", timeStamp=" + timestamp +
                ", symbol='" + symbol + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MarketEvent that = (MarketEvent) o;
        return eventType == that.eventType && timestamp.equals(that.timestamp) && symbol.equals(that.symbol);
    }

    @Override
    public int hashCode() {
        return Objects.hash(eventType, timestamp, symbol);
    }
}
