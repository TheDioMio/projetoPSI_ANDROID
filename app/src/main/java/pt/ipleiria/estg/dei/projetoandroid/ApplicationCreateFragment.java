package pt.ipleiria.estg.dei.projetoandroid;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import pt.ipleiria.estg.dei.projetoandroid.listeners.ApplicationsListener;
import pt.ipleiria.estg.dei.projetoandroid.modelo.AppSingleton;
import pt.ipleiria.estg.dei.projetoandroid.modelo.Application;

public class ApplicationCreateFragment extends Fragment {
    private EditText etCandidateName, etCandidateAge, etContact, etMotive;
    private Spinner spHomeType, spTimeAlone, spChildren, spBills, spFollowUp;
    private Button btnSubmit;

    private int candidateAge;
    private String candidateName, candidateContact, candidateMotive, homeType, timeAlone, children, bills, followup;

    public ApplicationCreateFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_application_create, container, false);

        etCandidateName = view.findViewById(R.id.etCandidateName);
        etCandidateAge = view.findViewById(R.id.etCandidateAge);
        etContact = view.findViewById(R.id.etContact);
        etMotive = view.findViewById(R.id.etMotive);
        spHomeType = view.findViewById(R.id.spHomeType);
        spTimeAlone = view.findViewById(R.id.spTimeAlone);
        spBills = view.findViewById(R.id.spBills);
        spChildren = view.findViewById(R.id.spChildren);
        spFollowUp = view.findViewById(R.id.spFollowUp);
        btnSubmit = view.findViewById(R.id.btnSubmit);

        ArrayList<String> listaSimNao = new ArrayList<>();
        listaSimNao.add("Sim");
        listaSimNao.add("Não");

        ArrayList<String> listaHabitacao = new ArrayList<>();
        listaHabitacao.add("Própria");
        listaHabitacao.add("Arrendada (Senhorio autoriza animais)");
        listaHabitacao.add("Arrendada (Senhorio não autoriza animais)");

        ArrayList<String> listaTempoSozinho = new ArrayList<>();
        listaTempoSozinho.add("Menos de 4 horas");
        listaTempoSozinho.add("Entre 4 a 8 Horas");
        listaTempoSozinho.add("Mais de 8 Horas");

        ArrayAdapter<String> adapterSimNao = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, listaSimNao);
        adapterSimNao.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        ArrayAdapter<String> adapterHabitacao = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, listaHabitacao);
        adapterHabitacao.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        ArrayAdapter<String> adapterTempo = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, listaTempoSozinho);
        adapterTempo.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spChildren.setAdapter(adapterSimNao);
        spBills.setAdapter(adapterSimNao);
        spFollowUp.setAdapter(adapterSimNao);
        spHomeType.setAdapter(adapterHabitacao);
        spTimeAlone.setAdapter(adapterTempo);

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //VALIDAR IDADE
                try {
                    String ageString = etCandidateAge.getText().toString();
                    if (ageString.isEmpty()) throw new NumberFormatException();
                    candidateAge = Integer.parseInt(ageString);
                } catch (NumberFormatException e) {
                    Toast.makeText(getContext(), "Por favor insira uma idade válida", Toast.LENGTH_SHORT).show();
                    return;
                }

                //LER TEXTOS
                candidateName = etCandidateName.getText().toString();
                candidateContact = etContact.getText().toString();
                candidateMotive = etMotive.getText().toString();

                if (candidateName.isEmpty() || candidateContact.isEmpty() || candidateMotive.isEmpty()) {
                    Toast.makeText(getContext(), "Preencha todos os campos obrigatórios", Toast.LENGTH_SHORT).show();
                    return;
                }

                //LER SPINNERS (DROPDOWNS)
                homeType = spHomeType.getSelectedItem().toString();
                timeAlone = spTimeAlone.getSelectedItem().toString();
                children = spChildren.getSelectedItem().toString();
                bills = spBills.getSelectedItem().toString();
                followup = spFollowUp.getSelectedItem().toString();

                //CRIAR O JSON
                JSONObject dataJson = new JSONObject();
                try {
                    dataJson.put("age", candidateAge);
                    dataJson.put("contact", candidateContact);
                    // O "motive" também vai aqui, mas o AppSingleton vai garantir que ele é enviado na raiz também
                    dataJson.put("motive", candidateMotive);

                    dataJson.put("home", homeType);
                    dataJson.put("timeAlone", timeAlone);
                    dataJson.put("children", children);
                    dataJson.put("bills", bills);
                    dataJson.put("followUp", followup);

                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(getContext(), "Erro ao criar dados da candidatura", Toast.LENGTH_SHORT).show();
                    return;
                }

                //OBTER ID DO ANIMAL
                int animalId = 0;
                if (getArguments() != null) {
                    animalId = getArguments().getInt("ID_ANIMAL", 0);
                }

                if (animalId == 0) {
                    Toast.makeText(getContext(), "Erro: Animal não identificado.", Toast.LENGTH_SHORT).show();
                    return;
                }

                SharedPreferences sharedPreferences = getContext().getSharedPreferences("DADOS_USER", Context.MODE_PRIVATE);
                int userId = sharedPreferences.getInt("USER_ID_INT", -1);

                if (userId == -1) {
                    Toast.makeText(getContext(), "Erro: Dados em falta em relação com o user.", Toast.LENGTH_SHORT).show();
                    return;
                }

                //CHAMAR A API
                AppSingleton.getInstance(getContext()).addApplicationAPI(
                        getContext(),
                        userId,
                        animalId,
                        candidateMotive,     // Motivo
                        dataJson.toString(), // JSON com os detalhes
                        new ApplicationsListener() {
                            @Override
                            public void onRefreshList(ArrayList<Application> listaCandidaturas) {
                                // SUCESSO!
                                Toast.makeText(getContext(), "Candidatura enviada com sucesso!", Toast.LENGTH_LONG).show();

                                // Fecha o formulário e volta atrás
                                if (getActivity() != null) {
                                    getActivity().onBackPressed();
                                }
                            }
                            
                            @Override public void onRefreshApplicationList(ArrayList<Application> list) {}
                            @Override public void onError(String error) {}
                            @Override public void onRefreshList(Object o) {}
                        }
                );
            }
        });
        return view;
    }
}