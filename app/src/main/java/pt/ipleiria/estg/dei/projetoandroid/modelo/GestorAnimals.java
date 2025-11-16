package pt.ipleiria.estg.dei.projetoandroid.modelo;

import android.os.Build;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;

public class GestorAnimals {
    private ArrayList<Animal> animals;

    private String caminho ="https:\\\127.0.0.1\\projetoPSI_WEB\\Assets\\img\\";

    public GestorAnimals() {
        animals = new ArrayList<>();
        gerarDadosDinamicos();
    }

    private void gerarDadosDinamicos() {
        Date dataFixa = Date.from(Instant.now());

        //está a carregar as imagens do disco local, mas no futuro vem da API
        ArrayList<String> imagens = new ArrayList<>();
        imagens.add("cao1");
        imagens.add("cao2");
        imagens.add("cao3");
        imagens.add("cao4");
        imagens.add("cao2");

        animals.add(new Animal(
                1,      // id
                2,      // size_id
                3,      // age_id
                1,      // animal_type_id
                1,      // breed_id
                1,      // vaccines_id
                3,     // user_id
                "Rex",  // name
                "Cão muito amigável e brincalhão", // description
                "Lisboa", // location
                "Cão",     // animal_type (string)
                "Labrador", // breed (string)
                "João",     // user (string)
                "Adulto",   // age (string)
                "Médio",    // size (string)
                "Com vacinas", // vaccines (string)
                true,      // neutered
                dataFixa,  // created_at
                imagens    // images
        ));

        animals.add(new Animal(
                2,
                2,
                3,
                2,
                3,
                1,
                10,
                "Mia",
                "Gato muito amigável e brincalhão",
                "Lisboa",
                "Gato",
                "Europeu",
                "João",
                "Adulto",
                "Médio",
                "Com vacinas",
                true,
                dataFixa,
                imagens
        ));


        animals.add(new Animal(
                3,
                2,
                3,
                1,
                2,
                1,
                10,
                "Bobi",
                "Cão muito amigável e brincalhão",
                "Lisboa",
                "Cão",
                "Labrador",
                "João",
                "Adulto",
                "Médio",
                "Com vacinas",
                true,
                dataFixa,
                imagens
        ));


        animals.add(new Animal(
                4,
                1,
                2,
                2,
                4,
                1,
                3,
                "Tareco",
                "Gato muito amigável e brincalhão",
                "Lisboa",
                "Cão",
                "Labrador",
                "João",
                "Adulto",
                "Médio",
                "Com vacinas",
                true,
                dataFixa,
                imagens
        ));


    }



    public ArrayList<Animal> getAnimals() {

        return new ArrayList<>(animals);
    }

    public Animal getAnimal(int idAnimal){

        for (Animal animal: animals) {
            if (animal.getId()==idAnimal){
                return animal;
            }
        }
        return null;
    }

    public void addAnimal(Animal animal) {
        animals.add(animal);
    }

    public ArrayList<Animal> getAnimalsByUser(int userId) {
        ArrayList<Animal> resultado = new ArrayList<>();
        for (Animal a : animals) {
            if (a.getUser_id() == userId) {
                resultado.add(a);
            }
        }
        return resultado;
    }

}
