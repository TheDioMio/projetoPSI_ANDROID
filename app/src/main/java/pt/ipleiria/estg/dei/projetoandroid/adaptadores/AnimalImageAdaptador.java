package pt.ipleiria.estg.dei.projetoandroid.adaptadores;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import pt.ipleiria.estg.dei.projetoandroid.R;
import pt.ipleiria.estg.dei.projetoandroid.modelo.AnimalFile;
import pt.ipleiria.estg.dei.projetoandroid.modelo.AppSingleton;

public class AnimalImageAdaptador
        extends RecyclerView.Adapter<AnimalImageAdaptador.ViewHolderImage> {

    private Context context;
    private ArrayList<AnimalFile> images;
    private LayoutInflater inflater;

    // imagem grande (fora do RecyclerView)
    private ImageView imgPrincipal;

    public AnimalImageAdaptador(Context context,
                                ArrayList<AnimalFile> images,
                                ImageView imgPrincipal) {
        this.context = context;
        this.images = images;
        this.imgPrincipal = imgPrincipal;
        this.inflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public ViewHolderImage onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.item_animal_image, parent, false);
        return new ViewHolderImage(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderImage holder, int position) {
        AnimalFile file = images.get(position);
        holder.update(file);
    }

    @Override
    public int getItemCount() {
        return images == null ? 0 : images.size();
    }

    /* ---------------- ViewHolder ---------------- */

    class ViewHolderImage extends RecyclerView.ViewHolder {

        ImageView imgThumb;

        public ViewHolderImage(@NonNull View itemView) {
            super(itemView);
            imgThumb = itemView.findViewById(R.id.imgAnimalThumb);
        }

        public void update(AnimalFile file) {

            if (file == null || file.getFileAddress() == null) {
                imgThumb.setImageResource(R.mipmap.placeholder);
                return;
            }

            String imgPath = file.getFileAddress();
            String imageUrl;

            if (imgPath.startsWith("http")) {
                imageUrl = imgPath;
            } else {
                imageUrl = AppSingleton.getInstance(context).getEndereco() + AppSingleton.getInstance(context).FRONTEND_BASE_URL + imgPath;
            }

            // miniatura
            Glide.with(context)
                    .load(imageUrl)
                    .placeholder(R.mipmap.placeholder)
                    .error(R.mipmap.placeholder)
                    .centerCrop()
                    .into(imgThumb);

            // clique → imagem principal
            imgThumb.setOnClickListener(v -> {
                Glide.with(context)
                        .load(imageUrl)
                        .placeholder(R.mipmap.placeholder)
                        .error(R.mipmap.placeholder)
                        .centerCrop()
                        .into(imgPrincipal);
            });
        }
    }
}
