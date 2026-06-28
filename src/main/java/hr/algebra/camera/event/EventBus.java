package hr.algebra.camera.event;

import hr.algebra.camera.event.events.DataChangedEvent;

import java.util.ArrayList;
import java.util.List;

public class EventBus {
    private static final EventBus INSTANCE = new EventBus();

    private final List<EventListener> listeners;

    //TODO CopyWriteOnWriteArrayList
    private EventBus() {
        listeners = new ArrayList<>();
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
