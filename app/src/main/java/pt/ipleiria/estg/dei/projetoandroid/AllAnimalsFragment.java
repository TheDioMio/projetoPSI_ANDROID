package pt.ipleiria.estg.dei.projetoandroid;

import android.app.AlertDialog;
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

import java.util.ArrayList;

import pt.ipleiria.estg.dei.projetoandroid.adaptadores.ListaAnimalsAdaptador;
import pt.ipleiria.estg.dei.projetoandroid.adaptadores.ListaAnimalsGridAdaptador;
import pt.ipleiria.estg.dei.projetoandroid.listeners.AnimalsListener;
import pt.ipleiria.estg.dei.projetoandroid.modelo.Animal;
import pt.ipleiria.estg.dei.projetoandroid.modelo.AnimalFilter;
import pt.ipleiria.estg.dei.projetoandroid.modelo.AppSingleton;
import pt.ipleiria.estg.dei.projetoandroid.modelo.GestorAnimals;

public class AllAnimalsFragment extends Fragment implements AnimalsListener {
    private GridView lvAnimalsGrid;

    private ArrayList<Animal> animals;
    private ArrayList<Animal> animalsFiltrados; //os que ficam visiveis quando é filtrado

    private ListaAnimalsGridAdaptador adaptador;


    public AllAnimalsFragment() {
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
        View view = inflater.inflate(R.layout.fragment_all_animals, container, false);
        lvAnimalsGrid = view.findViewById(R.id.lvAnimalsGrid);
        AppSingleton.getInstance(getContext()).getAnimalsAPI(getContext());
        AppSingleton.getInstance(getContext()).setAnimalsListener(this);

        animals = new ArrayList<>();
        animalsFiltrados = new ArrayList<>();

        adaptador = new ListaAnimalsGridAdaptador(getContext(), animalsFiltrados);
        lvAnimalsGrid.setAdapter(adaptador);



        getParentFragmentManager().setFragmentResultListener(
                "FILTROS_REQUEST",
                getViewLifecycleOwner(),
                (requestKey, bundle) -> {
                    AnimalFilter filtro =
                            (AnimalFilter) bundle.getSerializable("FILTRO");
                    aplicarFiltros(filtro);
                }
        );


        lvAnimalsGrid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                int idAnimal = animalsFiltrados.get(position).getId();
                //Toast.makeText(getContext(), idAnimal, Toast.LENGTH_SHORT).show();
                // Cria o fragmento de detalhes
                AnimalDetailsFragment fragment = new AnimalDetailsFragment();

                // Passa o ID do animal como argumento
                Bundle args = new Bundle();
                args.putInt("ID_ANIMAL", idAnimal);
                fragment.setArguments(args);

                ((MenuMainActivity) requireActivity())
                        .abrirFragment(fragment, true);
                // Faz a troca do fragmento
//                getParentFragmentManager().beginTransaction()
//                        .replace(R.id.contentFragment, fragment) // o id do container dos fragments na tua MenuMaisActivity
//                        .addToBackStack(null) // permite voltar atrás com o botão "voltar"
//                        .commit();
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
}