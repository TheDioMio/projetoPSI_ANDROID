package pt.ipleiria.estg.dei.projetoandroid.modelo;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;

public class GestorUser {

    private ArrayList<User> users;

    private String caminho ="127.0.0.1\\projetoPSI_WEB\\Assets\\img\\";

    public GestorUser() {
        users = new ArrayList<>();
        gerarDadosDinamicos();
    }


    private void gerarDadosDinamicos() {
        //está a carregar as imagens do disco local, mas no futuro vem da API

        users.add(new User(1, "Mário Pina", "mpuser", caminho+"avatar1.jpg", "Marinha Grande", "mpina@gmail.com"));
        users.add(new User(1, "Diogo", "duser", caminho+"avatar2.jpg", "Leiria", "djacinto@gmail.com"));
        users.add(new User(1, "Igor", "iuser", caminho+"avatar3.jpg", "Alcanena", "ipatrocinio@gmail.com"));

    }

    public User getUser(int idUser){
        for (User user: users) {
            if (user.getId()==idUser){
                return user;
            }
        }
        return null;
    }

}
