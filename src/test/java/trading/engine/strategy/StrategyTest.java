package trading.engine.strategy;

import com.google.inject.Guice;
import com.google.inject.Injector;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import trading.engine.data.DataBar;
import trading.engine.data.DataHandler;
import trading.engine.data.HistoricalMySQLDataHandler;
import trading.engine.event.Event;
import trading.engine.guice.module.DataModule;
import trading.engine.guice.module.StrategyModule;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class StrategyTest {

    private final List<Event> queue = new ArrayList<>();
    private DataHandler data;
    private Strategy strategy;

    @BeforeEach
    public void setup() {
        Injector injector = Guice.createInjector(new DataModule(), new StrategyModule());
        data = injector.getInstance(HistoricalMySQLDataHandler.class);
        strategy = injector.getInstance(EMACrossoverStrategy.class);
    }

    @Test
    public void signalTest() {
        int size = 0;
        for (int i = 0; i < 50; i++) {
            data.updateBar(queue);
            String symbol = "aapl";
            if (data.getLatestBars(symbol, Integer.MAX_VALUE).isPresent()) {
                int newSize = data.getLatestBars(symbol, Integer.MAX_VALUE).get().size();
                if (newSize > size){  // make sure new bar is added
                    Optional<DataBar> bar = data.getLatestBar(symbol);
                    System.out.println(bar);
                    bar.ifPresent(dataBar -> System.out.println(strategy.generateSignal(dataBar)));
                    size = newSize;
                }
            }
        }
    }

}
