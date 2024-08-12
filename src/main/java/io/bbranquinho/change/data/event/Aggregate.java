package io.bbranquinho.change.data.event;

import java.util.List;
import java.util.Optional;

public interface Aggregate {
    String aggregateName();

    Optional<Long> getVersion();

    List<Event> getEvents();

    void eventCreated(List<Event> events);

    void eventCreated(Event... events);

    <T extends Aggregate> T applyEvents();
}
