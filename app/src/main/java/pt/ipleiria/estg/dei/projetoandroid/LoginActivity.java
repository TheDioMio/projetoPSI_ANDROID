package pt.ipleiria.estg.dei.projetoandroid;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import pt.ipleiria.estg.dei.projetoandroid.listeners.LoginListener;
import pt.ipleiria.estg.dei.projetoandroid.modelo.AppSingleton;
import pt.ipleiria.estg.dei.projetoandroid.modelo.User;
import pt.ipleiria.estg.dei.projetoandroid.utils.ServerConfig;

public class LoginActivity extends AppCompatActivity implements LoginListener {
    private TextView username, password;
    private Button btnLogin, btnSignup;
    private String email, avatar;
    private int user_id;
    private ImageButton btnSettings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        password = findViewById(R.id.etPassword_);
        username = findViewById(R.id.etUsername);
        btnLogin = findViewById(R.id.btnLogin);
        btnSignup = findViewById(R.id.btnSignup);
        btnSettings = findViewById(R.id.btnSettings);

        AppSingleton.getInstance(getApplicationContext()).setLoginListener(this);

        //carrega o caminho para a API da sharedPreferences
        AppSingleton.getInstance(getApplicationContext()).setEndereco(ServerConfig.getApiBase(getApplicationContext()));

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!isUsernameValid(username.getText().toString())) {
                    username.setError("Email inválido!!");
                    Toast.makeText(LoginActivity.this, "Email inválido!!", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (!isPasswordValid(password.getText().toString())) {
                    password.setError("Password inválida!!");
                    Toast.makeText(LoginActivity.this, "Password inválida!!", Toast.LENGTH_SHORT).show();
                    password.setText("");
                    return;
                }
                AppSingleton.getInstance(getApplicationContext()).loginAPI(username.getText().toString(), password.getText().toString(), getApplicationContext());

            }
        });

        btnSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), SignupActivity.class);
                startActivity(intent);
            }
        });

        btnSettings.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, SettingsActivity.class);
            startActivity(intent);
        });


    }

    @Override
    protected void onResume() {
        super.onResume();

        if (password != null) {
            password.setText("");
        }

    }

    private boolean isUsernameValid(String username){
        if ((username==null)||(username.isEmpty())){
            return false;
        }else if (username.length()<4){
            return false;
        }else
            return true;
    }

    private boolean isPasswordValid(String password){

        if ((password==null)||(password.isEmpty())){
            return false;
        }else if (password.length()<4){
            return false;
        }else
            return true;
    }

    @Override
    public void onValidateLogin(String token) {
        if(token != null) {
            //GUARDA O TOKEN NA SHARED PREFERENCES
            SharedPreferences sp = getSharedPreferences("DADOS_USER", Context.MODE_PRIVATE);
            sp.edit().putString(MenuMainActivity.TOKEN, token).apply();

            Intent intent = new Intent(getApplicationContext(), MenuMainActivity.class);
            startActivity(intent);
            finish();
        }else{
            Toast.makeText(this, R.string.txt_credenciais_invalidas, Toast.LENGTH_SHORT).show();
        }
    }
}