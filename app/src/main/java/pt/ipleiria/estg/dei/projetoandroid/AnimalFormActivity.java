package pt.ipleiria.estg.dei.projetoandroid;

import android.app.AlertDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;
import java.util.HashMap;

import pt.ipleiria.estg.dei.projetoandroid.adaptadores.AnimalPhotoAdaptador;
import pt.ipleiria.estg.dei.projetoandroid.adaptadores.MetaSpinnerAdaptador;
import pt.ipleiria.estg.dei.projetoandroid.listeners.MetaListener;
import pt.ipleiria.estg.dei.projetoandroid.modelo.AnimalEdit;
import pt.ipleiria.estg.dei.projetoandroid.modelo.AnimalFile;
import pt.ipleiria.estg.dei.projetoandroid.modelo.AppSingleton;
import pt.ipleiria.estg.dei.projetoandroid.modelo.MetaItem;

public class AnimalFormActivity extends AppCompatActivity {

    private Spinner spType, spBreed, spAge, spSize, spVaccination;

    private static final int MAX_PHOTOS = 5;

    public static final String EXTRA_MODE = "MODE";
    public static final String MODE_CREATE = "CREATE";
    public static final String MODE_EDIT = "EDIT";
    public static final String EXTRA_ANIMAL_ID = "ANIMAL_ID";

    private ArrayList<MetaItem> types = new ArrayList<>();
    private ArrayList<MetaItem> breeds = new ArrayList<>();
    private ArrayList<MetaItem> filteredBreeds = new ArrayList<>();
    private ArrayList<MetaItem> ages = new ArrayList<>();
    private ArrayList<MetaItem> sizes = new ArrayList<>();
    private ArrayList<MetaItem> vaccinations = new ArrayList<>();
    private TextInputEditText etName, etDescription, etLocation, etListingDescription;
    private CheckBox cbNeutered;
    private Spinner spStatus;
    private Button btnAddPhoto, btnSave;
    private RecyclerView rvPhotos;
    private AnimalPhotoAdaptador photoAdapter;
    // fotos que já vieram da API
    private ArrayList<AnimalFile> photos = new ArrayList<>();

    // IDs das fotos removidas
    private ArrayList<Integer> removedFileIds = new ArrayList<>();

    // fotos novas (Uri ou File)
    private ArrayList<Uri> addedPhotos = new ArrayList<>();

    private AnimalEdit animalEdit;

    private boolean isEditMode = false;
    private int editTypeId = -1;
    private int editBreedId = -1;
    private int editAgeId = -1;
    private int editSizeId = -1;
    private int editVaccinationId = -1;
    private int animalId = -1;


    private MetaSpinnerAdaptador typeAdapter, breedAdapter,
            ageAdapter, sizeAdapter, vaccinationAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_animal_form);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        spType = findViewById(R.id.spType);
        spBreed = findViewById(R.id.spBreed);
        spAge = findViewById(R.id.spAge);
        spSize = findViewById(R.id.spSize);
        spVaccination = findViewById(R.id.spVaccination);
        etName = findViewById(R.id.etName);
        etDescription = findViewById(R.id.etDescription);
        etLocation = findViewById(R.id.etLocation);
        etListingDescription = findViewById(R.id.etListingDescription);
        btnAddPhoto = findViewById(R.id.btnAddPhoto);
        btnSave = findViewById(R.id.btnSave);
        cbNeutered = findViewById(R.id.cbNeutered);
        spStatus = findViewById(R.id.spListingStatus);
        setupStatusSpinner();

        rvPhotos = findViewById(R.id.rvPhotos);

        rvPhotos.setLayoutManager(
                new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        );

        photoAdapter = new AnimalPhotoAdaptador(
                this,
                photos,
                addedPhotos,
                new AnimalPhotoAdaptador.OnPhotoRemoveListener() {

                    @Override
                    public void onRemoveApiPhoto(AnimalFile file, int position) {

                        new AlertDialog.Builder(AnimalFormActivity.this)
                                .setTitle("Remover foto")
                                .setMessage("Tem a certeza que deseja remover esta foto?")
                                .setPositiveButton("Remover", (dialog, which) -> {

                                    photos.remove(position);
                                    removedFileIds.add(file.getIdFile());
                                    photoAdapter.notifyItemRemoved(position);

                                })
                                .setNegativeButton("Cancelar", null)
                                .show();
                    }

                    @Override
                    public void onRemoveAddedPhoto(Uri uri, int position) {

                        new AlertDialog.Builder(AnimalFormActivity.this)
                                .setTitle("Remover foto")
                                .setMessage("Tem a certeza que deseja remover esta foto?")
                                .setPositiveButton("Remover", (dialog, which) -> {

                                    addedPhotos.remove(position);
                                    photoAdapter.notifyItemRemoved(
                                            photos.size() + position
                                    );

                                })
                                .setNegativeButton("Cancelar", null)
                                .show();
                    }
                }
        );

        rvPhotos.setAdapter(photoAdapter);

        btnSave.setOnClickListener(v -> {
            if (!validateForm()) return;

            if (isEditMode) {
                updateAnimal();
            } else {
                createAnimal();
            }
        });


        btnAddPhoto.setOnClickListener(v -> {
            if (photos.size() + addedPhotos.size() >= MAX_PHOTOS) {
                Toast.makeText(
                        this,
                        "Só pode ter até 5 fotos",
                        Toast.LENGTH_SHORT
                ).show();
                return;
            }

            Intent intent = new Intent(Intent.ACTION_PICK);
            intent.setType("image/*");
            photoPicker.launch(intent);
        });

        String mode = getIntent().getStringExtra(EXTRA_MODE);

        if (MODE_EDIT.equals(mode)) {
            isEditMode = true;
            animalId = getIntent().getIntExtra(EXTRA_ANIMAL_ID, -1);
        }

        if (isEditMode && animalId > 0) {

            AppSingleton.getInstance(this)
                    .getAnimalEditAPI(this, animalId,
                            animal -> {

                                animalEdit = animal;

                                photos.clear();
                                photos.addAll(animalEdit.getFiles());
                                photoAdapter.notifyDataSetChanged();

                                editTypeId = animal.getTypeId();
                                editBreedId = animal.getBreedId();
                                editAgeId = animal.getAgeId();
                                editSizeId = animal.getSizeId();
                                editVaccinationId = animal.getVaccinationId();

                                loadMeta(); // ⬅️ só agora
                            },
                            error -> Toast.makeText(this,
                                    "Erro ao carregar animal",
                                    Toast.LENGTH_SHORT).show()
                    );

        } else {
            loadMeta();
        }


    }

    private boolean validateForm() {

        if (etName.getText().toString().trim().isEmpty()) {
            etName.setError("Obrigatório");
            return false;
        }

        if (etDescription.getText().toString().trim().isEmpty()) {
            etDescription.setError("Obrigatório");
            return false;
        }
        if (etListingDescription.getText().toString().trim().isEmpty()) {
            etListingDescription.setError("Obrigatório");
            return false;
        }
        if (etLocation.getText().toString().trim().isEmpty()) {
            etLocation.setError("Obrigatório");
            return false;
        }

        if (spType.getSelectedItem() == null || spBreed.getSelectedItem() == null || spAge.getSelectedItem() == null ||
                spSize.getSelectedItem() == null || spVaccination.getSelectedItem() == null || spStatus.getSelectedItem() == null) {
            Toast.makeText(this, R.string.txt_obrigatorio_selecionar, Toast.LENGTH_SHORT).show();
            return false;
        }

        if (photos.size() + addedPhotos.size() == 0) {
            Toast.makeText(this, "Adicione pelo menos uma foto", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

    private void createAnimal() {

        AnimalEdit animal = buildAnimalFromForm();

        AppSingleton.getInstance(this)
                .createAnimalAPI(this, animal,
                        createdAnimalId -> {

                            // ⬅️ agora temos ID
                            uploadNewPhotos(createdAnimalId);

                            Toast.makeText(this,
                                    "Animal criado com sucesso",
                                    Toast.LENGTH_SHORT).show();

                            finish();
                        },
                        error -> {
                            if (error.networkResponse != null) {
                                String body = new String(error.networkResponse.data);
                                Log.e("API_ERROR_BODY", body);
                            } else {
                                Log.e("API_ERROR", error.toString());
                            }
                        }
//                        error -> Toast.makeText(
//                                this,
//                                error.getMessage() != null ? error.getMessage() : "Erro na comunicação com o servidor",
//                                Toast.LENGTH_SHORT
//                        ).show()
                );
    }

    private void updateAnimal() {

        AnimalEdit animal = buildAnimalFromForm();
        animal.setId(animalId);

        AppSingleton.getInstance(this)
                .updateAnimalAPI(this, animalId, animal,
                        response -> {

                            // 1️⃣ apagar fotos removidas
                            deleteRemovedPhotos();

                            // 2️⃣ enviar fotos novas
                            uploadNewPhotos(animalId);

                            Toast.makeText(this,
                                    "Animal atualizado",
                                    Toast.LENGTH_SHORT).show();

                            finish();
                        },
                        error -> Toast.makeText(
                                this,
                                error.getMessage() != null ? error.getMessage() : "Erro na comunicação com o servidor",
                                Toast.LENGTH_SHORT
                        ).show()
                );
    }


    private AnimalEdit buildAnimalFromForm() {

        AnimalEdit animal = new AnimalEdit();

        animal.setName(etName.getText().toString().trim());
        animal.setDescription(etDescription.getText().toString().trim());
        animal.setLocation(etLocation.getText().toString().trim());

        animal.setTypeId(((MetaItem) spType.getSelectedItem()).getId());
        animal.setBreedId(((MetaItem) spBreed.getSelectedItem()).getId());
        animal.setAgeId(((MetaItem) spAge.getSelectedItem()).getId());
        animal.setSizeId(((MetaItem) spSize.getSelectedItem()).getId());
        animal.setVaccinationId(((MetaItem) spVaccination.getSelectedItem()).getId());

        animal.setNeutered(cbNeutered.isChecked() ? 1:0);

        // LISTING
        animal.setListingDescription(
                etListingDescription.getText().toString().trim()
        );

        animal.setListingStatus(spStatus.getSelectedItemPosition());

        return animal;
    }

    private void uploadNewPhotos(int animalId) {

        if (addedPhotos.isEmpty()) return;

        AppSingleton.getInstance(this)
                .uploadAnimalPhotosAPI(
                        this,
                        animalId,
                        addedPhotos,
                        response -> { /* ok */ },
                        error -> Toast.makeText(
                                this,
                                error.getMessage() != null ? error.getMessage() : "Erro na comunicação com o servidor",
                                Toast.LENGTH_SHORT
                        ).show()
                );
    }

    private void deleteRemovedPhotos() {

        if (removedFileIds.isEmpty()) return;

        AppSingleton.getInstance(this)
                .deleteAnimalPhotosAPI(
                        this,
                        removedFileIds,
                        response -> { /* ok */ },
                        error -> Toast.makeText(
                                this,
                                error.getMessage() != null ? error.getMessage() : "Erro na comunicação com o servidor",
                                Toast.LENGTH_SHORT
                        ).show()
                );
    }



    //carregar a foto da galeria
    private ActivityResultLauncher<Intent> photoPicker = registerForActivityResult(
        new ActivityResultContracts.StartActivityForResult(),
        result -> {

            if (result.getResultCode() == RESULT_OK &&
                    result.getData() != null) {

                if (photos.size() + addedPhotos.size() >= MAX_PHOTOS) {
                    Toast.makeText(
                            this,
                            "Limite máximo de 5 fotos",
                            Toast.LENGTH_SHORT
                    ).show();
                    return;
                }

                Uri imageUri = result.getData().getData();

                addedPhotos.add(imageUri);

                photoAdapter.notifyItemInserted(
                        photos.size() + addedPhotos.size() - 1
                );
            }
        });



    private void setupStatusSpinner() {

        ArrayList<String> states = new ArrayList<>();
        states.add("Inativo");
        states.add("Ativo");
        states.add("Adotado");

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                states
        );

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spStatus.setAdapter(adapter);
    }

    private void loadMeta() {

        AppSingleton.getInstance(this)
                .getAnimalMetaAPI(this, new MetaListener() {
                    @Override
                    public void onMetaLoaded(HashMap<String, ArrayList<MetaItem>> meta) {
                        setupSpinners(meta);

                        if (isEditMode) {
                            positionAllSpinners(); // posiciona os spinners
                            fillEditFields(); // preenche os campos
                        }
                    }

                    @Override
                    public void onMetaError(String error) {
                        Toast.makeText(AnimalFormActivity.this, error, Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private int getTotalPhotosCount() {
        return photos.size() + addedPhotos.size();
    }


    private void positionAllSpinners() {

        // 1️⃣ TYPE
        selectSpinnerById(spType, types, editTypeId);

        // 2️⃣ AGE / SIZE / VACCINATION
        selectSpinnerById(spAge, ages, editAgeId);
        selectSpinnerById(spSize, sizes, editSizeId);
        selectSpinnerById(spVaccination, vaccinations, editVaccinationId);

        // 3️⃣ BREED — depois do filtro do type
        spType.post(() -> {
            selectSpinnerById(spBreed, filteredBreeds, editBreedId);
        });
    }

    private void fillEditFields() {

        if (animalEdit == null) return;

        // ===== ANIMAL =====
        etName.setText(animalEdit.getName());
        etDescription.setText(animalEdit.getDescription());
        etLocation.setText(animalEdit.getLocation());

        cbNeutered.setChecked(animalEdit.getNeutered() == 1);

        // ===== LISTING =====
        etListingDescription.setText(animalEdit.getListingDescription());

        selectListingStatus(animalEdit.getListingStatus());
    }

    private void selectListingStatus(int status) {

        int position = 0;

        switch (status) {
            case 0:
                position = 0; // Inativo
                break;
            case 1:
                position = 1; // Ativo
                break;
            case 4:
                position = 2; // Adotado
                break;
        }

        spStatus.setSelection(position);
    }



    //POPULAR OS SPINNERS
    private void setupSpinners(HashMap<String, ArrayList<MetaItem>> meta) {

        types = meta.get("types");
        breeds = meta.get("breeds");
        ages = meta.get("ages");
        sizes = meta.get("sizes");
        vaccinations = meta.get("vaccinations");

        filteredBreeds.clear();

        typeAdapter = new MetaSpinnerAdaptador(this, types);
        breedAdapter = new MetaSpinnerAdaptador(this, filteredBreeds);
        ageAdapter = new MetaSpinnerAdaptador(this, ages);
        sizeAdapter = new MetaSpinnerAdaptador(this, sizes);
        vaccinationAdapter = new MetaSpinnerAdaptador(this, vaccinations);

        spType.setAdapter(typeAdapter);
        spBreed.setAdapter(breedAdapter);
        spAge.setAdapter(ageAdapter);
        spSize.setAdapter(sizeAdapter);
        spVaccination.setAdapter(vaccinationAdapter);

        setupTypeListener();
    }


    //ALTERAR AS RAÇA CONSOANTE O TIPO DE ANIMAL
    private void setupTypeListener() {

        spType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {

                MetaItem selectedType = types.get(position);

                filteredBreeds.clear();

                for (MetaItem breed : breeds) {
                    if (breed.getAnimalTypeId() == selectedType.getId()) {
                        filteredBreeds.add(breed);
                    }
                }

                breedAdapter.notifyDataSetChanged();

                // Se estivermos a editar, posicionar a raça depois do filtro
                if (isEditMode && editBreedId > 0) {
                    selectSpinnerById(spBreed, filteredBreeds, editBreedId);
                    editBreedId = -1; // evita reposicionar sempre
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });
    }



    //QUANDO EDITAMOS POSICIONAR O SPINNER CORRETAMENTE
    private void selectSpinnerById(Spinner spinner, ArrayList<MetaItem> list, int id) {

        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getId() == id) {
                spinner.setSelection(i);
                break;
            }
        }
    }
}