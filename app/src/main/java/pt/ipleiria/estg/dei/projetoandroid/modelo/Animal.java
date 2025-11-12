package pt.ipleiria.estg.dei.projetoandroid.modelo;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;

public class Animal {

private int id, size, age, animal_type_id, breed_id, vaccines, user_id;

private String name, description, location;
private boolean neutered;
private Date created_at;
private ArrayList<String> images;

    public Animal(int id, int size, int age, int animal_type_id, int breed_id, int vaccines, int user_id, String name, String description, String location, boolean neutered, Date created_at, ArrayList<String> images) {
        this.id = id;
        this.size = size;
        this.age = age;
        this.animal_type_id = animal_type_id;
        this.breed_id = breed_id;
        this.vaccines = vaccines;
        this.user_id = user_id;
        this.name = name;
        this.description = description;
        this.location = location;
        this.neutered = neutered;
        this.created_at = created_at;
        this.images = images;
    }

    public ArrayList<String> getImages() {
        return images;
    }

    public void setImages(ArrayList<String> images) {
        this.images = images;
    }

    public int getId() {
        return id;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public int getAnimal_type_id() {
        return animal_type_id;
    }

    public void setAnimal_type_id(int animal_type_id) {
        this.animal_type_id = animal_type_id;
    }

    public int getBreed_id() {
        return breed_id;
    }

    public void setBreed_id(int breed_id) {
        this.breed_id = breed_id;
    }

    public int getVaccines() {
        return vaccines;
    }

    public void setVaccines(int vaccines) {
        this.vaccines = vaccines;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public boolean isNeutered() {
        return neutered;
    }

    public void setNeutered(boolean neutered) {
        this.neutered = neutered;
    }

    public Date getCreated_at() {
        return created_at;
    }

    public void setCreated_at(Date created_at) {
        this.created_at = created_at;
    }
}
