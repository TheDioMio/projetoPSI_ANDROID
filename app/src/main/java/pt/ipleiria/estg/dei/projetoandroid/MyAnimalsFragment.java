package pt.ipleiria.estg.dei.projetoandroid;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

import pt.ipleiria.estg.dei.projetoandroid.adaptadores.ListaAnimalsAdaptador;
import pt.ipleiria.estg.dei.projetoandroid.adaptadores.ListaAnimalsGridAdaptador;
import pt.ipleiria.estg.dei.projetoandroid.listeners.AnimalDeleteListener;
import pt.ipleiria.estg.dei.projetoandroid.listeners.AnimalsListener;
import pt.ipleiria.estg.dei.projetoandroid.modelo.Animal;
import pt.ipleiria.estg.dei.projetoandroid.modelo.AnimalFilter;
import pt.ipleiria.estg.dei.projetoandroid.modelo.AppSingleton;
import pt.ipleiria.estg.dei.projetoandroid.modelo.GestorAnimals;

public class MyAnimalsFragment extends Fragment implements AnimalsListener, AnimalDeleteListener {
    private ListView lvMyAnimals;

    private ArrayList<Animal> animals;
    private ArrayList<Animal> animalsFiltrados; //os que ficam visiveis quando é filtrado

    private ListaAnimalsAdaptador adaptador;

    private FloatingActionButton fabAddAnimal;

    public MyAnimalsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.menu_animals_search, menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.action_filter) {
            FilterDialog dialog = new FilterDialog();
            dialog.show(getParentFragmentManager(), "FILTER_DIALOG");
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void abrirDialogFiltros() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Filtros");
        builder.setMessage("Aqui vão ficar os filtros");
        builder.setPositiveButton("Pesquisar", null);
        builder.setNegativeButton("Cancelar", null);
        builder.show();
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_my_animals, container, false);
        lvMyAnimals = view.findViewById(R.id.lvMyAnimals);
        AppSingleton.getInstance(getContext()).getMyAnimalsAPI(getContext());  // ALterar para ir buscar apenas os Animals do user logado
        AppSingleton.getInstance(getContext()).setAnimalsListener(this);
        AppSingleton.getInstance(getContext()).setAnimalDeleteListener(this);
        animals = new ArrayList<>();
        animalsFiltrados = new ArrayList<>();

        adaptador = new ListaAnimalsAdaptador(getContext(), animalsFiltrados);
        lvMyAnimals.setAdapter(adaptador);

        fabAddAnimal =view.findViewById(R.id.fabAddAnimal);


        getParentFragmentManager().setFragmentResultListener(
                "FILTROS_REQUEST",
                getViewLifecycleOwner(),
                (requestKey, bundle) -> {
                    AnimalFilter filtro =
                            (AnimalFilter) bundle.getSerializable("FILTRO");
                    aplicarFiltros(filtro);
                }
        );


        fabAddAnimal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), AnimalFormActivity.class);
                        intent.putExtra(AnimalFormActivity.EXTRA_MODE,
                        AnimalFormActivity.MODE_CREATE);
                startActivity(intent);


            }
        });


        lvMyAnimals.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                int idAnimal = animalsFiltrados.get(position).getId();

                Intent intent = new Intent(getContext(), AnimalFormActivity.class);
                intent.putExtra(AnimalFormActivity.EXTRA_MODE,
                        AnimalFormActivity.MODE_EDIT);
                intent.putExtra(AnimalFormActivity.EXTRA_ANIMAL_ID, idAnimal);
                startActivity(intent);
            }
        });

        return view;
    }


    private void aplicarFiltros(AnimalFilter filtro) {

        animalsFiltrados.clear();

        for (Animal a : animals) {

            if (!filtro.isDefault(filtro.tipo)
                    && (a.getType() == null || !a.getType().equalsIgnoreCase(filtro.tipo)))
                continue;

            if (!filtro.isDefault(filtro.raca)
                    && (a.getBreed() == null || !a.getBreed().equalsIgnoreCase(filtro.raca)))
                continue;

            if (!filtro.isDefault(filtro.tamanho)
                    && (a.getSize() == null || !a.getSize().equalsIgnoreCase(filtro.tamanho)))
                continue;

            if (!filtro.isDefault(filtro.idade)
                    && (a.getAge() == null || !a.getAge().equalsIgnoreCase(filtro.idade)))
                continue;

            if (!filtro.isDefault(filtro.vacinacao)
                    && (a.getVacination() == null || !a.getVacination().equalsIgnoreCase(filtro.vacinacao)))
                continue;

            if (!filtro.isDefault(filtro.dono)
                    && (a.getOwnerName() == null || !a.getOwnerName().equalsIgnoreCase(filtro.dono)))
                continue;

            if (filtro.nome != null && !filtro.nome.trim().isEmpty()
                    && (a.getName() == null
                    || !a.getName().toLowerCase().contains(filtro.nome.toLowerCase())))
                continue;

            animalsFiltrados.add(a);
        }

        adaptador.notifyDataSetChanged();
    }

    @Override
    public void onResume() {
        super.onResume();

        AppCompatActivity act = (AppCompatActivity) requireActivity();
        if (act.getSupportActionBar() != null) {
            act.getSupportActionBar().setTitle(R.string.txt_animais);
            act.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }


    //METODOS IMPLEMENADOS DO LISTENER DOS ANIMAIS
    @Override
    public void onRefreshAnimalsList(ArrayList<Animal> animalsFromSingleton) {
        //lvAnimalsGrid.setAdapter(new ListaAnimalsGridAdaptador(getContext(), animals));

        animals.clear();
        animals.addAll(animalsFromSingleton);

        animalsFiltrados.clear();
        animalsFiltrados.addAll(animalsFromSingleton);

        adaptador.notifyDataSetChanged();

    }

    @Override
    public void onErro(String erro) {
        //tratamos o erro que devolva da API
    }


    //METODOS IMPLEMENTADOS DO DELETE lISTENER
    @Override
    public void onDeleteAnimalSuccess(int animalId) {
        // remover localmente
        for (int i = 0; i < animals.size(); i++) {
            if (animals.get(i).getId() == animalId) {
                animals.remove(i);
                break;
            }
        }

        animalsFiltrados.clear();
        animalsFiltrados.addAll(animals);

        adaptador.notifyDataSetChanged();

        Toast.makeText(getContext(),
                "Animal removido com sucesso",
                Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onDeleteAnimalError(String error) {
        Toast.makeText(getContext(), error, Toast.LENGTH_SHORT).show();
    }
}
