package ru.otus.mapper;

import ru.otus.annotation.Id;
import ru.otus.annotation.Table;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class EntityClassMetaDataImpl<T> implements EntityClassMetaData<T> {

    private final String name;
    private final Constructor<T> constructor;
    private final Field idField;
    private final List<Field> fields;
    private final List<Field> fieldsWithoutId;

    private EntityClassMetaDataImpl(String name, Constructor<T> constructor,
                                    Field idField, List<Field> fields, List<Field> fieldsWithoutId) {
        this.name = name;
        this.constructor = constructor;
        this.idField = idField;
        this.fields = fields;
        this.fieldsWithoutId = fieldsWithoutId;
    }

    public static <T> EntityClassMetaDataImpl<T> of(Class<T> clazz) {
        try {
            var name = clazz.getAnnotation(Table.class).value();
            var constructor = clazz.getDeclaredConstructor(null);
            var fieldsArray = clazz.getDeclaredFields();
            for (var field : fieldsArray) {
                field.setAccessible(true);
            }
            var fields = Arrays.asList(fieldsArray);
            var idField = fields.stream().filter(field -> field.isAnnotationPresent(Id.class))
                    .findAny().orElseThrow(() -> new IllegalStateException("Annotation @Id missing"));
            var fieldsWithoutId = fields.stream().filter(field -> !field.isAnnotationPresent(Id.class)).collect(Collectors.toList());
            return new EntityClassMetaDataImpl<>(name, constructor, idField, fields, fieldsWithoutId);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public Constructor<T> getConstructor() {
        return constructor;
    }

    @Override
    public Field getIdField() {
        return idField;
    }

    @Override
    public List<Field> getAllFields() {
        return fields;
    }

    @Override
    public List<Field> getFieldsWithoutId() {
        return fieldsWithoutId;
    }
}