package trading.engine.order;

import com.google.inject.Inject;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import trading.engine.data.BarValueType;
import trading.engine.data.DataHandler;
import trading.engine.event.DirectionType;
import trading.engine.event.OrderEvent;
import trading.engine.event.OrderType;
import trading.engine.event.SignalEvent;
import trading.engine.portfolio.Portfolio;

import java.time.LocalDate;
import java.util.Optional;

public class NaiveOrderManager implements OrderManager {

    private final Portfolio portfolio;
    private final DataHandler data;
    private final Logger logger = LogManager.getLogger(NaiveOrderManager.class);

    @Inject
    public NaiveOrderManager(Portfolio portfolio, DataHandler data) {
        this.portfolio = portfolio;
        this.data = data;
    }

    private void checkDate(SignalEvent event) {
        LocalDate eventDate = event.getTimestamp();
        LocalDate portfolioDate = portfolio.getCurrentPositions().getTimestamp();
        if (!portfolioDate.isEqual(eventDate)) {
            logger.error("the signal {} doesn't have the same date {} as current Positions", event, portfolioDate);
            throw new IllegalStateException("the signal doesn't have the same date as current Positions");
        }
    }

    /*generate generate order by fixed rule*/
    @Override
    public Optional<OrderEvent> generateOrder(SignalEvent event) {
        checkDate(event);
        int quantity;
        // long order
        if (event.getDirectionType() == DirectionType.LONG) {
            double ratio = event.getStrength() > 0.2 ? 0.1 : 0.05;
            double targetValue = Math.min(portfolio.getCurrentPositions().getCash(),
                    portfolio.getCurrentPositions().getTotalValue() * ratio);
            double price = data.fetchBarOn(event.getSymbol(), event.getTimestamp()).orElseThrow(
                    () -> new IllegalStateException(String.format("Cannot find data of %s", event.getSymbol()))
            ).getBarVal(BarValueType.CLOSE);
            quantity = (int) Math.floor(targetValue / price);

        // short order
        } else {
            double ratio = event.getStrength() > 0.2 ? 0.05 : 0.02;
            double targetValue = portfolio.getCurrentPositions().getTotalValue() * ratio;
            double price = data.fetchBarOn(event.getSymbol(), event.getTimestamp()).orElseThrow(
                    () -> new IllegalStateException(String.format("Cannot find data of %s", event.getSymbol()))
            ).getBarVal(BarValueType.CLOSE);
            quantity = (int) Math.floor(targetValue / price);
        }

        if (quantity == 0) {
            return Optional.empty();
        } else {
            return Optional.of(new OrderEvent(event.getTimestamp(), event.getSymbol(), OrderType.MARKET,
                    event.getDirectionType(), quantity));
        }
    }

}
