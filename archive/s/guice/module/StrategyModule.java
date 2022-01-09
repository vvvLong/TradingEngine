package trading.engine.guice.module;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import trading.engine.guice.annotation.strategy.LongWindow;
import trading.engine.guice.annotation.strategy.ShortWindow;
import trading.engine.guice.annotation.strategy.Smoothing;
import trading.engine.guice.annotation.strategy.Symbol;

public class StrategyModule extends AbstractModule {

    @Provides
    @Smoothing
    public static double provideSmoothing() {
        return 2;
    }

    @Provides
    @LongWindow
    public static int provideLongWindow() {
        return 20;
    }

    @Provides
    @ShortWindow
    public static int provideShortWindow() {
        return 5;
    }

    @Provides
    @Symbol
    public static String provideSymbol() {
        return "aapl";
    }


}
