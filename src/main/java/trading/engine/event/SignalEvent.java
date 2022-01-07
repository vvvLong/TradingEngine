package trading.engine.event;

import java.time.LocalDate;

public class SignalEvent implements Event {
    private final EventType eventType;
    private final String strategyID;
    private final String symbol;
    private final LocalDate timeStamp;
    private final DirectionType directionType;
    private final double strength;

    public SignalEvent(String strategyID, String symbol, LocalDate timeStamp, DirectionType directionType, double strength) {
        this.eventType = EventType.SIGNAL;
        this.strategyID = strategyID;
        this.symbol = symbol;
        this.timeStamp = timeStamp;
        this.directionType = directionType;
        this.strength = strength;
    }

    @Override
    public EventType getEventType() {
        return eventType;
    }

    public String getStrategyID() {
        return strategyID;
    }

    public String getSymbol() {
        return symbol;
    }

    public LocalDate getTimeStamp() {
        return timeStamp;
    }

    public DirectionType getDirectionType() {
        return directionType;
    }

    public double getStrength() {
        return strength;
    }

    @Override
    public String toString() {
        return "SignalEvent{" +
                "eventType=" + eventType +
                ", strategyID='" + strategyID + '\'' +
                ", symbol='" + symbol + '\'' +
                ", timeStamp=" + timeStamp +
                ", directionType=" + directionType +
                ", strength=" + strength +
                '}';
    }
}
