package hr.algebra.camera.event.events;

public class DataChangedEvent {
    private final int entityId;
    private final EntityType entityType;
    private final ActionType action;

    public DataChangedEvent(int entityId, EntityType entityType, ActionType action) {
        this.entityId = entityId;
        this.entityType = entityType;
        this.action = action;
    }

    public int getEntityId() {
        return entityId;
    }

    public EntityType getEntityType() {
        return entityType;
    }

    public ActionType getAction() {
        return action;
    }
}
