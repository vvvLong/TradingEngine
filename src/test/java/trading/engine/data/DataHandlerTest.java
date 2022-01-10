package trading.engine.data;

import com.google.inject.Guice;
import com.google.inject.Injector;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import trading.engine.event.EventQueues;
import trading.engine.event.MarketEvent;
import trading.engine.guice.module.DataModule;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;


public class DataHandlerTest {

    private HistoricalMySQLDataHandler handler;
    private final EventQueues queues = EventQueues.INSTANCE;

    @BeforeEach
    public void setup(){
        Injector injector = Guice.createInjector(new DataModule());
        handler = injector.getInstance(HistoricalMySQLDataHandler.class);
    }

    @Test
    @DisplayName("check initial states")
    public void initializationTest() {
        System.out.println(handler);

    }

    @Test
    @DisplayName("update 1 once")
    public void updateTest() {
        handler.update(queues);
        System.out.println("**************");
        System.out.println(handler);
        System.out.println("--------");
        System.out.println(queues);
        System.out.println("**************");
    }

    @Test
    @DisplayName("update multiple times")
    public void updateNTest() {
        for (int i =0; i<10; i++){
            updateTest();
        }
    }

    @Test
    @DisplayName("fetch bar on")
    public void fetchBarOnTest() {
        for (int i =0; i<20; i++){
            handler.update(queues);
        }
        System.out.println(handler);
        Optional<DataBar> bar1 = handler.fetchBarOn("jpm", LocalDate.of(2021, 1, 25));
        System.out.println(bar1);
        Optional<DataBar> bar2 = handler.fetchBarOn("jpm", LocalDate.of(2021, 1, 30));
        System.out.println(bar2);
        Optional<DataBar> bar3 = handler.fetchBarOn("jpm", LocalDate.of(2021, 2, 28));
        System.out.println(bar3);
        Optional<DataBar> bar4 = handler.fetchBarOn("jpm", LocalDate.of(2021, 1, 10));
        System.out.println(bar4);
    }

    @Test
    @DisplayName("fetch bar until")
    public void fetchBarUntilTest() {
        for (int i =0; i<30; i++){
            handler.update(queues);
        }
        System.out.println(handler);
        Optional<List<DataBar>> res = handler.fetchNBarsUntil("jpm", 5, LocalDate.of(2021, 2, 5));
        if (res.isPresent()) {
            List<DataBar> bars = res.get();
            for (DataBar b : bars) {
                System.out.println(b);
            }
        }
    }

    @Test
    @DisplayName("fetch bar between")
    public void fetchBarBetweenTest() {
        for (int i =0; i<30; i++){
            handler.update(queues);
        }
        System.out.println(handler);
        Optional<List<DataBar>> res = handler.fetchBarsBetween(
                "jpm", LocalDate.of(2021, 2, 1), LocalDate.of(2021, 2, 5));
        if (res.isPresent()) {
            List<DataBar> bars = res.get();
            for (DataBar b : bars) {
                System.out.println(b);
            }
        }
    }

    @Test
    @DisplayName("fetch bar from market event")
    public void fetchBarMETest() {
        for (int i =0; i<30; i++){
            handler.update(queues);
        }
        System.out.println(handler);
        Optional<DataBar> res = handler.fetchBarByME(new MarketEvent(LocalDate.of(2021, 2, 2), "aapl"));
        System.out.println(res);
    }

}
