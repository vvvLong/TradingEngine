package trading.engine.event;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class EventTest {

    @Test
    @DisplayName("event should be printed correctly")
    public void printMarketEvent() {
        Event e = new MarketEvent();
        System.out.print(e);
        System.out.print("\n");
    }

    @Test
    @DisplayName("event should be printed correctly")
    public void printOrderEvent() {
        Event e = new OrderEvent(OrderType.LIMIT, "AAPL", DirectionType.LONG, 10);
        System.out.print(e);
        System.out.print("\n");
    }

    @Test
    @DisplayName("event should be printed correctly")
    public void printFillEvent() {
        Event e = new FillEvent( new Date(),"NASDAQ", "AAPL", DirectionType.LONG, 1000, 10);
        System.out.print(e);
        System.out.print("\n");
    }

    @Test
    @DisplayName("event queue should be printed correctly")
    public void printEventQueue() {
        Event e1 = new MarketEvent();
        Event e2 = new OrderEvent(OrderType.LIMIT, "AAPL", DirectionType.LONG, 10);
        Event e3 = new FillEvent( new Date(),"NASDAQ", "AAPL", DirectionType.LONG, 1000, 10);
        List<Event> list = new ArrayList<>();
        list.add(e1);
        list.add(e2);
        list.add(e3);

        for (Event e : list) {
            System.out.print(e);
            System.out.print("\n");
        }

    }

}
