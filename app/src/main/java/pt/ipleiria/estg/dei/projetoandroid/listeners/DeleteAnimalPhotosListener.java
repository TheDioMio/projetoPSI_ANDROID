package pt.ipleiria.estg.dei.projetoandroid.listeners;

import com.android.volley.VolleyError;

public interface DeleteAnimalPhotosListener {
    void onDeleteAnimalPhotosSuccess();
    void onDeleteAnimalPhotosError(VolleyError error);
}
