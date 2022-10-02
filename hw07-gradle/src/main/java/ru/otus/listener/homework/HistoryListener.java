package ru.otus.listener.homework;

import ru.otus.listener.Listener;
import ru.otus.model.Message;

import java.util.HashMap;
import java.util.Optional;

public class HistoryListener implements Listener, HistoryReader {
    private final HashMap<Long, Message> history = new HashMap<>();

    @Override
    public void onUpdated(Message msg) {
        if (!history.containsKey(msg.getId())) {
            history.put(msg.getId(), msg.copy());
            return;
        }
        throw new IllegalStateException("Msg already in the history");
    }

    @Override
    public Optional<Message> findMessageById(long id) {
        return Optional.ofNullable(history.get(id));
    }
}