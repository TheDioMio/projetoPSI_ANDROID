package pt.ipleiria.estg.dei.projetoandroid;

import android.app.AlertDialog;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.GoogleMap;


import com.bumptech.glide.Glide;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;

import pt.ipleiria.estg.dei.projetoandroid.listeners.CommentActionListener;
import pt.ipleiria.estg.dei.projetoandroid.listeners.CommentCreateListener;
import pt.ipleiria.estg.dei.projetoandroid.modelo.Animal;
import pt.ipleiria.estg.dei.projetoandroid.modelo.AppSingleton;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import pt.ipleiria.estg.dei.projetoandroid.adaptadores.AnimalImageAdaptador;
import pt.ipleiria.estg.dei.projetoandroid.adaptadores.CommentAdaptador;
import pt.ipleiria.estg.dei.projetoandroid.modelo.Comment;

public class AnimalDetailsFragment extends Fragment implements CommentCreateListener {

//public class AnimalDetailsFragment extends Fragment implements OnMapReadyCallback {

    private MapView mapView;
    private GoogleMap gMap;

    private ImageView imgPrincipal;
    private LinearLayout layoutMiniaturas;
    private Button btnAdopt, btnSendComment, btnSendMessage;
    private TextView tvNome, tvLocalizacao, tvDescricao, tvOwnerName, tvOwnerEmail, tvListingDescription, tvViews, tvOwnerContact, tvSize, tvAge, tvVaccination, tvAnimalType, tvBreed, tvNeutered;
    private ImageView imgOwnerAvatar;
    private TextView tvOwnerAddress;
    private TextView tvAnimalExtraInfo;
    private Animal animal;

    private View rootView;
    private TextInputEditText etNewComment;
    private RecyclerView rvAnimalImages, rvComments;
    private AnimalImageAdaptador animalImageAdaptador;
    private CommentAdaptador commentAdaptador;


    public AnimalDetailsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_animal_show, container, false);
        rootView = view;
        imgPrincipal = view.findViewById(R.id.imgAnimalMain);

        tvNome = view.findViewById(R.id.tvAnimalName);
        tvDescricao = view.findViewById(R.id.tvAnimalDescription);
        tvLocalizacao = view.findViewById(R.id.tvLocation);
        rvAnimalImages = view.findViewById(R.id.rvAnimalImages);
        rvComments = view.findViewById(R.id.rvComments);
        tvAnimalExtraInfo = view.findViewById(R.id.tvAnimalExtraInfo);
        imgOwnerAvatar = view.findViewById(R.id.imgOwnerAvatar);
        tvOwnerName = view.findViewById(R.id.tvOwnerName);
        tvOwnerEmail = view.findViewById(R.id.tvOwnerEmail);
        tvOwnerAddress = view.findViewById(R.id.tvOwnerAddress);
        tvListingDescription = view.findViewById(R.id.tvListingDescription);
        tvViews = view.findViewById(R.id.tvViews);
        btnAdopt = view.findViewById(R.id.btnAdopt);
        btnSendComment = view.findViewById(R.id.btnSendComment);
        etNewComment = view.findViewById(R.id.etNewComment);
        btnSendMessage = view.findViewById(R.id.btnSendMessage);
        AppSingleton.getInstance(getContext()).setCommentCreateListener(this);
        // LayoutManagers
        rvAnimalImages.setLayoutManager(
                new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false)
        );

        rvComments.setLayoutManager(
                new LinearLayoutManager(getContext())
        );


        //obtém o animal da BD passado pelo Bundle
        Bundle args = getArguments();
        if (args != null) {
            int animalId = args.getInt("ID_ANIMAL");
            //colocar a ir buscar o animal ao singleton
            animal = AppSingleton.getInstance(getContext()).getAnimalBD(animalId);
            carregarDados(animal);
        }

        btnSendComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //primeiro vê se tem internet
                if (!AppSingleton.getInstance(getContext()).isConnectionInternet(getContext())) {
                    Snackbar.make(rootView,
                                    R.string.txt_offline_indisponivel,
                                    Snackbar.LENGTH_INDEFINITE)
                            .setAction(R.string.txt_ok, vv -> {})
                            .show();
                    return;
                }


                String text = etNewComment.getText().toString().trim();
                if (text.isEmpty()) {
                    Toast.makeText(getContext(), R.string.txt_comentario_vazio, Toast.LENGTH_SHORT).show();
                    etNewComment.setError(getString(R.string.txt_obrigatorio));
                    return;
                }
                //vamos chamar a API para fazer o create comment
                int animalId = args.getInt("ID_ANIMAL");
                AppSingleton.getInstance(getContext()).createCommentAPI(getContext(), animalId, text);
            }
        });

        btnSendMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!AppSingleton.getInstance(getContext()).isConnectionInternet(getContext())) {
                    Snackbar.make(rootView,
                                    R.string.txt_offline_indisponivel,
                                    Snackbar.LENGTH_INDEFINITE)
                            .setAction(R.string.txt_ok, vv -> {})
                            .show();
                    return;
                }

                //falta implementar chamar a vista de enviar a mensagem
                int receiverId = animal.getUserId(); // ID do dono do animal

                // cria o fragmento de mensagem em modo "escrever"
                MessageFragment frag = MessageFragment.newInstanceForCompose(
                        receiverId,
                        getString(R.string.txt_interesse_no_animal) + animal.getName()
                );

                requireActivity()
                        .getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.contentFragment, frag)
                        .addToBackStack(null)
                        .commit();

            }
        });

        //Botão para submeter a candidatura no animal em específico
        btnAdopt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //primeiro vê se tem internet

                if (!AppSingleton.getInstance(getContext()).isConnectionInternet(getContext())) {
                    Snackbar.make(rootView,
                                    R.string.txt_offline_indisponivel,
                                    Snackbar.LENGTH_INDEFINITE)
                            .setAction(R.string.txt_ok, vv -> {})
                            .show();
                    return ;
                }


                Fragment fragment = new ApplicationCreateFragment();

                Bundle args = new Bundle();
                if (animal != null) {
                    args.putInt("ID_ANIMAL", animal.getId());
                }
                fragment.setArguments(args);
                getParentFragmentManager()
                        .beginTransaction()
                        .replace(R.id.contentFragment, fragment)
                        .addToBackStack(null)
                        .commit();
            }
        });

        return view;
    }

//    private void carregarImagem(String img, ImageView destino) {
//
//        if (img == null || img.isEmpty()) {
//            destino.setImageResource(R.mipmap.placeholder);
//            return;
//        }
//
//        // URL → http/https
//        if (img.startsWith("http")) {
//            Glide.with(requireContext())
//                    .load(img)
//                    .placeholder(R.mipmap.placeholder)
//                    .error(R.mipmap.placeholder)
//                    .centerCrop()
//                    .into(destino);
//        }
//        // Drawable pelo nome
//        else {
//            int resId = getResources().getIdentifier(img, "drawable", requireContext().getPackageName());
//
//            if (resId != 0) {
//                Glide.with(requireContext())
//                        .load(resId)
//                        .placeholder(R.mipmap.placeholder)
//                        .error(R.mipmap.placeholder)
//                        .centerCrop()
//                        .into(destino);
//            } else {
//                destino.setImageResource(R.mipmap.placeholder);
//            }
//        }
//    }


    //Igor, tive que comentar isto por agora porque consome muitos recursos para a aplicação
    //como ela corre no thread principal, todas as outras funções ficas obsfuscadas por ela, e a app
    //fica constantemente em Application Not Responding.
//    @Override
//    public void onMapReady(@NonNull GoogleMap googleMap) {
//        gMap = googleMap;
//
//        if (animal == null || animal.getLocation() == null || animal.getLocation().isEmpty()) {
//            return;
//        }
//
//        String locationName = animal.getLocation(); // ex: "Leiria"
//
//        Geocoder geocoder = new Geocoder(requireContext(), Locale.getDefault());
//
//        try {
//            List<Address> results = geocoder.getFromLocationName(locationName, 1);
//
//            if (results != null && !results.isEmpty()) {
//
//                Address address = results.get(0);
//                LatLng posicao = new LatLng(
//                        address.getLatitude(),
//                        address.getLongitude()
//                );
//
//                gMap.addMarker(new MarkerOptions()
//                        .position(posicao)
//                        .title(animal.getName()));
//
//                gMap.moveCamera(CameraUpdateFactory.newLatLngZoom(posicao, 13f));
//
//            } else {
//                Toast.makeText(
//                        getContext(),
//                        "Localização não encontrada",
//                        Toast.LENGTH_SHORT
//                ).show();
//            }
//
//        } catch (IOException e) {
//            e.printStackTrace();
//            Toast.makeText(
//                    getContext(),
//                    "Erro ao obter localização",
//                    Toast.LENGTH_SHORT
//            ).show();
//        }
//    }


    private void carregarDados(Animal animal) {
        if (animal == null) return;

        tvNome.setText(animal.getName());
        tvDescricao.setText(animal.getDescription());
        tvLocalizacao.setText(animal.getLocation());
        tvListingDescription.setText(animal.getListingDescription());

        if (animal.getListingViews() > 0) {
            tvViews.setText(animal.getListingViews() + " visualizações");
            tvViews.setVisibility(View.VISIBLE);
        } else {
            tvViews.setVisibility(View.GONE);
        }
        String info =
                "Tipo: " + animal.getType() + "\n" +
                        "Raça: " + animal.getBreed() + "\n" +
                        "Idade: " + animal.getAge() + "\n" +
                        "Tamanho: " + animal.getSize() + "\n" +
                        "Castrado: " + animal.getNeutered() + "\n" +
                        "Vacinado: " + animal.getVacination();

        tvAnimalExtraInfo.setText(info);

        // ---------------- Dados do dono ----------------
        tvOwnerName.setText(animal.getOwnerName());
        tvOwnerEmail.setText(animal.getOwnerEmail());
        tvOwnerAddress.setText(animal.getOwnerAddress());

        String avatar = animal.getOwnerAvatar();

        if (avatar != null && !avatar.isEmpty()) {

            String avatarUrl = avatar.startsWith("http")
                    ? avatar
                    : AppSingleton.getInstance(getContext()).FRONTEND_BASE_URL + avatar;

            Glide.with(requireContext())
                    .load(avatarUrl)
                    .placeholder(R.mipmap.placeholder)
                    .error(R.mipmap.placeholder)
                    .circleCrop()
                    .into(imgOwnerAvatar);

        } else {
            imgOwnerAvatar.setImageResource(R.mipmap.placeholder);
        }

        // imagem principal
        if (!animal.getAnimalfiles().isEmpty()) {

            String imgPath = animal.getAnimalfiles().get(0).getFileAddress();
            String imageUrl;

            if (imgPath.startsWith("http")) {
                imageUrl = imgPath;
            } else {
                imageUrl = AppSingleton.getInstance(getContext()).FRONTEND_BASE_URL + imgPath;
            }

            Glide.with(requireContext())
                    .load(imageUrl)
                    .placeholder(R.mipmap.placeholder)
                    .error(R.mipmap.placeholder)
                    .centerCrop()
                    .into(imgPrincipal);

        } else {
            imgPrincipal.setImageResource(R.mipmap.placeholder);
        }

        animalImageAdaptador = new AnimalImageAdaptador(
                getContext(),
                animal.getAnimalfiles(),
                imgPrincipal
        );
        rvAnimalImages.setAdapter(animalImageAdaptador);

        commentAdaptador = new CommentAdaptador(
                getContext(),
                animal.getComments(),
                null,
                false);
        rvComments.setAdapter(commentAdaptador);
    }


    //Metodos implementados do listener do create comment
    @Override
    public void onCreateCommentSuccess(Comment comment) {
        // adiciona o comment a variavel local e atualiza

        Toast.makeText(getContext(), R.string.txt_comentario_adicionado_com_sucesso, Toast.LENGTH_SHORT).show();
        //agora atualizamos a lista dos comentários
       // animal.getComments().add(0, comment);   // 0 = adiciona no topo (mais recente primeiro)

        // Notifica o adapter
        commentAdaptador.notifyItemInserted(0);

        // Faz scroll para o novo comentário
        rvComments.scrollToPosition(0);

        // Limpa a caixa de texto
        etNewComment.setText("");

    }

    @Override
    public void onCreateCommentError(String error) {
        Toast.makeText(getContext(), error, Toast.LENGTH_SHORT).show();
    }
}

//
//    //Metodoso do Listener do Comments
//    @Override
//    public void onEditComment(Comment comment) {
//
//
//    }
//
//    @Override
//    public void onDeleteComment(Comment comment) {
//        new AlertDialog.Builder(requireContext())
//                .setTitle("Apagar comentário")
//                .setMessage("Tens a certeza que queres apagar este comentário?")
//                .setPositiveButton("Apagar", (dialog, which) -> {
//                    AppSingleton.getInstance(getContext())
//                            .deleteCommentAPI(getContext(), comment.getId(), this);
//                })
//                .setNegativeButton("Cancelar", null)
//                .show();
//    }
//}