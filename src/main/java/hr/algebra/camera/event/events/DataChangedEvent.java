package hr.algebra.camera.event.events;

public class DataChangedEvent {
    private final String entityType;
    private final int entityId;

    public DataChangedEvent(String entityType, int entityId) {
        this.entityType = entityType;
        this.entityId = entityId;
    }

    public String getEntityType() {
        return entityType;
    }

    public int getEntityId() {
        return entityId;
    }
}
