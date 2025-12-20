package pt.ipleiria.estg.dei.projetoandroid.modelo;

public class Me {

    //CLASSE PARA JÁ IGUAL AO USER MAS NO FUTURO VAI CONTER MAIS INFORMAÇÕES
    //MENSAGENS
    // CANDIDATURAS
    //ESTATISTICAS
    //ETC
    private int id;
    private String name, username, imgAvatar, address, email;

    public Me(int id, String name, String username, String imgAvatar, String address, String email) {
        this.id = id;
        this.name = name;
        this.username = username;
        this.imgAvatar = imgAvatar;
        this.address = address;
        this.email = email;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getImgAvatar() {
        return imgAvatar;
    }

    public void setImgAvatar(String imgAvatar) {
        this.imgAvatar = imgAvatar;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

}
