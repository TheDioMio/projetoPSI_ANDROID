package pt.ipleiria.estg.dei.projetoandroid;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Date;

import pt.ipleiria.estg.dei.projetoandroid.modelo.Animal;
import pt.ipleiria.estg.dei.projetoandroid.modelo.AnimalBreed;
import pt.ipleiria.estg.dei.projetoandroid.modelo.AnimalType;
import pt.ipleiria.estg.dei.projetoandroid.modelo.AppSingleton;
import pt.ipleiria.estg.dei.projetoandroid.modelo.User;

public class NewAnimalFragment extends Fragment {
    private EditText etNome, etDescription;
    private Spinner spTipo, spRaca, spIdade, spTamanho , spVaccination, spNeutered;;
    private Button btnGuardar;

    private AppSingleton app;

    private int userId;

    public NewAnimalFragment() {
        // Required empty public constructor
    }

    public static NewAnimalFragment newInstance(int userId) {
        NewAnimalFragment fragment = new NewAnimalFragment();
        Bundle args = new Bundle();
        args.putInt("USER_ID", userId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Receber o userId do Bundle
        if (getArguments() != null) {
            userId = getArguments().getInt("USER_ID", 0);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_new_animal, container, false);

        // Inicializar AppSingleton
        app = AppSingleton.getInstance();


        // Inicializar views
        etNome = view.findViewById(R.id.etNomeAnimal);
        etDescription = view.findViewById(R.id.etDescription);
        spTipo = view.findViewById(R.id.spinnerTipoAnimal);
        spRaca = view.findViewById(R.id.spinnerRaca);
        spIdade = view.findViewById(R.id.spinnerIdade);
        spTamanho = view.findViewById(R.id.spinnerTamanho);
        spVaccination = view.findViewById(R.id.spinnerVaccination);
        spNeutered = view.findViewById(R.id.spinnerNeutered);
        btnGuardar = view.findViewById(R.id.btnGuardarAnimal);

        // Carregar dados nos spinners
        carregarSpinnerTipoAnimal();
        carregarSpinnerIdade();
        carregarSpinnerTamanho();
        carregarSpinnerVaccination();
        carregarSpinnerNeutered();

        btnGuardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nome = etNome.getText().toString().trim();
                String descricao = etDescription.getText().toString().trim();

                if (nome.isEmpty()) {
                    etNome.setError("Introduza o nome do animal");
                    return;
                }

                if (descricao.isEmpty()) {
                    etDescription.setError("Introduza uma descrição");
                    return;
                }

                // Obter objetos selecionados dos spinners (não apenas strings!)
                AnimalType tipo = (AnimalType) spTipo.getSelectedItem();
                AnimalBreed raca = (AnimalBreed) spRaca.getSelectedItem();

                int tipoId = tipo.getId();
                int racaId = raca.getId();

                // Estes spinners são listas simples, logo a posição = ID
                int idadeId = spIdade.getSelectedItemPosition() + 1;
                int tamanhoId = spTamanho.getSelectedItemPosition() + 1;
                int vacinasId = spVaccination.getSelectedItemPosition() + 1;

                boolean neutered = spNeutered.getSelectedItem().toString().equals("Sim");

                User user = AppSingleton.getInstance().getUser(userId);
                if (user == null) {
                    Toast.makeText(getContext(), "Erro: usuário não encontrado", Toast.LENGTH_SHORT).show();
                    return;
                }

                Animal novoAnimal = new Animal(
                        0,
                        tamanhoId,
                        idadeId,
                        tipoId,
                        racaId,
                        vacinasId,
                        user.getId(),
                        nome,
                        descricao,
                        "Localização", // definir origem real depois
                        tipo.getDescription(),
                        raca.getDescription(),
                        user.getName(),
                        spIdade.getSelectedItem().toString(),
                        spTamanho.getSelectedItem().toString(),
                        spVaccination.getSelectedItem().toString(),
                        neutered,
                        new Date(),
                        new ArrayList<>()
                );

                // Adicionar ao gestor
                AppSingleton.getInstance().addAnimal(novoAnimal);

                Toast.makeText(getContext(), "Animal guardado com sucesso!", Toast.LENGTH_SHORT).show();

                // Voltar atrás
                Fragment parent = getParentFragmentManager().findFragmentById(R.id.contentFragment);
                if (parent instanceof MyAnimalsFragment) {
                    ((MyAnimalsFragment) parent).carregarLista(); // chama o método público do fragmento
                }

                // Fechar o fragmento NewAnimal
                getParentFragmentManager().beginTransaction().remove(NewAnimalFragment.this).commit();
            }
        });

        // Atualizar raças ao mudar tipo de animal
        spTipo.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {

                AnimalType tipoSelecionado = (AnimalType) spTipo.getSelectedItem();

                if (tipoSelecionado != null) {
                    carregarSpinnerRaca(tipoSelecionado.getId());
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {}
        });

        return view;
    }



    private void carregarSpinnerTipoAnimal() {

        ArrayList<AnimalType> tipos = app.getAnimalTypes();

        ArrayAdapter<AnimalType> adapter = new ArrayAdapter<>(
                getContext(),
                android.R.layout.simple_spinner_item,
                tipos
        );

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spTipo.setAdapter(adapter);
    }

    private void carregarSpinnerRaca(int tipoId) {

        ArrayList<AnimalBreed> racas = app.getBreedsByAnimalType(tipoId);

        ArrayAdapter<AnimalBreed> adapter = new ArrayAdapter<>(
                getContext(),
                android.R.layout.simple_spinner_item,
                racas
        );

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spRaca.setAdapter(adapter);
    }

    private void carregarSpinnerIdade() {

        ArrayList<String> idades = app.getAnimalAgesStrings();

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                getContext(),
                android.R.layout.simple_spinner_item,
                idades
        );

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spIdade.setAdapter(adapter);
    }

    private void carregarSpinnerTamanho() {

        ArrayList<String> tamanhos = app.getAnimalSizesStrings();

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                getContext(),
                android.R.layout.simple_spinner_item,
                tamanhos
        );

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spTamanho.setAdapter(adapter);
    }

    private void carregarSpinnerVaccination() {

        ArrayList<String> vacinas = app.getVaccinationStrings();

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                getContext(),
                android.R.layout.simple_spinner_item,
                vacinas
        );

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spVaccination.setAdapter(adapter);
    }

    private void carregarSpinnerNeutered() {

        ArrayAdapter<String> neuteredAdapter = new ArrayAdapter<>(
                getContext(),
                android.R.layout.simple_spinner_item,
                new String[]{"Sim", "Não"}
        );

        neuteredAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spNeutered.setAdapter(neuteredAdapter);


    }



}