package pt.ipleiria.estg.dei.projetoandroid.adaptadores;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import pt.ipleiria.estg.dei.projetoandroid.R;
import pt.ipleiria.estg.dei.projetoandroid.modelo.AnimalFile;
import pt.ipleiria.estg.dei.projetoandroid.modelo.AppSingleton;

public class AnimalPhotoAdaptador
        extends RecyclerView.Adapter<AnimalPhotoAdaptador.ViewHolder> {

    private Context context;
    private ArrayList<AnimalFile> apiPhotos;
    private ArrayList<Uri> addedPhotos;

    public interface OnPhotoRemoveListener {
        void onRemoveApiPhoto(AnimalFile file, int position);
        void onRemoveAddedPhoto(Uri uri, int position);
    }

    private OnPhotoRemoveListener listener;

    public AnimalPhotoAdaptador(Context context,
                                ArrayList<AnimalFile> apiPhotos,
                                ArrayList<Uri> addedPhotos,
                                OnPhotoRemoveListener listener) {
        this.context = context;
        this.apiPhotos = apiPhotos;
        this.addedPhotos = addedPhotos;
        this.listener = listener;
    }

    @Override
    public int getItemCount() {
        return apiPhotos.size() + addedPhotos.size();
    }

    @Override
    public int getItemViewType(int position) {
        return position < apiPhotos.size() ? 0 : 1;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(
            @NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context)
                .inflate(R.layout.item_animal_photo, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(
            @NonNull ViewHolder holder, int position) {

        if (getItemViewType(position) == 0) {

            AnimalFile file = apiPhotos.get(position);

            Glide.with(context)
                    .load(AppSingleton.getInstance(context).getEndereco() + AppSingleton.getInstance(context).FRONTEND_BASE_URL + file.getFileAddress())
                    .into(holder.imgPhoto);

            holder.btnRemove.setOnClickListener(v ->
                    listener.onRemoveApiPhoto(file, position)
            );

        } else {

            int index = position - apiPhotos.size();
            Uri uri = addedPhotos.get(index);

            Glide.with(context)
                    .load(uri)
                    .into(holder.imgPhoto);

            holder.btnRemove.setOnClickListener(v ->
                    listener.onRemoveAddedPhoto(uri, index)
            );
        }
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        ImageView imgPhoto;
        ImageButton btnRemove;

        ViewHolder(View itemView) {
            super(itemView);
            imgPhoto = itemView.findViewById(R.id.imgPhoto);
            btnRemove = itemView.findViewById(R.id.btnRemovePhoto);
        }
    }
}

