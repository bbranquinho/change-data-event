package io.bbranquinho.change.data.event.repository;

import io.bbranquinho.change.data.event.dynamo.AggregateEntity;
import io.bbranquinho.change.data.event.Aggregate;
import io.bbranquinho.change.data.event.dynamo.DynamoEvent;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;

import java.util.Optional;

public interface EventStoreMapper<A extends Aggregate, E extends AggregateEntity> extends EventStoreMapperRepository {

    String aggregateName();

    DynamoDbTable<E> getDynamoDbTable();

    DynamoDbTable<DynamoEvent> getEventStoreDynamoDbTable();

    default Optional<String> eventStorePrefix() {
        return Optional.empty();
    }

    E fromDomain(A aggregateRoot);

    A toDomain(E dynamoEntity);

}
