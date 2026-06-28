package hr.algebra.camera.event.events;

public class DataChangedEvent {
    private final int entityId;
    private final String entityType;
    private final String action;

    public DataChangedEvent(int entityId, String entityType, String action) {
        this.entityId = entityId;
        this.entityType = entityType;
        this.action = action;
    }

    public int getEntityId() {
        return entityId;
    }

    public String getEntityType() {
        return entityType;
    }

    public String getAction() {
        return action;
    }
}
