package pt.ipleiria.estg.dei.projetoandroid.adaptadores;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import pt.ipleiria.estg.dei.projetoandroid.R;
import pt.ipleiria.estg.dei.projetoandroid.modelo.Animal;
import pt.ipleiria.estg.dei.projetoandroid.modelo.AnimalFile;
import pt.ipleiria.estg.dei.projetoandroid.modelo.AppSingleton;
import pt.ipleiria.estg.dei.projetoandroid.modelo.Application;

public class ListaApplicationsAdaptor extends BaseAdapter {
    private Context context;
    private LayoutInflater inflater;
    private ArrayList<Application> candidaturas;

    public ListaApplicationsAdaptor(Context context, ArrayList<Application> candidaturas) {
        this.context = context;
        this.candidaturas = candidaturas;
    }

    @Override
    public int getCount() {
        return candidaturas.size();
    }

    @Override
    public Object getItem(int position) {
        return candidaturas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return candidaturas.get(position).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (inflater == null) {
            inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_card_candidaturas, null);
        }

        // 1. Vincular os elementos visuais
        ImageView imgAnimal = convertView.findViewById(R.id.imgFotoAnimalCandidatura);
        TextView tvNome = convertView.findViewById(R.id.tvNomeAnimalCandidatura);
        TextView tvDescricao = convertView.findViewById(R.id.tvDescricaoCandidatura);
        TextView tvStatusCandidatura = convertView.findViewById(R.id.tvStatusCandidatura);

        // 2. Obter a candidatura atual
        Application candidaturaAtual = candidaturas.get(position);

        // 3. Preencher a descrição (motivo), status e nome
        tvDescricao.setText(candidaturaAtual.getMotive());
        badgeStatus(candidaturaAtual.getStatus(), tvStatusCandidatura);
        tvNome.setText(candidaturaAtual.getAnimalName());

        //Dar load à imagem: tornado fácil porque o path já vem da API (NÃO DEPENDENTE DO ANIMAL)
        String imgPath = candidaturaAtual.getAnimalImage();
        animalImgLoad(imgPath, imgAnimal);

        return convertView;
    }

    private void animalImgLoad(String imgPath, ImageView imgAnimal) {
        if (imgPath != null && !imgPath.isEmpty()) {
            String imageUrl;
            imageUrl = AppSingleton.FRONTEND_BASE_URL + imgPath;

            Glide.with(context)
                    .load(imageUrl)
                    .placeholder(R.mipmap.default_avatar)
                    .error(R.mipmap.default_avatar)
                    .into(imgAnimal);

        } else {
            // Se a string da imagem for nula ou vazia, img default
            imgAnimal.setImageResource(R.mipmap.default_avatar);
        }
    }

    private void badgeStatus(String value, TextView textview) {
        textview.setText(value);

        if(value != null){
            switch (value) {
                // --- ESTADOS PENDENTES (AMARELO) ---
                case Application.STATUS_SENT:
                case Application.STATUS_PENDING:
                    textview.setTextColor(ColorStateList.valueOf(Color.parseColor("#FFC107")));
                    break;
                // --- ESTADO APROVADO (VERDE) ---
                case Application.STATUS_APPROVED:
                    textview.setTextColor(ColorStateList.valueOf(Color.parseColor("#4CAF50")));
                    break;

                // --- ESTADO REJEITADO (VERMELHO) ---
                case Application.STATUS_REJECTED:
                    textview.setTextColor(ColorStateList.valueOf(Color.parseColor("#F44336")));
                    break;

                default:
                    textview.setTextColor(ColorStateList.valueOf(Color.GRAY));
                    break;
            }
        }
    }
}
