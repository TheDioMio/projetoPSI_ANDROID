package pt.ipleiria.estg.dei.projetoandroid.listeners;

import com.android.volley.VolleyError;

public interface UploadAnimalPhotosListener {
    void onUploadAnimalPhotosSuccess();
    void onUploadAnimalPhotosError(VolleyError error);
}
