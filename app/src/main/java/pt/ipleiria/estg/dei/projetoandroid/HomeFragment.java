package pt.ipleiria.estg.dei.projetoandroid;

import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
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


public class HomeFragment extends Fragment {

    private ListView lvAnimals;

    private ArrayList<Animal> animals;

    private ListaAnimalsAdaptador adaptador;



    public HomeFragment() {
        // Required empty public constructor
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

        //animals = AppSingleton.getInstance(getContext()).getAnimals();



        //definir o adaptador
//        adaptador = new ListaAnimalsAdaptador(getContext(), animals);
//        lvAnimals.setAdapter(adaptador);

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
//                getParentFragmentManager().beginTransaction()
//                        .replace(R.id.contentFragment, fragment) // o id do container dos fragments na tua MenuMaisActivity
//                        .addToBackStack(null) // permite voltar atrás com o botão "voltar"
//                        .commit();
                ((MenuMainActivity) requireActivity())
                        .navegarPara(fragment);
            }
        });

        return view;
    }

//    @Override
//    public void onResume() {
//        super.onResume();
//
//        AppCompatActivity act = (AppCompatActivity) requireActivity();
//        if (act.getSupportActionBar() != null) {
//            act.getSupportActionBar().setTitle(R.string.txt_petpanion);
//            //act.getSupportActionBar().setDisplayHomeAsUpEnabled(false);
//        }
//    }
@Override
public void onResume() {
    super.onResume();

    AppCompatActivity act = (AppCompatActivity) requireActivity();
    if (act.getSupportActionBar() != null) {
        act.getSupportActionBar().setTitle(R.string.txt_petpanion);
    }

    if (act instanceof MenuMainActivity) {
        ((MenuMainActivity) act).showHamburger(); // <-- aqui
    }
}

}