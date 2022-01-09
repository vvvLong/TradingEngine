package trading.engine.data;

import org.jetbrains.annotations.NotNull;

import java.time.LocalDate;

public class DataBar implements Comparable<DataBar>{

    private final LocalDate timestamp;
    private final String symbol;
    private final double open;
    private final double high;
    private final double low;
    private final double close;
    private final double adjClose;
    private final double volume;

    public DataBar(LocalDate timestamp, String symbol, double open, double high, double low, double close, double adjClose, double volume) {
        this.timestamp = timestamp;
        this.symbol = symbol;
        this.open = open;
        this.high = high;
        this.low = low;
        this.close = close;
        this.volume = volume;
        this.adjClose = adjClose;
    }

    public LocalDate getTimestamp() {
        return timestamp;
    }

    public String getSymbol() {
        return symbol;
    }

    public double getBarVal(BarValueType type) {
        switch (type) {
            case LOW:
                return low;
            case HIGH:
                return high;
            case OPEN:
                return open;
            case ADJ_CLOSE:
                return adjClose;
            case VOLUME:
                return volume;
            case CLOSE:
            default:
                return close;
        }
    }

    @Override
    public String toString() {
        return "DataBar{" +
                "timestamp=" + timestamp +
                ", symbol='" + symbol + '\'' +
                ", open=" + open +
                ", high=" + high +
                ", low=" + low +
                ", close=" + close +
                ", adjClose=" + adjClose +
                ", volume=" + volume +
                '}';
    }

    @Override
    public int compareTo(@NotNull DataBar o) {
        if (this.timestamp.isBefore(o.timestamp)) {
            return -1;
        } else if (this.timestamp.isEqual(o.timestamp)) {
            return 0;
        } else {
            return 1;
        }
    }
}
