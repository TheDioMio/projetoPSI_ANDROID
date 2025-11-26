package pt.ipleiria.estg.dei.projetoandroid;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import pt.ipleiria.estg.dei.projetoandroid.adaptadores.ListaAnimalsAdaptador;
import pt.ipleiria.estg.dei.projetoandroid.modelo.Animal;
import pt.ipleiria.estg.dei.projetoandroid.modelo.AppSingleton;
import pt.ipleiria.estg.dei.projetoandroid.modelo.Application;
import pt.ipleiria.estg.dei.projetoandroid.modelo.User;

public class ApplicationsFragment extends Fragment {
    private Button btnSubmit;
    private EditText etDescription, etNomeCompleto, etIdade, etContacto;
    private TextView tvNomeCard, tvRacaCard;
    private ImageView imgAnimalCard;
    private AppSingleton singleton;
    private Animal animal;
    private Spinner spTipoHabitacao;
    private RadioGroup rgCriancas, rgCustos;

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
        etNomeCompleto = view.findViewById(R.id.etNomeCompleto);
        etIdade = view.findViewById(R.id.etIdade);
        etContacto = view.findViewById(R.id.etContacto);
        spTipoHabitacao = view.findViewById(R.id.spTipoHabitacao);
        rgCriancas = view.findViewById(R.id.rgCriancas);
        rgCustos = view.findViewById(R.id.rgCustos);

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
                //Vai buscar o utilizador:
                User user = ((MenuMainActivity) getActivity()).getUserLogado();
                if (user == null){
                    Toast.makeText(getContext(), "Utilizador não encontrado!", Toast.LENGTH_SHORT).show();
                    return;
                }
                //Verifica se o id do animal está a ser bem buscado:
                if(animal.getId() <= 0){
                    Toast.makeText(getContext(), R.string.txt_erro_buscar_id_animal, Toast.LENGTH_SHORT).show();
                    return;
                }

                //Vai buscar a descrição no formulário:
                String description = etDescription.getText().toString().trim();
                if(description.isEmpty()){
                    etDescription.setError(getString(R.string.txt_erro_descricao_candidatura));
                    return;
                }


                AppSingleton.getInstance().addApplication(user.getId(), animal.getId(), description);
                Toast.makeText(getContext(), R.string.txt_candidatura_enviada_com_sucesso, Toast.LENGTH_SHORT).show();


                //Este bloco de código é para ver as informações que foram passadas para dentro da candidatura (se está tudo OK)

                List<Application> listaCandidaturas = AppSingleton.getInstance().getApplications();
                if (listaCandidaturas != null && !listaCandidaturas.isEmpty()) {
                    Application ultimaCandidatura = listaCandidaturas.get(listaCandidaturas.size() - 1);
                    System.out.println("Candidatura Criada com Sucesso: ID do Utilizador: " + ultimaCandidatura.getUser_id()
                            + ", Descrição: " + ultimaCandidatura.getDescription() + ", ID do Animal: " + ultimaCandidatura.getAnimal_id());
                    Toast.makeText(getContext(), R.string.txt_candidatura_enviada_com_sucesso, Toast.LENGTH_SHORT).show();


                    /*O popBackStack faz "pop" do fragment em cima dos 13128312 fragmentos que temos na stack.
                    * Não dá para utilizar o .beginTransaction().remove(ApplicationsFragment.this).commit(),
                    * porque se não o ecrã fica todo branco!*/

                    getParentFragmentManager().popBackStack();
                } else {
                    System.out.println("ERRO: A lista de candidaturas está vazia após a adição.");
                }
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