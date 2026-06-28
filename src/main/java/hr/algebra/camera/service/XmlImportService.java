package hr.algebra.camera.service;

import hr.algebra.camera.exception.DataImportException;
import hr.algebra.camera.model.Brand;
import hr.algebra.camera.model.Camera;
import hr.algebra.camera.model.dto.CameraCatalogDto;
import hr.algebra.camera.model.dto.CameraDto;
import hr.algebra.camera.model.enums.CameraType;
import hr.algebra.camera.model.enums.Purpose;
import hr.algebra.camera.service.interfaces.IBrandService;
import hr.algebra.camera.service.interfaces.ICameraService;
import hr.algebra.camera.service.interfaces.IXmlImportService;
import jakarta.xml.bind.JAXBContext;

import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

public class XmlImportService implements IXmlImportService {
    private final ICameraService cameraService;
    private final IBrandService brandService;

    public XmlImportService(ICameraService cameraService, IBrandService brandService) {
        this.cameraService = cameraService;
        this.brandService = brandService;
    }

    @Override
    public int importFromFile(Path path) {
        try (InputStream in = Files.newInputStream(path)) {
            return importFromStream(in);
        } catch (DataImportException e) {
            throw e;
        } catch (Exception e) {
            throw new DataImportException("Failed to import from " + path, e);
        }
    }

    private int importFromStream(InputStream in) throws Exception {
        JAXBContext ctx = JAXBContext.newInstance(CameraCatalogDto.class);
        CameraCatalogDto catalog = (CameraCatalogDto) ctx.createUnmarshaller().unmarshal(in);
        Map<String, Brand> brandsByName = brandService.findAll().stream()
                .collect(Collectors.toMap(b -> b.getName().toLowerCase(), Function.identity()));
        Set<String> existingNames = cameraService.findAll().stream()
                .map(c -> c.getName().toLowerCase())
                .collect(Collectors.toSet());
        int imported = 0;
        for (CameraDto dto : catalog.getCameras()) {
            if (existingNames.contains(dto.getName().toLowerCase())) continue;
            Brand brand = resolveBrand(dto.getBrand(), brandsByName);
            cameraService.save(toCamera(dto, brand));
            imported++;
        }
        return imported;
    }

    private Brand resolveBrand(String name, Map<String, Brand> cache) {
        if (name == null || name.isBlank()) return null;

        Brand existing = cache.get(name.toLowerCase());
        if (existing != null) return existing;

        int id = brandService.save(new Brand(0, name, null));

        Brand created = new Brand(id, name, null);
        cache.put(name.toLowerCase(), created);

        return created;
    }

    private Camera toCamera(CameraDto cameraDto, Brand brand) {
        return new Camera(
                0, cameraDto.getName(), cameraDto.getReleaseYear(), cameraDto.getMegapixels(),
                cameraDto.getSensorType(), cameraDto.getIsoRange(), cameraDto.getMaxShootingSpeed(),
                cameraDto.getPrice(), null, brand,
                CameraType.valueOf(cameraDto.getCameraType()),
                Purpose.valueOf(cameraDto.getPurpose())
        );
    }
}
