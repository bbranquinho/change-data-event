package io.github.bbranquinho.idempotency;

import io.github.bbranquinho.dto.DynamoEvent;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;

public interface IdempotencyStrategy {

    Boolean isAlreadyProcessed(String token, DynamoDbTable<DynamoEvent> eventStoreDynamoDbTable);

}
