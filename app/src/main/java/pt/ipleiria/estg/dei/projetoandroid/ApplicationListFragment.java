package pt.ipleiria.estg.dei.projetoandroid;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView; // MUDANÇA IMPORTANTE
import java.util.ArrayList;

import pt.ipleiria.estg.dei.projetoandroid.adaptadores.ListaApplicationsAdaptor; // Importa o nosso adaptador corrigido
import pt.ipleiria.estg.dei.projetoandroid.listeners.ApplicationListener;
import pt.ipleiria.estg.dei.projetoandroid.modelo.AppSingleton;
import pt.ipleiria.estg.dei.projetoandroid.modelo.Application;

public class ApplicationListFragment extends Fragment implements ApplicationListener {

    private static final String ARG_TYPE = "type";
    private String type; // "sent" ou "received", é isto que troca entre as listas

    private ListView lvApplications;

    public ApplicationListFragment() { }

    public static ApplicationListFragment newInstance(String type) {
        ApplicationListFragment fragment = new ApplicationListFragment();
        Bundle args = new Bundle();
        args.putString(ARG_TYPE, type);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            type = getArguments().getString(ARG_TYPE);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_application_list, container, false);

        // 1. Encontrar a ListView pelo ID correto do XML
        lvApplications = view.findViewById(R.id.lvApplications);

        // 2. Chamar a API
        AppSingleton.getInstance(getContext()).getApplicationsAPI(getContext(), type, this);

        lvApplications.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Passo A: Obter o objeto da linha clicada
                // O adapter sabe qual é o objeto na posição X
                Application selected = (Application) parent.getItemAtPosition(position);

                // Passo B: Preparar os dados para enviar (apenas o ID chega)
                Bundle bundle = new Bundle();
                bundle.putInt("application_id", selected.getId());

                // Passo C: Criar o fragmento de detalhes e dar-lhe os argumentos
                ApplicationDetailsFragment detailsFragment = new ApplicationDetailsFragment();
                detailsFragment.setArguments(bundle);

                // Passo D: Navegar (Substituir o fragmento atual pelo de detalhes)
                requireActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.contentFragment, detailsFragment)
                        .addToBackStack(null)
                        .commit();
            }
        });

        return view;
    }

    // --- MÉTODOS DO LISTENER ---

    @Override
    public void onRefreshList(ArrayList<Application> listaCandidaturas) {
        // Este é o método que o AppSingleton chama quando os dados chegam
        if (listaCandidaturas != null && getContext() != null) {
            // Criar o adaptador e ligá-lo à ListView
            ListaApplicationsAdaptor adapter = new ListaApplicationsAdaptor(getContext(), listaCandidaturas);
            lvApplications.setAdapter(adapter);
        }
    }

    // Implementar restantes métodos da interface para evitar erros (podem ficar vazios)
    @Override
    public void onRefreshApplicationList(ArrayList<Application> list) {
        // Se a tua interface usar este nome, redireciona para o de cima
        onRefreshList(list);
    }

    @Override
    public void onError(String error) {
        // Podes adicionar um Toast aqui se quiseres ver erros
    }

    @Override
    public void onRefreshList(Object o) {
        // Método genérico, não utilizado neste contexto
    }
}