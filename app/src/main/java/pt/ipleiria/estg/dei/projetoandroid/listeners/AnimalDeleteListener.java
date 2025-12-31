package pt.ipleiria.estg.dei.projetoandroid.listeners;

public interface AnimalDeleteListener {
    void onDeleteAnimalSuccess(int animalId);

    void onDeleteAnimalError(String error);
}
