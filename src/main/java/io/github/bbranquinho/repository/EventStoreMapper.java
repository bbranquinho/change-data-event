package io.github.bbranquinho.repository;

import io.github.bbranquinho.Aggregate;
import io.github.bbranquinho.dto.AggregateEntity;
import io.github.bbranquinho.dto.DynamoEvent;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;

import java.util.Optional;

public interface EventStoreMapper<A extends Aggregate, E extends AggregateEntity> extends EventStoreMapperRepository {

    String domainName();

    DynamoDbTable<E> getDynamoDbTable();

    DynamoDbTable<DynamoEvent> getEventStoreDynamoDbTable();

    default Optional<String> eventStorePrefix() {
        return Optional.empty();
    }

    E fromDomain(A aggregateRoot);

    A toDomain(E dynamoEntity);

}
