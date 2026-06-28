package hr.algebra.camera.model.enums;

public enum CameraType {
    DSLR("DSLR"),
    MIRRORLESS("Mirrorless"),
    COMPACT("Compact"),
    ACTION_CAMERA("Action Camera"),
    MEDIUM_FORMAT("Medium Format");

    private final String displayName;

    CameraType(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }

    @Override
    public String toString() {
        return displayName;
    }
}
