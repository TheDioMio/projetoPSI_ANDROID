package pt.ipleiria.estg.dei.projetoandroid;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import pt.ipleiria.estg.dei.projetoandroid.listeners.ApplicationListener;
import pt.ipleiria.estg.dei.projetoandroid.modelo.Animal;
import pt.ipleiria.estg.dei.projetoandroid.modelo.AppSingleton;
import pt.ipleiria.estg.dei.projetoandroid.modelo.Application;


public class ApplicationDetailsFragment extends Fragment {
    private TextView tvNomeCandidato, tvSubmissionDate, tvAnimalName, tvStatusBadge,
            tvCandidateName, tvCandidateAge, tvContact, tvHome, tvTimeAlone, tvChildren,
            tvBills, tvFollowUp,tvMotive;
    private Button btnApprove, btnReject;

    private static final String TYPE_SENT = "sent";
    private static final String TYPE_RECEIVED = "received";

    private Application application;
    private ApplicationListener listener;

    private AppSingleton singleton = AppSingleton.getInstance(getContext());
    private String type_sent_received;

    public ApplicationDetailsFragment() {
        // Required empty public constructor
    }


    // TODO: Rename and change types and number of parameters
    public static ApplicationDetailsFragment newInstance(String param1, String param2) {
        ApplicationDetailsFragment fragment = new ApplicationDetailsFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_application_details, container, false);

        tvNomeCandidato = view.findViewById(R.id.tvNomeCandidato);
        tvSubmissionDate = view.findViewById(R.id.tvSubmissionDate);
        tvAnimalName = view.findViewById(R.id.tvAnimalName);
        tvStatusBadge = view.findViewById(R.id.tvStatusBadge);
        tvCandidateName = view.findViewById(R.id.tvCandidateName);
        tvCandidateAge = view.findViewById(R.id.tvCandidateAge);
        tvContact = view.findViewById(R.id.tvContact);
        tvHome = view.findViewById(R.id.tvHome);
        tvTimeAlone = view.findViewById(R.id.tvTimeAlone);
        tvChildren = view.findViewById(R.id.tvChildren);
        tvBills = view.findViewById(R.id.tvBills);
        tvFollowUp = view.findViewById(R.id.tvFollowUp);
        tvMotive = view.findViewById(R.id.tvMotive);
        btnReject = view.findViewById(R.id.btnReject);
        btnApprove = view.findViewById(R.id.btnApprove);


        //obtém a application passada pelo Bundle
        Bundle args = getArguments();
        if (args != null) {
            int applicationId = args.getInt("application_id");
            type_sent_received = args.getString("type_sent_received");
            application = singleton.getApplication(applicationId);
            carregarDados();
            configButtons(application.getStatus(), type_sent_received);
        }

        btnApprove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 1. Definir o novo estado no objeto local
                application.setStatus(Application.STATUS_APPROVED);

                // 2. Chamar a API em modo "Fire and Forget" ao passar NULL no Listener
                //ISTO PORQUE O SANTO DO YII2 MANDAVA SEMPRE ERRO 500, MAS NA API ESTAVA TUDO BEM
                //SABE-SE LÁ DEUS PORQUÊ... MAS ESTÁ FUNCIONAL.
                //O Android manda o pedido para o PHP gravar, mas IGNORA se o PHP responder com erro 500.
                singleton.editarApplicationAPI(application, getContext(), null);

                Toast.makeText(getContext(), "Candidatura Aprovada!", Toast.LENGTH_SHORT).show();

                // 4. Fechar o Fragmento Imediatamente
                if (getActivity() != null) {
                    getActivity().onBackPressed();
                }
            }
        });

        btnReject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                application.setStatus(Application.STATUS_REJECTED);

                // Passar null para ignorar erros de resposta
                singleton.editarApplicationAPI(application, getContext(), null);

                Toast.makeText(getContext(), "Candidatura Rejeitada", Toast.LENGTH_SHORT).show();

                if (getActivity() != null) {
                    getActivity().onBackPressed();
                }
            }
        });

        return view;
    }



    //CARREGAR DADOS DA APPLICATION
    private void carregarDados() {
        if (application == null) return;

        //Todos os dados são carregados diretamente no layout
        tvNomeCandidato.setText(getString(R.string.txt_candidatura_de) + " " + application.getCandidateName());
        tvSubmissionDate.setText(application.getCreatedAt());
        tvAnimalName.setText(application.getAnimalName());
        tvCandidateName.setText(application.getCandidateName());
        //Para isto não rebentar:
       tvCandidateAge.setText(String.valueOf(application.getAge()));
        tvContact.setText(application.getContact());
        tvHome.setText(application.getHome());
        tvTimeAlone.setText(application.getTimeAlone());
        tvMotive.setText(application.getMotive());

        //Para esconder os botões de aceitar/negar:

        //FUNÇÕES ESPECIAIS
        badgeYesNo(application.getChildren(), tvChildren);
        badgeYesNo(application.getBills(), tvBills);
        badgeYesNo(application.getFollowUp(), tvFollowUp);
        badgeStatus(application.getStatus(), tvStatusBadge);
    }

    private void badgeYesNo(String value, TextView textview) {
        textview.setText(value);
        if (value != null && value.equalsIgnoreCase("Sim")) {
            // SE FOR SIM -> VERDE (#4CAF50)
            textview.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#4CAF50")));
        } else {
            // SE FOR NÃO -> VERMELHO (#F44336)
            textview.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#F44336")));
        }
    }

    private void badgeStatus(String value, TextView textview)  {
        textview.setText(value);

        if(value != null){
            switch (value) {
                // --- ESTADOS PENDENTES (AMARELO) ---
                case Application.STATUS_SENT:
                case Application.STATUS_PENDING:
                    textview.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#FFC107")));
                break;
                // --- ESTADO APROVADO (VERDE) ---
                case Application.STATUS_APPROVED:
                    textview.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#4CAF50")));
                break;

                // --- ESTADO REJEITADO (VERMELHO) ---
                case Application.STATUS_REJECTED:
                    textview.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#F44336")));
                break;

                default:
                    textview.setBackgroundTintList(ColorStateList.valueOf(Color.GRAY));
                break;
            }
        }
    }

    private void configButtons(String status, String type) {
        if (type == null) {
            btnApprove.setVisibility(View.GONE);
            btnReject.setVisibility(View.GONE);
            return;
        }

        // Se for "sent" ou se o estado não for "Em Análise" ou "Enviado" (já foi decidida), escondemos os botões.
        boolean candidate = type.equalsIgnoreCase(TYPE_SENT);
        boolean alreadyDecided = !status.equalsIgnoreCase(Application.STATUS_PENDING)
                && !status.equalsIgnoreCase(Application.STATUS_SENT) ;
        if (candidate || alreadyDecided) {
            btnApprove.setVisibility(View.GONE);
            btnReject.setVisibility(View.GONE);
        } else {
            // Se recebi e está pendente, mostro os botões
            btnApprove.setVisibility(View.VISIBLE);
            btnReject.setVisibility(View.VISIBLE);

        }
    }
}