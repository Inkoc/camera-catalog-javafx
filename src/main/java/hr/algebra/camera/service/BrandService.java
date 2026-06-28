package hr.algebra.camera.service;

import hr.algebra.camera.model.Brand;
import hr.algebra.camera.repository.interfaces.IBrandRepository;
import hr.algebra.camera.service.interfaces.IBrandService;

import java.util.List;
import java.util.Optional;

public class BrandService implements IBrandService {

    private final IBrandRepository brandRepository;

    public BrandService(IBrandRepository brandRepository) {
        this.brandRepository = brandRepository;
    }

    @Override
    public List<Brand> findAll() {
        return brandRepository.findAll();
    }

    @Override
    public Optional<Brand> findById(int id) {
        return brandRepository.findById(id);
    }

    @Override
    public int save(Brand brand) {
        if (brand.getName() == null || brand.getName().isBlank()) {
            throw new IllegalArgumentException("Brand name cannot be empty");
        }

        return brandRepository.save(brand);
    }

    @Override
    public void update(Brand brand) {
        brandRepository.update(brand);
    }

    @Override
    public void deleteById(int id) {
        brandRepository.deleteById(id);
    }
}
