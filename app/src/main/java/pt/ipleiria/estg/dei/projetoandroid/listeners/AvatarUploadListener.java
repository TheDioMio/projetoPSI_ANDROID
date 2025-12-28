package pt.ipleiria.estg.dei.projetoandroid.listeners;

public interface AvatarUploadListener {
    void onSuccess(String avatarPath);
    void onError(String message);
}
