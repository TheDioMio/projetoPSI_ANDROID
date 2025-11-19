package pt.ipleiria.estg.dei.projetoandroid.modelo;

import androidx.annotation.NonNull;

public class Application {
    private int id, status, user_id, animal_id, type;
    private String description;

    public Application(int id, int status, int user_id, int animal_id, int type, int target_user_id, String description) {
        this.id = id;
        this.status = status;
        this.user_id = user_id;
        this.animal_id = animal_id;
        this.type = type;
        this.description = description;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public int getAnimal_id() {
        return animal_id;
    }

    public void setAnimal_id(int animal_id) {
        this.animal_id = animal_id;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
