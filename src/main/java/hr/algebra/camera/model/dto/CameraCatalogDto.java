package hr.algebra.camera.model.dto;

import jakarta.xml.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@XmlRootElement(name = "cameraCatalog")
@XmlAccessorType(XmlAccessType.FIELD)

public class CameraCatalogDto {
    @XmlElementWrapper(name = "cameras")
    @XmlElement(name = "camera")
    private List<CameraDto> cameras = new ArrayList<>();
    public List<CameraDto> getCameras() { return cameras; }
    public void setCameras(List<CameraDto> cameras) { this.cameras = cameras; }
}
