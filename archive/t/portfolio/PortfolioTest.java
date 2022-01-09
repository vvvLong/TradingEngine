package trading.engine.portfolio;

import com.google.inject.Guice;
import com.google.inject.Injector;
import org.junit.jupiter.api.*;
import trading.engine.data.DataHandler;
import trading.engine.data.HistoricalMySQLDataHandler;
import trading.engine.event.Event;
import trading.engine.event.EventType;
import trading.engine.event.FillEvent;
import trading.engine.event.MarketEvent;
import trading.engine.guice.module.DataModule;
import trading.engine.guice.module.EventModule;
import trading.engine.guice.module.PortfolioModule;
import trading.engine.guice.module.StrategyModule;

import java.util.ArrayList;
import java.util.List;

public class PortfolioTest {

    private final List<Event> queue = new ArrayList<>();
    private DataHandler data;
    private Portfolio portfolio;
    private Injector injector = Guice.createInjector(new DataModule(), new StrategyModule(), new PortfolioModule(),
            new EventModule());

    @BeforeEach
    public void setup() {
        data = injector.getInstance(HistoricalMySQLDataHandler.class);
        portfolio = injector.getInstance(NaivePortfolio.class);
    }

    @Test
    @DisplayName("data handler should be singleton")
    public void singletonTest() {
        Assumptions.assumeTrue(data == portfolio.getData());
    }

    private void printStatus() {
        System.out.println(portfolio.getCurrentHoldings());
        System.out.println("--------------");
        System.out.println(portfolio.getAllHoldings());
        System.out.println("--------------");
        System.out.println(portfolio.getCurrentPositions());
        System.out.println("--------------");
        System.out.println(portfolio.getAllPositions());
        System.out.println("--------------");
    }

    @Test
    @DisplayName("initial positions and holdings")
    public void initialTest() {
        printStatus();
    }

    private void warmupData() {
        for (int i = 0; i < 10; i++) {
            data.updateBar(queue);
        }
    }

    @Test
    @DisplayName("update from fill")
    public void updateFillTest() {
        // update data
        warmupData();
        while (!queue.isEmpty()) {
            Event event = queue.remove(0);
            if (event.getEventType() == EventType.MARKET) {
                portfolio.updateTimestamp((MarketEvent) event);
            }
        }

        // update from fill
        FillEvent fill = injector.getInstance(FillEvent.class);
        portfolio.updateFill(fill);
        printStatus();
    }
}
