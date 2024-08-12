package io.github.change.data.event;

import io.github.change.data.event.dynamo.AggregateEntity;
import io.github.change.data.event.dynamo.DynamoEvent;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;

public class ChangeDataEventManager {

    public final DynamoDbEnhancedClient dynamoDbEnhancedClient;

    public final TableNameFormatter tableNameFormatter;

    public ChangeDataEventManager(DynamoDbEnhancedClient dynamoDbEnhancedClient) {
        this(dynamoDbEnhancedClient, new TableNameFormatter() {
            @Override
            public String formatter(String tableName) {
                return tableName;
            }
            @Override
            public String eventStoreFormatter(String eventStoreTableName) {
                return eventStoreTableName;
            }
        });
    }

    public ChangeDataEventManager(DynamoDbEnhancedClient dynamoDbEnhancedClient, TableNameFormatter tableNameFormatter) {
        this.dynamoDbEnhancedClient = dynamoDbEnhancedClient;
        this.tableNameFormatter = tableNameFormatter;
    }

    public <A extends AggregateEntity> DynamoDbTable<A> createAggregateTable(String tableName, Class<A> clazz) {
        return dynamoDbEnhancedClient.table(tableNameFormatter.formatter(tableName), TableSchema.fromClass(clazz));
    }

    public DynamoDbTable<DynamoEvent> createEventStoreTable(String tableName) {
        return dynamoDbEnhancedClient.table(tableNameFormatter.eventStoreFormatter(tableName), TableSchema.fromClass(DynamoEvent.class));
    }

}
