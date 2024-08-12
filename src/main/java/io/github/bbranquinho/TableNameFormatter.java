package io.github.bbranquinho;

public interface TableNameFormatter {

    String formatter(String tableName);

    String eventStoreFormatter(String eventStoreTableName);

}
