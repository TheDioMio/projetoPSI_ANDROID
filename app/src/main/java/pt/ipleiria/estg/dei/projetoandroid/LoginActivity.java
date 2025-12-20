package pt.ipleiria.estg.dei.projetoandroid;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
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

public class LoginActivity extends AppCompatActivity implements LoginListener {
    private TextView username, password;
    private Button btnLogin, btnSignup;
    private String email, avatar;
    private int user_id;

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

        AppSingleton.getInstance(getApplicationContext()).setLoginListener(this);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                codigo da ficha books
//                if (!isEmailValido(email.getText().toString())) {
//                    Toast.makeText(LoginActivity.this, "Email inválido!!", Toast.LENGTH_SHORT).show();
//                    return;
//                }
//
//                if (!isPasswordValida(password.getText().toString())) {
//                    Toast.makeText(LoginActivity.this, "Password inválida!!", Toast.LENGTH_SHORT).show();
//                    password.setText("");
//                    return;
//                }
//
//                SingletonGestorLivros.getInstance(getApplicationContext()).loginAPI(email.getText().toString(), password.getText().toString(), getApplicationContext());



//                String password = LoginActivity.this.password.getText().toString();
//                String username = LoginActivity.this.username.getText().toString();

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


//                User user = AppSingleton.getInstance().getUserLogin(username, password);
//
//                if (user != null){
//                    Intent intent = new Intent(getApplicationContext(), MenuMainActivity.class);
//                    intent.putExtra(MenuMainActivity.IDUSER, user.getId());
//                    startActivity(intent);
//                }else {
//                    Toast.makeText(LoginActivity.this, "Credenciais inválidas!", Toast.LENGTH_SHORT).show();
//                }

            }
        });

        btnSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), SignupActivity.class);
                startActivity(intent);
            }
        });
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
            SharedPreferences sp = getSharedPreferences("TOKEN", Context.MODE_PRIVATE);
            sp.edit().putString("token", token).apply();

            Intent intent = new Intent(getApplicationContext(), MenuMainActivity.class);
            intent.putExtra(MenuMainActivity.TOKEN, token);
            startActivity(intent);
            finish();
        }else{
            Toast.makeText(this, R.string.txt_credenciais_invalidas, Toast.LENGTH_SHORT).show();
        }
    }
}