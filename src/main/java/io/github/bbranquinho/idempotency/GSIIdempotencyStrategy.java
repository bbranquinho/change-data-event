package io.github.bbranquinho.idempotency;

import io.github.bbranquinho.dto.DynamoEvent;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.Key;
import software.amazon.awssdk.enhanced.dynamodb.model.QueryConditional;
import software.amazon.awssdk.enhanced.dynamodb.model.QueryEnhancedRequest;

import static io.github.bbranquinho.dto.DynamoEvent.IDEMPOTENCE_INDEX_NAME;

public class GSIIdempotencyStrategy implements IdempotencyStrategy {

    // private static final Logger LOGGER = Logger.getLogger(GSIIdempotencyStrategy.class);

    private static final boolean GSI_CONSISTENT_READ = false;

    private static final int GSI_TOKEN_LIMIT = 1;

    public GSIIdempotencyStrategy() {
        // LOGGER.warn("GSIIdempotencyStrategy is being used.");
    }

    @Override
    public Boolean isAlreadyProcessed(String token, DynamoDbTable<DynamoEvent> eventStoreDynamoDbTable) {
        QueryConditional queryConditional = QueryConditional.keyEqualTo(Key.builder().partitionValue(token).build());
        QueryEnhancedRequest queryRequest = QueryEnhancedRequest.builder()
                                               .queryConditional(queryConditional)
                                               .consistentRead(GSI_CONSISTENT_READ)
                                               .limit(GSI_TOKEN_LIMIT)
                                               .build();

        return eventStoreDynamoDbTable.index(IDEMPOTENCE_INDEX_NAME)
                                      .query(queryRequest)
                                      .stream()
                                      .limit(1)
                                      .flatMap(page -> page.items().stream())
                                      .findAny()
                                      .isPresent();
    }

}
