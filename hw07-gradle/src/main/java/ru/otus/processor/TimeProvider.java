package ru.otus.processor;

import java.time.Instant;

public interface TimeProvider {
    Instant getTime();
}
