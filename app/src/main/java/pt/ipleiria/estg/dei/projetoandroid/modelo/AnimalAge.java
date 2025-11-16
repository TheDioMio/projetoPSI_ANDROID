package pt.ipleiria.estg.dei.projetoandroid.modelo;

import androidx.annotation.NonNull;

public class AnimalAge {

    private int id;
    private String description;

    public AnimalAge(int id, String description) {
        this.id = id;
        this.description = description;
    }

    public int getId() {
        return id;
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
        return this.description;
    }
}
