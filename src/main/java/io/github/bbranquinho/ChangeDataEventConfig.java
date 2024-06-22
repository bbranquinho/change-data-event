package io.github.bbranquinho;

import java.util.Optional;

public interface ChangeDataEventConfig {

    /**
     * The name of the application.
     */
    String appName();

    /**
     * This TTL is used just by the event store table. In other words,
     * the <b>change-data-event</b> library doesn't set automatically
     * a ttl for the main table.
     */
    default Optional<Long> ttlInSeconds() {
        return Optional.empty();
    }

}
