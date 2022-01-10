package trading.engine.order;

import trading.engine.event.OrderEvent;
import trading.engine.event.SignalEvent;

import java.util.Optional;

public interface OrderManager {

    /*generate order by signal event*/
    Optional<OrderEvent> generateOrder(SignalEvent event);

}
