package pt.ipleiria.estg.dei.projetoandroid.modelo;

import java.util.ArrayList;

public class GestorMessage {

private ArrayList<Message> messages;

    public GestorMessage() {

        messages = new ArrayList<>();
        gerarDadosDummy();

    }

    private void gerarDadosDummy() {

        messages.add(new Message(
                1,
                1,
                2,
                "Mais info",
                "Olá, pode dair mais info?",
                "23-11-2025 15:52")
        );

        messages.add(new Message(
                3,
                3,
                1,
                "Pedido de adoção",
                "Gostava de saber mais sobre o animal.", "2025-01-11 18:02")
        );

    }

    public ArrayList<Message> getMessages() {
        return new ArrayList<>(messages);
    }

    public ArrayList<Message> getMessageForUser (int userId) {
        ArrayList<Message> result = new ArrayList<>();

        for (Message m : messages) {
            if (m.getReciver_user_id() == userId) {
                result.add(m);
            }
        }

        return result;
    }

    public Message getMessageById(int id) {
        for (Message m : messages) {
            if (m.getId() == id) {
                return m;
            }
        }
        return null;
    }


}
