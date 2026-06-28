package hr.algebra.camera.utils;

import javafx.scene.image.Image;

import java.io.File;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

public final class ImageStorage {
    //TODO Add config
    private static final Path IMAGE_DIR = Path.of(System.getProperty("user.home"), ".camera-catalog", "images");

    private ImageStorage() {}

    public static String store(File source, int cameraId) {
        try {
            ensureDir();

            String filename = cameraId + extensionOf(source.getName());
            Files.copy(source.toPath(), IMAGE_DIR.resolve(filename), StandardCopyOption.REPLACE_EXISTING);

            return filename;
        } catch (IOException e) {
            throw new UncheckedIOException("Failed to store image", e);
        }
    }

    public static Image load(String filename) {
        if (filename == null || filename.isBlank()) return null;

        Path path = IMAGE_DIR.resolve(filename);
        if (!Files.exists(path)) return null;

        return new Image(path.toUri().toString());
    }

    public static void delete(String filename) {
        if (filename == null || filename.isBlank()) return;

        try {
            Files.deleteIfExists(IMAGE_DIR.resolve(filename));
        } catch (IOException ignored) {}
    }

    private static void ensureDir() throws IOException {
        Files.createDirectories(IMAGE_DIR);
    }

    private static String extensionOf(String name) {
        int dot = name.lastIndexOf('.');
        return dot == -1 ? "" : name.substring(dot);
    }
}
