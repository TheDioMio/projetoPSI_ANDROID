package pt.ipleiria.estg.dei.projetoandroid.listeners;

import com.android.volley.VolleyError;

import pt.ipleiria.estg.dei.projetoandroid.modelo.AnimalEdit;

public interface GetAnimalEditListener {
    void onGetAnimalEditSuccess(AnimalEdit animal);
    void onGetAnimalEditError(VolleyError error);
}
