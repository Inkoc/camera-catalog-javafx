package hr.algebra.camera.service;

import hr.algebra.camera.auth.SessionManager;
import hr.algebra.camera.model.Lens;
import hr.algebra.camera.repository.interfaces.ILensRepository;
import hr.algebra.camera.service.interfaces.ILensService;

import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

public class LensService implements ILensService {
    private final ILensRepository lensRepository;

    public LensService(ILensRepository lensRepository) {
        this.lensRepository = lensRepository;
    }

    @Override
    public List<Lens> filterLenses(Predicate<Lens> predicate) {
        return lensRepository.findAll()
                .stream()
                .filter(predicate)
                .toList();
    }

    @Override
    public List<Lens> findAll() {
        return lensRepository.findAll();
    }

    @Override
    public Optional<Lens> findById(int id) {
        return lensRepository.findById(id);
    }

    @Override
    public int save(Lens lens) {
        SessionManager.getInstance().requireAdmin();

        if (lens.getPrice() < 0) {
            throw new IllegalArgumentException("Price cannot be negative");
        }
        return lensRepository.save(lens);
    }

    @Override
    public void update(Lens lens) {
        SessionManager.getInstance().requireAdmin();

        lensRepository.update(lens);
    }

    @Override
    public void deleteById(int id) {
        SessionManager.getInstance().requireAdmin();

        lensRepository.deleteById(id);
    }
}
