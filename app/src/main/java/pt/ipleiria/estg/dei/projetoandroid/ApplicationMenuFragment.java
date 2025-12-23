package pt.ipleiria.estg.dei.projetoandroid;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

public class ApplicationMenuFragment extends Fragment {

    private Button btnReceived;
    private Button btnSent;

    public ApplicationMenuFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // 1. Inflar a View primeiro
        View view = inflater.inflate(R.layout.fragment_application_menu, container, false);

        // 2. Inicializar os botões usando a view inflada
        btnSent = view.findViewById(R.id.btnSent);
        btnReceived = view.findViewById(R.id.btnReceived);

        // 3. Configurar os cliques
        btnSent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navegar para a lista, indicando que queremos as "ENVIADAS"
                navigateToApplicationList("sent");
            }
        });

        btnReceived.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navegar para a lista, indicando que queremos as "RECEBIDAS"
                navigateToApplicationList("received");
            }
        });

        return view;
    }

    // Função auxiliar para trocar de fragmento
    private void navigateToApplicationList(String type) {
        Fragment listFragment = ApplicationListFragment.newInstance(type);

        // Verifica se a Activity existe
        if (getActivity() != null) {
            FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

            fragmentTransaction.replace(R.id.contentFragment, listFragment);
            fragmentTransaction.addToBackStack(null); // Permite voltar atrás com o botão "Back"
            fragmentTransaction.commit();
        }
    }
}