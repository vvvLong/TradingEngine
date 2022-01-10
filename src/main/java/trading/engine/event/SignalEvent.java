package trading.engine.event;

import com.google.inject.Inject;

import java.time.LocalDate;
import java.util.Objects;

public class SignalEvent implements Event {
    private final EventType eventType;
    private final LocalDate timestamp;
    private final String symbol;
    private final String strategyID;
    private final DirectionType directionType;
    private final double strength;

    @Inject
    public SignalEvent(LocalDate timestamp, String symbol, String strategyID, DirectionType directionType, double strength) {
        this.eventType = EventType.SIGNAL;
        this.strategyID = strategyID;
        this.symbol = symbol;
        this.timestamp = timestamp;
        this.directionType = directionType;
        this.strength = strength;
    }

    @Override
    public EventType getEventType() {
        return eventType;
    }

    @Override
    public LocalDate getTimestamp() {
        return timestamp;
    }

    @Override
    public String getSymbol() {
        return symbol;
    }

    public String getStrategyID() {
        return strategyID;
    }

    public DirectionType getDirectionType() {
        return directionType;
    }

    public double getStrength() {
        return strength;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SignalEvent that = (SignalEvent) o;
        return Double.compare(that.strength, strength) == 0 && eventType == that.eventType && Objects.equals(timestamp, that.timestamp) && Objects.equals(symbol, that.symbol) && Objects.equals(strategyID, that.strategyID) && directionType == that.directionType;
    }

    @Override
    public int hashCode() {
        return Objects.hash(eventType, timestamp, symbol, strategyID, directionType, strength);
    }

    @Override
    public String toString() {
        return "SignalEvent{" +
                "eventType=" + eventType +
                ", timeStamp=" + timestamp +
                ", symbol='" + symbol + '\'' +
                ", strategyID='" + strategyID + '\'' +
                ", directionType=" + directionType +
                ", strength=" + strength +
                '}';
    }
}
