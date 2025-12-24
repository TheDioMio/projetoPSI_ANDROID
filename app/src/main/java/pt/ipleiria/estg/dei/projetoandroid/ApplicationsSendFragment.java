package pt.ipleiria.estg.dei.projetoandroid;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;

import org.json.JSONException;
import org.json.JSONObject;

import pt.ipleiria.estg.dei.projetoandroid.listeners.ApplicationListener;
import pt.ipleiria.estg.dei.projetoandroid.modelo.Animal;
import pt.ipleiria.estg.dei.projetoandroid.modelo.AppSingleton;
import pt.ipleiria.estg.dei.projetoandroid.modelo.Application;

import java.util.ArrayList;

public class ApplicationsSendFragment extends Fragment implements ApplicationListener {

    private Button btnSubmit;
    private EditText etDescription, etNomeCompleto, etIdade, etContacto;
    private TextView tvNomeCard, tvRacaCard;
    private ImageView imgAnimalCard;
    private AppSingleton singleton;
    private Animal animal;
    private Spinner spTipoHabitacao, spHorasAnimal;
    private RadioGroup rgCriancas, rgCustos;

    public ApplicationsSendFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_send_applications, container, false);

        // 1. Inicializar Views
        btnSubmit = view.findViewById(R.id.btnSubmit);
        etDescription = view.findViewById(R.id.etDescription); // "Motivo"
        tvNomeCard = view.findViewById(R.id.tvNomeCard);
        tvRacaCard = view.findViewById(R.id.tvRacaCard);
        imgAnimalCard = view.findViewById(R.id.imgAnimalCard);

        // Novos campos do formulário
        etNomeCompleto = view.findViewById(R.id.etNomeCompleto);
        etIdade = view.findViewById(R.id.etIdade);
        etContacto = view.findViewById(R.id.etContacto);
        spTipoHabitacao = view.findViewById(R.id.spTipoHabitacao);
        spHorasAnimal = view.findViewById(R.id.spHorasAnimal);
        rgCriancas = view.findViewById(R.id.rgCriancas);
        rgCustos = view.findViewById(R.id.rgCustos);

        singleton = AppSingleton.getInstance(getContext());

        // 2. Receber ID do Animal
        Bundle args = getArguments();
        if (args != null) {
            int idRecebido = args.getInt("ID_ANIMAL");
            animal = singleton.getAnimalBD(idRecebido);

            if (animal != null) {
                carregarAnimal(animal);
            }
        }

        // 3. Botão Enviar
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                enviarCandidatura();
            }
        });

        return view;
    }

    private void enviarCandidatura() {
        // A. Validar Campos Obrigatórios
        if (!validarCampos()) return;

        // B. Obter ID do User (via SharedPreferences é mais seguro que via Activity cast)
        SharedPreferences sp = requireContext().getSharedPreferences("DADOS_USER", Context.MODE_PRIVATE);
        int userId = Integer.parseInt(sp.getString("IDUSER", "0"));

        if (userId == 0) {
            Toast.makeText(getContext(), "Erro: Utilizador não identificado. Faça login novamente.", Toast.LENGTH_SHORT).show();
            return;
        }

        // C. Construir o JSON com os dados extras (Data column)
        JSONObject jsonData = new JSONObject();
        try {
            jsonData.put("name", etNomeCompleto.getText().toString());
            jsonData.put("age", Integer.parseInt(etIdade.getText().toString()));
            jsonData.put("contact", etContacto.getText().toString());
            jsonData.put("motive", etDescription.getText().toString());

            // Spinners (Guardamos o índice selecionado: 0, 1, 2...)
            jsonData.put("home", spTipoHabitacao.getSelectedItemPosition());
            jsonData.put("timeAlone", spHorasAnimal.getSelectedItemPosition());

            // RadioGroups (Sim/Não ou opções)
            jsonData.put("children", rgCriancas.getCheckedRadioButtonId() == R.id.rbCriancasSim ? 1 : 0); // Ajusta o ID do teu RadioButton
            jsonData.put("bills", rgCustos.getCheckedRadioButtonId() == R.id.rbCustosSim ? 1 : 0);       // Ajusta o ID do teu RadioButton
            jsonData.put("followUp", 1); // Exemplo: Aceita visitas? Podes criar um RadioGroup para isto também

        } catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(getContext(), "Erro ao criar dados da candidatura", Toast.LENGTH_SHORT).show();
            return;
        }

        // D. Enviar para a API
        // Nota: O parametro 'description' na tabela application pode ser o motivo ou um resumo.
        // Aqui enviamos o motivo como descrição principal.
        String description = etDescription.getText().toString();
        String dataString = jsonData.toString();

        // Chama o método da API (que vamos criar a seguir no Singleton)
        singleton.addApplicationAPI(getContext(), userId, animal.getId(), description, dataString, this);
    }

    private boolean validarCampos() {
        if (etNomeCompleto.getText().toString().isEmpty()) { etNomeCompleto.setError("Obrigatório"); return false; }
        if (etIdade.getText().toString().isEmpty()) { etIdade.setError("Obrigatório"); return false; }
        if (etContacto.getText().toString().isEmpty()) { etContacto.setError("Obrigatório"); return false; }
        if (etDescription.getText().toString().isEmpty()) { etDescription.setError("Obrigatório"); return false; }

        if (rgCriancas.getCheckedRadioButtonId() == -1) {
            Toast.makeText(getContext(), "Selecione se tem crianças", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (rgCustos.getCheckedRadioButtonId() == -1) {
            Toast.makeText(getContext(), "Selecione a opção de custos", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private void carregarAnimal(Animal animal) {
        tvNomeCard.setText(animal.getName());
        tvRacaCard.setText(animal.getBreed());

        // Carregar Imagem (Assumindo que Animal tem getPhotos() ou getImgUrl())
        // Se a tua lista de imagens for uma ArrayList<File> ou Strings:
        String imgUrl = null;
//        if (animal.getPhotos() != null && !animal.getPhotos().isEmpty()) {
//            // Ajusta isto conforme o teu modelo Animal (getPath, getUrl, ou toString)
//            imgUrl = animal.getPhotos().get(0).getPath();
//        }

        carregarImagem(imgUrl, imgAnimalCard);
    }

    private void carregarImagem(String img, ImageView destino) {
        if (img == null || img.isEmpty()) {
            destino.setImageResource(R.mipmap.placeholder); // Garante que tens esta imagem
            return;
        }

        String urlFinal = img;
        if (!img.startsWith("http")) {
            // Se vier só o caminho relativo, junta o URL base do servidor
            urlFinal = singleton.FRONTEND_BASE_URL + img; // Ou onde tens as imagens guardadas
        }

        Glide.with(requireContext())
                .load(urlFinal)
                .placeholder(R.mipmap.placeholder)
                .error(R.mipmap.placeholder)
                .centerCrop()
                .into(destino);
    }

    @Override
    public void onRefreshApplicationList(ArrayList<Application> list) {

    }

    @Override
    public void onError(String error) {

    }

    @Override
    public void onRefreshList(Object o) {

    }

    @Override
    public void onRefreshList(ArrayList<Application> listaCandidaturas) {
        // Callback de sucesso da API
        Toast.makeText(getContext(), "Candidatura enviada com sucesso!", Toast.LENGTH_LONG).show();
        getParentFragmentManager().popBackStack(); // Volta para trás
    }
}