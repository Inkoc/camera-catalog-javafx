package hr.algebra.camera.utils;

import javafx.scene.image.Image;

import java.io.File;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Stream;

public final class ImageStorage {
    private static final Logger LOGGER = Logger.getLogger(ImageStorage.class.getName());

    private ImageStorage() {}

    public static String store(File source, int cameraId) {
        try {
            ensureDir();

            String filename = cameraId + extensionOf(source.getName());
            Files.copy(source.toPath(), AppPaths.IMAGES.resolve(filename), StandardCopyOption.REPLACE_EXISTING);

            return filename;
        } catch (IOException e) {
            throw new UncheckedIOException("Failed to store image", e);
        }
    }

    public static Image load(String filename) {
        if (filename == null || filename.isBlank()) return null;

        Path path = AppPaths.IMAGES.resolve(filename);
        if (!Files.exists(path)) return null;

        return new Image(path.toUri().toString());
    }

    public static void delete(String filename) {
        if (filename == null || filename.isBlank()) return;

        try {
            Files.deleteIfExists(AppPaths.IMAGES.resolve(filename));
        } catch (IOException e) {
            LOGGER.log(Level.WARNING, e, () -> "Failed to delete image: " + filename);
        }
    }

    private static void ensureDir() throws IOException {
        Files.createDirectories(AppPaths.IMAGES);
    }

    private static String extensionOf(String name) {
        int dot = name.lastIndexOf('.');
        return dot == -1 ? "" : name.substring(dot);
    }

    public static void clearAll() {
        if (!Files.exists(AppPaths.IMAGES)) return;
        try (Stream<Path> files = Files.list(AppPaths.IMAGES)) {
            files.forEach(p -> {
                try {
                    Files.deleteIfExists(p);
                } catch (IOException e) {
                    LOGGER.log(Level.WARNING, "Failed to delete image: " + p, e);
                }
            });
        } catch (IOException e) {
            LOGGER.log(Level.WARNING, "Failed to clear images directory", e);
        }
    }
}
