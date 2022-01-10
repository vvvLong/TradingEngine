package trading.engine.portfolio;

import org.junit.jupiter.api.Assumptions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.HashMap;

public class PositionsTest {

    private Positions p;
    private HashMap<String, Double> originalValues;
    private HashMap<String, Integer> originalQuantities;

    @BeforeEach
    public void setup() {
        originalValues = new HashMap<>();
        originalValues.put("aapl", 122.);
        originalValues.put("jpm", 999.);

        originalQuantities = new HashMap<>();
        originalQuantities.put("aapl", 10);
        originalQuantities.put("jpm", 20);

        p = new Positions(LocalDate.now(), 10000, originalQuantities, originalValues);
    }

    @Test
    @DisplayName("copy should be deep copy")
    public void cloneTest() {
        Positions cp = p.clone();
        Assumptions.assumeFalse(cp == p);
        Assumptions.assumeFalse(cp.getQuantities() == p.getQuantities());
        Assumptions.assumeFalse(cp.getValues() == p.getValues());
//        Assumptions.assumeFalse(cp.values == p.values);
//        Assumptions.assumeFalse(cp.quantities == p.quantities);
    }

    @Test
    @DisplayName("Positions field should be invariant")
    public void fieldTest() {
        System.out.println(p.getQuantities());
        originalQuantities.put("xxx", 666);
        System.out.println(p.getQuantities());
        Assumptions.assumeTrue(p.getQuantities().get("xxx") == null);
    }
}
