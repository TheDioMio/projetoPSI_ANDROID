package pt.ipleiria.estg.dei.projetoandroid;

import android.app.AlertDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.PickVisualMediaRequest;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.VolleyError;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;
import java.util.HashMap;

import pt.ipleiria.estg.dei.projetoandroid.adaptadores.AnimalPhotoAdaptador;
import pt.ipleiria.estg.dei.projetoandroid.adaptadores.CommentAdaptador;
import pt.ipleiria.estg.dei.projetoandroid.adaptadores.MetaSpinnerAdaptador;
import pt.ipleiria.estg.dei.projetoandroid.listeners.CommentActionListener;
import pt.ipleiria.estg.dei.projetoandroid.listeners.CommentDeleteListener;
import pt.ipleiria.estg.dei.projetoandroid.listeners.CommentUpdateListener;
import pt.ipleiria.estg.dei.projetoandroid.listeners.CreateAnimalListener;
import pt.ipleiria.estg.dei.projetoandroid.listeners.DeleteAnimalPhotosListener;
import pt.ipleiria.estg.dei.projetoandroid.listeners.GetAnimalEditListener;
import pt.ipleiria.estg.dei.projetoandroid.listeners.MetaListener;
import pt.ipleiria.estg.dei.projetoandroid.listeners.UpdateAnimalListener;
import pt.ipleiria.estg.dei.projetoandroid.listeners.UploadAnimalPhotosListener;
import pt.ipleiria.estg.dei.projetoandroid.modelo.Animal;
import pt.ipleiria.estg.dei.projetoandroid.modelo.AnimalEdit;
import pt.ipleiria.estg.dei.projetoandroid.modelo.AnimalFile;
import pt.ipleiria.estg.dei.projetoandroid.modelo.AppDBHelper;
import pt.ipleiria.estg.dei.projetoandroid.modelo.AppSingleton;
import pt.ipleiria.estg.dei.projetoandroid.modelo.Comment;
import pt.ipleiria.estg.dei.projetoandroid.modelo.MetaItem;

public class AnimalFormActivity extends AppCompatActivity implements CreateAnimalListener, GetAnimalEditListener, UpdateAnimalListener, UploadAnimalPhotosListener,
        DeleteAnimalPhotosListener, CommentDeleteListener, CommentUpdateListener, CommentActionListener {

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
    private RecyclerView rvPhotos, rvComments;
    private CommentAdaptador commentAdaptador;
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


    private MetaSpinnerAdaptador typeAdapter, breedAdapter, ageAdapter, sizeAdapter, vaccinationAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        //setContentView(R.layout.activity_animal_form);
        setContentView(R.layout.activity_animal_form_base);

        getLayoutInflater().inflate(
                R.layout.activity_animal_form,
                findViewById(R.id.contentForm),
                true
        );

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        AppSingleton.getInstance(this).setCreateAnimalListener(this);
        AppSingleton.getInstance(this).setGetAnimalEditListener(this);
        AppSingleton.getInstance(this).setUpdateAnimalListener(this);
        AppSingleton.getInstance(this).setUploadAnimalPhotosListener(this);
        AppSingleton.getInstance(this).setDeleteAnimalPhotosListener(this);
        AppSingleton.getInstance(this).setCommentDeleteListener(this);
        AppSingleton.getInstance(this).setCommentActionListener(this);
        AppSingleton.getInstance(this).setCommentUpdateListener(this);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

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
        rvComments = findViewById(R.id.rvComments);
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

        //alterar bara a barra
        btnSave.setOnClickListener(v -> {
            if (!validateForm()) return;

            if (isEditMode) {
                updateAnimal();
            } else {
                createAnimal();
            }
        });


        btnAddPhoto.setOnClickListener(v -> {
            // validação para não deixar criar mais de 5 fotos
            if (photos.size() + addedPhotos.size() >= MAX_PHOTOS) {
                Toast.makeText(
                        this,
                        "Só pode ter até 5 fotos",
                        Toast.LENGTH_SHORT
                ).show();
                return;
            }

            Intent intent;

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                intent = new Intent(MediaStore.ACTION_PICK_IMAGES);
            } else {
                intent = new Intent(Intent.ACTION_PICK,
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            }

            imagePicker.launch(intent);

//            Intent intent = new Intent(Intent.ACTION_PICK);
//            intent.setType("image/*");
//            photoPicker.launch(intent);
        });

        String mode = getIntent().getStringExtra(EXTRA_MODE);

        if (MODE_EDIT.equals(mode)) {
            isEditMode = true;
            animalId = getIntent().getIntExtra(EXTRA_ANIMAL_ID, -1);
        }

        if (isEditMode && animalId > 0) {
            AppSingleton.getInstance(this).getAnimalEditAPI(this, animalId);

        } else {
            loadMeta();
        }

    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();   // fecha o formulário
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_animal_form, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.action_save) {
            if (!validateForm()) return false;

            if (isEditMode) {
                updateAnimal();
            } else {
                createAnimal();
            }

            //return true;
        }
        return super.onOptionsItemSelected(item);
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
        AppSingleton.getInstance(this).createAnimalAPI(this, animal);

//        AnimalEdit animal = buildAnimalFromForm();
//
//        AppSingleton.getInstance(this)
//                .createAnimalAPI(this, animal,
//                        createdAnimalId -> {
//
//                            // ⬅️ agora temos ID
//                            uploadNewPhotos(createdAnimalId);
//
//                            Toast.makeText(this,
//                                    "Animal criado com sucesso",
//                                    Toast.LENGTH_SHORT).show();
//
//                            finish();
//                        },
//                        error -> {
//                            if (error.networkResponse != null) {
//                                String body = new String(error.networkResponse.data);
//                                Log.e("API_ERROR_BODY", body);
//                            } else {
//                                Log.e("API_ERROR", error.toString());
//                            }
//                        }
////                        error -> Toast.makeText(
////                                this,
////                                error.getMessage() != null ? error.getMessage() : "Erro na comunicação com o servidor",
////                                Toast.LENGTH_SHORT
////                        ).show()
//                );
    }

    private void updateAnimal() {
        AnimalEdit animal = buildAnimalFromForm();
        animal.setId(animalId);

        AppSingleton.getInstance(this).updateAnimalAPI(this, animalId, animal);

//        AnimalEdit animal = buildAnimalFromForm();
//        animal.setId(animalId);
//
//        AppSingleton.getInstance(this)
//                .updateAnimalAPI(this, animalId, animal,
//                        response -> {
//
//                            // 1️⃣ apagar fotos removidas
//                            deleteRemovedPhotos();
//
//                            // 2️⃣ enviar fotos novas
//                            uploadNewPhotos(animalId);
//
//                            Toast.makeText(this,
//                                    "Animal atualizado",
//                                    Toast.LENGTH_SHORT).show();
//
//                           // finish();
//                        },
//                        error -> Toast.makeText(
//                                this,
//                                error.getMessage() != null ? error.getMessage() : "Erro na comunicação com o servidor",
//                                Toast.LENGTH_SHORT
//                        ).show()
//                );
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
        if (addedPhotos.isEmpty()) {
            finish(); // mantém o teu comportamento
            return;
        }

        AppSingleton.getInstance(this).uploadAnimalPhotosAPI(this, animalId, addedPhotos);

//        if (addedPhotos.isEmpty()) return;
//
//        AppSingleton.getInstance(this)
//                .uploadAnimalPhotosAPI(
//                        this,
//                        animalId,
//                        addedPhotos,
//                        response -> { /* ok */
//                            finish();
//                            },
//                        error -> Toast.makeText(
//                                this,
//                                error.getMessage() != null ? error.getMessage() : "Erro na comunicação com o servidor",
//                                Toast.LENGTH_SHORT
//                        ).show()
//                );
    }

    private void deleteRemovedPhotos() {

        if (removedFileIds.isEmpty()) return;

        AppSingleton.getInstance(this).deleteAnimalPhotosAPI(this, removedFileIds);
//        if (removedFileIds.isEmpty()) return;
//
//        AppSingleton.getInstance(this)
//                .deleteAnimalPhotosAPI(
//                        this,
//                        removedFileIds,
//                        response -> { /* ok */ },
//                        error -> Toast.makeText(
//                                this,
//                                error.getMessage() != null ? error.getMessage() : "Erro na comunicação com o servidor",
//                                Toast.LENGTH_SHORT
//                        ).show()
//                );
    }

    //ponto 1
    private ActivityResultLauncher<Intent> imagePicker =
            registerForActivityResult(
                    new ActivityResultContracts.StartActivityForResult(),
                    result -> {
                        if (result.getResultCode() == RESULT_OK &&
                                result.getData() != null) {

                            if (photos.size() + addedPhotos.size() >= MAX_PHOTOS) {
                                Toast.makeText(this,"Limite máximo de 5 fotos", Toast.LENGTH_SHORT).show();
                                return;
                            }

                            Uri uri = result.getData().getData();
                            if (uri != null) {
                                addedPhotos.add(uri);
                                photoAdapter.notifyDataSetChanged();
                            }
                        }
                    });



// está a funcionar mas só para google fotos não para a galeria
    //carregar a foto da galeria
//    private ActivityResultLauncher<Intent> photoPicker = registerForActivityResult(
//        new ActivityResultContracts.StartActivityForResult(),
//        result -> {
//
//            if (result.getResultCode() == RESULT_OK &&
//                    result.getData() != null) {
//
//                if (photos.size() + addedPhotos.size() >= MAX_PHOTOS) {
//                    Toast.makeText(
//                            this,
//                            "Limite máximo de 5 fotos",
//                            Toast.LENGTH_SHORT
//                    ).show();
//                    return;
//                }
//
//                Uri imageUri = result.getData().getData();
//
//                addedPhotos.add(imageUri);
//
//                photoAdapter.notifyItemInserted(
//                        photos.size() + addedPhotos.size() - 1
//                );
//            }
//        });



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

    //METODOS DO LISTENER DO CREATE ANIMAL
    @Override
    public void onCreateAnimalSuccess(int animalId) {
        uploadNewPhotos(animalId);
    }

    @Override
    public void onCreateAnimalError(VolleyError error) {
        if (error.networkResponse != null) {
            Toast.makeText(getApplicationContext(),"Erro ao criar animal", Toast.LENGTH_SHORT).show();
            String body = new String(error.networkResponse.data);
            Log.e("API_ERROR_BODY", body);
        } else {
            Log.e("API_ERROR", error.toString());
        }
    }

    //METODOS DO LISTENER DO ANIMAL EDIT (RECEBE OS DADOS DO ANIMAL QUE VAMOS ALTERAR)
    @Override
    public void onGetAnimalEditSuccess(AnimalEdit animal) {
        animalEdit = animal;

        photos.clear();
        photos.addAll(animal.getFiles());
        photoAdapter.notifyDataSetChanged();

        editTypeId = animal.getTypeId();
        editBreedId = animal.getBreedId();
        editAgeId = animal.getAgeId();
        editSizeId = animal.getSizeId();
        editVaccinationId = animal.getVaccinationId();

        //carregar os comentários
        Animal auxAnimal = AppSingleton.getInstance(this).getMyAnimal(animalId);;

        if (auxAnimal.getComments() != null && !auxAnimal.getComments().isEmpty()) {

            findViewById(R.id.tvCommentsTitle).setVisibility(View.VISIBLE);
            rvComments.setVisibility(View.VISIBLE);

            rvComments.setLayoutManager(new LinearLayoutManager(this));

            commentAdaptador = new CommentAdaptador(
                    this,
                    //auxAnimal.getComments(),
                    new ArrayList<>(auxAnimal.getComments()),
                    this,
                    true// <-- porque permite editar/apagar
            );

            rvComments.setAdapter(commentAdaptador);
        }
        loadMeta();
    }

    @Override
    public void onGetAnimalEditError(VolleyError error) {
        Toast.makeText(getApplicationContext(),"Erro ao carregar animal", Toast.LENGTH_SHORT).show();
        finish();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        AppSingleton.getInstance(this).setGetAnimalEditListener(null);
        AppSingleton.getInstance(this).setCreateAnimalListener(null);
        AppSingleton.getInstance(this).setUpdateAnimalListener(null);
        AppSingleton.getInstance(this).setDeleteAnimalPhotosListener(null);
        AppSingleton.getInstance(this).setUploadAnimalPhotosListener(null);
        AppSingleton.getInstance(this).setCommentDeleteListener(this);
        AppSingleton.getInstance(this).setCommentActionListener(this);
    }


    //METODOS DO LISTENER DO UPDATE ANIMAL
    @Override
    public void onUpdateAnimalSuccess() {
        // apagar fotos removidas
        deleteRemovedPhotos();
        // enviar fotos novas
        uploadNewPhotos(animalId);
    }

    @Override
    public void onUpdateAnimalError(VolleyError error) {
        Toast.makeText(this, error != null && error.getMessage() != null ? error.getMessage() : "Erro na comunicação com o servidor", Toast.LENGTH_SHORT).show();
    }

    //METODOS DO LISTENER DOS UPLOAD DA FOTOS DOS ANIMAIS
    @Override
    public void onUploadAnimalPhotosSuccess() {
        if (isEditMode) {
            Toast.makeText(getApplicationContext(), "Animal atualizado com sucesso.", Toast.LENGTH_SHORT).show();
        }else {
            Toast.makeText(getApplicationContext(), "Animal criado com sucesso.", Toast.LENGTH_SHORT).show();
        }
        finish();

    }

    @Override
    public void onUploadAnimalPhotosError(VolleyError error) {
        Toast.makeText(this, error != null && error.getMessage() != null
                        ? error.getMessage()
                        : "Erro na comunicação com o servidor",
                Toast.LENGTH_SHORT
        ).show();
    }

    //METODOS DO LISTENER DO DELETE FOTOS
    @Override
    public void onDeleteAnimalPhotosSuccess() {
        // correu bem mas não precisamos fazer nada aqui
    }

    @Override
    public void onDeleteAnimalPhotosError(VolleyError error) {
        Toast.makeText(getApplicationContext(),
                error != null && error.getMessage() != null
                        ? error.getMessage()
                        : "Erro ao remover fotos",
                Toast.LENGTH_SHORT
        ).show();
    }





    //METODOS DO LISTENER DO DELETE COMMENTS
    @Override
    public void onDeleteCommentSuccess(int commentId) {
        Toast.makeText(this, "Comentário apagado!", Toast.LENGTH_SHORT).show();

        // Vai buscar à BD (que já foi atualizada pelo removerCommentBD)
        ArrayList<Comment> commentsAtualizados = AppSingleton.getInstance(this)
                .appBD.getCommentsByAnimalId(animalId);

        Log.d("DEBUG", "BD tem: " + commentsAtualizados.size() + " comentários");

        commentAdaptador.setComments(commentsAtualizados);

        // SÓ esconde se realmente ficou vazia
        if (commentsAtualizados.isEmpty()) {
            findViewById(R.id.tvCommentsTitle).setVisibility(View.GONE);
            rvComments.setVisibility(View.GONE);
        }

    }

    @Override
    public void onDeleteCommentError(String error) {
        Toast.makeText(getApplicationContext(), error, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onEditComment(Comment comment, int position) {
        final TextInputEditText input = new TextInputEditText(this);
        input.setText(comment.getText());
        input.setSelection(comment.getText().length());

        new AlertDialog.Builder(this)
                .setTitle("Editar comentário")
                .setView(input)
                .setPositiveButton("Guardar", (d, w) -> {

                    String newText = input.getText().toString().trim();

                    if (newText.isEmpty()) {
                        Toast.makeText(this, "O comentário não pode ficar vazio", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    AppSingleton.getInstance(this).updateCommentAPI(this, comment.getIdComment(), newText);
                })
                .setNegativeButton("Cancelar", null)
                .show();


    }

    @Override
    public void onDeleteComment(Comment comment, int position) {
        new AlertDialog.Builder(this)
                .setTitle("Apagar comentário")
                .setMessage("Tem a certeza que quer apagar este comentário?")
                .setPositiveButton("Apagar", (d, w) -> {
                    AppSingleton.getInstance(this).deleteCommentAPI(this, comment.getIdComment());
                })
                .setNegativeButton("Cancelar", null)
                .show();
    }

    //Metodos do Listener do Update Comments
    @Override
    public void onUpdateCommentSuccess(Comment comment) {
        Toast.makeText(this, "Comentário Alterado com sucesso!", Toast.LENGTH_SHORT).show();

        // Vai buscar à BD (que já foi atualizada pelo removerCommentBD)
        ArrayList<Comment> commentsAtualizados = AppSingleton.getInstance(this)
                .appBD.getCommentsByAnimalId(animalId);

        Log.d("DEBUG", "BD tem: " + commentsAtualizados.size() + " comentários");

        commentAdaptador.setComments(commentsAtualizados);
    }

    @Override
    public void onUpdateCommentError(String error) {
        Toast.makeText(getApplicationContext(), error, Toast.LENGTH_SHORT).show();
    }
}