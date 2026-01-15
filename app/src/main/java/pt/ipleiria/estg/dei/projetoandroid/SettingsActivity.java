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
        et_ServerPort = findViewById(R.id.et_ServerPort);
        btSave = findViewById(R.id.bt_save);

        // 2) Carregar valores atuais
        loadCurrentConfig();

        // 3) Clique no botão
        btSave.setOnClickListener(v -> saveConfig());
    }

    private void loadCurrentConfig() {
        String host = pt.ipleiria.estg.dei.projetoandroid.utils.ServerConfig.getApiHost(this);
        int port = pt.ipleiria.estg.dei.projetoandroid.utils.ServerConfig.getApiPort(this);

        et_ServerIp.setText(host);
        et_ServerPort.setText(String.valueOf(port));
    }

    private void saveConfig() {
        String host = et_ServerIp.getText().toString().trim();
        String portStr = et_ServerPort.getText().toString().trim();

        if (host.isEmpty() || portStr.isEmpty()) {
            Toast.makeText(this, "Preenche IP e Porta", Toast.LENGTH_SHORT).show();
            return;
        }

        int port;
        try {
            port = Integer.parseInt(portStr);
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Porta inválida", Toast.LENGTH_SHORT).show();
            return;
        }

        if (port < 1 || port > 65535) {
            Toast.makeText(this, "Porta deve estar entre 1 e 65535", Toast.LENGTH_SHORT).show();
            return;
        }

        pt.ipleiria.estg.dei.projetoandroid.utils.ServerConfig.saveApi(this, host, port);

        Toast.makeText(this, "Configuração guardada", Toast.LENGTH_SHORT).show();
        finish();
    }
}