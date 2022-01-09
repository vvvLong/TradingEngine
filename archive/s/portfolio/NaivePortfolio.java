package trading.engine.portfolio;

import com.google.inject.Inject;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import trading.engine.data.BarValueType;
import trading.engine.data.DataHandler;
import trading.engine.data.HistoricalMySQLDataHandler;
import trading.engine.event.*;
import trading.engine.guice.annotation.data.SymbolList;
import trading.engine.guice.annotation.portfolio.InitialCapital;
import trading.engine.guice.annotation.portfolio.StartDate;

import java.time.LocalDate;
import java.util.*;

public class NaivePortfolio implements Portfolio{

    private final List<String> symbolList;
    private final LocalDate startDate;
    private final double initialCapital;
    private Positions currentPositions;
    private final List<Positions> allPositions;
    private Holdings currentHoldings;
    private final List<Holdings> allHoldings;
    private final DataHandler data;
    private final Logger logger = LogManager.getLogger(NaivePortfolio.class);

    @Inject
    public NaivePortfolio(@SymbolList List<String> symbolList, @StartDate LocalDate startDate,
                          @InitialCapital double initialCapital, DataHandler data) {
        this.symbolList = symbolList;
        this.startDate = startDate;
        this.initialCapital = initialCapital;
        this.data = data;
        // initialize position and holding maps
        HashMap<String, Integer> pMap = new HashMap<>();
        HashMap<String, Double> hMap = new HashMap<>();
        for (String s : symbolList) {
            pMap.put(s, 0);
            hMap.put(s, 0.);
        }
        // construct positions
        this.currentPositions = new Positions(startDate, pMap);
        this.allPositions = new ArrayList<>();
        this.allPositions.add(currentPositions);
        // construct holdings
        this.currentHoldings = new Holdings(startDate, hMap, initialCapital);
        this.allHoldings = new ArrayList<>();
        this.allHoldings.add(currentHoldings);
    }

    @Override
    public void updateTimestamp(MarketEvent event) {
        // get the latest time
        LocalDate time = null;
        for (String s : symbolList) {
            Optional<LocalDate> t = data.getLatestBarDate(s);
            if (t.isPresent()) {
                time = t.get();
                break;
            }
        }
        if (time == null) {
            logger.info("No Latest Bar available for the selected symbols");
            throw new IllegalStateException("No Latest Bar available for the selected symbols");
        }

        // update positions
        Positions p = new Positions(time, currentPositions.getPositions());
        this.currentPositions = p;
        this.allPositions.add(p);

        // update holdings
        HashMap<String, Double> hMap = new HashMap<>();
        for (String s : symbolList) {
            int quantity = currentPositions.getPosition(s);
            Optional<Double> price = data.getLatestBarValue(s, BarValueType.ADJ_CLOSE);
            if (price.isPresent()) {
                hMap.put(s, quantity * price.get());
            } else {
                hMap.put(s, currentHoldings.getHolding(s));  // holding unchanged if no available price
            }
        }
        currentHoldings = new Holdings(time, hMap, currentHoldings.getCash());
        allHoldings.add(currentHoldings);
    }

    private void updatePositionsFromFill(FillEvent event) {
        int direction;
        if (event.getDirectionType() == DirectionType.LONG) {
            direction = 1;
        } else {
            direction = -1;
        }
        String symbol = event.getSymbol();
        int prevPosition = currentPositions.getPosition(symbol);
        currentPositions.getPositions().put(symbol, prevPosition + direction * event.getFillQuantity());
    }

    private void updateHoldingsFromFill(FillEvent event) {
        double direction;
        if (event.getDirectionType() == DirectionType.LONG) {
            direction = 1.;
        } else {
            direction = -1.;
        }
        String s = event.getSymbol();
        Map<String, Double> hMap = currentHoldings.getHoldings();

        // update holding
        int quantity = currentPositions.getPosition(s);
        Optional<Double> price = data.getLatestBarValue(s, BarValueType.ADJ_CLOSE);
        price.ifPresent(aDouble -> hMap.put(s, quantity * aDouble));

        // update cash
        currentHoldings.setCash(currentHoldings.getCash() + direction * event.getFillValue());

        // update total
        currentHoldings.calcTotal();
    }

    @Override
    public void updateFill(FillEvent event) {
        updatePositionsFromFill(event);
        updateHoldingsFromFill(event);
    }

    /*
    * Naively generate market order
    * */
    @Override
    public OrderEvent generateOrder(SignalEvent event) {
        String symbol = event.getSymbol();
        DirectionType direction = event.getDirectionType();
        double strength = event.getStrength();
        int quantity = strength > 0.1 ? 100 : 50;
        return new OrderEvent(OrderType.MARKET, symbol, direction, quantity);
    }

    @Override
    public void updateSignal(SignalEvent event, List<Event> eventQueue) {
        eventQueue.add(this.generateOrder(event));
    }

    @Override
    public List<String> getSymbolList() {
        return symbolList;
    }

    @Override
    public LocalDate getStartDate() {
        return startDate;
    }

    @Override
    public double getInitialCapital() {
        return initialCapital;
    }

    @Override
    public Positions getCurrentPositions() {
        return currentPositions;
    }

    @Override
    public List<Positions> getAllPositions() {
        return allPositions;
    }

    @Override
    public Holdings getCurrentHoldings() {
        return currentHoldings;
    }

    @Override
    public List<Holdings> getAllHoldings() {
        return allHoldings;
    }

    public DataHandler getData() {
        return data;
    }

    @Override
    public String toString() {
        return "NaivePortfolio{" +
                "symbolList=" + symbolList +
                ", startDate=" + startDate +
                ", initialCapital=" + initialCapital +
                ", currentPositions=" + currentPositions +
                ", allPositions=" + allPositions +
                ", currentHoldings=" + currentHoldings +
                ", allHoldings=" + allHoldings +
                '}';
    }
}
