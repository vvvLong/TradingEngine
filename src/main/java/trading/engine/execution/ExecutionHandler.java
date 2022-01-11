package trading.engine.execution;

import trading.engine.event.FillEvent;
import trading.engine.event.MarketEvent;
import trading.engine.event.OrderEvent;

import java.util.Optional;

public interface ExecutionHandler {

    /*  execute an order triggered by MarketEvent and returns a FillEvent */
    Optional<FillEvent> execute(OrderEvent orderEvent, MarketEvent marketEvent);

}
