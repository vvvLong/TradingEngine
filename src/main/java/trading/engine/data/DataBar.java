package trading.engine.data;

import java.time.LocalDate;

public class DataBar {

    private final LocalDate timestamp;
    private final double open;
    private final double high;
    private final double low;
    private final double close;
    private final double volume;
    private final double adjClose;

    public DataBar(LocalDate timestamp, double open, double high, double low, double close, double volume, double adjClose) {
        this.timestamp = timestamp;
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
            default:
                return close;
        }
    }

    @Override
    public String toString() {
        return "DataBar{" +
                "timestamp=" + timestamp +
                ", open=" + open +
                ", high=" + high +
                ", low=" + low +
                ", close=" + close +
                ", volume=" + volume +
                ", adjClose=" + adjClose +
                '}';
    }
}
