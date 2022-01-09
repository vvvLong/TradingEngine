package trading.engine.event;

import java.util.ArrayDeque;
import java.util.Queue;

public class EventQueues {

    public static final EventQueues INSTANCE = new EventQueues();  // singleton
    public final Queue<MarketEvent> marketQueue;
    public final Queue<OrderEvent> orderQueue;
    public final Queue<SignalEvent> signalQueue;
    public final Queue<FillEvent> fillQueue;

    private EventQueues() {
        marketQueue = new ArrayDeque<>();
        orderQueue = new ArrayDeque<>();
        signalQueue = new ArrayDeque<>();
        fillQueue = new ArrayDeque<>();
    }

    @Override
    public String toString() {
        return "EventQueues{" +
                "\nmarketQueue=" + marketQueue +
                "\norderQueue=" + orderQueue +
                "\nsignalQueue=" + signalQueue +
                "\nfillQueue=" + fillQueue +
                "\n}";
    }
}
