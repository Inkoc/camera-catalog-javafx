package hr.algebra.camera.model;

public class Brand extends AbstractEntity{
    private String countryOfOrigin;

    public Brand(int id, String name, String countryOfOrigin) {
        super(id, name);
        this.countryOfOrigin = countryOfOrigin;
    }

    public String getCountry() {
        return countryOfOrigin;
    }

    public void setCountry(String countryOfOrigin) {
        this.countryOfOrigin = countryOfOrigin;
    }

    @Override
    public String toString() {
        return "Brand{" +
                "id=" + getId() +
                ", name='" + getName() + '\'' +
                ", countryOfOrigin='" + countryOfOrigin + '\'' +
                '}';
    }
}

