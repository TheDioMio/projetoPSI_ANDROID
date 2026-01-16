package pt.ipleiria.estg.dei.projetoandroid;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;

import pt.ipleiria.estg.dei.projetoandroid.adaptadores.ListaMessagesAdaptador;
import pt.ipleiria.estg.dei.projetoandroid.modelo.Animal;
import pt.ipleiria.estg.dei.projetoandroid.modelo.AppSingleton;
import pt.ipleiria.estg.dei.projetoandroid.modelo.Message;
import pt.ipleiria.estg.dei.projetoandroid.modelo.User;
import android.content.Context;

import com.google.android.material.snackbar.Snackbar;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MessageListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MessageListFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private ArrayList<Message> todas = new ArrayList<>();
    private ArrayList<Message> recebidas = new ArrayList<>();
    private ArrayList<Message> enviadas = new ArrayList<>();
    private ListaMessagesAdaptador adaptador;
    private int myId = -1;

    public MessageListFragment() {
        // Required empty public constructor
    }



    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MessageListFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MessageListFragment newInstance(String param1, String param2) {
        MessageListFragment fragment = new MessageListFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_message_list, container, false);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ListView lvMessages = view.findViewById(R.id.lvMessages);

        SharedPreferences sp = requireContext().getSharedPreferences("DADOS_USER", Context.MODE_PRIVATE);
        myId = sp.getInt("USER_ID_INT", -1);

        Button btnRecebidas = view.findViewById(R.id.btnRecebidas);
        Button btnEnviadas  = view.findViewById(R.id.btnEnviadas);

        btnRecebidas.setOnClickListener(v -> mostrarRecebidas());
        btnEnviadas.setOnClickListener(v -> mostrarEnviadas());


        adaptador = new ListaMessagesAdaptador(getContext(), new ArrayList<>());
        lvMessages.setAdapter(adaptador);

        lvMessages.setOnItemClickListener((parent, v, position, id) -> {

            Message selecionada = (Message) parent.getItemAtPosition(position);

            MessageFragment frag = MessageFragment.newInstanceForRead(selecionada, myId);

            System.out.println("CLICK myId=" + myId); // debub

            requireActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.contentFragment, frag)
                    .addToBackStack(null)
                    .commit();
        });

        AppSingleton singleton = AppSingleton.getInstance(getContext());

        singleton.setMessageListener(new pt.ipleiria.estg.dei.projetoandroid.listeners.MessagesListener() {
            @Override
            public void onRefreshListaMessages(ArrayList lista) {
                atualizarListas(lista);
                mostrarRecebidas();
            }

            @Override
            public void onErro(String erro) {
                // opcional: Toast
                // Toast.makeText(getContext(), erro, Toast.LENGTH_SHORT).show();
            }
        });

        singleton.getAllMessagesAPI(getContext());



    }

    private void atualizarListas(ArrayList lista) {
        todas.clear();
        recebidas.clear();
        enviadas.clear();

        for (Object o : lista) {
            Message m = (Message) o;
            todas.add(m);

            if (m.getReciver_user_id() == myId) {
                recebidas.add(m);
            }
            if (m.getSender_user_id() == myId) {
                enviadas.add(m);
            }
        }
    }

    private void mostrarRecebidas() {
        adaptador.atualizar(recebidas);
    }

    private void mostrarEnviadas() {
        adaptador.atualizar(enviadas);
    }
    

}