package io.github.bbranquinho.idempotency;

import io.github.bbranquinho.dto.DynamoEvent;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;

public class NoIdempotencyStrategy implements IdempotencyStrategy {

//    private static final Logger LOGGER = Logger.getLogger(NoIdempotencyStrategy.class);

    public NoIdempotencyStrategy() {
//        LOGGER.warn("NoIdempotencyStrategy is being used.");
    }

    @Override
    public Boolean isAlreadyProcessed(String token, DynamoDbTable<DynamoEvent> eventStoreDynamoDbTable) {
        return false;
    }
}
