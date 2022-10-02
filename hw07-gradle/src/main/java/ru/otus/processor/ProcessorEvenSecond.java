package ru.otus.processor;

import ru.otus.model.Message;

public class ProcessorEvenSecond implements Processor {
    private final TimeProvider timeProvider;

    public ProcessorEvenSecond(TimeProvider timeProvider) {
        this.timeProvider = timeProvider;
    }

    @Override
    public Message process(Message message) {
        var sec = timeProvider.getTime().toEpochMilli() / 1000;
        if (sec % 2 == 0) {
            throw new ProcessorException("Even second");
        }
        return message;
    }
}