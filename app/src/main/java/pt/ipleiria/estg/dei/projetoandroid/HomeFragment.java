package pt.ipleiria.estg.dei.projetoandroid;

import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;

import pt.ipleiria.estg.dei.projetoandroid.adaptadores.ListaAnimalsAdaptador;
import pt.ipleiria.estg.dei.projetoandroid.modelo.Animal;
import pt.ipleiria.estg.dei.projetoandroid.modelo.AppSingleton;
import pt.ipleiria.estg.dei.projetoandroid.modelo.GestorAnimals;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment {

    private ListView lvAnimals;

    private ArrayList<Animal> animals;

    private ListaAnimalsAdaptador adaptador;


    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public HomeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HomeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
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

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        lvAnimals = view.findViewById(R.id.lvAnimals);

        View header = inflater.inflate(R.layout.header_home, lvAnimals, false);
        View footer = inflater.inflate(R.layout.footer, lvAnimals, false);

        lvAnimals.addHeaderView(header);
        lvAnimals.addFooterView(footer);

        animals = AppSingleton.getInstance(getContext()).getAnimals();

        //definir o adaptador
        adaptador = new ListaAnimalsAdaptador(getContext(), animals);
        lvAnimals.setAdapter(adaptador);

        lvAnimals.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                int idAnimal = animals.get(position-1).getId();
                //Toast.makeText(getContext(), idAnimal, Toast.LENGTH_SHORT).show();
                // Cria o fragmento de detalhes
                AnimalDetailsFragment fragment = new AnimalDetailsFragment();

                // Passa o ID do animal como argumento
                Bundle args = new Bundle();
                args.putInt("ID_ANIMAL", idAnimal);
                fragment.setArguments(args);

                // Faz a troca do fragmento
                getParentFragmentManager().beginTransaction()
                        .replace(R.id.contentFragment, fragment) // o id do container dos fragments na tua MenuMaisActivity
                        .addToBackStack(null) // permite voltar atrás com o botão "voltar"
                        .commit();
            }
        });

        return view;
    }


}