package trading.engine.injection.module;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import trading.engine.execution.ExecutionHandler;
import trading.engine.execution.NaiveExecutionHandler;

public class ExecutionModule extends AbstractModule {

    @Provides
    public ExecutionHandler provideExecutionHandler(NaiveExecutionHandler impl) {
        return impl;
    }
}
