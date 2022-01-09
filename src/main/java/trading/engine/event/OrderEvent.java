package trading.engine.event;


import java.time.LocalDate;
import java.util.Objects;

public class OrderEvent implements Event{

    private final EventType eventType;
    private final LocalDate timestamp;
    private final String symbol;
    private final OrderType orderType;
    private final DirectionType directionType;
    private final int quantity;

    public OrderEvent(LocalDate timestamp, String symbol, OrderType orderType, DirectionType directionType, int quantity) {
        this.eventType = EventType.ORDER;
        this.orderType = orderType;
        this.symbol = symbol;
        this.directionType = directionType;
        this.quantity = quantity;
        this.timestamp = timestamp;
    }

    @Override
    public EventType getEventType() {
        return eventType;
    }

    @Override
    public LocalDate getTimestamp() {
        return timestamp;
    }

    public OrderType getOrderType() {
        return orderType;
    }

    @Override
    public String getSymbol() {
        return symbol;
    }

    public DirectionType getDirectionType() {
        return directionType;
    }

    public int getQuantity() {
        return quantity;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OrderEvent that = (OrderEvent) o;
        return quantity == that.quantity && eventType == that.eventType && orderType == that.orderType && Objects.equals(symbol, that.symbol) && directionType == that.directionType && Objects.equals(timestamp, that.timestamp);
    }

    @Override
    public int hashCode() {
        return Objects.hash(eventType, orderType, symbol, directionType, quantity, timestamp);
    }

    @Override
    public String toString() {
        return "OrderEvent{" +
                "eventType=" + eventType +
                ", timestamp=" + timestamp +
                ", symbol='" + symbol + '\'' +
                ", orderType=" + orderType +
                ", directionType=" + directionType +
                ", quantity=" + quantity +
                '}';
    }

}
