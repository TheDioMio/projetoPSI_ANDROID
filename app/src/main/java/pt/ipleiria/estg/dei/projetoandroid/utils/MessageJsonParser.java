package pt.ipleiria.estg.dei.projetoandroid.utils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import pt.ipleiria.estg.dei.projetoandroid.modelo.Message;

public class MessageJsonParser {

    public static ArrayList<Message> parserJsonMessages(JSONArray response) {
        ArrayList<Message> lista = new ArrayList<>();

        try {
            for (int i = 0; i < response.length(); i++) {
                JSONObject obj = response.getJSONObject(i);

                int id = obj.getInt("id");
                int senderId = obj.getInt("sender_user_id");
                int receiverId = obj.getInt("receiver_user_id");
                String subject = obj.getString("subject");
                String text = obj.getString("text");
                String createdAt = obj.optString("created_at", "");
                String senderUsername = obj.optString("sender_username", "");
                String receiverUsername = obj.optString("receiver_username", "");

                Message m = new Message(
                        id,
                        senderId,
                        receiverId,
                        subject,
                        text,
                        createdAt,
                        senderUsername,
                        receiverUsername
                );

                lista.add(m);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return lista;
    }
}
