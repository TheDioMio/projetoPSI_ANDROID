package pt.ipleiria.estg.dei.projetoandroid;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import pt.ipleiria.estg.dei.projetoandroid.modelo.Animal;
import pt.ipleiria.estg.dei.projetoandroid.modelo.SingletonGestorAnimals;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AnimalDetailsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AnimalDetailsFragment extends Fragment {

    private ImageView imgPrincipal;
    private LinearLayout layoutMiniaturas;
    private TextView tvNome, tvIdade, tvRaca, tvLocalizacao, tvDescricao;

    private Animal animal;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public AnimalDetailsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AnimalDetailsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AnimalDetailsFragment newInstance(String param1, String param2) {
        AnimalDetailsFragment fragment = new AnimalDetailsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_animal_details, container, false);

        imgPrincipal = view.findViewById(R.id.imgPrincipal);
        layoutMiniaturas = view.findViewById(R.id.layoutMiniaturas);
        tvNome = view.findViewById(R.id.tvNomeAnimal);
        tvIdade = view.findViewById(R.id.tvIdadeAnimal);
        tvRaca = view.findViewById(R.id.tvRacaAnimal);
        tvLocalizacao = view.findViewById(R.id.tvLocalizacaoAnimal);
        tvDescricao = view.findViewById(R.id.tvDescricaoAnimal);

        //obtém o animal passado pelo Bundle
        Bundle args = getArguments();
        if (args != null) {
            int animalId = args.getInt("ID_ANIMAL");
            animal = SingletonGestorAnimals.getInstance().getAnimal(animalId);
            carregarDados(animal);
        }

        return view;
    }

    private void carregarDados(Animal animal) {
        if (animal == null) return;

        tvNome.setText(animal.getName());
        tvIdade.setText("Idade: " + animal.getAge() + " anos");
        tvRaca.setText("Raça: " + animal.getBreed_id());
        tvLocalizacao.setText("Localização: " + animal.getLocation());
        tvDescricao.setText(animal.getDescription());

        // imagem principal
        if (animal.getImages() != null && !animal.getImages().isEmpty()) {
            Glide.with(this)
                    .load(animal.getImages().get(0))
                    .centerCrop()
                    .placeholder(R.mipmap.placeholder)
                    .into(imgPrincipal);

            // carregar as miniaturas
            for (String url : animal.getImages()) {
                ImageView miniatura = new ImageView(getContext());
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(200, 200);
                params.setMargins(8, 0, 8, 0);
                miniatura.setLayoutParams(params);
                miniatura.setScaleType(ImageView.ScaleType.CENTER_CROP);

                Glide.with(this)
                        .load(url)
                        .placeholder(R.mipmap.placeholder)
                        .into(miniatura);

                // muda a imagem principal ao clicar
                miniatura.setOnClickListener(v ->
                        Glide.with(this)
                                .load(url)
                                .centerCrop()
                                .placeholder(R.mipmap.placeholder)
                                .into(imgPrincipal)
                );

                layoutMiniaturas.addView(miniatura);
            }
        }

    }
}