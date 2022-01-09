package trading.engine.event;

import com.google.inject.Inject;

import java.time.LocalDate;
import java.util.Objects;

public class FillEvent implements Event{

    private final EventType eventType;
    private final LocalDate timestamp;
    private final String symbol;
    private final DirectionType directionType;
    private final double price;
    private final int quantity;
    private final double commission;
    private final String exchange;

    @Inject
    public FillEvent(LocalDate timestamp, String symbol, DirectionType directionType, double price, int quantity, double commission, String exchange) {
        this.eventType = EventType.FILL;
        this.timestamp = timestamp;
        this.symbol = symbol;
        this.directionType = directionType;
        this.price = price;
        this.quantity = quantity;
        this.commission = commission;
        this.exchange = exchange;
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

    public DirectionType getDirectionType() {
        return directionType;
    }

    public double getPrice() {
        return price;
    }

    public int getQuantity() {
        return quantity;
    }

    public double getCommission() {
        return commission;
    }

    public String getExchange() {
        return exchange;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FillEvent fillEvent = (FillEvent) o;
        return Double.compare(fillEvent.price, price) == 0 && quantity == fillEvent.quantity && Double.compare(fillEvent.commission, commission) == 0 && eventType == fillEvent.eventType && Objects.equals(timestamp, fillEvent.timestamp) && Objects.equals(symbol, fillEvent.symbol) && directionType == fillEvent.directionType && Objects.equals(exchange, fillEvent.exchange);
    }

    @Override
    public int hashCode() {
        return Objects.hash(eventType, timestamp, symbol, directionType, price, quantity, commission, exchange);
    }

    @Override
    public String toString() {
        return "FillEvent{" +
                "eventType=" + eventType +
                ", timestamp=" + timestamp +
                ", symbol='" + symbol + '\'' +
                ", directionType=" + directionType +
                ", price=" + price +
                ", quantity=" + quantity +
                ", commission=" + commission +
                ", exchange='" + exchange + '\'' +
                '}';
    }
}
