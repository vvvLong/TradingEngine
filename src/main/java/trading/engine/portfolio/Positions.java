package trading.engine.portfolio;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

public class Positions {
    private final LocalDate timeStamp;
    private final Map<String, Integer> positions;

    public Positions(LocalDate timeStamp, Map<String, Integer> positions) {
        this.timeStamp = timeStamp;
        this.positions = positions;
    }

    public LocalDate getTimeStamp() {
        return timeStamp;
    }

    public Map<String, Integer> getPositions() {
        return positions;
    }

    public int getPosition(String symbol) {
        return positions.get(symbol);
    }

    @Override
    public String toString() {
        return "Positions{" +
                "timeStamp=" + timeStamp +
                ", positions=" + positions +
                '}';
    }
}
