package io.bbranquinho.change.data.event.idempotency;

import io.bbranquinho.change.data.event.dynamo.DynamoEvent;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;

public interface IdempotencyStrategy {

    Boolean isAlreadyProcessed(String token, DynamoDbTable<DynamoEvent> eventStoreDynamoDbTable);

}
