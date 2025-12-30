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
    private String candidateName, candidateContact, candidateMotive;
    // Variáveis auxiliares para os spinners
    private String homeTypeStr, timeAloneStr, childrenStr, billsStr, followupStr;

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

        //Configurar as Listas dos Spinners
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

        //Configurar os Adaptadores ---
        ArrayAdapter<String> adapterSimNao = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, listaSimNao);
        adapterSimNao.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        ArrayAdapter<String> adapterHabitacao = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, listaHabitacao);
        adapterHabitacao.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        ArrayAdapter<String> adapterTempo = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, listaTempoSozinho);
        adapterTempo.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        //Associar Adaptadores aos Spinners ---
        spChildren.setAdapter(adapterSimNao);
        spBills.setAdapter(adapterSimNao);
        spFollowUp.setAdapter(adapterSimNao);
        spHomeType.setAdapter(adapterHabitacao);
        spTimeAlone.setAdapter(adapterTempo);

        //Botão Enviar ---
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Validar Idade (Converter String para Int)
                try {
                    String ageString = etCandidateAge.getText().toString();
                    if (ageString.isEmpty()) throw new NumberFormatException();
                    candidateAge = Integer.parseInt(ageString);
                } catch (NumberFormatException e) {
                    Toast.makeText(getContext(), "Por favor insira uma idade válida", Toast.LENGTH_SHORT).show();
                    return;
                }

                //Ler campos obrigatórios
                candidateName = etCandidateName.getText().toString();
                candidateContact = etContact.getText().toString();
                candidateMotive = etMotive.getText().toString();

                if (candidateName.isEmpty() || candidateContact.isEmpty() || candidateMotive.isEmpty()) {
                    Toast.makeText(getContext(), "Preencha todos os campos obrigatórios", Toast.LENGTH_SHORT).show();
                    return;
                }

                //Ler o valor selecionado nos spinners
                homeTypeStr = spHomeType.getSelectedItem().toString();
                timeAloneStr = spTimeAlone.getSelectedItem().toString();
                childrenStr = spChildren.getSelectedItem().toString();
                billsStr = spBills.getSelectedItem().toString();
                followupStr = spFollowUp.getSelectedItem().toString();

                //Obter IDs
                int animalId = 0;
                if (getArguments() != null) {
                    animalId = getArguments().getInt("ID_ANIMAL", 0);
                }

                SharedPreferences sharedPreferences = getContext().getSharedPreferences("DADOS_USER", Context.MODE_PRIVATE);
                int userId = sharedPreferences.getInt("USER_ID_INT", -1);

                if (animalId == 0 || userId == -1) {
                    Toast.makeText(getContext(), "Erro: Animal ou Utilizador não identificado.", Toast.LENGTH_SHORT).show();
                    return;
                }

                //Criar o JSON com as CONVERSÕES para Inteiros
                JSONObject dataJson = new JSONObject();
                try {
                    // Campos de Texto/Numéricos diretos
                    dataJson.put("age", candidateAge);
                    dataJson.put("name", candidateName);
                    dataJson.put("contact", candidateContact);
                    dataJson.put("motive", candidateMotive);

                    // Campos convertidos (String -> Int)
                    //Enviamos 0, 1, 2 em vez de "Sim", "Não"
                    dataJson.put("home", converterHabitacao(homeTypeStr));
                    dataJson.put("timeAlone", converterTempo(timeAloneStr));
                    dataJson.put("children", converterSimNao(childrenStr));
                    dataJson.put("bills", converterSimNao(billsStr));
                    dataJson.put("followUp", converterSimNao(followupStr));

                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(getContext(), "Erro ao criar JSON", Toast.LENGTH_SHORT).show();
                    return;
                }

                //Enviar para a API
                AppSingleton.getInstance(getContext()).addApplicationAPI(
                        getContext(),
                        userId,
                        animalId,
                        candidateMotive,
                        dataJson.toString(), //Envia o JSON com números
                        new ApplicationsListener() {
                            @Override
                            public void onRefreshList(ArrayList<Application> listaCandidaturas) {
                                Toast.makeText(getContext(), "Candidatura enviada com sucesso!", Toast.LENGTH_LONG).show();
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

    // =========================================================================
    // FUNÇÕES AUXILIARES DE CONVERSÃO (Igualam o Android ao PHP)
    // =========================================================================

    // Converte: Sim -> 1, Não -> 0
    private int converterSimNao(String valor) {
        if (valor != null && (valor.equalsIgnoreCase("Sim") || valor.equalsIgnoreCase("Yes"))) {
            return 1;
        }
        return 0;
    }

    // Converte Habitação baseada no índice ou texto
    private int converterHabitacao(String valor) {
        if (valor == null) return 1;
        if (valor.startsWith("Própria")) return 1;
        if (valor.contains("autoriza") && !valor.contains("não")) return 2; // Arrendada com animais
        if (valor.contains("não autoriza")) return 3; // Arrendada sem animais
        return 1; // Default
    }

    // Converte Tempo Sozinho baseada nos índices do array do PHP (0, 1, 2)
    private int converterTempo(String valor) {
        if (valor == null) return 0;
        if (valor.contains("Menos")) return 0; // ID 0 no PHP
        if (valor.contains("Entre")) return 1; // ID 1 no PHP
        return 2;                              // ID 2 no PHP ("Mais de 8")
    }
}