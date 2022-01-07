package trading.engine.strategy;

import trading.engine.data.DataBar;
import trading.engine.event.SignalEvent;

import java.util.Optional;

public interface Strategy {

    /*generate signal event*/
    Optional<SignalEvent> generateSignal(DataBar bar);

}
