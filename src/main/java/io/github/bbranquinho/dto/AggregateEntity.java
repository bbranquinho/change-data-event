package io.github.bbranquinho.dto;

import java.util.Optional;

public interface AggregateEntity {

    String aggregatePk();

    Optional<String> aggregateSk();

}
