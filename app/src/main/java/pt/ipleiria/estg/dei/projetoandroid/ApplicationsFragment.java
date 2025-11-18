package pt.ipleiria.estg.dei.projetoandroid;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import pt.ipleiria.estg.dei.projetoandroid.adaptadores.ListaAnimalsAdaptador;
import pt.ipleiria.estg.dei.projetoandroid.modelo.Animal;
import pt.ipleiria.estg.dei.projetoandroid.modelo.AppSingleton;
import pt.ipleiria.estg.dei.projetoandroid.modelo.User;

public class ApplicationsFragment extends Fragment {
    private Button btnSubmit;
    private EditText etDescription;
    private TextView tvNomeCard, tvRacaCard;
    private ImageView imgAnimalCard;
    private AppSingleton singleton;
    private Animal animal;


    public ApplicationsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_applications, container, false);

        btnSubmit = view.findViewById(R.id.btnSubmit);
        etDescription = view.findViewById(R.id.etDescription);
        tvNomeCard = view.findViewById(R.id.tvNomeCard);
        tvRacaCard = view.findViewById(R.id.tvRacaCard);
        imgAnimalCard = view.findViewById(R.id.imgAnimalCard);

        Bundle args = getArguments();
        if (args != null) {
            int idRecebido = args.getInt("ID_ANIMAL");
            singleton = AppSingleton.getInstance();
            animal = singleton.getAnimal(idRecebido);
        } else {
            System.out.println("DEBUG: args é NULL");
        }

        if (animal != null) {
            System.out.println("Animal encontrado: " + animal.getName());
            carregarAnimal(animal);
        } else {
            System.out.println("O objeto animal está NULL!");
        }
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                User user = ((MenuMainActivity) getActivity()).getUserLogado();
                if (user == null) return;
            }
        });
        return view;
    }

    private void carregarAnimal(Animal animal) {
        tvNomeCard.setText(animal.getName());
        tvRacaCard.setText(animal.getBreed());
        List<String> imagens = animal.getImages();

        carregarImagem(imagens.get(0), imgAnimalCard);
    }


    private void carregarImagem(String img, ImageView destino) {

        if (img == null || img.isEmpty()) {
            destino.setImageResource(R.mipmap.placeholder);
            return;
        }

        // URL → http/https
        if (img.startsWith("http")) {
            Glide.with(requireContext())
                    .load(img)
                    .placeholder(R.mipmap.placeholder)
                    .error(R.mipmap.placeholder)
                    .centerCrop()
                    .into(destino);
        }
        // Drawable pelo nome
        else {
            int resId = getResources().getIdentifier(img, "drawable", requireContext().getPackageName());

            if (resId != 0) {
                Glide.with(requireContext())
                        .load(resId)
                        .placeholder(R.mipmap.placeholder)
                        .error(R.mipmap.placeholder)
                        .centerCrop()
                        .into(destino);
            } else {
                destino.setImageResource(R.mipmap.placeholder);
            }
        }
    }
}