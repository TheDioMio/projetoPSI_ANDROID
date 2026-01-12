package pt.ipleiria.estg.dei.projetoandroid.listeners;

import com.android.volley.VolleyError;

public interface UpdateAnimalListener {
    void onUpdateAnimalSuccess();
    void onUpdateAnimalError(VolleyError error);
}
