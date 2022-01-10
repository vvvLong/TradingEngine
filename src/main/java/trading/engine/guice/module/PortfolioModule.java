package trading.engine.guice.module;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import trading.engine.guice.annotation.portfolio.InitialCapital;
import trading.engine.guice.annotation.portfolio.PortfolioID;

public class PortfolioModule extends AbstractModule {

    @Provides
    @PortfolioID
    public String providePortfolioID() {
        return "Naive Portfolio";
    }

    @Provides
    @InitialCapital
    public double provideInitialCapital() {
        return 1_000_000.;
    }

}
