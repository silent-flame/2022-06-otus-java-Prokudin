package ru.otus.cache;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;
import java.util.function.Function;

public class MyCache<K, V> implements HwCache<K, V> {
    private static final Logger log = LoggerFactory.getLogger(MyCache.class);
    //Надо реализовать эти методы
    private final Map<K, V> cache = new WeakHashMap<>();
    private final List<HwListener<K, V>> listeners = new ArrayList<>();

    private final Function<K, V> delegate;

    public MyCache(Function<K, V> delegate) {
        this.delegate = delegate;
    }

    private enum Event {
        PUT, REMOVE, GET
    }

    @Override
    public void put(K key, V value) {
        cache.put(key, value);
        notifyListeners(key, value, Event.PUT);
    }

    @Override
    public void remove(K key) {
        V value = cache.remove(key);
        notifyListeners(key, value, Event.REMOVE);
    }

    @Override
    public V get(K key) {
        V value = cache.get(key);
        if (value == null) {
            value = delegate.apply(key);
            put(key, value);
        }
        notifyListeners(key, value, Event.GET);
        return value;
    }

    @Override
    public void addListener(HwListener<K, V> listener) {
        listeners.add(listener);
    }

    @Override
    public void removeListener(HwListener<K, V> listener) {
        listeners.remove(listener);
    }

    private void notifyListeners(K key, V value, Event event) {
        listeners.forEach(listener ->
                {
                    try {

                        listener.notify(key, value, event.name());
                    } catch (Exception e) {
                        log.error("Listener error", e);
                    }
                }
        );
    }
}