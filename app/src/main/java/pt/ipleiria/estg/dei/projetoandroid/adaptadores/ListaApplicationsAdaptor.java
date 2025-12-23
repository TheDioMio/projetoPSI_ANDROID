package pt.ipleiria.estg.dei.projetoandroid.adaptadores;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import org.w3c.dom.Text;

import java.util.ArrayList;

import pt.ipleiria.estg.dei.projetoandroid.R;
import pt.ipleiria.estg.dei.projetoandroid.modelo.Animal;
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

        // 3. Preencher a descrição (motivo) e status
        tvDescricao.setText(candidaturaAtual.getMotive());
        tvStatusCandidatura.setText(candidaturaAtual.getStatusTexto());

        // 4. Ir buscar os dados do Animal associado através do Singleton
        int idAnimal = candidaturaAtual.getAnimalId();
        Animal animal = AppSingleton.getInstance(context).getAnimal(idAnimal);

        if (animal != null) {
            // Se o animal existir na lista local, mostramos os dados da lista local
            tvNome.setText(animal.getName());

            // Tenta carregar a imagem
            // Usei uma coisa genérica, à espera do código do igor para carregar as imgs do animal
//            String imgUrl = null;
            // if (animal.getPhotos() != null && !animal.getPhotos().isEmpty()) imgUrl = animal.getPhotos().get(0);

//            carregarImagem(imgUrl, imgAnimal);

        } else {
            // Se o animal não for encontrado (ex: base de dados local vazia ou animal apagado)
            tvNome.setText(candidaturaAtual.getAnimalName());
            imgAnimal.setImageResource(R.mipmap.placeholder);
        }

        return convertView;
    }

    private void carregarImagem(String img, ImageView destino) {
        if (img == null || img.isEmpty()) {
            destino.setImageResource(R.mipmap.placeholder);
            return;
        }

        String urlFinal = img;
        if (!img.startsWith("http")) {
            urlFinal = AppSingleton.FRONTEND_BASE_URL + img;
        }

        Glide.with(context)
                .load(urlFinal)
                .placeholder(R.mipmap.placeholder)
                .error(R.mipmap.placeholder)
                .centerCrop()
                .into(destino);
    }
}