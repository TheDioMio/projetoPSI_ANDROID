package pt.ipleiria.estg.dei.projetoandroid;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.bumptech.glide.Glide;
import com.google.android.material.snackbar.Snackbar;

import pt.ipleiria.estg.dei.projetoandroid.listeners.AvatarUploadListener;
import pt.ipleiria.estg.dei.projetoandroid.listeners.UserUpdateListener;
import pt.ipleiria.estg.dei.projetoandroid.modelo.AppSingleton;
import pt.ipleiria.estg.dei.projetoandroid.modelo.Me;
import pt.ipleiria.estg.dei.projetoandroid.modelo.User;

public class ProfileActivity extends AppCompatActivity {

    EditText etName, etUsername, etEmail, etLocation;
    EditText etPassword, etNewPassword, etRptPassword;
    ImageView imgProfile;
    Button btnSave, btnCancel;
    ImageButton imgProfileChange;
    private View rootView;
    private static final int PICK_IMAGE = 1000;
    private Uri avatarUri;
    private static final int PERMISSION_REQUEST = 2000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_profile);
        rootView= findViewById(android.R.id.content);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        etName = findViewById(R.id.et_name);
        etUsername = findViewById(R.id.et_username);
        etEmail = findViewById(R.id.et_email);
        etLocation = findViewById(R.id.et_location);

        etPassword = findViewById(R.id.et_password);
        etNewPassword = findViewById(R.id.et_newPassword);
        etRptPassword = findViewById(R.id.et_rpt_new_password);

        imgProfileChange = findViewById(R.id.imgProfileChange);
        imgProfile = findViewById(R.id.imgProfile);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(R.string.txt_user_details);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        carregarDados();

        imgProfileChange.setOnClickListener(v -> escolherImagem());

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_animal_form, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.action_save) {

            //primeiro ver se tem internet
            if (!AppSingleton.getInstance(getApplicationContext()).isConnectionInternet(getApplicationContext())) {
                Snackbar.make(rootView,
                                R.string.txt_offline_indisponivel,
                                Snackbar.LENGTH_INDEFINITE)
                        .setAction(R.string.txt_ok, vv -> {})
                        .show();
                return true;
            }


            SharedPreferences sp = getSharedPreferences("DADOS_USER", MODE_PRIVATE);
            int userId = sp.getInt("USER_ID_INT", -1);

            if (userId == -1) {
                Toast.makeText(this, "ID do utilizador não encontrado", Toast.LENGTH_SHORT).show();
                return true;
            }

            Me me = new Me(
                    userId,
                    etName.getText().toString(),
                    etUsername.getText().toString(),
                    null,
                    etLocation.getText().toString(),
                    etEmail.getText().toString()
            );

            AppSingleton.getInstance(this).updateMe(
                    this,
                    me,
                    new UserUpdateListener() {

                        @Override
                        public void onUpdateSuccess(Me updatedUser) {
                            Toast.makeText(ProfileActivity.this, R.string.txt_utilizador_atualizado_com_sucesso, Toast.LENGTH_LONG).show();
                            setResult(Activity.RESULT_OK);
                            finish();
                        }

                        @Override
                        public void onUpdateError(String error) {
                            Toast.makeText(ProfileActivity.this, error, Toast.LENGTH_LONG).show();
                        }
                    }
            );




        } else if (item.getItemId() == android.R.id.home) {
            // Fecha a Activity quando clicas na seta
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }






    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }


    private void carregarDados() {
        SharedPreferences sp = getSharedPreferences("DADOS_USER", MODE_PRIVATE);

        etName.setText(sp.getString(MenuMainActivity.NAME, ""));
        etUsername.setText(sp.getString(MenuMainActivity.USERNAME, ""));
        etEmail.setText(sp.getString(MenuMainActivity.EMAIL, ""));
        etLocation.setText(sp.getString(MenuMainActivity.ADDRESS, ""));

        String avatarPath = sp.getString(MenuMainActivity.IMGAVATAR, null);

        if (avatarPath != null && !avatarPath.isEmpty()) {
            String avatarUrl = AppSingleton.getInstance(getApplicationContext()).getEndereco() + AppSingleton.getInstance(this).FRONTEND_BASE_URL + avatarPath;

            Glide.with(this)
                    .load(avatarUrl)
                    .placeholder(R.mipmap.default_avatar)
                    .error(R.mipmap.default_avatar)
                    .circleCrop()
                    .into(imgProfile);
        }else{
            imgProfile.setImageResource(R.mipmap.default_avatar);
        }
    }

    private void escolherImagem() {

        if (!AppSingleton.getInstance(getApplicationContext()).isConnectionInternet(getApplicationContext())) {
            Snackbar.make(rootView,
                            R.string.txt_offline_indisponivel,
                            Snackbar.LENGTH_INDEFINITE)
                    .setAction(R.string.txt_ok, vv -> {})
                    .show();
            return ;
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (checkSelfPermission(Manifest.permission.READ_MEDIA_IMAGES)
                    != PackageManager.PERMISSION_GRANTED) {

                requestPermissions(
                        new String[]{Manifest.permission.READ_MEDIA_IMAGES},
                        PERMISSION_REQUEST
                );
                return;
            }
        }

        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("image/*");

        startActivityForResult(intent, PICK_IMAGE);
//        Intent intent = new Intent(Intent.ACTION_PICK);
//        intent.setType("image/*");
//        startActivityForResult(intent, PICK_IMAGE);
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE && resultCode == RESULT_OK && data != null) {

            avatarUri = data.getData();

            // 🔒 Mantém permissão permanente para ler a imagem
            getContentResolver().takePersistableUriPermission(
                    avatarUri,
                    Intent.FLAG_GRANT_READ_URI_PERMISSION
            );

            // 👁 Preview imediato
            imgProfile.setImageURI(avatarUri);

            // ⬆ Upload para a API
            uploadAvatar();
        }
    }


    private void uploadAvatar() {
        AppSingleton.getInstance(this).uploadAvatar(
                this,
                avatarUri,
                new AvatarUploadListener() {
                    @Override
                    public void onSuccess(String avatarPath) {

                        Toast.makeText(
                                ProfileActivity.this,
                                "Avatar atualizado",
                                Toast.LENGTH_SHORT
                        ).show();

                        // 🔔 informa o Menu que houve atualização
                        setResult(Activity.RESULT_OK);
                    }

                    @Override
                    public void onError(String message) {
                        Toast.makeText(
                                ProfileActivity.this,
                                message,
                                Toast.LENGTH_SHORT
                        ).show();
                    }
                }
        );
    }

}