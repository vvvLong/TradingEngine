package trading.engine.execution;

import trading.engine.event.FillEvent;
import trading.engine.event.OrderEvent;

public interface ExecutionHandler {

    /*  execute an order triggered by OrderEvent and returns a FillEvent */
    FillEvent execute(OrderEvent event);

}
