package trading.engine;

import trading.engine.backtest.Backtest;
import trading.engine.backtest.NaiveBacktest;

public class Main {
    public static void main(String[] args) {
        Backtest backtest = new NaiveBacktest();
        backtest.run();
        backtest.report();
    }
}
