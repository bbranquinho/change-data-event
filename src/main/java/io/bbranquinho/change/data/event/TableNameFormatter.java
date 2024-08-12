package io.bbranquinho.change.data.event;

public interface TableNameFormatter {

    String formatter(String tableName);

    String eventStoreFormatter(String eventStoreTableName);

}
