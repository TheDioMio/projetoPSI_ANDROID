package pt.ipleiria.estg.dei.projetoandroid.listeners;

import java.util.ArrayList;

import pt.ipleiria.estg.dei.projetoandroid.modelo.Animal;

public interface MyAnimalsListener {
    void onRefreshMyAnimals(ArrayList<Animal> myAnimals);
    void onErro(String erro);
}
