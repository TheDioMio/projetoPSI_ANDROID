package pt.ipleiria.estg.dei.projetoandroid;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

import pt.ipleiria.estg.dei.projetoandroid.adaptadores.ListaAnimalsAdaptador;
import pt.ipleiria.estg.dei.projetoandroid.modelo.Animal;
import pt.ipleiria.estg.dei.projetoandroid.modelo.AppSingleton;
import pt.ipleiria.estg.dei.projetoandroid.modelo.User;

///**
// * A simple {@link Fragment} subclass.
// * Use the {@link MyAnimalsFragment#newInstance} factory method to
// * create an instance of this fragment.
// */
public class MyAnimalsFragment extends Fragment {

    //Codigo que veio dos ppt das aulas
    //public static final int REQUEST_CODE_ADICIONAR_CONTACTO = 1;

    private final int ADD = 100;
    private final int EDIT = 200;

    private ListView lvMyAnimals;
    private ListaAnimalsAdaptador adapter;
    private ArrayList<Animal> animaisDoUser;

    public MyAnimalsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // REGISTAR O LISTENER AQUI !! (antes de onCreateView)
        getParentFragmentManager().setFragmentResultListener(
                "update_request",
                this,
                (requestKey, result) -> carregarLista()
        );
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //Aqui recebe o codigo para saber se vem do edit ou do add e atualiza a lista
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_my_animals, container, false);

        lvMyAnimals = view.findViewById(R.id.lvMyAnimals);
        FloatingActionButton fabAdd = view.findViewById(R.id.fabAddAnimal);

        lvMyAnimals.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
               Intent intent = new Intent(getContext(), AnimalDetailsActivity.class);
               startActivityForResult(intent, EDIT);
            }
        });


        fabAdd.setOnClickListener(v -> {
            User user = ((MenuMainActivity) getActivity()).getUserLogado();
            if (user == null) return;

//            Codigo que veio da Ficha
//            public void onClickAdicionarContacto(View view) {
//                Intent intent = new Intent(this, AdicionarContactoActivity.class);
//                startActivityForResult(intent, REQUEST_CODE_ADICIONAR_CONTACTO);
//            }

            Bundle bundle = new Bundle();
            bundle.putInt("USER_ID", user.getId());

            NewAnimalFragment fragment = new NewAnimalFragment();
            fragment.setArguments(bundle);

            getParentFragmentManager()
                    .beginTransaction()
                    .add(R.id.contentFragment, fragment)
                    .addToBackStack(null)
                    .commit();
        });

        // Carregar lista
        carregarLista();



        return view;
    }



    public void carregarLista() {
        User userLogado = ((MenuMainActivity) getActivity()).getUserLogado();
        if (userLogado == null) return;

        int userId = userLogado.getId();

        //ArrayList<Animal> animaisDoUserTemp = AppSingleton.getInstance(getContext()).getAnimalsByUser(userId);

//        if (adapter == null) {
//            // Cria o adapter pela primeira vez
//            animaisDoUser = new ArrayList<>(animaisDoUserTemp);
//            adapter = new ListaAnimalsAdaptador(getContext(), animaisDoUser);
//            lvMyAnimals.setAdapter(adapter);
//        } else {
//            // Atualiza lista existente
//            animaisDoUser.clear();
//            animaisDoUser.addAll(animaisDoUserTemp);
//            adapter.notifyDataSetChanged();
//        }
    }

}