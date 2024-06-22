package io.github.bbranquinho;

import io.github.bbranquinho.dto.AggregateEntity;
import io.github.bbranquinho.dto.DynamoEvent;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;

public class ChangeDataEventManager {

    private final String environment;

    private final String appName;

    public final DynamoDbEnhancedClient dynamoDbEnhancedClient;

    public ChangeDataEventManager(String environment,
                                  DynamoDbEnhancedClient dynamoDbEnhancedClient,
                                  ChangeDataEventConfig changeDataEventConfig) {
        this.environment = environment;
        this.appName = changeDataEventConfig.appName();
        this.dynamoDbEnhancedClient = dynamoDbEnhancedClient;
    }

    public <A extends AggregateEntity> DynamoDbTable<A> createAggregateTable(String tableName, Class<A> clazz) {
        return dynamoDbEnhancedClient.table(createDynamoTableName(tableName, environment), TableSchema.fromClass(clazz));
    }

    public DynamoDbTable<DynamoEvent> createEventStoreTable(String tableName) {
        return dynamoDbEnhancedClient.table(createDynamoTableName(tableName, environment), TableSchema.fromClass(DynamoEvent.class));
    }

    /**
     * The table name is composed the <code>appName</code>, <code>tableName</code>
     * and <code>environment</code>.
     *
     * @param tableName Name of the table.
     * @return the table name.
     */
    private String createDynamoTableName(String tableName, String environment) {
        return String.format("%s.%s.%s", appName, tableName, environment);
    }

}
