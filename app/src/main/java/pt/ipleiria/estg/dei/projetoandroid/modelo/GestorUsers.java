package pt.ipleiria.estg.dei.projetoandroid.modelo;

import java.util.ArrayList;
import java.util.Objects;

public class GestorUsers {

    private ArrayList<User> users;


    public GestorUsers() {
        users = new ArrayList<>();
        gerarDadosDinamicos();
    }


    private void gerarDadosDinamicos() {
        //está a carregar as imagens do disco local, mas no futuro vem da API


        users.add(new User(1, "Mário Pina", "mpuser", "avatar1", "Marinha Grande", "mpina@gmail.com"));
        users.add(new User(2, "Diogo", "duser", "avatar2", "Leiria", "djacinto@gmail.com"));
        users.add(new User(3, "Igor", "iuser", "avatar3", "Alcanena", "ipatrocinio@gmail.com"));

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



    //Função para adicionar novos Users, AINDA NÃO ESTÁ A METER AS PASSWORDS!
    public void addUser(String username, String email, String password) {
        int newId;
        if(users.isEmpty()){
            newId = 1;
        } else {
            newId = users.get(users.size() -1).getId() + 1;
        }

        //Valores que foram feitos padrão, porque não são metidos no layout login:
        String defaultName = username;
        String defaultAvatar = "avatar1";
        String defaultAddress = "Desconhecida";

        users.add(new User(newId, defaultName, username, defaultAvatar, defaultAddress, email));
    }
}
