package trading.engine.portfolio;

import com.google.inject.Guice;
import com.google.inject.Injector;
import org.junit.jupiter.api.*;
import trading.engine.data.DataHandler;
import trading.engine.data.HistoricalMySQLDataHandler;
import trading.engine.event.*;
import trading.engine.injection.module.DataModule;
import trading.engine.injection.module.EventModule;
import trading.engine.injection.module.PortfolioModule;
import trading.engine.injection.module.StrategyModule;

public class PortfolioTest {

    private final EventQueues queues = EventQueues.INSTANCE;
    private DataHandler data;
    private Portfolio portfolio;
    private final Injector injector = Guice.createInjector(new DataModule(), new StrategyModule(), new PortfolioModule(),
            new EventModule());
    private FillEvent fill;

    @BeforeEach
    public void setup() {
        data = injector.getInstance(HistoricalMySQLDataHandler.class);
        portfolio = injector.getInstance(NaivePortfolio.class);
        fill = injector.getInstance(FillEvent.class);
    }

    @Test
    @DisplayName("data handler should be singleton")
    public void singletonTest() {
        Assumptions.assumeTrue(data == ((NaivePortfolio) portfolio).getData());
    }

    private void printStatus() {
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
    @DisplayName("update from market")
    public void updateMarketTest() {
        updateMarket(10);
        System.out.println(((NaivePortfolio) portfolio).getLatestDate());
        System.out.println(portfolio.getAllPositions());
    }

    @Test
    @DisplayName("update from fill")
    public void updateFillTest() {
        updateMarket(10);
        System.out.println("----------- Bar ------------");
        System.out.println(data.fetchBarOn("jpm", ((NaivePortfolio) portfolio).getLatestDate()));
        System.out.println("----------- Fill ------------");
        System.out.println(fill);
        System.out.println("----------- Before Fill ------------");
        System.out.println(portfolio.getCurrentPositions());
        portfolio.updateByFE(fill);
        System.out.println("----------- After Fill ------------");
        System.out.println(portfolio.getCurrentPositions());
        System.out.println("----------- After Market ------------");
        updateMarket(2);
        System.out.println(data.fetchBarOn("jpm", ((NaivePortfolio) portfolio).getLatestDate()));
        System.out.println(portfolio.getCurrentPositions());
    }
}
