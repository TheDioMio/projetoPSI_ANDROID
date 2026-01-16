package pt.ipleiria.estg.dei.projetoandroid.adaptadores;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;

import pt.ipleiria.estg.dei.projetoandroid.R;
import pt.ipleiria.estg.dei.projetoandroid.modelo.Animal;
import pt.ipleiria.estg.dei.projetoandroid.modelo.AnimalFile;
import pt.ipleiria.estg.dei.projetoandroid.modelo.AppSingleton;

public class ListaAnimalsAdaptador extends BaseAdapter {

    private Context context;
    private LayoutInflater inflater;
    private ArrayList<Animal> animals;

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

        if (inflater == null) {
            inflater = LayoutInflater.from(context);
        }

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_animal_card, parent, false);
        }

        ViewHolderLista viewHolder = (ViewHolderLista) convertView.getTag();
        if (viewHolder == null) {
            viewHolder = new ViewHolderLista(convertView);
            convertView.setTag(viewHolder);
        }

        viewHolder.update(animal);

        return convertView;
    }

    // ================= VIEW HOLDER =================

    private class ViewHolderLista {
        private TextView tvName, tvAge, tvBreed;
        private ImageView imgAnimal;
        private ImageButton btnDelete;

        public ViewHolderLista(View view) {
            tvName = view.findViewById(R.id.tvName);
            tvAge = view.findViewById(R.id.tvAge);
            tvBreed = view.findViewById(R.id.tvBreed);
            imgAnimal = view.findViewById(R.id.imgAnimal);
            btnDelete = view.findViewById(R.id.btnDelete);

            // Garantir que o botão não bloqueia o clique da ListView
            btnDelete.setFocusable(false);
            btnDelete.setFocusableInTouchMode(false);
        }

        public void update(Animal animal) {
            tvName.setText(animal.getName());
            tvAge.setText(animal.getAge());
            tvBreed.setText(animal.getBreed()+" ("+animal.getType()+")");

            carregarImagem(animal);

            btnDelete.setOnClickListener(v -> mostrarDialogApagar(animal));
        }

        private void carregarImagem(Animal animal) {
            String imgPath = null;

            if (animal.getAnimalfiles() != null && !animal.getAnimalfiles().isEmpty()) {
                AnimalFile file = animal.getAnimalfiles().get(0);
                if (file != null) {
                    imgPath = file.getFileAddress();
                }
            }

            if (imgPath != null) {
                String imageUrl = imgPath.startsWith("http")
                        ? imgPath
                        : AppSingleton.getInstance(context).getEndereco() + AppSingleton.getInstance(context).FRONTEND_BASE_URL + imgPath;

                Glide.with(context)
                        .load(imageUrl)
                        .placeholder(R.mipmap.default_avatar)
                        .error(R.mipmap.default_avatar)
                        .into(imgAnimal);
            } else {
                imgAnimal.setImageResource(R.mipmap.default_avatar);
            }
        }

        private void mostrarDialogApagar(Animal animal) {



            //primeiro vê se tem internet
            if (!AppSingleton.getInstance(context.getApplicationContext()).isConnectionInternet(context.getApplicationContext())) {
                Snackbar.make(btnDelete,
                                R.string.txt_offline_indisponivel,
                                Snackbar.LENGTH_INDEFINITE)
                        .setAction(R.string.txt_ok, v -> {})
                        .show();
                return;
            }

            new AlertDialog.Builder(context)
                    .setTitle("Eliminar animal")
                    .setMessage("Tem a certeza que deseja eliminar este animal?")
                    .setCancelable(false)
                    .setPositiveButton("Apagar", (dialog, which) -> {

                        AppSingleton.getInstance(context)
                                .deleteAnimalAPI(context, animal.getId());

                    })
                    .setNegativeButton("Cancelar", (dialog, which) -> dialog.dismiss())
                    .show();
        }
    }
}

