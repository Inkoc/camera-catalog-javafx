package hr.algebra.camera.service;

import hr.algebra.camera.model.Camera;
import hr.algebra.camera.model.Lens;
import hr.algebra.camera.repository.interfaces.ICameraRepository;
import hr.algebra.camera.service.interfaces.ICameraService;

import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

public class CameraService implements ICameraService {

    private final ICameraRepository cameraRepository;

    public CameraService(ICameraRepository cameraRepository) {
        this.cameraRepository = cameraRepository;
    }

    @Override
    public List<Camera> findAll() {
        return cameraRepository.findAll();
    }

    @Override
    public Optional<Camera> findById(int id) {
        return cameraRepository.findById(id);
    }

    @Override
    public int save(Camera camera) {
        if (camera.getName() == null || camera.getName().isBlank()) {
            throw new IllegalArgumentException("Camera name cannot be empty");
        }

        return cameraRepository.save(camera);
    }

    @Override
    public void update(Camera camera) {
        if (camera.getName() == null || camera.getName().isBlank()) {
            throw new IllegalArgumentException("Camera name cannot be empty");
        }

        cameraRepository.update(camera);
    }

    @Override
    public void deleteById(int id) {
        cameraRepository.deleteById(id);
    }

    @Override
    public List<Camera> filterCameras(Predicate<Camera> predicate) {
        return cameraRepository.findAll()
                .stream()
                .filter(predicate)
                .toList();
    }

    @Override
    public void attachLens(int cameraId, int lensId) {
        cameraRepository.attachLens(cameraId, lensId);
    }

    @Override
    public void detachLens(int cameraId, int lensId) {
        cameraRepository.detachLens(cameraId, lensId);
    }

    @Override
    public List<Lens> findLensesForCamera(int cameraId) {
        return cameraRepository.findLensesForCamera(cameraId);
    }
}
