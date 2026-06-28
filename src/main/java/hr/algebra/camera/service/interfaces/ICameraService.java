package hr.algebra.camera.service.interfaces;

import hr.algebra.camera.model.Camera;
import hr.algebra.camera.model.Lens;

import java.util.List;
import java.util.function.Predicate;

public interface ICameraService extends IService<Camera> {
    List<Camera> filterCameras(Predicate<Camera> predicate);

    void attachLens(int cameraId, int lensId);
    void detachLens(int cameraId, int lensId);
    List<Lens> findLensesForCamera(int cameraId);
    void clearCatalog();
}
