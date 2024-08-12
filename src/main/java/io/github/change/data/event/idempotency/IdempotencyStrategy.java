package io.github.change.data.event.idempotency;

import io.github.change.data.event.dynamo.DynamoEvent;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;

public interface IdempotencyStrategy {

    Boolean isAlreadyProcessed(String token, DynamoDbTable<DynamoEvent> eventStoreDynamoDbTable);

}
