package trading.engine.guice.module;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import trading.engine.data.DataHandler;
import trading.engine.data.HistoricalMySQLDataHandler;
import trading.engine.guice.annotation.portfolio.InitialCapital;
import trading.engine.guice.annotation.portfolio.StartDate;

import java.time.LocalDate;

public class PortfolioModule extends AbstractModule {

    @Provides
    @InitialCapital
    public double provideInitialCapital() {
        return 1_000_000.;
    }

    @Provides
    @StartDate
    public LocalDate provideStartDate() {
        return LocalDate.of(2021, 2, 1);
    }

    @Provides
    public DataHandler provideDataHandler(HistoricalMySQLDataHandler impl) {
        return impl;
    }

}
