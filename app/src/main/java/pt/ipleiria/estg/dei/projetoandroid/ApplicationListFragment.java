package pt.ipleiria.estg.dei.projetoandroid;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView; // MUDANÇA IMPORTANTE
import java.util.ArrayList;

import pt.ipleiria.estg.dei.projetoandroid.adaptadores.ListaApplicationsAdaptor; // Importa o nosso adaptador corrigido
import pt.ipleiria.estg.dei.projetoandroid.listeners.ApplicationListener;
import pt.ipleiria.estg.dei.projetoandroid.modelo.AppSingleton;
import pt.ipleiria.estg.dei.projetoandroid.modelo.Application;

public class ApplicationListFragment extends Fragment implements ApplicationListener {

    private static final String ARG_TYPE = "type";
    private String type; // "sent" ou "received"

    private ListView lvApplications; // Mudámos de RecyclerView para ListView

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
        // Layout que contém a ListView
        View view = inflater.inflate(R.layout.fragment_application_list, container, false);

        // 1. Encontrar a ListView pelo ID correto do XML
        lvApplications = view.findViewById(R.id.lvApplications);
        // Nota: ListView não precisa de LayoutManager

        // 2. Chamar a API
        // O "this" funciona porque implementamos ApplicationListener
        AppSingleton.getInstance(getContext()).getApplicationsAPI(getContext(), type, this);

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