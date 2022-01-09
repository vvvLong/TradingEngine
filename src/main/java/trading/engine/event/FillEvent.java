package trading.engine.event;

import com.google.inject.Inject;

import java.time.LocalDate;
import java.util.Date;

public class FillEvent implements Event{

    private final EventType eventType;
    private final LocalDate timestamp;
    private final String exchange;
    private final String symbol;
    private final DirectionType directionType;
    private final double fillValue;  // total value of the filled positions
    private final double fillCost;  // total trading cost of the fill
    private final int fillQuantity;

    @Inject
    public FillEvent(LocalDate timestamp, String exchange, String symbol, DirectionType directionType, double fillValue, double fillCost, int fillQuantity) {
        this.eventType = EventType.FILL;
        this.timestamp = timestamp;
        this.exchange = exchange;
        this.symbol = symbol;
        this.directionType = directionType;
        this.fillValue = fillValue;
        this.fillCost = fillCost;
        this.fillQuantity = fillQuantity;
    }

    @Override
    public EventType getEventType() {
        return eventType;
    }

    public LocalDate getTimestamp() {
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

    public int getFillQuantity() {
        return fillQuantity;
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
