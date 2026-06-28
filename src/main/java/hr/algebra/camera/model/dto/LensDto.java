package hr.algebra.camera.model.dto;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;

@XmlAccessorType(XmlAccessType.FIELD)
public class LensDto {
    private String name;
    private int focalLength;
    private double aperture;
    private String mountType;
    private double price;

    public LensDto() {}

    public void setName(String name) {
        this.name = name;
    }

    public void setFocalLength(int focalLength) {
        this.focalLength = focalLength;
    }

    public void setAperture(double aperture) {
        this.aperture = aperture;
    }

    public void setMountType(String mountType) {
        this.mountType = mountType;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public int getFocalLength() {
        return focalLength;
    }

    public double getAperture() {
        return aperture;
    }

    public String getMountType() {
        return mountType;
    }

    public double getPrice() {
        return price;
    }
}
