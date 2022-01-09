package trading.engine.event;


import java.time.LocalDate;

public interface Event {

    EventType getEventType();

    LocalDate getTimestamp();

    String getSymbol();

}
