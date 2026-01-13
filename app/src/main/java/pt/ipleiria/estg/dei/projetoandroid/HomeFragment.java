package pt.ipleiria.estg.dei.projetoandroid;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

public class HomeFragment extends Fragment {

    public HomeFragment() {
        // Construtor vazio obrigatório
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onResume() {
        super.onResume();

        // Verifica se a Activity existe para evitar erros
        if (getActivity() != null && getActivity() instanceof AppCompatActivity) {
            AppCompatActivity act = (AppCompatActivity) getActivity();

            // Define o título na barra superior
            if (act.getSupportActionBar() != null) {
                act.getSupportActionBar().setTitle(R.string.txt_petpanion);
            }

            // Garante que o menu hambúrguer aparece
            if (act instanceof MenuMainActivity) {
                ((MenuMainActivity) act).showHamburger();
            }
        }
    }
}