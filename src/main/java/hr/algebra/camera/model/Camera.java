package hr.algebra.camera.model;

import hr.algebra.camera.model.enums.CameraType;
import hr.algebra.camera.model.enums.Purpose;

import java.util.List;

public class Camera extends AbstractEntity{
    private int releaseYear;
    private double megapixels;
    private String sensorType;
    private String isoRange;
    private int maxShootingSpeed;
    private double price;
    private String imagePath;

    private Brand brand;
    private CameraType cameraType;
    private Purpose purpose;
    private final List<Lens> compatibleLenses;

    public Camera(int id,
                  String name,
                  int releaseYear,
                  double megapixels,
                  String sensorType,
                  String isoRange,
                  int maxShootingSpeed,
                  double price,
                  String imagePath,
                  Brand brand,
                  CameraType cameraType,
                  Purpose purpose,
                  List<Lens> compatibleLenses
    ) {
        super(id, name);
        this.releaseYear = releaseYear;
        this.megapixels = megapixels;
        this.sensorType = sensorType;
        this.isoRange = isoRange;
        this.maxShootingSpeed = maxShootingSpeed;
        this.price = price;
        this.imagePath = imagePath;
        this.brand = brand;
        this.cameraType = cameraType;
        this.purpose = purpose;
        this.compatibleLenses = compatibleLenses;
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

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public Brand getBrand() {
        return brand;
    }

    public void setBrand(Brand brand) {
        this.brand = brand;
    }

    public CameraType getCameraType() {
        return cameraType;
    }

    public void setCameraType(CameraType cameraType) {
        this.cameraType = cameraType;
    }

    public Purpose getPurpose() {
        return purpose;
    }

    public void setPurpose(Purpose purpose) {
        this.purpose = purpose;
    }

    public List<Lens> getCompatibleLenses() {
        return List.copyOf(compatibleLenses);
    }

    public void addCompatibleLenses(Lens lens) {
        if (lens != null && !compatibleLenses.contains(lens)) {
            compatibleLenses.add(lens);
        }
    }

    public void removeCompatibleLens(Lens lens) {
        compatibleLenses.remove(lens);
    }

    @Override
    public String toString() {
        return "Camera{" +
                "id=" + getId() +
                ", name='" + getName() + '\'' +
                ", releaseYear=" + releaseYear +
                ", megapixels=" + megapixels +
                ", sensorType='" + sensorType + '\'' +
                ", isoRange='" + isoRange + '\'' +
                ", maxShootingSpeed=" + maxShootingSpeed +
                ", price=" + price +
                ", imagePath='" + imagePath + '\'' +
                ", brand=" + brand +
                ", cameraType=" + cameraType +
                ", purpose=" + purpose +
                ", compatibleLenses=" + compatibleLenses +
                '}';
    }
}
