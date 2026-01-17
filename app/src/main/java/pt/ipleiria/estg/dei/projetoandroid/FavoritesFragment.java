package pt.ipleiria.estg.dei.projetoandroid;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.TextView;

import java.util.ArrayList;

import pt.ipleiria.estg.dei.projetoandroid.adaptadores.ListaAnimalsGridAdaptador;
import pt.ipleiria.estg.dei.projetoandroid.listeners.FavoriteRemovedListener;
import pt.ipleiria.estg.dei.projetoandroid.modelo.Animal;
import pt.ipleiria.estg.dei.projetoandroid.modelo.AppSingleton;


public class FavoritesFragment extends Fragment implements FavoriteRemovedListener {

    private GridView gridFavoritos;
    private TextView tvEmpty;

    private ArrayList<Animal> favoritos = new ArrayList<>();
    private ListaAnimalsGridAdaptador adapter;


    public FavoritesFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_favorites, container, false);

        gridFavoritos = view.findViewById(R.id.gridFavorites);
        tvEmpty = view.findViewById(R.id.tvEmpty);

        loadFavoritos();

        adapter = new ListaAnimalsGridAdaptador(getContext(), favoritos, this);
        gridFavoritos.setAdapter(adapter);

        updateEmptyState();

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        requireActivity().setTitle(getString(R.string.txt_favoritos));
        loadFavoritos();
        adapter.notifyDataSetChanged();
        updateEmptyState();
    }

    private void loadFavoritos() {

        favoritos.clear();

        ArrayList<Animal> todos =
                AppSingleton.getInstance(getContext()).getAnimals(); // lista completa

        AppSingleton singleton = AppSingleton.getInstance(getContext());

        for (Animal animal : todos) {
            if (singleton.isFavorite(animal.getId())) {
                favoritos.add(animal);
            }
        }
    }

    private void updateEmptyState() {
        if (favoritos.isEmpty()) {
            tvEmpty.setVisibility(View.VISIBLE);
            gridFavoritos.setVisibility(View.GONE);
        } else {
            tvEmpty.setVisibility(View.GONE);
            gridFavoritos.setVisibility(View.VISIBLE);
        }
    }


    @Override
    public void onFavoriteRemoved(int animalId) {
        for (int i = 0; i < favoritos.size(); i++) {
            if (favoritos.get(i).getId() == animalId) {
                favoritos.remove(i);
                adapter.notifyDataSetChanged();
                break;
            }
        }

        updateEmptyState();
    }
}