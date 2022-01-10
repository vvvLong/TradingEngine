package trading.engine.strategy;

import trading.engine.event.MarketEvent;
import trading.engine.event.SignalEvent;

import java.util.Optional;

public interface Strategy {

    /*return unique strategy ID*/
    String getStrategyID();

    /*generate signal event on market event*/
    Optional<SignalEvent> generateSignal(MarketEvent event);

}
