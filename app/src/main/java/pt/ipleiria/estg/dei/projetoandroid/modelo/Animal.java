package pt.ipleiria.estg.dei.projetoandroid.modelo;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;

public class Animal implements Serializable {
    private int id;
    private String name;
    private String description;
    private String createdAt;
    private String age;
    private String size;
    private String type;
    private String breed;
    private String neutered;
    private String vacination ;
    private String location;
    private String ownerName;
    private String ownerAddress;
    private String ownerEmail;
    private String ownerAvatar;

    private String listingDescription;
    private int listingViews;

    private int status;
    private int userId;
    private int userRole;

    private ArrayList<Comment> comments;
    private ArrayList<AnimalFile> animalfiles;

    public Animal(int id, String name, String description, String createdAt, String age, String size, String type,
                  String breed, String neutered, String vacination, String location, String ownerName,
                  String ownerAddress, String ownerEmail, String ownerAvatar, String listingDescription,
                  int listingViews, int status, int userId, int userRole, ArrayList<Comment> comments,
                  ArrayList<AnimalFile> animalfiles) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.createdAt = createdAt;
        this.age = age;
        this.size = size;
        this.type = type;
        this.breed = breed;
        this.neutered = neutered;
        this.vacination = vacination;
        this.location = location;
        this.ownerName = ownerName;
        this.ownerAddress = ownerAddress;
        this.ownerEmail = ownerEmail;
        this.ownerAvatar = ownerAvatar;
        this.listingDescription = listingDescription;
        this.listingViews = listingViews;
        this.status = status;
        this.userId = userId;
        this.userRole = userRole;
        this.comments = (comments != null) ? comments : new ArrayList<>();
        this.animalfiles = (animalfiles != null) ? animalfiles : new ArrayList<>();
    }

    public int getListingViews() {
        return listingViews;
    }

    public void setListingViews(int listingViews) {
        this.listingViews = listingViews;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getUserRole() {
        return userRole;
    }

    public void setUserRole(int userRole) {
        this.userRole = userRole;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getBreed() {
        return breed;
    }

    public void setBreed(String breed) {
        this.breed = breed;
    }

    public String getNeutered() {
        return neutered;
    }

    public void setNeutered(String neutered) {
        this.neutered = neutered;
    }

    public String getVacination() {
        return vacination;
    }

    public void setVacination(String vacination) {
        this.vacination = vacination;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }

    public String getOwnerAddress() {
        return ownerAddress;
    }

    public void setOwnerAddress(String ownerAddress) {
        this.ownerAddress = ownerAddress;
    }

    public String getOwnerEmail() {
        return ownerEmail;
    }

    public void setOwnerEmail(String ownerEmail) {
        this.ownerEmail = ownerEmail;
    }

    public String getOwnerAvatar() {
        return ownerAvatar;
    }

    public void setOwnerAvatar(String ownerAvatar) {
        this.ownerAvatar = ownerAvatar;
    }

    public String getListingDescription() {
        return listingDescription;
    }

    public void setListingDescription(String listingDescription) {
        this.listingDescription = listingDescription;
    }


    public ArrayList<Comment> getComments() {
        return comments;
    }

    public void setComments(ArrayList<Comment> comments) {
        this.comments = comments;
    }

    public ArrayList<AnimalFile> getAnimalfiles() {
        return animalfiles;
    }

    public void setAnimalfiles(ArrayList<AnimalFile> animalfiles) {
        this.animalfiles = animalfiles;
    }
}
