package trading.engine.execution;

import com.google.inject.Inject;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import trading.engine.data.BarValueType;
import trading.engine.data.DataHandler;
import trading.engine.data.HistoricalMySQLDataHandler;
import trading.engine.event.FillEvent;
import trading.engine.event.MarketEvent;
import trading.engine.event.OrderEvent;
import trading.engine.portfolio.NaivePortfolio;

import java.time.LocalDate;
import java.util.Optional;

public class NaiveExecutionHandler implements ExecutionHandler {

    private final DataHandler data;
    private final Logger logger;

    @Inject
    public NaiveExecutionHandler (DataHandler data) {
        this.data = data;
        this.logger = LogManager.getLogger(NaivePortfolio.class);
    }

    /* fill the order by next day's open price */
    @Override
    public Optional<FillEvent> execute(OrderEvent orderEvent, MarketEvent marketEvent) {
        String symbol = orderEvent.getSymbol();
        LocalDate nextDay = marketEvent.getTimestamp();
        if (!symbol.equals(marketEvent.getSymbol()) || nextDay.isBefore(orderEvent.getTimestamp())) {
            logger.info("order {} and market {} unmatched", orderEvent, marketEvent);
            return Optional.empty();
        }
        // get the next open
        double filledPrice = data.fetchBarOn(symbol, nextDay).orElseThrow(
                () -> new IllegalStateException(String.format("The symbol %s is not available on date %s", symbol, nextDay))
        ).getBarVal(BarValueType.OPEN);
        // return new Fill, assume 1% commission floored at 10
        return Optional.of(new FillEvent(
                nextDay,
                symbol,
                orderEvent.getDirectionType(),
                filledPrice,
                orderEvent.getQuantity(),
                Math.max(10, filledPrice * orderEvent.getQuantity() * 0.01),
                "NYSE"));
    }
}
