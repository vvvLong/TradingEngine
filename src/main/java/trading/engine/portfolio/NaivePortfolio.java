package trading.engine.portfolio;

import com.google.inject.Inject;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import trading.engine.data.BarValueType;
import trading.engine.data.DataBar;
import trading.engine.data.DataHandler;
import trading.engine.event.*;
import trading.engine.injection.annotation.data.StartDate;
import trading.engine.injection.annotation.portfolio.InitialCapital;
import trading.engine.injection.annotation.portfolio.PortfolioID;


import javax.inject.Singleton;
import java.time.LocalDate;
import java.util.*;

@Singleton
public class NaivePortfolio implements Portfolio {
    private final String portfolioID;
    private final LocalDate startDate;
    private final double initialCapital;
    private final Stack<Positions> allPositions = new Stack<>();
    private final DataHandler data;
    private final Logger logger = LogManager.getLogger(NaivePortfolio.class);
    private LocalDate latestDate;

    @Inject
    public NaivePortfolio(@PortfolioID String portfolioID, @StartDate LocalDate startDate, @InitialCapital double initialCapital, DataHandler data) {
        this.portfolioID = portfolioID;
        this.startDate = startDate;
        this.initialCapital = initialCapital;
        this.allPositions.add(new Positions(startDate, initialCapital, new HashMap<>(), new HashMap<>()));
        this.data = data;
        this.latestDate = startDate;
    }

    @Override
    public void updateByME(MarketEvent event) {

        if (event.getTimestamp().isBefore(latestDate)) {
            logger.error("the market event {} happens before the current date {}", event, latestDate);
            throw new IllegalArgumentException("the market event happens before portfolio current date");
        }

        // if date is updated, add new Positions
        if (event.getTimestamp().isAfter(latestDate)) {
            latestDate = event.getTimestamp();
            Positions p = allPositions.peek();
            allPositions.push(new Positions(latestDate, p.getCash(), p.getQuantities(), p.getValues()));
        }

        // update only if the symbol exists
        if (allPositions.peek().contains(event.getSymbol())) {
            // get data
            DataBar bar = data.fetchBarByME(event).orElseThrow(
                    () -> new IllegalArgumentException("Market Event doesn't exist in data!"));

            // update values
            Positions p = allPositions.pop();  // take out the first Positions
            String symbol = event.getSymbol();
            Optional<Integer> quantity = p.getQuantityOf(symbol);
            if (quantity.isPresent()) {
                Map<String, Double> values = p.getValues();  // should be a copy of the map
                values.put(symbol, bar.getBarVal(BarValueType.ADJ_CLOSE) * quantity.get());
                Positions newP = new Positions(latestDate, p.getCash(), p.getQuantities(), values);
                allPositions.push(newP);
            } else {
                logger.error("symbol {} doesn't exist in latest positions", symbol);
                throw new IllegalStateException("symbol doesn't exist in latest positions");
            }
        }

    }

    @Override
    public void updateByFE(FillEvent event) {
        if (!event.getTimestamp().isEqual(latestDate)) {
            logger.error("the fill event {} doesn't happen on the current date {}", event, latestDate);
            throw new IllegalArgumentException("the fill event doesn't happen on the current date");
        }

        // get data
        DataBar bar = data.fetchBarOn(event.getSymbol(), event.getTimestamp()).orElseThrow(
                () -> new IllegalArgumentException("Market Event doesn't exist in data!"));

        Positions p = allPositions.pop();  // remove
        Map<String, Integer> quantities = p.getQuantities();  // should be a copy of the map
        Map<String, Double> values = p.getValues();  // should be a copy of the map
        String symbol = event.getSymbol();

        // update quantities, values nad cash
        int newQ;
        double newV;
        double newC;
        if (event.getDirectionType() == DirectionType.LONG) {
            newQ = quantities.getOrDefault(symbol, 0) + event.getQuantity();
            newC = p.getCash() - event.getPrice() * event.getQuantity() - event.getCommission();
        } else {
            newQ = quantities.getOrDefault(symbol, 0) - event.getQuantity();
            newC = p.getCash() + event.getPrice() * event.getQuantity() - event.getCommission();
        }
        newV = newQ * bar.getBarVal(BarValueType.ADJ_CLOSE);
        quantities.put(symbol, newQ);
        values.put(symbol, newV);
        Positions newP = new Positions(latestDate, newC, quantities, values);
        allPositions.push(newP);
    }

    @Override
    public String getPortfolioID() {
        return portfolioID;
    }

    @Override
    public double getInitialCapital() {
        return initialCapital;
    }

    @Override
    public Positions getCurrentPositions() {
        return allPositions.peek();
    }

    @Override
    public List<Positions> getAllPositions() {
        return new ArrayList<>(allPositions);
    }

    @Override
    public LocalDate getStartDate() {
        return startDate;
    }

    // for test
    public DataHandler getData() {
        return data;
    }

    // for test
    public LocalDate getLatestDate() {
        return latestDate;
    }

}
