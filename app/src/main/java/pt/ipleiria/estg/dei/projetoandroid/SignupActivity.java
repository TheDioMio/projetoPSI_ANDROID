package pt.ipleiria.estg.dei.projetoandroid;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.snackbar.Snackbar;

import pt.ipleiria.estg.dei.projetoandroid.listeners.SignupListener;
import pt.ipleiria.estg.dei.projetoandroid.modelo.AppSingleton;

public class SignupActivity extends AppCompatActivity implements SignupListener {

    private EditText etUsername, etPassword, etEmail;
    private Button btnSignup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_signup);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        AppSingleton.getInstance(this).setSignupListener(this);

        etUsername = findViewById(R.id.etUsername);
        etPassword = findViewById(R.id.etPassword);
        etEmail = findViewById(R.id.etEmail);
        btnSignup = findViewById(R.id.btnSignup);

        btnSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!AppSingleton.getInstance(getApplicationContext()).isConnectionInternet(getApplicationContext())) {
                    Snackbar.make(findViewById(android.R.id.content),
                                    R.string.txt_offline_indisponivel,
                                    Snackbar.LENGTH_INDEFINITE)
                            .setAction(R.string.txt_ok, vv -> {})
                            .show();
                    return;
                }


                String username = etUsername.getText().toString();
                String password = etPassword.getText().toString();
                String email = etEmail.getText().toString();

                if(username.isEmpty() || password.isEmpty() || email.isEmpty()){
                    Toast.makeText(SignupActivity.this, R.string.txt_toast_registo_utilizador_erro_campos, Toast.LENGTH_SHORT).show();
                    return;
                }

                AppSingleton.getInstance(getApplicationContext()).signupAPI(username, email, password, getApplicationContext());

            }
        });
    }

    @Override
    public void onSignupResultListener(boolean success, String message) {


        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();

        if (success) {
            etUsername.setText("");
            etPassword.setText("");
            etEmail.setText("");
            finish();
        }
    }
}