package pt.ipleiria.estg.dei.projetoandroid;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import pt.ipleiria.estg.dei.projetoandroid.utils.ServerConfig;

public class SettingsActivity extends AppCompatActivity {

    private static final String PREFS = "SERVER_CONFIG";
    private static final String KEY_HOST = "HOST";
    private static final String KEY_PORT = "PORT";
    private static final String DEFAULT_HOST = "172.20.10.2";
    private static final int DEFAULT_PORT = 1883;
    private EditText et_ServerIp;
    private EditText et_ServerPort;
    private Button btSave;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_settings);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        et_ServerIp = findViewById(R.id.et_ServerIp);
       // et_ServerPort = findViewById(R.id.et_ServerPort);
        btSave = findViewById(R.id.bt_save);

        // 2) Carregar valores atuais
        loadCurrentConfig();

        // 3) Clique no botão
        btSave.setOnClickListener(v -> saveConfig());
    }

    private void loadCurrentConfig() {
        String apiBase = ServerConfig.getApiBase(this);
        et_ServerIp.setText(apiBase);
    }

    private void saveConfig() {
        String apiBase = et_ServerIp.getText().toString().trim();

        if (apiBase.isEmpty()) {
            Toast.makeText(this, "Preenche o endereço da API", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!apiBase.startsWith("http://") && !apiBase.startsWith("https://")) {
            Toast.makeText(this, "O endereço deve começar por http:// ou https://", Toast.LENGTH_SHORT).show();
            return;
        }

        ServerConfig.saveApiBase(this, apiBase);

        Toast.makeText(this, "Configuração guardada", Toast.LENGTH_SHORT).show();
        finish();
    }
}