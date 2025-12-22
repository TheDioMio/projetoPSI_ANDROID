package pt.ipleiria.estg.dei.projetoandroid.modelo;

import java.io.Serializable;

public class Message implements Serializable {

    private int id, sender_user_id, reciver_user_id;
    private String subject, text, created_at, sender_username, receiver_username ;

    public Message (int id, int sender_user_id, int reciver_user_id, String subject, String text, String created_at, String sender_username, String receiver_username) {

        this.id= id;
        this.sender_user_id = sender_user_id;
        this.reciver_user_id = reciver_user_id;
        this.subject = subject;
        this.text = text;
        this.created_at = created_at;
        this.sender_username = sender_username;
        this.receiver_username = receiver_username;
    }

    public int getId() {return id;}
    public int getSender_user_id() {return sender_user_id;}
    public int getReciver_user_id () {return reciver_user_id;}
    public String getSubject() {return subject;}
    public String getText() {return text;}
    public String getCreated_at() {return created_at;}
    public String getSender_username() {return sender_username;}
    public String getReceiver_username() {return receiver_username;}
}
