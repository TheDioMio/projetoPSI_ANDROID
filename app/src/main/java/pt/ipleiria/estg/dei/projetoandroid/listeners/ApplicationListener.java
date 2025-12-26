package pt.ipleiria.estg.dei.projetoandroid.listeners;

import pt.ipleiria.estg.dei.projetoandroid.modelo.Application;

public interface ApplicationListener {
    void onRefreshDetails(Application application);

    void onError(String s);
}
