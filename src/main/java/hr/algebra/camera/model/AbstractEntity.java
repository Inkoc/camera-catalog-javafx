package hr.algebra.camera.model;

import java.util.Objects;

public abstract class AbstractEntity implements Comparable<AbstractEntity>{
    private int id;
    private String name;

    protected AbstractEntity(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AbstractEntity that = (AbstractEntity) o;
        return id == that.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "AbstractEntity{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }

    @Override
    public int compareTo(AbstractEntity o) {
        if (this.name == null || o.getName() == null) {
            return 0;
        }
        return this.name.compareToIgnoreCase(o.getName());
    }
}
