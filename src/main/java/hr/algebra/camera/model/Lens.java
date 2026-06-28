package hr.algebra.camera.model;

public class Lens  extends AbstractEntity{
    private int focalLength;
    private double aperture;
    private String mountType;
    private double price;

    public Lens(int id, String name, int focalLength, double aperture, String mountType, double price) {
        super(id, name);
        this.focalLength = focalLength;
        this.aperture = aperture;
        this.mountType = mountType;
        this.price = price;
    }

    public int getFocalLength() {
        return focalLength;
    }

    public void setFocalLength(int focalLength) {
        this.focalLength = focalLength;
    }

    public double getAperture() {
        return aperture;
    }

    public void setAperture(double aperture) {
        this.aperture = aperture;
    }

    public String getMountType() {
        return mountType;
    }

    public void setMountType(String mountType) {
        this.mountType = mountType;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    @Override
    public String toString() {
        return "Lens{" +
                "id=" + getId() +
                ", name='" + getName() + '\'' +
                ", focalLength=" + focalLength +
                ", aperture=" + aperture +
                ", mountType='" + mountType + '\'' +
                ", price=" + price +
                '}';
    }
}
