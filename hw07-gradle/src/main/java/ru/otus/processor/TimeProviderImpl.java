package ru.otus.processor;

import java.time.Instant;

public class TimeProviderImpl implements TimeProvider {
    @Override
    public Instant getTime() {
        return Instant.now();
    }
}