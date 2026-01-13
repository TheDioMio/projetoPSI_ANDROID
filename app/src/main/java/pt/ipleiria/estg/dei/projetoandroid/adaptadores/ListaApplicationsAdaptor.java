package pt.ipleiria.estg.dei.projetoandroid.adaptadores;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import pt.ipleiria.estg.dei.projetoandroid.R;
import pt.ipleiria.estg.dei.projetoandroid.listeners.ApplicationListener;
import pt.ipleiria.estg.dei.projetoandroid.modelo.Animal;
import pt.ipleiria.estg.dei.projetoandroid.modelo.AnimalFile;
import pt.ipleiria.estg.dei.projetoandroid.modelo.AppSingleton;
import pt.ipleiria.estg.dei.projetoandroid.modelo.Application;

public class ListaApplicationsAdaptor extends BaseAdapter {
    private Context context;
    private LayoutInflater inflater;
    private ArrayList<Application> candidaturas;
    private AppSingleton singleton;
    private ApplicationListener listener;
    private String type; //Este type é "sent" ou "received", para dar para esconder o botão delete no listview.

    public ListaApplicationsAdaptor(Context context, ArrayList<Application> candidaturas, String type) {
        this.context = context;
        this.type = type;

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
        ImageButton btnDelete = convertView.findViewById(R.id.btnDelete);


        // 2. Obter a candidatura atual
        Application candidaturaAtual = candidaturas.get(position);

        // 3. Preencher a descrição (motivo), status e nome
        tvDescricao.setText(candidaturaAtual.getMotive());
        badgeStatus(candidaturaAtual.getStatus(), tvStatusCandidatura);
        tvNome.setText(candidaturaAtual.getAnimalName());

        //Dar load à imagem: tornado fácil porque o path já vem da API (NÃO DEPENDENTE DO ANIMAL)
        String imgPath = candidaturaAtual.getAnimalImage();
        animalImgLoad(imgPath, imgAnimal);

        boolean isCandidate = type != null && type.equalsIgnoreCase("sent");
        boolean isNotDecided = candidaturaAtual.getStatus().equals(Application.STATUS_SENT) ||
                candidaturaAtual.getStatus().equals(Application.STATUS_PENDING)
                || candidaturaAtual.getStatus().equals(Application.STATUS_CANCELLED);


        //Se a candidatura for enviada pelo user e se ainda não estiver decidida, mostrar o btn e fazer o onClickListener
        if (isCandidate && isNotDecided) {
            btnDelete.setVisibility(View.VISIBLE);
            btnDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    //ESTE BUILDER É PARA A CAIXA DE DIÁLOGO, DE CONFIRMAÇÃO DE APAGAR!
                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setTitle("Eliminar Candidatura");
                    builder.setMessage("Tem a certeza que deseja eliminar esta candidatura? Esta ação é permanente e não pode ser desfeita!");
                    builder.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            singleton.removerApplicationAPI(candidaturaAtual, context, new pt.ipleiria.estg.dei.projetoandroid.listeners.ApplicationListener() {
                                @Override
                                public void onRefreshDetails(Application app) {
                                    //Remover o item da lista interna do adaptador
                                    candidaturas.remove(candidaturaAtual); //
                                    //ISTO É IMPORTANTE! -> Notificar a ListView para se redesenhar (desaparece a linha)
                                    notifyDataSetChanged(); //
                                    Toast.makeText(context, "Candidatura eliminada!", Toast.LENGTH_SHORT).show();
                                }
                                @Override
                                public void onError(String error) {
                                }
                            });
                        }
                    });

                    builder.setNegativeButton("Não", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    builder.show();
                }
            });

        } else {
            // Se for recebida ou já não estiver pendente, escondemos o botão
            btnDelete.setVisibility(View.GONE);
        }


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
