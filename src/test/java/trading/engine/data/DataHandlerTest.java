package trading.engine.data;

import com.google.inject.Guice;
import com.google.inject.Injector;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import trading.engine.guice.module.DataModule;


public class DataHandlerTest {

    private HistoricalMySQLDataHandler handler;

    @BeforeEach
    public void InitializationTest(){
        Injector injector = Guice.createInjector(new DataModule());
        handler = injector.getInstance(HistoricalMySQLDataHandler.class);
    }

    @Test
    public void updateBarTest() {
        handler.updateBar();
        System.out.println(handler.getCurrentDate());
        System.out.println(handler.getLatestBars());
        System.out.println(handler.getEventsQueue());
    }

    @Test
    public void getLatestBarTest() {
        handler.updateBar();
        System.out.println(handler.getLatestBar("jpm"));
    }

    @Test
    public void getLatestBarsTest() {
        for (int i = 0; i<6; i++) { handler.updateBar(); }
        System.out.println(handler.getLatestBars("aapl", 4));
    }

    @Test
    public void getLatestBarDateTest() {
        handler.updateBar();
        System.out.println(handler.getLatestBarDate("aapl"));
    }

    @Test
    public void getLatestBarValueTest() {
        String symbol = "aapl";
        handler.updateBar();
        System.out.println(handler.getLatestBars().get(symbol));
        for (BarValueType t : BarValueType.values()) {
            System.out.println(t);
            System.out.println(handler.getLatestBarValue(symbol, t));
        }
    }

    @Test
    public void getLatestBarValuesTest() {
        String symbol = "aapl";
        for (int i = 0; i<6; i++) { handler.updateBar(); }
        System.out.println(handler.getLatestBars().get(symbol));
        for (BarValueType t : BarValueType.values()) {
            System.out.println(t);
            System.out.println(handler.getLatestBarValues(symbol, t, 4));
        }
    }


}
