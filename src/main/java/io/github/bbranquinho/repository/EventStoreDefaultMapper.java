package io.github.bbranquinho.repository;

import io.github.bbranquinho.dto.DynamoEvent;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;

public interface EventStoreDefaultMapper extends DynamoDbTable<DynamoEvent> {
}
