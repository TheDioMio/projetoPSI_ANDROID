package pt.ipleiria.estg.dei.projetoandroid.listeners;
import java.util.ArrayList;

import pt.ipleiria.estg.dei.projetoandroid.modelo.Message;

public interface MessagesListener {

    void onRefreshListaMessages(ArrayList<Message> lista);
    void onErro(String erro);


}
