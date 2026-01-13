package pt.ipleiria.estg.dei.projetoandroid.listeners;

import java.util.ArrayList;

import pt.ipleiria.estg.dei.projetoandroid.modelo.Application;

public interface ApplicationsListener {
    void onRefreshList(ArrayList<Application> listaCandidaturas);

    // Implementar restantes métodos da interface para evitar erros (podem ficar vazios)
    void onRefreshApplicationList(ArrayList<Application> list);

    void onError(String error);

    void onRefreshList(Object o);

    void onApplicationsOffline(ArrayList<Application> cachedApplications);
}
