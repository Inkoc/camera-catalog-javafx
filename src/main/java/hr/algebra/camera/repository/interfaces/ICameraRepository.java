package hr.algebra.camera.repository.interfaces;

import hr.algebra.camera.model.Camera;
import hr.algebra.camera.model.Lens;

import java.util.List;

public interface ICameraRepository extends IRepository<Camera> {
    void attachLens(int cameraId, int lensId);
    void detachLens(int cameraId, int lensId);
    List<Lens> findLensesForCamera(int cameraId);
    void clearAllData();
}
