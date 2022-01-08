package trading.engine.portfolio;

import trading.engine.event.*;

import java.time.LocalDate;
import java.util.List;

/*
* Interface for portfolio object;
* A position is a symbol with its quantity;
* A holding is a symbol with its value;
* Portfolio holdings include cash, and total
* */
public interface Portfolio {

    /*
    * Adds a new record to the positions' matrix for the current market data bar. This reflects the PREVIOUS bar, i.e. all
    * current market data at this stage is known (OHLCV).
    * Makes use of a MarketEvent from the events queue.
    * */
    void updateTimestamp(MarketEvent event);

    /*
    * Updates the portfolio current positions and holdings from a FillEvent.
    * */
    void updateFill(FillEvent event);

    /*
    * generate Order Event
    * */
    OrderEvent generateOrder(SignalEvent event);

    /*
    * calls the generateOrder method and adds the generated order to the events queue
    * */
    void updateSignal(SignalEvent event, List<Event> queue);

    /*
    * getters
    * */
    List<String> getSymbolList();

    LocalDate getStartDate();

    double getInitialCapital();

    Positions getCurrentPositions();

    List<Positions> getAllPositions();

    Holdings getCurrentHoldings();

    List<Holdings> getAllHoldings();
}
