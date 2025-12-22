package pt.ipleiria.estg.dei.projetoandroid.modelo;

import java.io.Serializable;

public class Message implements Serializable {

    private int id, sender_user_id, reciver_user_id;
    private String subject, text, created_at;


    public Message (int id, int sender_user_id, int reciver_user_id, String subject, String text, String created_at) {

        this.id= id;
        this.sender_user_id = sender_user_id;
        this.reciver_user_id = reciver_user_id;
        this.subject = subject;
        this.text = text;
        this.created_at = created_at;
    }

    public int getId() {return id;}
    public int getSender_user_id() {return sender_user_id;}
    public int getReciver_user_id () {return reciver_user_id;}
    public String getSubject() {return subject;}
    public String getText() {return text;}
    public String getCreated_at() {return created_at;}
}
