package trading.engine.backtest;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import trading.engine.data.DataHandler;
import trading.engine.data.HistoricalMySQLDataHandler;
import trading.engine.event.*;
import trading.engine.execution.ExecutionHandler;
import trading.engine.execution.NaiveExecutionHandler;
import trading.engine.order.NaiveOrderHandler;
import trading.engine.order.OrderHandler;
import trading.engine.portfolio.NaivePortfolio;
import trading.engine.portfolio.Portfolio;
import trading.engine.strategy.EMACrossoverStrategy;
import trading.engine.strategy.Strategy;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.Period;
import java.time.temporal.TemporalAmount;
import java.util.*;

public class NaiveBacktest implements Backtest {

    private Logger logger;

    private DataHandler data;
    private Portfolio portfolio;
    private ExecutionHandler execution;
    private Strategy strategy;
    private OrderHandler order;
    private EventQueues events = EventQueues.INSTANCE;

    private LocalDate stateDate;
    private LocalDate endDate;
    private TemporalAmount timeDelta;
    private List<String> symbolList;

    public NaiveBacktest() {
        // config
        stateDate = LocalDate.of(2021, 1, 15);
        endDate = LocalDate.of(2022, 1, 15);
        timeDelta = Period.ofDays(1);
        symbolList = new ArrayList<>(Arrays.asList("jpm", "aapl"));

        // create objects
        try {
            data = new HistoricalMySQLDataHandler(symbolList, stateDate, timeDelta);
        } catch (SQLException e) {
            logger.error("SQL connection failed");
            e.printStackTrace();
        }
        portfolio = new NaivePortfolio("Naive-01", stateDate, 1_000_000, data);
        execution = new NaiveExecutionHandler(data);
        strategy = new EMACrossoverStrategy("aapl", 2., 22, 5, data);
        order = new NaiveOrderHandler(portfolio, data);
    }

    @Override
    public void run() {
        
        LocalDate today = stateDate;
        
        // loop through the back test periods
        while (today.isBefore(endDate)) {
            
            // enter the next date
            today = today.plus(timeDelta);
            // market feeds data, today's data available, assume we are standing at the end of the day
            data.update(events);
            
            // process all market events
            while (!events.marketQueue.isEmpty()) {
                MarketEvent marketEvent = events.marketQueue.poll();
                // update portfolio for the close prices
                portfolio.updateByME(marketEvent);
                // generate signals by the latest prices
                strategy.generateSignal(marketEvent).ifPresent((e) -> events.signalQueue.add(e));

                // try to execute remaining orders on new open prices
                Queue<OrderEvent> remainings = new ArrayDeque<>();
                while (!events.orderQueue.isEmpty()) {
                    OrderEvent orderEvent = events.orderQueue.poll();
                    Optional<FillEvent> fill = execution.execute(orderEvent, marketEvent);
                    if (fill.isPresent()) {
                        events.fillQueue.add(fill.get());
                    } else {
                        remainings.add(orderEvent);
                    }
                }
                events.orderQueue.addAll(remainings);
                
            }
            
            // update portfolio for trades that filled today ()
            while (!events.fillQueue.isEmpty()) {
                FillEvent fillEvent = events.fillQueue.poll();
                portfolio.updateByFE(fillEvent);
            }
            
            // converts all signals into today's orders
            while (!events.signalQueue.isEmpty()) {
                SignalEvent signalEvent = events.signalQueue.poll();
                order.generateOrder(signalEvent).ifPresent((e) -> events.orderQueue.add(e));
            }

        }
    }

    @Override
    public void report() {
        System.out.println(portfolio.getCurrentPositions());
    }
}
