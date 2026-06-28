package hr.algebra.camera.event;

import hr.algebra.camera.event.events.DataChangedEvent;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class EventBus {
    private static final EventBus INSTANCE = new EventBus();

    private final List<EventListener> listeners;

    private EventBus() {
        listeners = new CopyOnWriteArrayList<>();
    }

    public static EventBus getInstance() {
        return INSTANCE;
    }

    public void subscribe(EventListener listener) {
        if (!listeners.contains(listener)) {
            listeners.add(listener);
        }
    }

    public void unsubscribe(EventListener listener) {
        listeners.remove(listener);
    }

    public void publish(DataChangedEvent event) {
        for (EventListener listener : listeners) {
            listener.onEvent(event);
        }
    }
}
