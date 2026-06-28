package hr.algebra.camera.model.dto;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;

@XmlAccessorType(XmlAccessType.FIELD)
public class CameraDto {
    private String name;
    private int releaseYear;
    private double megapixels;
    private String sensorType;
    private String isoRange;
    private int maxShootingSpeed;
    private double price;
    private String cameraType;
    private String purpose;
    private String brand;

    public CameraDto() {
    }

    public String getName() {
        return name;
    }

    public int getReleaseYear() {
        return releaseYear;
    }

    public double getMegapixels() {
        return megapixels;
    }

    public String getSensorType() {
        return sensorType;
    }

    public String getIsoRange() {
        return isoRange;
    }

    public int getMaxShootingSpeed() {
        return maxShootingSpeed;
    }

    public double getPrice() {
        return price;
    }

    public String getCameraType() {
        return cameraType;
    }

    public String getPurpose() {
        return purpose;
    }

    public String getBrand() {
        return brand;
    }
}
