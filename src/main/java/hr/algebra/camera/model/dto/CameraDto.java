package hr.algebra.camera.model.dto;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlElementWrapper;

import java.util.ArrayList;
import java.util.List;

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

    @XmlElementWrapper(name = "lenses")
    @XmlElement(name = "lens")
    private List<LensDto> lenses = new ArrayList<>();

    public CameraDto() {}

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getReleaseYear() {
        return releaseYear;
    }

    public void setReleaseYear(int releaseYear) {
        this.releaseYear = releaseYear;
    }

    public double getMegapixels() {
        return megapixels;
    }

    public void setMegapixels(double megapixels) {
        this.megapixels = megapixels;
    }

    public String getSensorType() {
        return sensorType;
    }

    public void setSensorType(String sensorType) {
        this.sensorType = sensorType;
    }

    public String getIsoRange() {
        return isoRange;
    }

    public void setIsoRange(String isoRange) {
        this.isoRange = isoRange;
    }

    public int getMaxShootingSpeed() {
        return maxShootingSpeed;
    }

    public void setMaxShootingSpeed(int maxShootingSpeed) {
        this.maxShootingSpeed = maxShootingSpeed;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getCameraType() {
        return cameraType;
    }

    public void setCameraType(String cameraType) {
        this.cameraType = cameraType;
    }

    public String getPurpose() {
        return purpose;
    }

    public void setPurpose(String purpose) {
        this.purpose = purpose;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public List<LensDto> getLenses() {
        return lenses;
    }

    public void setLenses(List<LensDto> lenses) {
        this.lenses = lenses;
    }
}
