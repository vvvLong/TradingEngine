package trading.engine.event;

import java.util.Date;

public class FillEvent implements Event{

    private final EventType eventType;
    private final Date timestamp;
    private final String exchange;
    private final String symbol;
    private final DirectionType directionType;
    private final double fillValue;  // total value of the filled positions
    private final double fillCost;  // total trading cost of the fill

    public FillEvent(Date timestamp, String exchange, String symbol, DirectionType directionType, double fillValue, double fillCost) {
        this.eventType = EventType.FILL;
        this.timestamp = timestamp;
        this.exchange = exchange;
        this.symbol = symbol;
        this.directionType = directionType;
        this.fillValue = fillValue;
        this.fillCost = fillCost;
    }

    @Override
    public EventType getEventType() {
        return eventType;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public String getExchange() {
        return exchange;
    }

    public String getSymbol() {
        return symbol;
    }

    public DirectionType getDirectionType() {
        return directionType;
    }

    public double getFillValue() {
        return fillValue;
    }

    public double getFillCost() {
        return fillCost;
    }

    @Override
    public String toString() {
        return "FillEvent{" +
                "timestamp=" + timestamp +
                ", exchange='" + exchange + '\'' +
                ", symbol='" + symbol + '\'' +
                ", directionType=" + directionType +
                ", fillValue=" + fillValue +
                ", fillCost=" + fillCost +
                '}';
    }

}
