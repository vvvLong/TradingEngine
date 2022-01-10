package trading.engine.injection.module;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import trading.engine.event.DirectionType;
import trading.engine.event.FillEvent;
import trading.engine.event.SignalEvent;

import java.time.LocalDate;

public class EventModule extends AbstractModule {
    @Provides
    public FillEvent provideFillEvent() {
        return new FillEvent(LocalDate.of(2021, 1, 25), "jpm", DirectionType.SHORT,
                 100, 200, 100, "NYSE");
    }

    @Provides
    public SignalEvent provideSignalEvent() {
        return new SignalEvent(LocalDate.of(2021, 2, 4), "jpm", "test str", DirectionType.LONG,
                0.22);
    }
}
