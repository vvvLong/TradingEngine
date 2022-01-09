package trading.engine.strategy;

import com.google.inject.Inject;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import trading.engine.data.BarValueType;
import trading.engine.data.DataBar;
import trading.engine.event.DirectionType;
import trading.engine.event.SignalEvent;
import trading.engine.guice.annotation.strategy.LongWindow;
import trading.engine.guice.annotation.strategy.ShortWindow;
import trading.engine.guice.annotation.strategy.Smoothing;
import trading.engine.guice.annotation.strategy.Symbol;

import java.time.LocalDate;
import java.util.Optional;

/*
* toy ema cross over strategy
* multiplier = smoothing / (1 + days)
* ema = multiplier * price_today + (1 - multiplier) * ema_previous
* */
public class EMACrossoverStrategy implements Strategy {

    private final int longWindow;
    private final int shortWindow;
    private final double longMultiplier;
    private final double shortMultiplier;
    private int longWarmup;
    private int shortWarmup;
    private double longEMA;
    private double shortEMA;
    private final String symbol;
    private final String strategyName;
    private static final Logger logger = LogManager.getLogger(EMACrossoverStrategy.class);

    @Inject
    public EMACrossoverStrategy(@Smoothing double smoothing, @LongWindow int longWindow, @ShortWindow int shortWindow, @Symbol String symbol) {
        checkArgs(smoothing, longWindow, shortWindow);
        this.longWindow = longWindow;
        this.shortWindow = shortWindow;
        this.symbol = symbol;
        this.longMultiplier = smoothing / (1 + longWindow);
        this.shortMultiplier = smoothing / (1 + shortWindow);
        this.longWarmup = -longWindow;
        this.shortWarmup = -shortWindow;
        this.longEMA = 0.;
        this.shortEMA = 0.;
        this.strategyName = String.format("EMACrossover(%s, %s, %s)-%s", shortWindow, longWindow, smoothing, symbol);
    }

    private void checkArgs(double smoothing, int longWindow, int shortWindow) {
        if (smoothing < 0) {
            String msg = String.format("smoothing=%s < 0", smoothing);
            logger.info(msg);
            throw new IllegalArgumentException(msg);
        }
        if (longWindow < 0) {
            String msg = String.format("longWindow=%s < 0", longWindow);
            logger.info(msg);
            throw new IllegalArgumentException(msg);
        }
        if (shortWindow < 0) {
            String msg = String.format("shortWindow=%s < 0", shortWindow);
            logger.info(msg);
            throw new IllegalArgumentException(msg);
        }
        if (longWindow <= shortWindow) {
            String msg = String.format("longWindow=%s <= shortWindow=%s", longWindow, shortWindow);
            logger.info(msg);
            throw new IllegalArgumentException(msg);
        }
    }

    private void calLongEMA(double price) {
        if (longWarmup < 0) {  // accumulate sum if days not enough
            longEMA += price;
            longWarmup++;
            if (longWarmup == 0) { longEMA /= longWindow; }  // use avg as starting value
            logger.info("long window is in warmup, {} days remains", -longWarmup);

        } else {
            longEMA = longMultiplier * price + (1 - longMultiplier) * longEMA;
        }
    }

    private void calShortEMA(double price) {
        if (shortWarmup < 0) {  // accumulate sum if days not enough
            shortEMA += price;
            shortWarmup++;
            if (shortWarmup == 0) { shortEMA /= shortWindow; }  // use avg as starting value
            logger.info("short window is in warmup, {} days remains", -shortWarmup);

        } else {
            shortEMA = shortMultiplier * price + (1 - shortMultiplier) * shortEMA;
        }
    }

    @Override
    public Optional<SignalEvent> generateSignal(DataBar bar) {
        calLongEMA(bar.getBarVal(BarValueType.CLOSE));
        calShortEMA(bar.getBarVal(BarValueType.CLOSE));

        if (longWarmup < 0 || shortWarmup < 0) {
            logger.info("calling generateSignal() before warmup period");
            return Optional.empty();
        }

        DirectionType direction;
        double strength;
        if (shortEMA > longEMA) {
            direction = DirectionType.LONG;
            strength = shortEMA / longEMA - 1;
        } else {
            direction = DirectionType.SHORT;
            strength = 1 - shortEMA / longEMA;
        }

        return Optional.of(new SignalEvent(strategyName, symbol, LocalDate.now(), direction, strength));
    }

}
