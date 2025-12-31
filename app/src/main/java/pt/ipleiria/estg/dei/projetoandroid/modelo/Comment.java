package pt.ipleiria.estg.dei.projetoandroid.modelo;

import java.io.Serializable;

public class Comment implements Serializable {

    private int idComment;
    private int idAnimal;
    private int userId;
    private String text;
    private String date;
    private String userName;
    private String userAvatar;

    public Comment(int idComment, int idAnimal, int userId, String text, String date, String userName, String userAvatar) {
        this.idComment = idComment;
        this.idAnimal = idAnimal;
        this.userId = userId;
        this.text = text;
        this.date = date;
        this.userName = userName;
        this.userAvatar = userAvatar;
    }

    public int getIdComment() {
        return idComment;
    }

    public void setIdComment(int idComment) {
        this.idComment = idComment;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getIdAnimal() {
        return idAnimal;
    }

    public void setIdAnimal(int idAnimal) {
        this.idAnimal = idAnimal;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserAvatar() {
        return userAvatar;
    }

    public void setUserAvatar(String userAvatar) {
        this.userAvatar = userAvatar;
    }
}
