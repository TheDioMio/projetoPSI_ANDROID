package pt.ipleiria.estg.dei.projetoandroid.adaptadores;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.ArrayList;

import pt.ipleiria.estg.dei.projetoandroid.R;
import pt.ipleiria.estg.dei.projetoandroid.modelo.Animal;

public class ListaAnimalsAdaptador extends BaseAdapter {

    Context context;
    LayoutInflater inflater;
    ArrayList<Animal> animals;

    public ListaAnimalsAdaptador(Context context, ArrayList<Animal> animals) {
        this.context = context;
        this.animals = animals;
    }


    @Override
    public int getCount() {
        return animals.size();
    }

    @Override
    public Object getItem(int position) {
        return animals.get(position);
    }

    @Override
    public long getItemId(int position) {
        return animals.get(position).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Animal animal = animals.get(position);
        if (inflater == null){
            inflater = LayoutInflater.from(context);

        }

        if (convertView == null){
            convertView = inflater.inflate(R
                    .layout.item_animal_card, null);
        }

        ViewHolderLista viewHolder = (ViewHolderLista) convertView.getTag();
        if (viewHolder == null){
            viewHolder = new ViewHolderLista(convertView);
            convertView.setTag(viewHolder);   // funciona como o putExtra mas para avista
        }

        viewHolder.update(animal);

        return convertView;
    }

    private class ViewHolderLista{
        private TextView tvName, tvAge, tvBreed;
        private ImageView imgCapa;

        public ViewHolderLista(View view) {
            tvName = view.findViewById(R.id.tvName);
            tvAge = view.findViewById(R.id.tvAge);
            tvBreed = view.findViewById(R.id.tvBreed);
            imgCapa = view.findViewById(R.id.imgCapa);
        }


        public void update(Animal animal) {
            tvName.setText(animal.getName());
            tvAge.setText(String.valueOf(animal.getAge()));
            tvBreed.setText(String.valueOf(animal.getBreed_id()));

            String imgPath = null;

            if (animal.getImages() != null && !animal.getImages().isEmpty()) {
                imgPath = animal.getImages().get(0);
            }

            if (imgPath == null || imgPath.isEmpty()) {
                imgCapa.setImageResource(R.mipmap.placeholder);
                return;
            }

            if (imgPath.startsWith("http")) {
                // 🔹 Se a imagem vem da internet → Glide
                Glide.with(imgCapa.getContext())
                        .load(imgPath)
                        .centerCrop()
                        .placeholder(R.mipmap.placeholder)
                        .error(R.mipmap.placeholder)
                        .into(imgCapa);

            } else {
                // 🔹 Se a imagem é local → drawable
                int resId = imgCapa.getContext()
                        .getResources()
                        .getIdentifier(imgPath, "drawable", imgCapa.getContext().getPackageName());

                if (resId != 0) {
                    imgCapa.setImageResource(resId);
                } else {
                    imgCapa.setImageResource(R.mipmap.placeholder);
                }
            }
        }

//

    }
}
