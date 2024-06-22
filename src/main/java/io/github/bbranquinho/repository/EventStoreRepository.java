package io.github.bbranquinho.repository;

import io.github.bbranquinho.Aggregate;
import io.github.bbranquinho.dto.AggregateEntity;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public interface EventStoreRepository {

    default void save(Aggregate... aggregates) {
        save(Arrays.asList(aggregates));
    }

    default void save(String token, Aggregate... aggregates) {
        save(Optional.of(token), Arrays.asList(aggregates));
    }

    default void save(List<Aggregate> aggregates) {
        save(Optional.empty(), aggregates);
    }

    default void save(String token, List<Aggregate> aggregates) {
        save(Optional.of(token), aggregates);
    }

    void save(Optional<String> token, List<Aggregate> aggregates);

    <A extends Aggregate, E extends AggregateEntity> void registerMapper(EventStoreMapper<A, E> dynamoDbMapper);

}

