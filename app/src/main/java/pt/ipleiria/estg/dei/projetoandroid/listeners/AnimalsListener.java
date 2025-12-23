package pt.ipleiria.estg.dei.projetoandroid.listeners;

import java.util.ArrayList;

import pt.ipleiria.estg.dei.projetoandroid.modelo.Animal;

public interface AnimalsListener {
    void onRefreshAnimalsList(ArrayList<Animal> animals);
    void onErro(String erro);

}
