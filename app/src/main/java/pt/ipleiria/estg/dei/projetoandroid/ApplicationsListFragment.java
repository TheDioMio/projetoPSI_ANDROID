package pt.ipleiria.estg.dei.projetoandroid;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView; // MUDANÇA IMPORTANTE
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;

import pt.ipleiria.estg.dei.projetoandroid.adaptadores.ListaApplicationsAdaptor; // Importa o nosso adaptador corrigido
import pt.ipleiria.estg.dei.projetoandroid.listeners.ApplicationsListener;
import pt.ipleiria.estg.dei.projetoandroid.modelo.AppSingleton;
import pt.ipleiria.estg.dei.projetoandroid.modelo.Application;

public class ApplicationsListFragment extends Fragment implements ApplicationsListener {
    private static final String ARG_TYPE = "type";
    private String type; // "sent" ou "received", é isto que troca entre as listas
    private ListView lvApplications;

    private ArrayList<Application> applications;
    private ListaApplicationsAdaptor adaptor;

    private View rootView;

    public ApplicationsListFragment() { }

    public static ApplicationsListFragment newInstance(String type) {
        ApplicationsListFragment fragment = new ApplicationsListFragment();
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
        //Encontrar a ListView pelo ID correto do XML
        lvApplications = view.findViewById(R.id.lvApplications);
        //Iniciar a lista vazia
        applications = new ArrayList<>();
        //Iniciar o adaptador SÓ UMA VEZ AQUI
        adaptor = new ListaApplicationsAdaptor(getContext(), applications, type);
        lvApplications.setAdapter(adaptor);

        //Configurar Listener e chamar API
        AppSingleton.getInstance(getContext()).setApplicationsListener(this);
        AppSingleton.getInstance(getContext()).getApplicationsAPI(getContext(), type);

        lvApplications.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (!AppSingleton.getInstance(getContext()).isConnectionInternet(getContext())) {
                    Snackbar.make(lvApplications,
                                    R.string.txt_offline_indisponivel,
                                    Snackbar.LENGTH_INDEFINITE)
                            .setAction(R.string.txt_ok, vv -> {})
                            .show();
                    return;
                }
                //Ir buscar o item clicado: O adapter sabe qual é o objeto na posição X
                Application selected = (Application) parent.getItemAtPosition(position);

                //Preparar os dados para enviar
                Bundle bundle = new Bundle();
                bundle.putInt("application_id", selected.getId());
                bundle.putString("type_sent_received", type);

                //Criar o fragmento de detalhes e dar-lhe os argumentos
                ApplicationDetailsFragment detailsFragment = new ApplicationDetailsFragment();
                detailsFragment.setArguments(bundle);

                //Substituir o fragmento atual pelo de detalhes
                requireActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.contentFragment, detailsFragment)
                        .addToBackStack(null)
                        .commit();
            }
        });

        return view;
    }

    //Adicionar onResume para gerir o título da Toolbar
    @Override
    public void onResume() {
        super.onResume();
        AppCompatActivity act = (AppCompatActivity) requireActivity();
        if (act.getSupportActionBar() != null) {
            // Define o título consoante o tipo da lista
            if ("sent".equals(type)) {
                act.getSupportActionBar().setTitle("Candidaturas Enviadas");
            } else {
                act.getSupportActionBar().setTitle("Candidaturas Recebidas");
            }
            act.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    //Limpar o listener ao destruir a vista
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        AppSingleton.getInstance(getContext()).setApplicationsListener(null);
    }


    
    
    // --- MÉTODOS DO LISTENER ---
    @Override
    public void onRefreshList(ArrayList<Application> listaCandidaturas) {
        // Este é o método que o AppSingleton chama quando os dados chegam
        if (listaCandidaturas != null) {
            //Limpa e atualiza a lista
            applications.clear();
            applications.addAll(listaCandidaturas);
            
            //Avisa o adaptador que os dados mudaram
            adaptor.notifyDataSetChanged();
        }
    }

    @Override
    public void onRefreshApplicationList(ArrayList<Application> list) {
        onRefreshList(list);
    }

    @Override
    public void onError(String error) {
        Toast.makeText(getContext(), "Erro inesperado", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onRefreshList(Object o) {
    }

    @Override
    public void onApplicationsOffline(ArrayList<Application> cachedApplications) {
        // Mostra o aviso (Snackbar)
        if (rootView != null) {
            Snackbar.make(
                    rootView,
                    R.string.txt_sem_internet_a_mostrar_dados_guardados,
                    Snackbar.LENGTH_INDEFINITE
            ).setAction(R.string.txt_ok, v -> {}).show();
        }

        // VERIFICAÇÃO E ATUALIZAÇÃO
        if (cachedApplications != null && !cachedApplications.isEmpty()) {
            //Para ajudar debug
            System.out.println("DEBUG CACHE: A atualizar lista com " + cachedApplications.size() + " itens.");

            //Atualiza a lista local
            applications.clear();
            applications.addAll(cachedApplications);

            //Como o notifyDataSetChanged() não estava a atualizar a lista, tive que forçar...
            adaptor = new ListaApplicationsAdaptor(getContext(), applications, type);
            lvApplications.setAdapter(adaptor);

        } else {
            System.out.println("DEBUG FRAGMENT: A lista de cache veio vazia.");
        }
    }
}