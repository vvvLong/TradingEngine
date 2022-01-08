package trading.engine.portfolio;

import com.google.inject.Guice;
import com.google.inject.Injector;
import org.junit.jupiter.api.*;
import trading.engine.data.DataHandler;
import trading.engine.data.HistoricalMySQLDataHandler;
import trading.engine.event.Event;
import trading.engine.guice.module.DataModule;
import trading.engine.guice.module.PortfolioModule;
import trading.engine.guice.module.StrategyModule;

import java.util.ArrayList;
import java.util.List;

public class PortfolioTest {

    private final List<Event> queue = new ArrayList<>();
    private DataHandler data;
    private Portfolio portfolio;

    @BeforeEach
    public void setup() {
        Injector injector = Guice.createInjector(new DataModule(), new StrategyModule(), new PortfolioModule());
        data = injector.getInstance(HistoricalMySQLDataHandler.class);
        portfolio = injector.getInstance(NaivePortfolio.class);
    }

    @Test
    @DisplayName("data handler should be singleton")
    public void singletonTest() {
        Assumptions.assumeTrue(data == portfolio.getData());
    }
}
