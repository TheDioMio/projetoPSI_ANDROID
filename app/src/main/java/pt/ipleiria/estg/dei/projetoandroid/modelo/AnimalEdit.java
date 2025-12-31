package pt.ipleiria.estg.dei.projetoandroid.modelo;

import java.io.Serializable;
import java.util.List;

public class AnimalEdit implements Serializable {

    // ===== ANIMAL =====
    private int id;
    private String name;
    private String description;
    private String location;

    private int typeId;
    private int breedId;
    private int ageId;
    private int sizeId;
    private int vaccinationId;
    private int neutered; // 0 / 1

    // ===== ANÚNCIO =====
    private int listingId;
    private String listingDescription;
    private int listingStatus; // 0, 1, 4

    // ===== FOTOS =====
    private List<AnimalFile> files;

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

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public int getTypeId() {
        return typeId;
    }

    public void setTypeId(int typeId) {
        this.typeId = typeId;
    }

    public int getBreedId() {
        return breedId;
    }

    public void setBreedId(int breedId) {
        this.breedId = breedId;
    }

    public int getAgeId() {
        return ageId;
    }

    public void setAgeId(int ageId) {
        this.ageId = ageId;
    }

    public int getSizeId() {
        return sizeId;
    }

    public void setSizeId(int sizeId) {
        this.sizeId = sizeId;
    }

    public int getVaccinationId() {
        return vaccinationId;
    }

    public void setVaccinationId(int vaccinationId) {
        this.vaccinationId = vaccinationId;
    }

    public int getNeutered() {
        return neutered;
    }

    public void setNeutered(int neutered) {
        this.neutered = neutered;
    }

    public int getListingId() {
        return listingId;
    }

    public void setListingId(int listingId) {
        this.listingId = listingId;
    }

    public String getListingDescription() {
        return listingDescription;
    }

    public void setListingDescription(String listingDescription) {
        this.listingDescription = listingDescription;
    }

    public int getListingStatus() {
        return listingStatus;
    }

    public void setListingStatus(int listingStatus) {
        this.listingStatus = listingStatus;
    }

    public List<AnimalFile> getFiles() {
        return files;
    }

    public void setFiles(List<AnimalFile> files) {
        this.files = files;
    }
}
