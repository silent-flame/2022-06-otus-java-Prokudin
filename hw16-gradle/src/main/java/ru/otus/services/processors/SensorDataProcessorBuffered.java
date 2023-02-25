package ru.otus.services.processors;

import lombok.SneakyThrows;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.api.SensorDataProcessor;
import ru.otus.api.model.SensorData;
import ru.otus.lib.SensorDataBufferedWriter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

// Этот класс нужно реализовать
public class SensorDataProcessorBuffered implements SensorDataProcessor {
    private static final Logger log = LoggerFactory.getLogger(SensorDataProcessorBuffered.class);

    private final int bufferSize;

    private List<SensorData> dataBuffer;
    private final SensorDataBufferedWriter writer;

    public SensorDataProcessorBuffered(int bufferSize, SensorDataBufferedWriter writer) {
        this.bufferSize = bufferSize;
        this.dataBuffer = Collections.synchronizedList(new ArrayList<>());
        this.writer = writer;
    }

    @Override
    @SneakyThrows
    public synchronized void process(SensorData data) {
        log.info("Process");
        if (dataBuffer.size() >= bufferSize) {
            flush();
        }
        dataBuffer.add(data);
    }

    public synchronized void flush() {
        log.info("Flush");
        try {
            if (!dataBuffer.isEmpty()) {
                var data = dataBuffer.stream().sorted(Comparator.comparing(SensorData::getMeasurementTime)).toList();
                writer.writeBufferedData(data);
                dataBuffer.clear();
            }
        } catch (Exception e) {
            log.error("Ошибка в процессе записи буфера", e);
        }
    }

    @Override
    public void onProcessingEnd() {
        flush();
    }
}
