package io.github.change.data.event.dynamo;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.change.data.event.Aggregate;
import io.github.change.data.event.Event;
import org.jboss.logging.Logger;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbSecondaryPartitionKey;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbSortKey;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@DynamoDbBean
public class DynamoEvent {

    private static final Logger LOGGER = Logger.getLogger(DynamoEvent.class);

    public static final String IDEMPOTENCE_INDEX_NAME = "gsiIdempotence";

    private static final Long INITIAL_VERSION = 1L;

    private String id;

    private Long version;

    private String token;

    private List<DynamoEventData> events;

    private Long datetime;

    private Long ttl;

    public DynamoEvent() {
    }

    public DynamoEvent(String id,
                       Long version,
                       String token,
                       List<DynamoEventData> events,
                       Long datetime,
                       Long ttl) {
        this.id = id;
        this.version = version;
        this.token = token;
        this.events = events;
        this.datetime = datetime;
        this.ttl = ttl;
    }

    public static <A extends Aggregate> DynamoEvent fromDomain(ObjectMapper objectMapper,
                                                               Optional<String> token,
                                                               A aggregate,
                                                               String id,
                                                               Long now,
                                                               Optional<Long> ttl) {
        List<DynamoEventData> eventData = aggregate.getEvents()
                                                   .stream()
                                                   .map(e -> fromDomain(objectMapper, e))
                                                   .flatMap(Optional::stream)
                                                   .collect(Collectors.toList());

        return new DynamoEvent(
                id,
                aggregate.getVersion().map(v -> v + 1L).orElse(INITIAL_VERSION),
                token.orElse(null),
                eventData,
                now,
                ttl.orElse(null)
        );
    }

    private static Optional<DynamoEventData> fromDomain(ObjectMapper objectMapper, Event event) {
        try {
            String type = Optional.ofNullable(event.typeTransformer()).orElse(event.getClass().getName());
            String data = Optional.ofNullable(event.dataTransformer()).orElse(objectMapper.writeValueAsString(event));
            return Optional.of(new DynamoEventData(type, data));
        } catch (JsonProcessingException e) {
            LOGGER.error(String.format("Error to parse object [%s].", e.getMessage()), e);
            return Optional.empty();
        }
    }

    @DynamoDbPartitionKey
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @DynamoDbSortKey
    public Long getVersion() {
        return version;
    }

    public void setVersion(Long version) {
        this.version = version;
    }

    @DynamoDbSecondaryPartitionKey(indexNames = {IDEMPOTENCE_INDEX_NAME})
    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public List<DynamoEventData> getEvents() {
        return events;
    }

    public void setEvents(List<DynamoEventData> events) {
        this.events = events;
    }

    public Long getDatetime() {
        return datetime;
    }

    public void setDatetime(Long datetime) {
        this.datetime = datetime;
    }

    public Long getTtl() {
        return ttl;
    }

    public void setTtl(Long ttl) {
        this.ttl = ttl;
    }
}
