package io.github.change.data.event.idempotency;

import io.github.change.data.event.dynamo.DynamoEvent;
import org.jboss.logging.Logger;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;

public class NoIdempotencyStrategy implements IdempotencyStrategy {

    private static final Logger LOGGER = Logger.getLogger(NoIdempotencyStrategy.class);

    public NoIdempotencyStrategy() {
        LOGGER.info("NoIdempotencyStrategy enabled.");
    }

    @Override
    public Boolean isAlreadyProcessed(String token, DynamoDbTable<DynamoEvent> eventStoreDynamoDbTable) {
        return false;
    }
}
