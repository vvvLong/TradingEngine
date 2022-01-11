package trading.engine.execution;

import com.google.inject.Guice;
import com.google.inject.Injector;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import trading.engine.data.DataHandler;
import trading.engine.data.HistoricalMySQLDataHandler;
import trading.engine.event.EventQueues;
import trading.engine.event.FillEvent;
import trading.engine.event.MarketEvent;
import trading.engine.event.OrderEvent;
import trading.engine.injection.module.DataModule;
import trading.engine.injection.module.EventModule;
import trading.engine.injection.module.ExecutionModule;

public class ExecutionTest {

    private ExecutionHandler execution;
    private DataHandler data;
    private OrderEvent order;
    private EventQueues queues = EventQueues.INSTANCE;

    @BeforeEach
    public void setup() {
        Injector injector = Guice.createInjector(new DataModule(), new EventModule(), new ExecutionModule());
        execution = injector.getInstance(NaiveExecutionHandler.class);
        data = injector.getInstance(HistoricalMySQLDataHandler.class);
        order = injector.getInstance(OrderEvent.class);
    }

    private void updateMarket(int n) {
        for (int i = 0; i < n; i++) {
            data.update(queues);
        }
    }


    @Test
    @DisplayName("generate correct order")
    public void executeTest() {
        updateMarket(30);
        System.out.println("-------- order date --------");
        System.out.println(order.getTimestamp());
        FillEvent fill = execution.execute(order);
        System.out.println("-------- fill --------");
        System.out.println(fill);
        System.out.println("-------- bar --------");
        System.out.println(data.fetchBarOn(fill.getSymbol(), fill.getTimestamp()));
    }
}
