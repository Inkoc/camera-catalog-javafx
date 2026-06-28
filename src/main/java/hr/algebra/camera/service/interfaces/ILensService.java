package hr.algebra.camera.service.interfaces;

import hr.algebra.camera.model.Lens;

import java.util.List;
import java.util.function.Predicate;

public interface ILensService extends IService<Lens> {
    List<Lens> filterLenses(Predicate<Lens> predicate);
}
