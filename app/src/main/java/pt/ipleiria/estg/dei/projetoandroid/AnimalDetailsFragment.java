package pt.ipleiria.estg.dei.projetoandroid;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.GoogleMap;


import com.bumptech.glide.Glide;

import java.util.List;

import pt.ipleiria.estg.dei.projetoandroid.modelo.Animal;
import pt.ipleiria.estg.dei.projetoandroid.modelo.AppSingleton;
import pt.ipleiria.estg.dei.projetoandroid.modelo.GestorAnimals;
public class AnimalDetailsFragment extends Fragment {
//public class AnimalDetailsFragment extends Fragment implements OnMapReadyCallback {

    private MapView mapView;
    private GoogleMap gMap;

    private ImageView imgPrincipal;
    private LinearLayout layoutMiniaturas;
    private Button btnApplication;
    private TextView tvNome, tvLocalizacao, tvDescricao, tvOwnerName, tvOwnerEmail, tvOwnerContact, tvSize, tvAge, tvVaccination, tvAnimalType, tvBreed, tvNeutered;

    private Animal animal;

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public AnimalDetailsFragment() {
        // Required empty public constructor
    }

    public static AnimalDetailsFragment newInstance(String param1, String param2) {
        AnimalDetailsFragment fragment = new AnimalDetailsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_animal_details, container, false);

        imgPrincipal = view.findViewById(R.id.imgPrincipal);
        layoutMiniaturas = view.findViewById(R.id.layoutMiniaturas);
        tvNome = view.findViewById(R.id.tvNomeAnimal);
        tvLocalizacao = view.findViewById(R.id.tvLocalizacaoAnimal);

        tvDescricao = view.findViewById(R.id.tvDescricaoAnimal);
        btnApplication = view.findViewById(R.id.btnApplication);
        tvAge = view.findViewById(R.id.tvAge);
        tvVaccination = view.findViewById(R.id.tvVaccination);
        tvNeutered = view.findViewById(R.id.tvNeutered);
        tvBreed = view.findViewById(R.id.tvBreed);
        tvAnimalType = view.findViewById(R.id.tvAnimalType);
        tvSize = view.findViewById(R.id.tvSize);


        //obtém o animal passado pelo Bundle
        Bundle args = getArguments();
        if (args != null) {
            int animalId = args.getInt("ID_ANIMAL");
            animal = AppSingleton.getInstance().getAnimal(animalId);
            carregarDados(animal);
        }


        //Botão para submeter a candidatura no animal em específico
        btnApplication.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment = new ApplicationsFragment();

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
//        mapView = view.findViewById(R.id.mapView);
//        mapView.onCreate(savedInstanceState);
//        mapView.getMapAsync(this);

        return view;
    }

    private void carregarImagem(String img, ImageView destino) {

        if (img == null || img.isEmpty()) {
            destino.setImageResource(R.mipmap.placeholder);
            return;
        }

        // URL → http/https
        if (img.startsWith("http")) {
            Glide.with(requireContext())
                    .load(img)
                    .placeholder(R.mipmap.placeholder)
                    .error(R.mipmap.placeholder)
                    .centerCrop()
                    .into(destino);
        }
        // Drawable pelo nome
        else {
            int resId = getResources().getIdentifier(img, "drawable", requireContext().getPackageName());

            if (resId != 0) {
                Glide.with(requireContext())
                        .load(resId)
                        .placeholder(R.mipmap.placeholder)
                        .error(R.mipmap.placeholder)
                        .centerCrop()
                        .into(destino);
            } else {
                destino.setImageResource(R.mipmap.placeholder);
            }
        }
    }

//    @Override
//    public void onMapReady(GoogleMap googleMap) {
//        gMap = googleMap;
//
//        if (animal == null) return;
//
//        String locationName = animal.getLocation();   // Ex: “Leiria”
//
//        Geocoder geocoder = new Geocoder(getContext(), Locale.getDefault());
//
//        try {
//            List<Address> results = geocoder.getFromLocationName(locationName, 1);
//
//            if (results != null && !results.isEmpty()) {
//
//                Address addr = results.get(0);
//                double lat = addr.getLatitude();
//                double lng = addr.getLongitude();
//
//                LatLng posicao = new LatLng(lat, lng);
//
//                gMap.addMarker(new MarkerOptions()
//                        .position(posicao)
//                        .title(animal.getName()));
//
//                gMap.moveCamera(CameraUpdateFactory.newLatLngZoom(posicao, 12));
//
//            } else {
//                // Se não encontrou a cidade
//                Toast.makeText(getContext(), "Localização não encontrada: " + locationName, Toast.LENGTH_SHORT).show();
//            }
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }





    private void carregarDados(Animal animal) {
        if (animal == null) return;

        // Alterar o modo como mostramos os dados para em vez de ser colocado aqui no código ser colocado no layout
        tvNome.setText(animal.getName());
        tvAnimalType.setText(animal.getAnimal_type());
        tvBreed.setText(animal.getBreed());
        tvAge.setText(animal.getAge());
        tvSize.setText(animal.getSize());
        tvVaccination.setText(animal.getVaccines());
        tvNeutered.setText(String.valueOf(animal.isNeutered()));
        tvLocalizacao.setText( animal.getLocation());
        tvDescricao.setText(animal.getDescription());



        List<String> imagens = animal.getImages();

        if (imagens != null && !imagens.isEmpty()) {

            // ---- 1) Carregar a imagem principal ----
            carregarImagem(imagens.get(0), imgPrincipal);

            // ---- 2) Criar miniaturas ----
            for (String img : imagens) {

                ImageView mini = new ImageView(requireContext());
                LinearLayout.LayoutParams params =
                        new LinearLayout.LayoutParams(200, 200);
                params.setMargins(8, 0, 8, 0);
                mini.setLayoutParams(params);
                mini.setScaleType(ImageView.ScaleType.CENTER_CROP);

                carregarImagem(img, mini);

                mini.setOnClickListener(v -> carregarImagem(img, imgPrincipal));

                layoutMiniaturas.addView(mini);
            }
        }
    }
//
//    @Override
//    public void onResume() {
//        super.onResume();
//        if (mapView != null) mapView.onResume();
//    }
//
//    @Override
//    public void onPause() {
//        super.onPause();
//        if (mapView != null) mapView.onPause();
//    }
//
//    @Override
//    public void onDestroy() {
//        super.onDestroy();
//        if (mapView != null) mapView.onDestroy();
//    }
//
//    @Override
//    public void onLowMemory() {
//        super.onLowMemory();
//        if (mapView != null) mapView.onLowMemory();
//    }

}