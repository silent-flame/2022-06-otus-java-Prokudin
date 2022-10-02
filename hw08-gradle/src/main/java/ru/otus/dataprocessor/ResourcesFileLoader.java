package ru.otus.dataprocessor;

import com.fasterxml.jackson.databind.ObjectMapper;
import ru.otus.model.Measurement;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ResourcesFileLoader implements Loader {
    private static final ObjectMapper objectMapper = new ObjectMapper();
    private final String fileName;

    public ResourcesFileLoader(String fileName) {
        this.fileName = fileName;
    }

    @Override
    public List<Measurement> load() {
        try {
            List<Map<String, Object>> content = objectMapper.readValue(new File(fileName), List.class);
            return content.stream().map(map -> new Measurement((String) map.get("name"), (Double) map.get("value"))).collect(Collectors.toList());
        } catch (IOException e) {
            throw new FileProcessException(e);
        }
    }
}
