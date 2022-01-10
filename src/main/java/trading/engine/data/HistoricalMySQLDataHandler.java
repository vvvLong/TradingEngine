package trading.engine.data;

import com.google.inject.Inject;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import trading.engine.event.EventQueues;
import trading.engine.event.MarketEvent;
import trading.engine.guice.annotation.data.*;

import javax.inject.Singleton;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAmount;
import java.util.*;

@Singleton
public class HistoricalMySQLDataHandler implements DataHandler {

    private final List<String> symbolList;
    private final TemporalAmount timeDelta;  // the unit of time increment
    private final Map<MarketEvent, DataBar> dataBuffer;
    private LocalDate startDate;
    private LocalDate currentDate;
    private final Connection conn;
    private static final Logger logger = LogManager.getLogger(HistoricalMySQLDataHandler.class);
    private boolean firstDataPresented = false;  // flag to check if first data come through

    @Inject
    public HistoricalMySQLDataHandler(@SymbolList List<String> symbolList, @StartDate LocalDate startDate, @TimeDelta TemporalAmount timeDelta) throws SQLException {
        this.symbolList = symbolList;
        this.startDate = startDate;
        this.currentDate = startDate;
        this.timeDelta = timeDelta;
        this.dataBuffer = new HashMap<>();
        this.conn = getConnection().orElseThrow(() -> new SQLException("MySQL connection failed"));
    }

    private Optional<Connection> getConnection() {
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
        try {
            Connection conn = DriverManager.getConnection(prop.getProperty("SQL_URL"), prop.getProperty("SQL_USER"), prop.getProperty("SQL_PASSWORD"));
            return Optional.of(conn);
        } catch (SQLException e) {
            e.printStackTrace();
            logger.error("MySQL connection failed");
            return Optional.empty();
        }
    }

    @Override
    public Optional<DataBar> fetchBarOn(String symbol, LocalDate date) {
        if (date.isAfter(currentDate)) {
            logger.info("data of {} ended at {} is not available, the latest available date is {}", symbol, date, currentDate);
            return Optional.empty();
        }
        if (date.isBefore(startDate)) {
            logger.info("data of {} started at {} is not available, the earliest available date is {}", symbol, date, startDate);
            return Optional.empty();
        }
        String dateS = date.format(DateTimeFormatter.ISO_LOCAL_DATE);
        String sql = "SELECT * FROM " + symbol + " WHERE DATE=STR_TO_DATE('" + dateS + "', '%Y-%m-%d')";
        try (Statement st = conn.createStatement()) {
            ResultSet rs = st.executeQuery(sql);
            if (rs.next()) {
                return Optional.of(new DataBar(
                        date,
                        symbol,
                        rs.getDouble("Open"),
                        rs.getDouble("High"),
                        rs.getDouble("Low"),
                        rs.getDouble("Close"),
                        rs.getDouble("Adj_Close"),
                        rs.getDouble("Volume")
                ));
            } else {
                logger.info("The symbol: {} unavailable on {}", symbol, date);
                return Optional.empty();
            }

        } catch (SQLException e) {
            e.printStackTrace();
            logger.error("MySQL connection failed");
            return Optional.empty();
        }
    }

    @Override
    public Optional<List<DataBar>> fetchNBarsUntil(String symbol, int numOfBars, LocalDate end) {
        if (end.isAfter(currentDate)) {
            logger.info("data of {} ended at {} is not available, the latest available date is {}", symbol, end, currentDate);
            return Optional.empty();
        }
        List<DataBar> res = new ArrayList<>();
        int count = 0;
        LocalDate date = end;
        while (count < numOfBars && date.isAfter(startDate)) {
            Optional<DataBar> bar = fetchBarOn(symbol, date);
            if (bar.isPresent()) {
                res.add(bar.get());
                count++;
            }
            date = date.minus(timeDelta);
        }
        if (count == 0) {
            return Optional.empty();
        }
        if (count < numOfBars) {
            logger.warn("available bars of {} is less than {} requested", symbol, numOfBars);
        }
        Collections.sort(res);
        return Optional.of(res);
    }

    private Optional<List<DataBar>> fetchBetween(String symbol, LocalDate start, LocalDate end) {
        List<DataBar> res = new ArrayList<>();
        int count = 0;
        String startS = start.format(DateTimeFormatter.ISO_LOCAL_DATE);
        String endS = end.format(DateTimeFormatter.ISO_LOCAL_DATE);
        String sql = "SELECT * FROM " + symbol + " WHERE DATE BETWEEN '" + startS + "' AND '" + endS + "'";
        try (Statement st = conn.createStatement()) {
            ResultSet rs = st.executeQuery(sql);
            while (rs.next()) {
                 res.add(new DataBar(
                        LocalDate.parse(rs.getString("Date")),
                        symbol,
                        rs.getDouble("Open"),
                        rs.getDouble("High"),
                        rs.getDouble("Low"),
                        rs.getDouble("Close"),
                        rs.getDouble("Adj_Close"),
                        rs.getDouble("Volume")
                 ));
                 count++;
            }

            if (count==0) {
                logger.info("The symbol: {} unavailable between {} and {}", symbol, startS, endS);
                return Optional.empty();
            } else {
                Collections.sort(res);
                return Optional.of(res);
            }

        } catch (SQLException e) {
            e.printStackTrace();
            logger.error("MySQL connection failed");
            return Optional.empty();
        }
    }

    @Override
    public Optional<List<DataBar>> fetchBarsBetween(String symbol, LocalDate start, LocalDate end) {
        if (end.isAfter(currentDate)) {
            logger.info("data of {} ended at {} is not available, the latest available date is {}", symbol, end, currentDate);
            return Optional.empty();
        }
        if (start.isBefore(startDate)) {
            logger.info("data of {} started at {} is not available, the earliest available date is {}", symbol, start, startDate);
            return Optional.empty();
        }
        return fetchBetween(symbol, start, end);
    }

    @Override
    public Optional<DataBar> fetchBarByME(MarketEvent event) {
        if (dataBuffer.containsKey(event)) {
            return Optional.of(dataBuffer.get(event));
        } else {
            return fetchBarOn(event.getSymbol(), event.getTimestamp());
        }
    }

    /*
    * simulate real time data feed by advance a time delta manually
    * */
    @Override
    public void update(EventQueues queue) {
        currentDate = currentDate.plus(timeDelta);
        boolean dataPresented = false;
        for (String symbol : symbolList) {
            Optional<DataBar> bar = fetchBarOn(symbol, currentDate);
            if (bar.isPresent()) {
                MarketEvent event = new MarketEvent(currentDate, symbol);
                dataBuffer.put(event, bar.get());
                queue.marketQueue.add(event);
                dataPresented = true;
            }
        }
        if (!firstDataPresented && !dataPresented) {
            startDate = currentDate;
        } else if (!firstDataPresented) {
            firstDataPresented = true;
        }
    }

    @Override
    public String toString() {
        return "HistoricalMySQLDataHandler{" +
                "\nsymbolList=" + symbolList +
                "\ntimeDelta=" + timeDelta +
                "\ndataBuffer=" + dataBuffer +
                "\nstartDate=" + startDate +
                "\ncurrentDate=" + currentDate +
                "\n}";
    }
}
