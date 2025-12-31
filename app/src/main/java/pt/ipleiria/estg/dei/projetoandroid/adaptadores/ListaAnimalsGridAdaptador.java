package pt.ipleiria.estg.dei.projetoandroid.adaptadores;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import pt.ipleiria.estg.dei.projetoandroid.R;
import pt.ipleiria.estg.dei.projetoandroid.modelo.Animal;
import pt.ipleiria.estg.dei.projetoandroid.modelo.AnimalFile;
import pt.ipleiria.estg.dei.projetoandroid.modelo.AppSingleton;

public class ListaAnimalsGridAdaptador extends BaseAdapter
{

    Context context;
    LayoutInflater inflater;
    ArrayList<Animal> animals;

    public ListaAnimalsGridAdaptador(Context context, ArrayList<Animal> animals) {
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
                    .layout.item_animal_card_grid, null);
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
        private ImageView imgAnimal;

        public ViewHolderLista(View view) {
            tvName = view.findViewById(R.id.tvName);
            tvAge = view.findViewById(R.id.tvAge);
            tvBreed = view.findViewById(R.id.tvBreed);
            imgAnimal = view.findViewById(R.id.imgAnimal);
        }


        public void update(Animal animal) {
            tvName.setText(animal.getName());
            tvAge.setText(animal.getAge());
            tvBreed.setText(animal.getBreed()+" ("+animal.getType()+")");

            String imgPath = null;

            if (animal.getAnimalfiles() != null && !animal.getAnimalfiles().isEmpty()) {
                AnimalFile file = animal.getAnimalfiles().get(0);

                if (file != null) {
                    imgPath = file.getFileAddress();
                }



                String imageUrl;

                if (imgPath.startsWith("http")) {
                    imageUrl = imgPath;
                } else {
                    imageUrl = AppSingleton.getInstance(context).FRONTEND_BASE_URL + imgPath;
                }

                Glide.with(context)
                        .load(imageUrl)
                        .placeholder(R.mipmap.default_avatar)
                        .error(R.mipmap.default_avatar)
                        .centerCrop()
                        .into(imgAnimal);

            } else {
                imgAnimal.setImageResource(R.mipmap.default_avatar);
            }
        }
    }
}

