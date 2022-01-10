package trading.engine.injection.module;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import trading.engine.injection.annotation.portfolio.InitialCapital;
import trading.engine.injection.annotation.portfolio.PortfolioID;
import trading.engine.portfolio.NaivePortfolio;
import trading.engine.portfolio.Portfolio;

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

    @Provides
    public Portfolio providePortfolio(NaivePortfolio impl) { return impl; }

}
