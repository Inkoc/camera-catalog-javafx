package hr.algebra.camera.service;

import hr.algebra.camera.exception.DataImportException;
import hr.algebra.camera.model.Camera;
import hr.algebra.camera.model.Lens;
import hr.algebra.camera.model.dto.CameraCatalogDto;
import hr.algebra.camera.model.dto.CameraDto;
import hr.algebra.camera.model.dto.LensDto;
import hr.algebra.camera.service.interfaces.ICameraService;
import hr.algebra.camera.service.interfaces.IXmlExportService;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.Marshaller;

import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class XmlExportService implements IXmlExportService {
    private final ICameraService cameraService;
    public XmlExportService(ICameraService cameraService) {
        this.cameraService = cameraService;
    }

    @Override
    public void exportCatalog(Path file) {
        try {
            CameraCatalogDto catalog = new CameraCatalogDto();
            List<CameraDto> dtos = cameraService.findAll().stream()
                    .map(this::toDto)
                    .toList();
            catalog.setCameras(dtos);
            JAXBContext ctx = JAXBContext.newInstance(CameraCatalogDto.class);
            Marshaller marshaller = ctx.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            try (OutputStream out = Files.newOutputStream(file)) {
                marshaller.marshal(catalog, out);
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new DataImportException("Failed to export catalog to " + file, e);
        }
    }

    private CameraDto toDto(Camera camera) {
        CameraDto cameraDto = new CameraDto();
        cameraDto.setName(camera.getName());
        cameraDto.setReleaseYear(camera.getReleaseYear());
        cameraDto.setMegapixels(camera.getMegapixels());
        cameraDto.setSensorType(camera.getSensorType());
        cameraDto.setIsoRange(camera.getIsoRange());
        cameraDto.setMaxShootingSpeed(camera.getMaxShootingSpeed());
        cameraDto.setPrice(camera.getPrice());
        cameraDto.setCameraType(camera.getCameraType().name());
        cameraDto.setPurpose(camera.getPurpose().name());
        cameraDto.setBrand(camera.getBrand() == null ? null : camera.getBrand().getName());
        cameraDto.setLenses(cameraService.findLensesForCamera(camera.getId()).stream()
                .map(this::toLensDto).toList());
        return cameraDto;
    }

    private LensDto toLensDto(Lens l) {
        LensDto lensDto = new LensDto();
        lensDto.setName(l.getName());
        lensDto.setFocalLength(l.getFocalLength());
        lensDto.setAperture(l.getAperture());
        lensDto.setMountType(l.getMountType());
        lensDto.setPrice(l.getPrice());
        return lensDto;
    }
}
