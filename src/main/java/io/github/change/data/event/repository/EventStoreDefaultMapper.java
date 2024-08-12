package io.github.change.data.event.repository;

import io.github.change.data.event.dynamo.DynamoEvent;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;

public interface EventStoreDefaultMapper extends DynamoDbTable<DynamoEvent> {
}
