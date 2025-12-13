package pt.ipleiria.estg.dei.projetoandroid;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import pt.ipleiria.estg.dei.projetoandroid.modelo.AppSingleton;
import pt.ipleiria.estg.dei.projetoandroid.modelo.Message;
import pt.ipleiria.estg.dei.projetoandroid.modelo.User;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MessageFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MessageFragment extends Fragment {

    // -------------------------------
    // ARGUMENTOS (MODO)
    // -------------------------------
    private static final String ARG_MODE = "mode";
    private static final String ARG_MESSAGE_ID = "message_id";
    private static final String ARG_RECEIVER_ID = "receiver_id";
    private static final String ARG_DEFAULT_SUBJECT = "default_subject";

    private static final int MODE_READ = 0;
    private static final int MODE_COMPOSE = 1;

    private int mode = MODE_COMPOSE;
    private int messageId;
    private int receiverId;
    private String defaultSubject;

    // -------------------------------
    // VIEWS
    // -------------------------------
    private TextView tvSendMessageTo;
    private EditText etSubject;
    private EditText etMensagem;
    private Button btnSend;


    public MessageFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MessageFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MessageFragment newInstance(String param1, String param2) {
        MessageFragment fragment = new MessageFragment();
        Bundle args = new Bundle();
        // args.putString(ARG_PARAM1, param1);
        // args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public static MessageFragment newInstanceForRead (int messageId){
        MessageFragment fragment = new MessageFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_MODE, MODE_READ);
        args.putInt(ARG_MESSAGE_ID, messageId);
        fragment.setArguments(args);
        return fragment;
    }

    public static MessageFragment newInstanceForCompose (int receiverId, @Nullable String defaultSubject) {
        MessageFragment fragment = new MessageFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_MODE, MODE_COMPOSE);
        args.putInt(ARG_RECEIVER_ID, receiverId);
        args.putString(ARG_DEFAULT_SUBJECT, defaultSubject);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mode = getArguments().getInt(ARG_MODE, MODE_COMPOSE);

            if (mode == MODE_READ) {
                messageId = getArguments().getInt(ARG_MESSAGE_ID);
            } else {
                receiverId = getArguments().getInt(ARG_RECEIVER_ID);
                defaultSubject = getArguments().getString(ARG_DEFAULT_SUBJECT);
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_message, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        tvSendMessageTo = view.findViewById(R.id.tv_sendMessageTo);
        etSubject = view.findViewById(R.id.etSubject);
        etMensagem = view.findViewById(R.id.etMensagem);
        btnSend = view.findViewById(R.id.btnSend);

        if (mode == MODE_READ) {
            setupReadMode();
        } else {
            setupComposeMode();
        }
    }


    // -------------------------------
    // MODO: LER MENSAGEM
    // -------------------------------
    private void setupReadMode() {
        Message msg = AppSingleton.getInstance().getMessage(messageId);
        if (msg == null) return;

        // preencher dados
        etSubject.setText(msg.getSubject());
        etMensagem.setText(msg.getText());

        User sender = AppSingleton.getInstance().getUser(msg.getSender_user_id());
        if (sender != null) {
            tvSendMessageTo.setText("Mensagem de: " + sender.getName());
        } else {
            tvSendMessageTo.setText("Mensagem de utilizador #" + msg.getSender_user_id());
        }

        // tornar os campos só de leitura
        makeReadOnly(etSubject);
        makeReadOnly(etMensagem);

        // botão passa a "Responder"
        btnSend.setText("Responder");

        btnSend.setOnClickListener(v -> {

            MessageFragment frag = MessageFragment.newInstanceForCompose(
                    msg.getSender_user_id(),
                    "Re: " + msg.getSubject()
            );

            requireActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.contentFragment, frag)
                    .addToBackStack(null)
                    .commit();
        });
    }

    // -------------------------------
    // MODO: ESCREVER / RESPONDER
    // -------------------------------
    private void setupComposeMode() {

        User receiver = AppSingleton.getInstance().getUser(receiverId);
        if (receiver != null) {
            tvSendMessageTo.setText("Enviar mensagem para: " + receiver.getName());
        } else {
            tvSendMessageTo.setText("Enviar mensagem para utilizador #" + receiverId);
        }

        if (defaultSubject != null) {
            etSubject.setText(defaultSubject);
        }

        btnSend.setText(getString(R.string.txt_send));

        btnSend.setOnClickListener(v -> {

            String subject = etSubject.getText().toString().trim();
            String text = etMensagem.getText().toString().trim();

            System.out.println("---- NOVA MENSAGEM ----");
            System.out.println("PARA: " + receiverId);
            System.out.println("ASSUNTO: " + subject);
            System.out.println("TEXTO:\n" + text);
            System.out.println("------------------------");

            // aqui apenas simula envio ─ no futuro: API
        });
    }



    public void makeReadOnly (EditText editText) {

        if (editText == null) return;
        editText.setFocusable(false);
        editText.setFocusableInTouchMode(false);
        editText.setClickable(false);
        editText.setLongClickable(false);
        editText.setCursorVisible(false);
        editText.setKeyListener(null);
        editText.setBackground(null);
    }
}