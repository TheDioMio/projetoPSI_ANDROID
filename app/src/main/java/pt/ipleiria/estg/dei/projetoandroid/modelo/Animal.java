package pt.ipleiria.estg.dei.projetoandroid.modelo;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;

public class Animal implements Serializable {

private int id, size_id, age_id, animal_type_id, breed_id, vaccines_id, user_id;


//implementar para trazer os dados do owner
private String name, description, location, animal_type, breed, user, age, size, vaccines, ownerName, email;
private boolean neutered;
private Date created_at;
private ArrayList<String> images;

    public Animal(int id, int size_id, int age_id, int animal_type_id, int breed_id, int vaccines_id, int user_id, String name, String description, String location, String animal_type, String breed, String user, String age, String size, String vaccines, String ownerName, String email, boolean neutered, Date created_at, ArrayList<String> images) {
        this.id = id;
        this.size_id = size_id;
        this.age_id = age_id;
        this.animal_type_id = animal_type_id;
        this.breed_id = breed_id;
        this.vaccines_id = vaccines_id;
        this.user_id = user_id;
        this.name = name;
        this.description = description;
        this.location = location;
        this.animal_type = animal_type;
        this.breed = breed;
        this.user = user;
        this.age = age;
        this.size = size;
        this.vaccines = vaccines;
        this.ownerName = ownerName;
        this.email = email;
        this.neutered = neutered;
        this.created_at = created_at;
        this.images = images;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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

    public int getSize_id() {
        return size_id;
    }

    public void setSize_id(int size_id) {
        this.size_id = size_id;
    }

    public int getAge_id() {
        return age_id;
    }

    public void setAge_id(int age_id) {
        this.age_id = age_id;
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

    public int getVaccines_id() {
        return vaccines_id;
    }

    public void setVaccines_id(int vaccines_id) {
        this.vaccines_id = vaccines_id;
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

    public String getAnimal_type() {
        return animal_type;
    }

    public void setAnimal_type(String animal_type) {
        this.animal_type = animal_type;
    }

    public String getBreed() {
        return breed;
    }

    public void setBreed(String breed) {
        this.breed = breed;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getVaccines() {
        return vaccines;
    }

    public void setVaccines(String vaccines) {
        this.vaccines = vaccines;
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
