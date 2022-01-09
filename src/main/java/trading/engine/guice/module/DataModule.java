package trading.engine.guice.module;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import trading.engine.event.Event;
import trading.engine.guice.annotation.data.*;

import java.time.LocalDate;
import java.time.Period;
import java.time.temporal.TemporalAmount;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DataModule extends AbstractModule {

    @Provides
    @SymbolList
    public static List<String> provideSymbolList() {
        return new ArrayList<>(Arrays.asList("jpm", "aapl"));
    }

    @Provides
    @StartDate
    public static LocalDate provideStartDate() {
        return LocalDate.of(2021, 1, 15);
    }

    @Provides
    @TimeDelta
    public static TemporalAmount provideTimeDelta() {
        return Period.ofDays(1);
    }

}
