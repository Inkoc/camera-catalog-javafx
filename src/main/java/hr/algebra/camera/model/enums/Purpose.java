package hr.algebra.camera.model.enums;

public enum Purpose {
    PORTRAIT("Portrait"),
    LANDSCAPE("Landscape"),
    WILDLIFE("Wildlife"),
    SPORTS("Sports"),
    VLOGGING("Vlogging"),
    STREET_PHOTO("Street Photo");

    private final String displayName;

    Purpose(String displayName) {
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
