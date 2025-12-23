package pt.ipleiria.estg.dei.projetoandroid;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import pt.ipleiria.estg.dei.projetoandroid.modelo.Animal;
import pt.ipleiria.estg.dei.projetoandroid.modelo.AppSingleton;
import pt.ipleiria.estg.dei.projetoandroid.modelo.Application;


public class ApplicationDetailsFragment extends Fragment {
    private TextView tvNomeCandidato, tvSubmissionDate, tvAnimalName, tvStatusBadge,
            tvCandidateName, tvCandidateAge, tvContact, tvHome, tvTimeAlone, tvChildren,
            tvBills, tvFollowUp,tvMotive;
    private Button btnApprove, btnReject;

    private Application application;

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
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
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
            application = AppSingleton.getInstance(getContext()).getApplication(applicationId);
            carregarDados(application);
        }

        return view;
    }



    //CARREGAR DADOS DA APPLICATION
    private void carregarDados(Application application) {
        if (application == null) return;

        //Todos os dados são carregados diretamente no layout
        tvNomeCandidato.setText(application.getCandidateName());
        tvSubmissionDate.setText(application.getCreatedAt());
        tvAnimalName.setText(application.getAnimalName());
        tvStatusBadge.setText(application.getStatusTexto());
        tvCandidateName.setText(application.getCandidateName());
        tvCandidateAge.setText(application.getAge() + "");
        tvContact.setText(application.getContact());
        tvHome.setText(application.getHome());
        tvTimeAlone.setText(application.getTimeAlone());
        tvChildren.setText(application.getChildren());
        tvBills.setText(application.getBills());
        tvFollowUp.setText(application.getFollowUp());
        tvMotive.setText(application.getMotive());
    }
}