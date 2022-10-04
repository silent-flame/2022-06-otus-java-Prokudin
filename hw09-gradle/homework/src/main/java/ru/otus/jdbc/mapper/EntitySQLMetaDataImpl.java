package ru.otus.jdbc.mapper;

import java.lang.reflect.Field;
import java.util.StringJoiner;
import java.util.stream.Collectors;

public class EntitySQLMetaDataImpl<T> implements EntitySQLMetaData {
    private final String selectAllSql;
    private final String selectByIdSql;
    private final String insertSql;
    private final String updateSql;

    public EntitySQLMetaDataImpl(EntityClassMetaData<T> metaDataManager) {
        this.selectAllSql = String.format("SELECT * FROM %s", metaDataManager.getName());
        this.selectByIdSql = String.format("SELECT * FROM %s WHERE %s= ?", metaDataManager.getName(), metaDataManager.getIdField().getName());
        var joiner = new StringJoiner(",");
        for (var field : metaDataManager.getFieldsWithoutId()) {
            joiner.add("?");
        }
        var fieldNames = metaDataManager.getFieldsWithoutId().stream().map(Field::getName).collect(Collectors.joining(","));
        this.insertSql = String.format("INSERT INTO %s (%s) VALUES (%s)", metaDataManager.getName(), fieldNames, joiner);
        var mainJoiner = new StringJoiner(" ");
        mainJoiner.add("UPDATE").add(metaDataManager.getName()).add("SET");
        var updateJoiner = new StringJoiner(",");
        metaDataManager.getFieldsWithoutId().forEach(field ->
                updateJoiner.add(field.getName()).add("= ?")
        );
        mainJoiner.merge(updateJoiner);
        mainJoiner.add("WHERE").add(metaDataManager.getIdField().getName()).add("= ?");
        this.updateSql = mainJoiner.toString();
    }

    @Override
    public String getSelectAllSql() {
        return selectAllSql;
    }

    @Override
    public String getSelectByIdSql() {
        return selectByIdSql;
    }

    @Override
    public String getInsertSql() {
        return insertSql;
    }

    @Override
    public String getUpdateSql() {
        return updateSql;
    }
}