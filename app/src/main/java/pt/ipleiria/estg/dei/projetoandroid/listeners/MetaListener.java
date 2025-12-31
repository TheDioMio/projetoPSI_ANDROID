package pt.ipleiria.estg.dei.projetoandroid.listeners;

import java.util.ArrayList;
import java.util.HashMap;

import pt.ipleiria.estg.dei.projetoandroid.modelo.MetaItem;

public interface MetaListener {
    void onMetaLoaded(HashMap<String, ArrayList<MetaItem>> meta);
    void onMetaError(String error);
}
