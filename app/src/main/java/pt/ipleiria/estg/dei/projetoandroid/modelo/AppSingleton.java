package pt.ipleiria.estg.dei.projetoandroid.modelo;

import java.util.ArrayList;

public class AppSingleton {
    private static AppSingleton instance = null;
    private GestorAnimals gestorAnimals = new GestorAnimals();

    public static synchronized AppSingleton getInstance(){
        if (instance == null){
            instance = new AppSingleton();
        }
        return instance;
    }


    public ArrayList<Animal> getAnimals() {

        return gestorAnimals.getAnimals();
    }

    public Animal getAnimal(int idAnimal){
        return gestorAnimals.getAnimal(idAnimal);
    }


}
