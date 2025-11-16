package pt.ipleiria.estg.dei.projetoandroid.modelo;

import java.util.ArrayList;

public class GestorAnimalBreed {


    private ArrayList<AnimalBreed> breeds;

    public GestorAnimalBreed() {
        breeds = new ArrayList<>();
        gerarDados();
    }

    private void gerarDados() {
        breeds.add(new AnimalBreed(1, 1,"Labrador"));
        breeds.add(new AnimalBreed(2, 1,"Golden Retriever"));
        breeds.add(new AnimalBreed(3, 2,"Sem Pelo"));
        breeds.add(new AnimalBreed(4, 2,"Azul Inglês"));
        breeds.add(new AnimalBreed(5, 3,"Piriquito"));
        breeds.add(new AnimalBreed(6, 3,"Papagaio"));
        breeds.add(new AnimalBreed(7, 4,"Desconhecido"));
    }

    public ArrayList<AnimalBreed> getBreeds() {
        return new ArrayList<>(breeds);
    }

    public AnimalBreed getAnimalBreed(int idBreed){

        for (AnimalBreed breed: breeds) {
            if (breed.getId()==idBreed){
                return breed;
            }
        }
        return null;
    }
    public ArrayList<AnimalBreed> getBreedsByType(int animalTypeId) {
        ArrayList<AnimalBreed> resultado = new ArrayList<>();

        for (AnimalBreed breed : breeds) {
            if (breed.getAnimalType_id() == animalTypeId) {
                resultado.add(breed);
            }
        }
        return resultado;
    }
}
