package pt.ipleiria.estg.dei.projetoandroid.modelo;

public class MetaItem {
    private int id;
    private String description;

    // só usado para BREEDS
    private Integer animalTypeId;

    public MetaItem(int id, String description, Integer animalTypeId) {
        this.id = id;
        this.description = description;
        this.animalTypeId = animalTypeId;
    }

    public int getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }

    public Integer getAnimalTypeId() {
        return animalTypeId;
    }

    @Override
    public String toString() {
        return description; // IMPORTANTE para Spinner
    }
}
