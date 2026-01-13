package pt.ipleiria.estg.dei.projetoandroid;

import android.app.Activity;
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
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;

import pt.ipleiria.estg.dei.projetoandroid.adaptadores.ListaAnimalsAdaptador;
import pt.ipleiria.estg.dei.projetoandroid.adaptadores.ListaAnimalsGridAdaptador;
import pt.ipleiria.estg.dei.projetoandroid.listeners.AnimalDeleteListener;
import pt.ipleiria.estg.dei.projetoandroid.listeners.AnimalsListener;
import pt.ipleiria.estg.dei.projetoandroid.listeners.MyAnimalsListener;
import pt.ipleiria.estg.dei.projetoandroid.modelo.Animal;
import pt.ipleiria.estg.dei.projetoandroid.modelo.AnimalFilter;
import pt.ipleiria.estg.dei.projetoandroid.modelo.AppSingleton;
import pt.ipleiria.estg.dei.projetoandroid.modelo.GestorAnimals;

public class MyAnimalsFragment extends Fragment implements MyAnimalsListener, AnimalDeleteListener {



    private ListView lvMyAnimals;

    private ArrayList<Animal> myAnimals;
    private ArrayList<Animal> animalsFiltrados; //os que ficam visiveis quando é filtrado

    private ListaAnimalsAdaptador adaptador;
    private View rootView;
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
            FilterDialog dialog = FilterDialog.newInstance(myAnimals);
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
        rootView = view;
        lvMyAnimals = view.findViewById(R.id.lvMyAnimals);

        myAnimals = new ArrayList<>();
        animalsFiltrados = new ArrayList<>();

        adaptador = new ListaAnimalsAdaptador(getContext(), animalsFiltrados);
        lvMyAnimals.setAdapter(adaptador);

        fabAddAnimal =view.findViewById(R.id.fabAddAnimal);

        AppSingleton.getInstance(getContext()).setMyAnimalsListener(this);
        AppSingleton.getInstance(getContext()).setAnimalDeleteListener(this);

        AppSingleton.getInstance(getContext()).getMyAnimalsAPI(getContext());




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

                if (!AppSingleton.getInstance(getContext()).isConnectionInternet(getContext())) {
                    Snackbar.make(rootView,
                                    R.string.txt_offline_indisponivel,
                                    Snackbar.LENGTH_INDEFINITE)
                            .setAction(R.string.txt_ok, vv -> {})
                            .show();
                    return;
                }


                Intent intent = new Intent(getContext(), AnimalFormActivity.class);
                        intent.putExtra(AnimalFormActivity.EXTRA_MODE,
                        AnimalFormActivity.MODE_CREATE);
                startActivityForResult(intent, MenuMainActivity.ADD_ANIMAL);
                //colocar for result

            }
        });


        lvMyAnimals.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                if (!AppSingleton.getInstance(getContext()).isConnectionInternet(getContext())) {
                    Snackbar.make(rootView,
                                    R.string.txt_offline_indisponivel,
                                    Snackbar.LENGTH_INDEFINITE)
                            .setAction(R.string.txt_ok, vv -> {})
                            .show();
                    return;
                }

                int idAnimal = animalsFiltrados.get(position).getId();

                Intent intent = new Intent(getContext(), AnimalFormActivity.class);
                intent.putExtra(AnimalFormActivity.EXTRA_MODE,
                        AnimalFormActivity.MODE_EDIT);
                intent.putExtra(AnimalFormActivity.EXTRA_ANIMAL_ID, idAnimal);
                startActivityForResult(intent, MenuMainActivity.EDIT_ANIMAL);
                //colocar for result
            }
        });

        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK) {

            // Voltar a ir buscar os animals
            AppSingleton.getInstance(getContext()).getMyAnimalsAPI(getContext());

            if (requestCode == MenuMainActivity.ADD_ANIMAL) {
                Toast.makeText(getContext(),
                        R.string.txt_animal_adicionado_com_sucesso,
                        Toast.LENGTH_SHORT).show();
            }
            else if (requestCode == MenuMainActivity.EDIT_ANIMAL) {
                Toast.makeText(getContext(),
                        R.string.txt_animal_editado_com_sucesso,
                        Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void aplicarFiltros(AnimalFilter filtro) {

        animalsFiltrados.clear();

        for (Animal a : myAnimals) {

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
    public void onRefreshMyAnimals(ArrayList<Animal> Animals) {
        //lvAnimalsGrid.setAdapter(new ListaAnimalsGridAdaptador(getContext(), animals));

        myAnimals.clear();
        myAnimals.addAll(Animals);

        animalsFiltrados.clear();
        animalsFiltrados.addAll(myAnimals);

        adaptador.notifyDataSetChanged();
    }

    @Override
    public void onMyAnimalsOffline(ArrayList<Animal> cachedAnimals) {
        Snackbar.make(
                        rootView,
                        R.string.txt_sem_internet_a_mostrar_dados_guardados,
                        Snackbar.LENGTH_INDEFINITE
                )
                .setAction(R.string.txt_ok, v -> {
                    // Ao clicar em OK simplesmente fecha
                })
                .show();

        // Atualiza a lista base
        myAnimals.clear();
        myAnimals.addAll(cachedAnimals);

        // Atualiza o que está visível
        animalsFiltrados.clear();
        animalsFiltrados.addAll(cachedAnimals);

        // Atualiza o ListView
        adaptador.notifyDataSetChanged();
    }

    @Override
    public void onErro(String erro) {
        //tratamos o erro que devolva da API
    }


    //METODOS IMPLEMENTADOS DO DELETE lISTENER
    @Override
    public void onDeleteAnimalSuccess(int animalId) {
        AppSingleton.getInstance(getContext()).getMyAnimalsAPI(getContext());
        // remover localmente
        for (int i = 0; i < myAnimals.size(); i++) {
            if (myAnimals.get(i).getId() == animalId) {
                myAnimals.remove(i);
                break;
            }
        }

        animalsFiltrados.clear();
        animalsFiltrados.addAll(myAnimals);

        adaptador.notifyDataSetChanged();

        Toast.makeText(getContext(),
                "Animal removido com sucesso",
                Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onDeleteAnimalError(String error) {
        Toast.makeText(getContext(), error, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        AppSingleton.getInstance(getContext()).setMyAnimalsListener(null);
        AppSingleton.getInstance(getContext()).setAnimalDeleteListener(null);
    }

}
