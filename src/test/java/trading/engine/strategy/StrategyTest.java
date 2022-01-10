package trading.engine.strategy;

import com.google.inject.Guice;
import com.google.inject.Injector;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import trading.engine.data.DataBar;
import trading.engine.data.DataHandler;
import trading.engine.data.HistoricalMySQLDataHandler;
import trading.engine.event.Event;
import trading.engine.event.EventQueues;
import trading.engine.event.MarketEvent;
import trading.engine.event.SignalEvent;
import trading.engine.guice.module.DataModule;
import trading.engine.guice.module.StrategyModule;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class StrategyTest {

    private final EventQueues queues = EventQueues.INSTANCE;
    private DataHandler data;
    private Strategy strategy;

    @BeforeEach
    public void setup() {
        Injector injector = Guice.createInjector(new DataModule(), new StrategyModule());
        data = injector.getInstance(HistoricalMySQLDataHandler.class);
        strategy = injector.getInstance(EMACrossoverStrategy.class);
    }

    @Test
    @DisplayName("data should be singleton")
    public void singletonTest() {
        Assertions.assertEquals(data, ((EMACrossoverStrategy) strategy).getData());
    }

    @Test
    @DisplayName("generate correct signal")
    public void signalTest() {
        // generate signals
        for (int i = 0; i < 50; i++) {
            data.update(queues);
            while (!queues.marketQueue.isEmpty()) {
                Optional<SignalEvent> signal = strategy.generateSignal(queues.marketQueue.poll());
                signal.ifPresent(queues.signalQueue::add);
            }
        }

        System.out.println("*********** Signal Queue ***************");
        for(SignalEvent s : queues.signalQueue){
            System.out.println(s);
        }
    }

}
