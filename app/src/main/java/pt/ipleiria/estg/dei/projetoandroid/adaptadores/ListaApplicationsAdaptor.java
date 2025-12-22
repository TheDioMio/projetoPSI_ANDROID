package pt.ipleiria.estg.dei.projetoandroid.adaptadores;

import android.content.Context;
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

        ImageView imgAnimal = convertView.findViewById(R.id.imgFotoAnimalCandidatura);
        TextView tvNome = convertView.findViewById(R.id.tvNomeAnimalCandidatura);
        TextView tvDescricao = convertView.findViewById(R.id.tvDescricaoCandidatura);

        Application candidaturaAtual = candidaturas.get(position);
        int idAnimal = candidaturaAtual.getAnimal_id();
        Animal animal = AppSingleton.getInstance(context).getAnimal(idAnimal);

        tvDescricao.setText(candidaturaAtual.getDescription());

        if (animal != null) {
            tvNome.setText(animal.getName());
// a class animal mudou, adaptar aos novos caampos
//            if (animal.getImages() != null && !animal.getImages().isEmpty()) {
//                carregarImagem(animal.getImages().get(0), imgAnimal);
//            } else {
//                imgAnimal.setImageResource(R.mipmap.placeholder);
//            }
        } else {
            tvNome.setText("Animal removido");
        }
        return convertView;
    }

    private void carregarImagem(String img, ImageView destino) {
        if (img == null || img.isEmpty()) {
            destino.setImageResource(R.mipmap.placeholder);
            return;
        }
        if (img.startsWith("http")) {
            Glide.with(context).load(img).placeholder(R.mipmap.placeholder).centerCrop().into(destino);
        } else {
            int resId = context.getResources().getIdentifier(img, "drawable", context.getPackageName());
            if (resId != 0) {
                Glide.with(context).load(resId).centerCrop().into(destino);
            } else {
                destino.setImageResource(R.mipmap.placeholder);
            }
        }
    }

}
