package pt.ipleiria.estg.dei.projetoandroid;

import android.app.AlertDialog;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

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
    private static final String ARG_RECEIVER_USERNAME = "receiver_username";
    private String receiverUsername;

    private static final int MODE_READ = 0;
    private static final int MODE_COMPOSE = 1;

    private int mode = MODE_COMPOSE;
    private int messageId;
    private int receiverId;
    private String defaultSubject;
    private static final String ARG_MESSAGE = "message";
    private static final String ARG_MY_ID = "my_id";
    private int myId = -1;

    // -------------------------------
    // VIEWS
    // -------------------------------
    private TextView tvSendMessageTo;
    private EditText etSubject;
    private EditText etMensagem;
    private Button btnSend;
    private ImageButton btnDelete;


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

    public static MessageFragment newInstanceForRead(Message message, int myId){
        MessageFragment fragment = new MessageFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_MODE, MODE_READ);
        args.putSerializable(ARG_MESSAGE, message);
        args.putInt(ARG_MY_ID, myId);
        fragment.setArguments(args);
        return fragment;
    }

    public static MessageFragment newInstanceForCompose (int receiverId, @Nullable String receiverUsername, @Nullable String defaultSubject) {
        MessageFragment fragment = new MessageFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_MODE, MODE_COMPOSE);
        args.putInt(ARG_RECEIVER_ID, receiverId);
        args.putString(ARG_RECEIVER_USERNAME, receiverUsername);
        args.putString(ARG_DEFAULT_SUBJECT, defaultSubject);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mode = getArguments().getInt(ARG_MODE, MODE_COMPOSE);
            myId = getArguments().getInt(ARG_MY_ID, -1);

            if (mode == MODE_READ) {
                messageId = getArguments().getInt(ARG_MESSAGE_ID);
            } else {
                receiverId = getArguments().getInt(ARG_RECEIVER_ID);
                receiverUsername = getArguments().getString(ARG_RECEIVER_USERNAME);
                defaultSubject = getArguments().getString(ARG_DEFAULT_SUBJECT);
            }
        }

        System.out.println("DEBUG onCreate myId=" + myId);
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
        btnDelete = view.findViewById(R.id.btnDelete);

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
        Message msg = (Message) getArguments().getSerializable(ARG_MESSAGE);
        if (msg == null) return;

        // preencher dados
        etSubject.setText(msg.getSubject());
        etMensagem.setText(msg.getText());

        boolean souSender = (msg.getSender_user_id() == myId);

        // Cabeçalho correto: DE vs PARA
        if (souSender) {
            // Eu enviei -> mostrar PARA (receiver)
            String receiverName = msg.getReceiver_username();
            if (receiverName != null && !receiverName.trim().isEmpty()) {
                tvSendMessageTo.setText("Mensagem para: " + receiverName);
            } else {
                tvSendMessageTo.setText("Mensagem para utilizador #" + msg.getReciver_user_id());
            }
        } else {
            // Eu recebi -> mostrar DE (sender)
            String senderName = msg.getSender_username();
            if (senderName != null && !senderName.trim().isEmpty()) {
                tvSendMessageTo.setText("Mensagem de: " + senderName);
            } else {
                tvSendMessageTo.setText("Mensagem de utilizador #" + msg.getSender_user_id());
            }
        }

        // tornar os campos só de leitura
        makeReadOnly(etSubject);
        makeReadOnly(etMensagem);

        if (btnDelete != null) {
            btnDelete.setVisibility(souSender ? View.VISIBLE : View.GONE);
        }

        System.out.println("DEBUG myId=" + myId
                + " msgId=" + msg.getId()
                + " sender=" + msg.getSender_user_id()
                + " receiver=" + msg.getReciver_user_id());

        if (souSender) {
            btnSend.setText("Editar");
            btnSend.setOnClickListener(v -> enableEditMode(msg));
        } else {
            btnSend.setText("Responder");
            btnSend.setOnClickListener(v -> {

                MessageFragment frag = MessageFragment.newInstanceForCompose(
                        msg.getSender_user_id(),
                        msg.getReceiver_username(),
                        "Re: " + msg.getSubject()
                );

                requireActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.contentFragment, frag)
                        .addToBackStack(null)
                        .commit();
            });
        }

        if (souSender && btnDelete != null) {
            btnDelete.setOnClickListener(v -> {

                new AlertDialog.Builder(requireContext())
                        .setTitle("Apagar mensagem")
                        .setMessage("Tens a certeza que queres apagar esta mensagem?")
                        .setPositiveButton("Apagar", (dialog, which) -> {

                            AppSingleton.getInstance(getContext()).apagarMensagemAPI(
                                    msg.getId(),
                                    getContext(),
                                    new AppSingleton.SendMessageListener() {
                                        @Override
                                        public void onSuccess() {
                                            requireActivity()
                                                    .getSupportFragmentManager()
                                                    .popBackStack();
                                        }

                                        @Override
                                        public void onError(String erro) {
                                            Toast.makeText(getContext(), erro, Toast.LENGTH_SHORT).show();
                                        }
                                    }
                            );
                        })
                        .setNegativeButton("Cancelar", null)
                        .show();
            });
        }
    }

    // -------------------------------
    // MODO: ESCREVER / RESPONDER
    // -------------------------------
    private void setupComposeMode() {

        btnDelete.setVisibility(View.GONE);

        Message msg = (Message) getArguments().getSerializable(ARG_MESSAGE);

        int targetUserId = receiverId;     // por defeito usa o receiverId vindo nos args
        String targetUsername = receiverUsername;      // tenta obter username se existir

        // Se o compose foi aberto a partir de uma mensagem (reply), decide quem é o "outro lado"
        if (msg != null) {
            boolean euSouSender = (msg.getSender_user_id() == myId);

            if (euSouSender) {
                // eu enviei a msg original -> responder vai para o receiver original
                targetUserId = msg.getReciver_user_id();
                targetUsername = msg.getReceiver_username();
            } else {
                // eu recebi a msg original -> responder vai para o sender original
                targetUserId = msg.getSender_user_id();
                targetUsername = msg.getSender_username();
            }

            // IMPORTANTÍSSIMO: garantir que o envio vai para o user certo
            receiverId = targetUserId;
        }

        // Cabeçalho do compose: SEMPRE "Mensagem para:"
        if (targetUsername != null && !targetUsername.trim().isEmpty()) {
            tvSendMessageTo.setText("Mensagem para: " + targetUsername);
        } else {
            // fallback: tentar ir ao cache
            User u = AppSingleton.getInstance(getContext()).getUser(targetUserId);
            if (u != null) {
                // se tiveres getUsername(), usa-o em vez de getName()
                tvSendMessageTo.setText("Mensagem para: " + u.getName());
            } else {
                tvSendMessageTo.setText("Mensagem para utilizador #" + targetUserId);
            }
        }

        // Assunto por defeito (ex: "Re: ...")
        if (defaultSubject != null && !defaultSubject.trim().isEmpty()) {
            etSubject.setText(defaultSubject);
        }

        btnSend.setText(getString(R.string.txt_send));

        btnSend.setOnClickListener(v -> {

            String subject = etSubject.getText().toString().trim();
            String text = etMensagem.getText().toString().trim();

            if (subject.isEmpty()) {
                etSubject.setError("Assunto obrigatório");
                return;
            }

            if (text.isEmpty()) {
                etMensagem.setError("Mensagem obrigatória");
                return;
            }

            AppSingleton.getInstance(getContext()).enviarMensagemAPI(
                    receiverId,
                    subject,
                    text,
                    getContext(),
                    new AppSingleton.SendMessageListener() {
                        @Override
                        public void onSuccess() {
                            requireActivity().getSupportFragmentManager().popBackStack();
                        }

                        @Override
                        public void onError(String erro) {
                            // Toast.makeText(getContext(), erro, Toast.LENGTH_SHORT).show();
                        }
                    }
            );
        });
    }

    private void enableEditMode(Message msg) {
        // tornar editável
        etSubject.setFocusableInTouchMode(true);
        etSubject.setClickable(true);
        etSubject.setCursorVisible(true);
        etSubject.setKeyListener(new EditText(getContext()).getKeyListener());

        etMensagem.setFocusableInTouchMode(true);
        etMensagem.setClickable(true);
        etMensagem.setCursorVisible(true);
        etMensagem.setKeyListener(new EditText(getContext()).getKeyListener());

        btnSend.setText("Guardar");
        btnSend.setOnClickListener(v -> saveEdit(msg));
    }

    private void saveEdit(Message msg) {
        String subject = etSubject.getText().toString().trim();
        String text = etMensagem.getText().toString().trim();

        if (subject.isEmpty()) {
            etSubject.setError("Assunto obrigatório");
            return;
        }
        if (text.isEmpty()) {
            etMensagem.setError("Mensagem obrigatória");
            return;
        }

        AppSingleton.getInstance(getContext()).editarMensagemAPI(
                msg.getId(),
                subject,
                text,
                getContext(),
                new AppSingleton.SendMessageListener() {
                    @Override
                    public void onSuccess() {
                        // volta atrás (ou podes voltar a readOnly)
                        requireActivity().getSupportFragmentManager().popBackStack();
                    }

                    @Override
                    public void onError(String erro) {
                        // Toast opcional
                    }
                }
        );
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