package ru.otus.dataprocessor;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.util.Map;

public class FileSerializer implements Serializer {
    private static final ObjectMapper objectMapper = new ObjectMapper();

    private final String fileName;

    public FileSerializer(String fileName) {
        this.fileName = fileName;
    }

    @Override
    public void serialize(Map<String, Double> data) {
        try {
            objectMapper.writeValue(new File(fileName), data);
        } catch (IOException e) {
            throw new FileProcessException(e);
        }
    }
}
