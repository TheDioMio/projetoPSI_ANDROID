package pt.ipleiria.estg.dei.projetoandroid;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

    private ListView lvMyAnimals;
    private ListaAnimalsAdaptador adapter;
    private ArrayList<Animal> animaisDoUser;

    public MyAnimalsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_my_animals, container, false);

        lvMyAnimals = view.findViewById(R.id.lvMyAnimals);
        FloatingActionButton fabAdd = view.findViewById(R.id.fabAddAnimal);

        fabAdd.setOnClickListener(v -> {
            User user = ((MenuMainActivity) getActivity()).getUserLogado();
            if (user == null) return;

            Bundle bundle = new Bundle();
            bundle.putInt("USER_ID", user.getId());

            NewAnimalFragment fragment = new NewAnimalFragment();
            fragment.setArguments(bundle);

            getParentFragmentManager()
                    .beginTransaction()
                    .replace(R.id.contentFragment, fragment)
                    .addToBackStack(null)
                    .commit();
        });


        // Carregar lista
        carregarLista();

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        // Atualiza a lista quando voltar do NewAnimalFragment
        carregarLista();
    }

    public void carregarLista() {
        User userLogado = ((MenuMainActivity) getActivity()).getUserLogado();
        if (userLogado == null) return;

        int userId = userLogado.getId();

        ArrayList<Animal> animaisDoUserTemp = AppSingleton.getInstance().getAnimalsByUser(userId);

        if (adapter == null) {
            // Cria o adapter pela primeira vez
            animaisDoUser = new ArrayList<>(animaisDoUserTemp);
            adapter = new ListaAnimalsAdaptador(getContext(), animaisDoUser);
            lvMyAnimals.setAdapter(adapter);
        } else {
            // Atualiza lista existente
            animaisDoUser.clear();
            animaisDoUser.addAll(animaisDoUserTemp);
            adapter.notifyDataSetChanged();
        }
    }

}