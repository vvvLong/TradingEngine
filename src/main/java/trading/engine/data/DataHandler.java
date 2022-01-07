package trading.engine.data;

import trading.engine.event.Event;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface DataHandler {

    /*
    * Returns the last bar updated.
    */
    Optional<DataBar> getLatestBar(String symbol);

    /*
    * Returns the last N bars updated.
    * Latest bar appends to the latest of the list
    */
    Optional<List<DataBar>> getLatestBars(String symbol, int numOfBars);

    /*
    Returns a Date object for the last bar.
    */
    Optional<LocalDate> getLatestBarDate(String symbol);

    /*
    Returns one of the Open, High, Low, Close, Volume, adj Close, open interest from the last bar.
    */
    Optional<Double> getLatestBarValue(String symbol, BarValueType valueType);

    /*
    * Returns the last N bar values from the latest_symbol list, or N-k if less available.
    * Latest bar appends to the latest of the list
    * */
    Optional<List<Double>> getLatestBarValues(String symbol, BarValueType valueType, int numOfBars);

    /*
    * Pushes the latest bars to the bars_queue for each symbol in a tuple OHLCVI format: (datetime, open, high, low,
    * close, volume, open interest).
    * */
    void updateBar(List<Event> eventQueue);

}
