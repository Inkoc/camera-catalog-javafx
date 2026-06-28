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
import hr.algebra.camera.service.interfaces.IDataImportService;
import jakarta.xml.bind.JAXBContext;

import java.io.InputStream;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

public class DataImportService implements IDataImportService {
    private final ICameraService cameraService;
    private final IBrandService brandService;

    public DataImportService(ICameraService cameraService, IBrandService brandService) {
        this.cameraService = cameraService;
        this.brandService = brandService;
    }

    @Override
    public int importFromUrl(String url) {
        CameraCatalogDto catalog = fetchAndParse(url);

        Map<String, Brand> brandsByName = brandService.findAll().stream()
                .collect(Collectors.toMap(b -> b.getName().toLowerCase(), Function.identity()));

        Set<String> existingNames = cameraService.findAll().stream()
                .map(c -> c.getName().toLowerCase())
                .collect(Collectors.toSet());

        int imported = 0;
        for (CameraDto dto : catalog.getCameras()) {
            if (existingNames.contains(dto.getName().toLowerCase())) {
                continue;
            }
            Brand brand = resolveBrand(dto.getBrand(), brandsByName);
            cameraService.save(toCamera(dto, brand));
            imported++;
        }

        return imported;
    }

    private CameraCatalogDto fetchAndParse(String url) {
        try {
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder(URI.create(url)).GET().build();
            HttpResponse<InputStream> response =
                    client.send(request, HttpResponse.BodyHandlers.ofInputStream());
            if (response.statusCode() != 200) {
                throw new DataImportException("Server returned HTTP " + response.statusCode(), null);
            }
            JAXBContext ctx = JAXBContext.newInstance(CameraCatalogDto.class);
            return (CameraCatalogDto) ctx.createUnmarshaller().unmarshal(response.body());
        } catch (DataImportException e) {
            throw e;
        } catch (Exception e) {
            throw new DataImportException("Failed to fetch/parse catalog from " + url, e);
        }
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

    private Camera toCamera(CameraDto d, Brand brand) {
        return new Camera(
                0, d.getName(), d.getReleaseYear(), d.getMegapixels(),
                d.getSensorType(), d.getIsoRange(), d.getMaxShootingSpeed(),
                d.getPrice(), null, brand,
                CameraType.valueOf(d.getCameraType()),
                Purpose.valueOf(d.getPurpose()),
                new ArrayList<>()
        );
    }
}
