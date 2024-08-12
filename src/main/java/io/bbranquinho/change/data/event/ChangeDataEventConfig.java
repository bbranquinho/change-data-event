package io.bbranquinho.change.data.event;

import java.util.Optional;

public interface ChangeDataEventConfig {

    default boolean enableEventStore() {
        return true;
    }

    /**
     * This TTL is used just by the event store table.
     * The <b>change-data-event</b> library doesn't set
     * automatically a ttl for the main table.
     */
    default Optional<Long> ttlInSeconds() {
        return Optional.empty();
    }

}
