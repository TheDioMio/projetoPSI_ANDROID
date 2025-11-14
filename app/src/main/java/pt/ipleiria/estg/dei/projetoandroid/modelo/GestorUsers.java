package pt.ipleiria.estg.dei.projetoandroid.modelo;

import java.util.ArrayList;
import java.util.Objects;

public class GestorUsers {

    private ArrayList<User> users;

    private String caminho ="https:\\127.0.0.1\\projetoPSI_WEB\\Assets\\img\\";

    public GestorUsers() {
        users = new ArrayList<>();
        gerarDadosDinamicos();
    }


    private void gerarDadosDinamicos() {
        //está a carregar as imagens do disco local, mas no futuro vem da API

        users.add(new User(1, "Mário Pina", "mpuser", caminho+"avatar1.jpg", "Marinha Grande", "mpina@gmail.com"));
        users.add(new User(2, "Diogo", "duser", caminho+"avatar2.jpg", "Leiria", "djacinto@gmail.com"));
        users.add(new User(3, "Igor", "iuser", caminho+"avatar3.jpg", "Alcanena", "ipatrocinio@gmail.com"));

    }

    public User getUser(int idUser){
        for (User user: users) {
            if (user.getId()==idUser){
                return user;
            }
        }
        return null;
    }

    public ArrayList<User> getUsers() {

        return new ArrayList<>(users);
    }

    public User getUserLogin(String username, String password){

        for (User user: users) {
            if (Objects.equals(user.getUsername(), username)){
                return user;
            }
        }
        return null;
    }

}
