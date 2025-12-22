package pt.ipleiria.estg.dei.projetoandroid.listeners;

import java.util.ArrayList;

import pt.ipleiria.estg.dei.projetoandroid.modelo.Application;
import pt.ipleiria.estg.dei.projetoandroid.modelo.Message;

public interface ApplicationListener {
    void onRefreshApplicationList(ArrayList<Application> list);

    void onError(String error);

    void onRefreshList(Object o);

    void onRefreshList(ArrayList<Application> listaCandidaturas);
}
