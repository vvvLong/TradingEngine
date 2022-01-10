package trading.engine.portfolio;

import org.jetbrains.annotations.NotNull;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class Positions implements Comparable<Positions>, Cloneable {
    private final LocalDate timestamp;
    private final double totalValue;
    private final double cash;
    private Map<String, Double> values;  // securities' total values
    private Map<String, Integer> quantities;  // securities' quantities

    public Positions(LocalDate timeStamp, double cash, Map<String, Integer> quantities, Map<String, Double> values) {
        if (!quantities.keySet().equals(values.keySet())) {
            throw new IllegalArgumentException("The given quantities and values have different symbols");
        }
        // remove entry with 0 quantity
        quantities = new HashMap<>(quantities);  // make sure invariance
        values = new HashMap<>(values);
        for (String symbol : quantities.keySet()) {
            if (quantities.get(symbol) == 0) {
                quantities.remove(symbol);
                values.remove(symbol);
            }
        }
        this.timestamp = timeStamp;
        this.quantities = quantities;
        this.values = values;
        this.cash = cash;
        this.totalValue = cash + values.values().stream().mapToDouble(Double::doubleValue).sum();
    }

    public LocalDate getTimestamp() {
        return timestamp;
    }

    public Map<String, Integer> getQuantities() {
        return new HashMap<>(quantities);
    }

    public Map<String, Double> getValues() {
        return new HashMap<>(values);
    }

    public Optional<Integer> getQuantityOf(String symbol) {
        return Optional.ofNullable(quantities.get(symbol));
    }

    public Optional<Double> getValueOf(String symbol) {
        return Optional.ofNullable(values.get(symbol));
    }

    public double getCash(){
        return cash;
    }

    public double getTotalValue(){
        return totalValue;
    }

    public boolean contains(String symbol) {
        return quantities.containsKey(symbol);
    }

    @Override
    public String toString() {
        return "Positions{" +
                "\ntimestamp=" + timestamp +
                "\ntotalValue=" + totalValue +
                "\ncash=" + cash +
                "\nvalues=" + values +
                "\nquantities=" + quantities +
                "\n}";
    }

    @Override
    public int compareTo(@NotNull Positions o) {
        if (this.timestamp.isBefore(o.timestamp)) {
            return -1;
        } else if (this.timestamp.isEqual(o.timestamp)) {
            return 0;
        } else {
            return 1;
        }
    }

    @Override
    public Positions clone() {
        try {
            Positions clone = (Positions) super.clone();
            clone.quantities = new HashMap<>(quantities);
            clone.values = new HashMap<>(values);
            return clone;
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }
}
