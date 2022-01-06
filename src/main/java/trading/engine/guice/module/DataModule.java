package trading.engine.guice.module;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import trading.engine.event.Event;
import trading.engine.guice.annotation.data.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DataModule extends AbstractModule {

    @Provides
    @EventQueue
    public static List<Event> provideEventQueue() {
        return new ArrayList<>();
    }

    @Provides
    @SymbolList
    public static List<String> provideSymbolList() {
        return new ArrayList<>(Arrays.asList("jpm", "aapl"));
    }

    @Provides
    @CurrentDate
    public static LocalDate provideCurrentDate() {
        return LocalDate.of(2021, 1, 15);
    }

    @Provides
    @DayIncrement
    public static int provideDayIncrement() {
        return 1;
    }

}
