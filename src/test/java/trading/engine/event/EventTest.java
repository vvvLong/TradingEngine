package trading.engine.event;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.ArrayDeque;

public class EventTest {

    @Test
    @DisplayName("print market event")
    public void printMarketEvent() {
        Event e = new MarketEvent(LocalDate.of(2022, 1, 1), "aapl");
        System.out.print(e);
        System.out.print("\n");
    }

    @Test
    @DisplayName("print order event")
    public void printOrderEvent() {
        Event e = new OrderEvent(LocalDate.of(2022, 1, 1), "AAPL", OrderType.LIMIT,  DirectionType.LONG, 10);
        System.out.print(e);
        System.out.print("\n");
    }

    @Test
    @DisplayName("print fill event")
    public void printFillEvent() {
        Event e = new FillEvent( LocalDate.of(2022, 1, 1),"AAPL", DirectionType.LONG, 100, 20, 10, "NASDAQ");
        System.out.print(e);
        System.out.print("\n");
    }

    @Test
    @DisplayName("print event queue")
    public void printEventQueue() {
        MarketEvent e1 = new MarketEvent(LocalDate.of(2022, 1, 1), "aapl");
        OrderEvent e2 = new OrderEvent(LocalDate.of(2022, 1, 1), "AAPL", OrderType.LIMIT,  DirectionType.LONG, 10);
        FillEvent e3 = new FillEvent( LocalDate.of(2022, 1, 1),"AAPL", DirectionType.LONG, 100, 20, 10, "NASDAQ");
        EventQueues queues = EventQueues.INSTANCE;
        queues.marketQueue.add(e1);
        queues.orderQueue.add(e2);
        queues.fillQueue.add(e3);

        System.out.println(queues);

    }

}
