package hr.algebra.camera.utils;

import javafx.concurrent.Task;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public final class ThreadManager {
    private static final ExecutorService POOL = Executors.newCachedThreadPool();

    private ThreadManager(){ }

    public static <T> void run(Task<T> task) {
        POOL.execute(task);
    }

    public static void shutdown() {
        POOL.shutdownNow();
    }
}
