package io.bbranquinho.change.data.event.dynamo;

import java.util.Optional;

public interface AggregateEntity {

    String aggregatePk();

    Optional<String> aggregateSk();

}
