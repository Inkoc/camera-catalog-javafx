package hr.algebra.camera.utils;

import java.nio.file.Path;

public final class AppPaths {
    public static final Path BASE = Path.of(System.getProperty("user.home"), ".camera-catalog");
    public static final Path IMAGES = BASE.resolve("images");
    public static final Path ACTION_LOG = BASE.resolve("action-log.xml");
    private AppPaths() {}

}
