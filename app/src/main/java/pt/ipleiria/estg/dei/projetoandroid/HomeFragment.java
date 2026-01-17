package pt.ipleiria.estg.dei.projetoandroid;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import pt.ipleiria.estg.dei.projetoandroid.listeners.StatsListener;
import pt.ipleiria.estg.dei.projetoandroid.modelo.AppSingleton;

public class HomeFragment extends Fragment implements StatsListener {


    private TextView txtAnimalsAdopted, txtAnimalsWaiting, txtActiveUsers,
            txtActiveListings, txtTotalViews;

    public HomeFragment() {
 
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        txtAnimalsAdopted = view.findViewById(R.id.txtAnimalsAdopted);
        txtAnimalsWaiting = view.findViewById(R.id.txtAnimalsWaiting);
        txtActiveUsers = view.findViewById(R.id.txtActiveUsers);
        txtActiveListings = view.findViewById(R.id.txtActiveListings);
        txtTotalViews = view.findViewById(R.id.txtTotalViews);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        //pede as estatisticas a API
        if(AppSingleton.isConnectionInternet(getContext())) {
            AppSingleton.getInstance(getContext()).getStatsAPI(getContext(), this);
        }

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

    @Override
    public void onStatsSuccess() {
        Toast.makeText(getContext(), "Sucesso", Toast.LENGTH_SHORT).show();
        txtAnimalsAdopted.setText(String.valueOf(AppSingleton.getInstance(getContext()).getAnimalsAdopted()));
        txtAnimalsWaiting.setText(String.valueOf(AppSingleton.getInstance(getContext()).getAnimalsWaiting()));
        txtActiveUsers.setText(String.valueOf(AppSingleton.getInstance(getContext()).getActiveUsers()));
        txtActiveListings.setText(String.valueOf(AppSingleton.getInstance(getContext()).getActiveListings()));
        txtTotalViews.setText(String.valueOf(AppSingleton.getInstance(getContext()).getTotalViews()));
    }

    @Override
    public void onStatsError() {

    }
}