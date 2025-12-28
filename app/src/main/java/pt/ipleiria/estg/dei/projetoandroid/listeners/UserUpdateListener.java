package pt.ipleiria.estg.dei.projetoandroid.listeners;

import pt.ipleiria.estg.dei.projetoandroid.modelo.Me;

public interface UserUpdateListener {
    void onUpdateSuccess(Me me);
    void onUpdateError(String error);
}
