package ru.otus.jdbc.mapper;

import ru.otus.core.repository.DataTemplate;
import ru.otus.core.repository.executor.DbExecutor;
import ru.otus.core.sessionmanager.DataBaseOperationException;

import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.Callable;
import java.util.stream.Collectors;

/**
 * Сохратяет объект в базу, читает объект из базы
 */
public class DataTemplateJdbc<T> implements DataTemplate<T> {

    private final DbExecutor dbExecutor;
    private final EntitySQLMetaData sqlMetaData;
    private final EntityClassMetaData<T> metaData;

    public DataTemplateJdbc(DbExecutor dbExecutor, EntitySQLMetaData sqlMetaData, EntityClassMetaData<T> metaData) {
        this.dbExecutor = dbExecutor;
        this.sqlMetaData = sqlMetaData;
        this.metaData = metaData;
    }

    @Override
    public Optional<T> findById(Connection connection, long id) {
        return dbExecutor.executeSelect(connection, sqlMetaData.getSelectByIdSql(), List.of(id), rs ->
                wrapException(() -> {
                    if (rs.next()) {
                        return map(rs);
                    }
                    return null;
                })
        );
    }

    private T map(ResultSet rs) {
        return wrapException(() ->
                {
                    T instance = metaData.getConstructor().newInstance();
                    metaData.getAllFields().forEach(field ->
                            wrapException(() -> {
                                field.set(instance, rs.getObject(field.getName()));
                                return null;
                            })
                    );
                    return instance;
                }
        );
    }

    @Override
    public List<T> findAll(Connection connection) {
        return dbExecutor.executeSelect(connection, sqlMetaData.getSelectAllSql(), Collections.emptyList(), rs ->
                wrapException(() -> {
                    List<T> items = new ArrayList<>();
                    while (rs.next()) {
                        items.add(map(rs));
                    }
                    return items;
                })
        ).get();
    }

    @Override
    public long insert(Connection connection, T entity) {
        return dbExecutor.executeStatement(connection, sqlMetaData.getInsertSql(), fields(entity));
    }

    @Override
    public void update(Connection connection, T entity) {
        var params = fields(entity);
        params.add(wrapException(() -> metaData.getIdField().get(entity)));
        dbExecutor.executeStatement(connection, sqlMetaData.getUpdateSql(), params);
    }

    private List<Object> fields(T entity) {
        var fields = metaData.getFieldsWithoutId();
        return fields.stream().map(field -> wrapException(() -> field.get(entity))).collect(Collectors.toList());
    }

    private <T> T wrapException(Callable<T> action) {
        try {
            return action.call();
        } catch (Exception ex) {
            throw new DataBaseOperationException("exception", ex);
        }
    }
}