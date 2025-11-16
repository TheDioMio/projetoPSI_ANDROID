package pt.ipleiria.estg.dei.projetoandroid.modelo;

import androidx.annotation.NonNull;

public class AnimalBreed {

    private int id, animalType_id;
    private String description;

    public AnimalBreed(int id, int animalType_id, String description) {
        this.id = id;
        this.animalType_id = animalType_id;
        this.description = description;
    }

    public int getId() {
        return id;
    }


    public int getAnimalType_id() {
        return animalType_id;
    }

    public void setAnimalType_id(int animalType_id) {
        this.animalType_id = animalType_id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @NonNull
    @Override
    public String toString() {
        return description;
    }
}
