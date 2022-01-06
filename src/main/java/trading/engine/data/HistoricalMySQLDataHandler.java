package trading.engine.data;

import com.google.inject.Inject;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import trading.engine.event.Event;
import trading.engine.event.MarketEvent;
import trading.engine.guice.annotation.data.*;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class HistoricalMySQLDataHandler implements DataHandler {

    private final List<Event> eventsQueue;
    private final List<String> symbolList;
    private LocalDate currentDate;
    private final int dayIncrement;
    private final Map<String, List<DataBar>> latestBars;
    private boolean newDataAvailable;  // flag to check if there is unseen data
    private static final Logger logger = LogManager.getLogger(HistoricalMySQLDataHandler.class);

    @Inject
    public HistoricalMySQLDataHandler(@EventQueue List<Event> eventsQueue, @SymbolList List<String> symbolList,
                                      @CurrentDate LocalDate currentDate, @DayIncrement int dayIncrement) {
        this.eventsQueue = eventsQueue;
        this.symbolList = symbolList;
        this.currentDate = currentDate;
        this.dayIncrement = dayIncrement;  // the unit of day increment
        this.latestBars = new HashMap<>();
        this.newDataAvailable = true;

        for (String s : symbolList) {
            latestBars.put(s, new ArrayList<>());
        }  // create empty list
    }

    @Override
    public Optional<DataBar> getLatestBar(String symbol) throws IllegalArgumentException {
        if (symbolList.contains(symbol)) {
            List<DataBar> bars = latestBars.get(symbol);
            // if symbol doesn't exist or no latest data, return empty
            if (bars == null || bars.isEmpty()) {
                return Optional.empty();
            }
            return Optional.of(bars.get(bars.size() - 1));
        } else {
            String msg = String.format("The symbol=%s doesn't exist in SymbolList", symbol);
            throw new IllegalArgumentException(msg);
        }
    }

    @Override
    public Optional<List<DataBar>> getLatestBars(String symbol, int numOfBars) throws IllegalArgumentException {
        if (symbolList.contains(symbol)) {
            List<DataBar> bars = latestBars.get(symbol);
            // if symbol doesn't exist or no latest data, return empty
            if (bars == null || bars.isEmpty()) {
                return Optional.empty();
            }
            // else return up to numOfBars Bars if available
            int length = bars.size();
            int returnSize = Math.min(length, numOfBars);
            return Optional.of(bars.subList(length - returnSize, length));
        } else {
            String msg = String.format("The symbol=%s doesn't exist in SymbolList", symbol);
            throw new IllegalArgumentException(msg);
        }
    }

    @Override
    public Optional<LocalDate> getLatestBarDate(String symbol) throws IllegalArgumentException {
        if (symbolList.contains(symbol)) {
            List<DataBar> bars = latestBars.get(symbol);
            // if symbol doesn't exist or no latest data, return empty
            if (bars == null || bars.isEmpty()) {
                return Optional.empty();
            }
            return Optional.of(bars.get(bars.size() - 1).getTimestamp());
        } else {
            String msg = String.format("The symbol=%s doesn't exist in SymbolList", symbol);
            throw new IllegalArgumentException(msg);
        }
    }

    @Override
    public Optional<Double> getLatestBarValue(String symbol, BarValueType valueType) throws IllegalArgumentException {
        if (symbolList.contains(symbol)) {
            List<DataBar> bars = latestBars.get(symbol);
            // if symbol doesn't exist or no latest data, return empty
            if (bars == null || bars.isEmpty()) {
                return Optional.empty();
            }
            // else
            return Optional.of(bars.get(bars.size() - 1).getBarVal(valueType));
        } else {
            String msg = String.format("The symbol=%s doesn't exist in SymbolList", symbol);
            throw new IllegalArgumentException(msg);
        }
    }

    @Override
    public Optional<List<Double>> getLatestBarValues(
            String symbol, BarValueType valueType, int numOfBars) throws IllegalArgumentException {
        if (symbolList.contains(symbol)) {
            List<DataBar> bars = latestBars.get(symbol);
            // if symbol doesn't exist or no latest data, return empty
            if (bars == null || bars.isEmpty()) {
                return Optional.empty();
            }
            // else
            int returnSize = Math.min(bars.size(), numOfBars);
            List<DataBar> sublist = bars.subList(bars.size() - returnSize, bars.size());
            List<Double> res = new ArrayList<>();
            for (DataBar bar : sublist) {
                res.add(bar.getBarVal(valueType));
            }
            return Optional.of(res);
        } else {
            String msg = String.format("The symbol=%s doesn't exist in SymbolList", symbol);
            throw new IllegalArgumentException(msg);
        }
    }

    /*
     * fetch one bar from mysql and add into latestBars as simulated market events;
     * one call for an increment of dayIncrement days;
     * */
    @Override
    public void updateBar() throws RuntimeException {
        // load config
        Properties prop = new Properties();
        try (InputStream in = new FileInputStream("src/main/resources/config.properties")) {
            prop.load(in);
            if (prop.isEmpty()) {
                logger.info("Properties file is empty");
                throw new RuntimeException("The properties file is empty");
            }

        } catch (IOException e) {
            e.printStackTrace();
            logger.info("Fail to load config.properties");
        }

        // connect to mysql
        try (Connection conn = DriverManager.getConnection(
                prop.getProperty("SQL_URL"), prop.getProperty("SQL_USER"), prop.getProperty("SQL_PASSWORD"))) {
            // sql %Y-%m-%d
            String date = currentDate.format(DateTimeFormatter.ISO_LOCAL_DATE);
            for (String symbol : symbolList) {
                String sql = "SELECT * FROM " + symbol + " WHERE DATE=STR_TO_DATE('" + date + "', '%Y-%m-%d')";
                Statement st = conn.createStatement();
                ResultSet rs = st.executeQuery(sql);
                if (rs.next()) {
                    latestBars.get(symbol).add(new DataBar(
                            currentDate,
                            rs.getDouble("Open"),
                            rs.getDouble("High"),
                            rs.getDouble("Low"),
                            rs.getDouble("Close"),
                            rs.getDouble("Volume"),
                            rs.getDouble("Adj_Close")
                    ));
                } else {
                    // one symbol not available
                    newDataAvailable = false;
                    logger.info("The symbol: {} unavailable on {}", symbol, date);
                }
            }
            // increment date
            currentDate = currentDate.plusDays(dayIncrement);
            // append market event
            eventsQueue.add(new MarketEvent());

        } catch (SQLException e) {
            e.printStackTrace();
            logger.info("MySQL query failed!");
        }

    }

    public List<Event> getEventsQueue() {
        return eventsQueue;
    }

    public LocalDate getCurrentDate() {
        return currentDate;
    }

    public Map<String, List<DataBar>> getLatestBars() {
        return latestBars;
    }

}
