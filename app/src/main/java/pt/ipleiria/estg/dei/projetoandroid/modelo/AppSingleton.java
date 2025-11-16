package pt.ipleiria.estg.dei.projetoandroid.modelo;

import java.util.ArrayList;

public class AppSingleton {
    private static AppSingleton instance = null;
    private GestorAnimals gestorAnimals = new GestorAnimals();
    private GestorUsers gestorUsers = new GestorUsers();
    private GestorAnimalType gestorAnimalType = new GestorAnimalType();
    private GestorAnimalBreed gestorAnimalBreed = new GestorAnimalBreed();
    private GestorAnimalAge gestorAnimalAge = new GestorAnimalAge();
    private GestorAnimalSize gestorAnimalSize = new GestorAnimalSize();
    private GestorVaccination gestorVaccination = new GestorVaccination();

    public static synchronized AppSingleton getInstance(){
        if (instance == null){
            instance = new AppSingleton();
        }
        return instance;
    }


    // -------------------------
    // GESTOR ANIMAL
    // -------------------------
    public ArrayList<Animal> getAnimals() {

        return gestorAnimals.getAnimals();
    }

    public Animal getAnimal(int idAnimal){
        return gestorAnimals.getAnimal(idAnimal);
    }

    public void addAnimal(Animal animal) {
        gestorAnimals.addAnimal(animal);
    }

    public ArrayList<Animal> getAnimalsByUser(int userId) {
        return gestorAnimals.getAnimalsByUser(userId);
    }

    // -------------------------
    // GESTOR User
    // -------------------------
    public User getUserLogin(String username, String password){
        return gestorUsers.getUserLogin(username, password);
    }

    public User getUser(int idUser){
        return gestorUsers.getUser(idUser);
    }


    // -------------------------
    // GESTOR ANIMAL TYPE
    // -------------------------
    public ArrayList<AnimalType> getAnimalTypes() {
        return gestorAnimalType.getAnimalTypes();
    }

    public AnimalType getAnimalType(int id) {
        return gestorAnimalType.getAnimalType(id);
    }

    // -------------------------
    // GESTOR BREED
    // -------------------------
    public ArrayList<AnimalBreed> getBreedsByAnimalType(int tipoId) {
        return gestorAnimalBreed.getBreedsByType(tipoId);
    }

    public AnimalBreed getBreed(int id) {
        return gestorAnimalBreed.getAnimalBreed(id);
    }

    // -------------------------
    // GESTOR AGE
    // -------------------------
    public ArrayList<String> getAnimalAgesStrings() {
        return gestorAnimalAge.getAgesStrings();
    }

    // -------------------------
    // GESTOR SIZE
    // -------------------------
    public ArrayList<String> getAnimalSizesStrings() {
        return gestorAnimalSize.getSizesStrings();
    }

    public ArrayList<AnimalSize> getAnimalSizes() {
        return gestorAnimalSize.getSizes();
    }

    // -------------------------
    // GESTOR Vaccination
    // -------------------------
    public ArrayList<String> getVaccinationStrings() {
        return gestorVaccination.getVaccinationStrings();
    }
    public ArrayList<Vaccination> getVaccinations() {
        return gestorVaccination.getVaccinations();
    }

}
