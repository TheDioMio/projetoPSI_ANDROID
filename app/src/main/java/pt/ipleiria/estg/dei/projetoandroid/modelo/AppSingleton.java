package pt.ipleiria.estg.dei.projetoandroid.modelo;

import java.util.ArrayList;

public class AppSingleton {
    private static AppSingleton instance = null;
    private GestorAnimals gestorAnimals = new GestorAnimals();
    private GestorUsers gestorUsers = new GestorUsers();

    public static synchronized AppSingleton getInstance(){
        if (instance == null){
            instance = new AppSingleton();
        }
        return instance;
    }


    /*--> MÉTODOS DE ANIMAIS <--*/
    public ArrayList<Animal> getAnimals() {

        return gestorAnimals.getAnimals();
    }

    public Animal getAnimal(int idAnimal){
        return gestorAnimals.getAnimal(idAnimal);
    }
    /*--> MÉTODOS DE ANIMAIS <--*/



    /*--> MÉTODOS DE UTILIZADORES <--*/
    public User getUserLogin(String username, String password){
        return gestorUsers.getUserLogin(username, password);
    }

    public User getUser(int idUser){
        return gestorUsers.getUser(idUser);
    }

    public void addUser(String username, String email, String password) {
        gestorUsers.addUser(username, email, password);
    }
    /*--> MÉTODOS DE UTILIZADORES <--*/
}
