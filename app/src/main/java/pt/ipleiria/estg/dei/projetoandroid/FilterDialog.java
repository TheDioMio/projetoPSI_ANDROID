package pt.ipleiria.estg.dei.projetoandroid;



import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.*;

        import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import java.util.*;

import pt.ipleiria.estg.dei.projetoandroid.modelo.Animal;
import pt.ipleiria.estg.dei.projetoandroid.modelo.AnimalFilter;
import pt.ipleiria.estg.dei.projetoandroid.modelo.AppSingleton;

public class FilterDialog extends DialogFragment {

    private ArrayList<Animal> animals;

    private EditText etNome;
    private Spinner spTipo, spRaca, spTamanho, spIdade, spVacinacao, spDono;
    private Button btnCancelar, btnPesquisar;

    public FilterDialog() {
        //animals = AppSingleton.getInstance(requireContext()).getAnimals();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        animals = AppSingleton
                .getInstance(requireContext())
                .getAnimals();
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = requireActivity().getLayoutInflater();

        View view = inflater.inflate(R.layout.dialog_filters, null);

        bindViews(view);
        preencherFiltros();

        btnCancelar.setOnClickListener(v -> dismiss());

        btnPesquisar.setOnClickListener(v -> {

            AnimalFilter filtro = new AnimalFilter();

            filtro.tipo = spTipo.getSelectedItem().toString();
            filtro.raca = spRaca.getSelectedItem().toString();
            filtro.tamanho = spTamanho.getSelectedItem().toString();
            filtro.idade = spIdade.getSelectedItem().toString();
            filtro.vacinacao = spVacinacao.getSelectedItem().toString();
            filtro.dono = spDono.getSelectedItem().toString();
            filtro.nome = etNome.getText().toString();

            Bundle result = new Bundle();
            result.putSerializable("FILTRO", filtro);

            getParentFragmentManager()
                    .setFragmentResult("FILTROS_REQUEST", result);

            dismiss();
        });

        builder.setView(view);
        return builder.create();
    }

    private void bindViews(View view) {
        etNome = view.findViewById(R.id.etNome);
        spTipo = view.findViewById(R.id.spTipo);
        spRaca = view.findViewById(R.id.spRaca);
        spTamanho = view.findViewById(R.id.spTamanho);
        spIdade = view.findViewById(R.id.spIdade);
        spVacinacao = view.findViewById(R.id.spVacinacao);
        spDono = view.findViewById(R.id.spDono);

        btnCancelar = view.findViewById(R.id.btnCancelar);
        btnPesquisar = view.findViewById(R.id.btnPesquisar);
    }

    private void preencherFiltros() {

        Set<String> tipos = new HashSet<>();
        Set<String> racas = new HashSet<>();
        Set<String> tamanhos = new HashSet<>();
        Set<String> idades = new HashSet<>();
        Set<String> donos = new HashSet<>();

        for (Animal a : animals) {
            if (a.getType() != null) tipos.add(a.getType());
            if (a.getBreed() != null) racas.add(a.getBreed());
            if (a.getSize() != null) tamanhos.add(a.getSize());
            if (a.getAge() != null) idades.add(a.getAge());
            if (a.getOwnerName() != null) donos.add(a.getOwnerName());
        }

        preencherSpinner(spTipo, tipos, "Todos");
        preencherSpinner(spRaca, racas, "Todos");
        preencherSpinner(spTamanho, tamanhos, "Todos");
        preencherSpinner(spIdade, idades, "Todos");
        preencherSpinner(spDono, donos, "Todos");

        preencherSpinner(spVacinacao, Arrays.asList( "Vacinado", "Não vacinado"),"Todos");
    }

    private void preencherSpinner(Spinner spinner, Collection<String> valores, String primeiro) {

        ArrayList<String> lista = new ArrayList<>(valores);
        Collections.sort(lista); // ordenar só os valores reais

        if (primeiro != null) {
            lista.add(0, primeiro); // força "Todos" na primeira posição
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                getContext(),
                android.R.layout.simple_spinner_item,
                lista
        );

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
    }

}
