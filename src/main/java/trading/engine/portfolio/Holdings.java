package trading.engine.portfolio;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

public class Holdings {

    private final LocalDate timeStamp;
    private final Map<String, Double> holdings;
    private double cash;
    private double total;

    public Holdings(LocalDate timeStamp, Map<String, Double> holdings, double cash) {
        this.timeStamp = timeStamp;
        this.holdings = holdings;
        this.cash = cash;
        this.total = cash + holdings.values().stream().mapToDouble(Double::doubleValue).sum();
    }

    public void calcTotal() {
        this.total = cash + holdings.values().stream().mapToDouble(Double::doubleValue).sum();
    }

    public LocalDate getTimeStamp() {
        return timeStamp;
    }

    public Map<String, Double> getHoldings() {
        return holdings;
    }

    public double getHolding(String symbol) {
        return holdings.get(symbol);
    }

    public double getCash() {
        return cash;
    }

    public double getTotal() {
        return total;
    }

    @Override
    public String toString() {
        return "Holdings{" +
                "timeStamp=" + timeStamp +
                ", holdings=" + holdings +
                ", cash=" + cash +
                ", total=" + total +
                '}';
    }

    public void setCash(double cash) {
        this.cash = cash;
    }
}

