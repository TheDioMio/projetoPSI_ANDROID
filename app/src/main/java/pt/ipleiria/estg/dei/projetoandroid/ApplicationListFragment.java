package pt.ipleiria.estg.dei.projetoandroid;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import pt.ipleiria.estg.dei.projetoandroid.adaptadores.ListaApplicationsAdaptor;
import pt.ipleiria.estg.dei.projetoandroid.modelo.AppSingleton;
import pt.ipleiria.estg.dei.projetoandroid.modelo.Application;
import pt.ipleiria.estg.dei.projetoandroid.modelo.User;

public class ApplicationListFragment extends Fragment {
    private ListView lvApplications;
    private ArrayList<Application> userApplications;
    private ListaApplicationsAdaptor adaptador;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_application_list, container, false);

        lvApplications = view.findViewById(R.id.lvApplications);

       //Vai buscar o user logado e carrega as candidaturas dele.
        User userLogado = ((MenuMainActivity) getActivity()).getUserLogado();
        if (userLogado != null) {
            carregarMinhasCandidaturas(userLogado.getId());
        }
        return view;
    }

    private void carregarMinhasCandidaturas(int userId) {
        // Vai buscar TODAS as candidaturas, criadas por todos os users
        List<Application> todasCandidaturas = AppSingleton.getInstance(getContext()).getApplications();
        userApplications = new ArrayList<>();
        // Aqui isto filtra para as do user em questão
        for (Application app : todasCandidaturas) {
            if (app.getUser_id() == userId) {
                userApplications.add(app);
            }
        }
        if (userApplications.isEmpty()) {
            Toast.makeText(getContext(), R.string.txt_nenhuma_candidatura_feita, Toast.LENGTH_SHORT).show();
        }
        adaptador = new ListaApplicationsAdaptor(getContext(), userApplications);
        lvApplications.setAdapter(adaptador);
    }
}