package pt.ipleiria.estg.dei.projetoandroid.listeners;

import com.android.volley.VolleyError;

public interface CreateAnimalListener {
    void onCreateAnimalSuccess(int animalId);
    void onCreateAnimalError(VolleyError error);
}
