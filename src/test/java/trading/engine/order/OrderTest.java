package trading.engine.order;

import com.google.inject.Guice;
import com.google.inject.Injector;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import trading.engine.data.DataHandler;
import trading.engine.event.EventQueues;
import trading.engine.event.MarketEvent;
import trading.engine.event.SignalEvent;
import trading.engine.injection.module.DataModule;
import trading.engine.injection.module.EventModule;
import trading.engine.injection.module.PortfolioModule;
import trading.engine.portfolio.Portfolio;

public class OrderTest {
    private OrderHandler order;
    private SignalEvent signal;
    private DataHandler data;
    private Injector injector = Guice.createInjector(new EventModule(), new PortfolioModule(), new DataModule());
    private EventQueues queues = EventQueues.INSTANCE;
    private Portfolio portfolio;

    @BeforeEach
    public void setup() {
        order = injector.getInstance(NaiveOrderHandler.class);
        signal = injector.getInstance(SignalEvent.class);
        data = injector.getInstance(DataHandler.class);
        portfolio = injector.getInstance(Portfolio.class);
    }

    private void updateMarket(int n) {
        for (int i = 0; i < n; i++) {
            data.update(queues);
            while (!queues.marketQueue.isEmpty()) {
                MarketEvent e = queues.marketQueue.poll();
                portfolio.updateByME(e);
            }
        }
    }

    @Test
    @DisplayName("generate correct order")
    public void orderTest() {
        updateMarket(20);
        System.out.println("--------- current portfolio -------------");
        System.out.println(portfolio.getCurrentPositions());
        System.out.println("--------- order -------------");
        System.out.println(order.generateOrder(signal));
    }

}
