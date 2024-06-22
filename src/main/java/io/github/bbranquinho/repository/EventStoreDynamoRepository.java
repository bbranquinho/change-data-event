package io.github.bbranquinho.repository;

import io.github.bbranquinho.Aggregate;
import io.github.bbranquinho.ChangeDataEventConfig;
import io.github.bbranquinho.dto.AggregateEntity;
import io.github.bbranquinho.dto.DynamoEvent;
import io.github.bbranquinho.idempotency.IdempotencyStrategy;
import com.fasterxml.jackson.databind.ObjectMapper;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.model.TransactWriteItemsEnhancedRequest;
import software.amazon.awssdk.services.dynamodb.model.IdempotentParameterMismatchException;

import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class EventStoreDynamoRepository implements EventStoreRepository {

    //private static final Logger LOGGER = Logger.getLogger(EventStoreDynamoRepository.class);

    private static final String DEFAULT_EVENT_STORE_DELIMITER = "#";

    private final Map<String, EventStoreMapper> eventStoreMapperByDomainName = new HashMap<>();

    private final ObjectMapper objectMapper;

    private final DynamoDbEnhancedClient dynamoDbEnhancedClient;

    private final ChangeDataEventConfig changeDataEventConfig;

    private final IdempotencyStrategy idempotencyStrategy;

    public EventStoreDynamoRepository(ObjectMapper objectMapper,
                                      DynamoDbEnhancedClient dynamoDbEnhancedClient,
                                      ChangeDataEventConfig changeDataEventConfig,
                                      IdempotencyStrategy idempotencyStrategy) {
        this.objectMapper = objectMapper;
        this.dynamoDbEnhancedClient = dynamoDbEnhancedClient;
        this.changeDataEventConfig = changeDataEventConfig;
        this.idempotencyStrategy = idempotencyStrategy;
    }

    @Override
    public void save(Optional<String> token, List<Aggregate> aggregates) {
        Instant now = Instant.now();
        Long datetime = now.toEpochMilli();
        Optional<Long> ttl = getTTLIntervalInSeconds().map(now::plusSeconds).map(Instant::getEpochSecond);
        TransactWriteItemsEnhancedRequest.Builder requestBuilder = TransactWriteItemsEnhancedRequest.builder();

        token.ifPresent(requestBuilder::clientRequestToken);

        aggregates.forEach(aggregate -> {
            EventStoreMapper eventStoreMapper = eventStoreMapperByDomainName.get(aggregate.domainName());

            if (eventStoreMapper == null) {
                throw new IllegalStateException(String.format("There is no mapper registered for the %s.", aggregate.getClass()));
            }

            if (token.isPresent() && idempotencyStrategy.isAlreadyProcessed(token.get(), eventStoreMapper.getEventStoreDynamoDbTable())) {
                throw IdempotentParameterMismatchException
                        .builder()
                        .message(String.format("Idempotency Token [%s] already processed.", token))
                        .build();
            }

            AggregateEntity aggregateEntity = eventStoreMapper.fromDomain(aggregate);
            requestBuilder.addPutItem(eventStoreMapper.getDynamoDbTable(), aggregateEntity);

            String id = String.format("%s%s%s", eventStoreMapper.eventStorePrefix().orElse(""),
                                   aggregateEntity.aggregatePk(),
                                   aggregateEntity.aggregateSk().map(s -> getEventStoreDelimiter() + s).orElse("")
            );

            DynamoEvent event = DynamoEvent.fromDomain(objectMapper, token, aggregate, id, datetime, ttl);
            requestBuilder.addPutItem(eventStoreMapper.getEventStoreDynamoDbTable(), event);
        });

        dynamoDbEnhancedClient.transactWriteItems(requestBuilder.build());
    }

    public String getEventStoreDelimiter() {
        return DEFAULT_EVENT_STORE_DELIMITER;
    }

    public Optional<Long> getTTLIntervalInSeconds() {
        return changeDataEventConfig.ttlInSeconds();
    }

    @Override
    public <A extends Aggregate, E extends AggregateEntity> void registerMapper(EventStoreMapper<A, E> dynamoDbMapper) {
        //LOGGER.warnf("Registering mapper for domain %s and class %s.", dynamoDbMapper.domainName(), dynamoDbMapper.getClass());

        if (this.eventStoreMapperByDomainName.containsKey(dynamoDbMapper.domainName())) {
            throw new IllegalStateException(String.format("Domain %s already registered.", dynamoDbMapper.domainName()));
        }

        this.eventStoreMapperByDomainName.put(dynamoDbMapper.domainName(), dynamoDbMapper);
    }

}
