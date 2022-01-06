package trading.engine.event;


public class OrderEvent implements Event{

    private final EventType eventType;
    private final OrderType orderType;
    private final String symbol;
    private final DirectionType directionType;
    private final int quantity;

    public OrderEvent(OrderType orderType, String symbol, DirectionType directionType, int quantity) {
        this.eventType = EventType.ORDER;
        this.orderType = orderType;
        this.symbol = symbol;
        this.directionType = directionType;
        this.quantity = quantity;
    }

    @Override
    public EventType getEventType() { return  eventType; }

    public OrderType getOrderType() {
        return orderType;
    }

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
    public String toString() {
        return "OrderEvent{" +
                "orderType=" + orderType +
                ", symbol='" + symbol + '\'' +
                ", directionType=" + directionType +
                ", quantity=" + quantity +
                '}';
    }

}
