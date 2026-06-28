package hr.algebra.camera.event;

import hr.algebra.camera.event.events.DataChangedEvent;

public interface EventListener {
    void onEvent(DataChangedEvent event);
}
