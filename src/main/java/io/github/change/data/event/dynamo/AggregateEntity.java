package io.github.change.data.event.dynamo;

import java.util.Optional;

public interface AggregateEntity {

    String aggregatePk();

    Optional<String> aggregateSk();

}
