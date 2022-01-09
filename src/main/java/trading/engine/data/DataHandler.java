package trading.engine.data;

import trading.engine.event.Event;
import trading.engine.event.EventQueues;
import trading.engine.event.MarketEvent;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public interface DataHandler {

    /*
     * Returns a bar of the symbol on the date
     */
    Optional<DataBar> fetchBarOn(String symbol, LocalDate date);

    /*
    * Returns the last N available bars ended at timestamp
    */
    Optional<List<DataBar>> fetchNBarsUntil(String symbol, int numOfBars, LocalDate end);

    /*
     * Returns available bars between the two timestamps
     */
    Optional<List<DataBar>> fetchBarsBetween(String symbol, LocalDate start, LocalDate end);

    /*
     * fetch a bar specified by market event
     */
    Optional<DataBar> fetchBarByME(MarketEvent event);

    /*
    * Pushes the latest bars to the data buffer and generate market events into event queue
    */
    void update(EventQueues queue);

    /*
    * convert list of bars into list of a specified value
    * */
    default List<Double> bars2Values(List<DataBar> bars, BarValueType type) {
        ArrayList<Double> res = new ArrayList<>();
        for (DataBar bar : bars) {
            res.add(bar.getBarVal(type));
        }
        return res;
    }

    /*
     * convert list of bars into list of time
     * */
    default List<LocalDate> bars2Timestamps(List<DataBar> bars) {
        ArrayList<LocalDate> res = new ArrayList<>();
        for (DataBar bar : bars) {
            res.add(bar.getTimestamp());
        }
        return res;
    }

    /*
     * convert list of bars into list of symbol
     * */
    default List<String> bars2Symbol(List<DataBar> bars) {
        ArrayList<String> res = new ArrayList<>();
        for (DataBar bar : bars) {
            res.add(bar.getSymbol());
        }
        return res;
    }

}
