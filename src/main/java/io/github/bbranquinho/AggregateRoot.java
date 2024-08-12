package io.github.bbranquinho;

import java.util.Arrays;
import java.util.List;

import static java.util.Collections.emptyList;
import static java.util.Collections.unmodifiableList;
import static software.amazon.awssdk.utils.CollectionUtils.mergeLists;

public abstract class AggregateRoot implements Aggregate {

    private List<Event> events = emptyList();

    @Override
    public List<Event> getEvents() {
        return unmodifiableList(events);
    }

    @Override
    public void eventCreated(List<Event> events) {
        this.events = unmodifiableList(mergeLists(this.events, events));
    }

    @Override
    public void eventCreated(Event... events) {
        eventCreated(Arrays.asList(events));
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T extends Aggregate> T applyEvents() {
        Aggregate aggregate = this;
        for (Event event : aggregate.getEvents()) {
            aggregate = event.apply(aggregate);
        }
        aggregate.eventCreated(events);
        return (T) aggregate;
    }

}
