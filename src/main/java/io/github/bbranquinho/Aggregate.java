package io.github.bbranquinho;

import java.util.List;
import java.util.Optional;

public interface Aggregate {
    String domainName();

    Optional<Long> getVersion();

    List<Event> getEvents();

    void eventCreated(List<Event> events);

    void eventCreated(Event... events);

    <T extends Aggregate> T applyEvents();
}
