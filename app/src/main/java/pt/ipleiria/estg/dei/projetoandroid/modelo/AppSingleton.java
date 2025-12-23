package pt.ipleiria.estg.dei.projetoandroid.modelo;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Base64;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.android.volley.AuthFailureError;
import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import pt.ipleiria.estg.dei.projetoandroid.MenuMainActivity;
import pt.ipleiria.estg.dei.projetoandroid.R;
import pt.ipleiria.estg.dei.projetoandroid.listeners.ApplicationListener;
import pt.ipleiria.estg.dei.projetoandroid.listeners.LoginListener;
import pt.ipleiria.estg.dei.projetoandroid.listeners.MenuListener;
import pt.ipleiria.estg.dei.projetoandroid.listeners.MessagesListener;
import pt.ipleiria.estg.dei.projetoandroid.utils.ApplicationJsonParser;
import pt.ipleiria.estg.dei.projetoandroid.utils.MessageJsonParser;
import pt.ipleiria.estg.dei.projetoandroid.utils.UserJsonParser;

public class AppSingleton {

    private static RequestQueue volleyQueue;

    public String endereco = "http://10.0.2.2/projetoPSI_WEB/backend/web/api";
    //endereço para as imagens
    public static final String FRONTEND_BASE_URL = "http://10.0.2.2/projetoPSI_WEB/frontend/web";
    private String getmUrlAPILogin = endereco+"/auth/login";
    private String getmUrlAPIMe = endereco+"/users/me";
    private String getMessageURL = endereco+"/messages";
    private String getSentApplications = endereco+"application/sent";

    public void setLoginListener(LoginListener loginListener) {
        this.loginListener = loginListener;
    }
    private LoginListener loginListener;
    private MenuListener menuListener;
    private ApplicationListener applicationListener;

    private Me me;
    private ArrayList<Application> applications;

    private MessagesListener messagesListener;



    private static AppSingleton instance = null;
    private GestorAnimals gestorAnimals = new GestorAnimals();
    private GestorUsers gestorUsers = new GestorUsers();
    private GestorAnimalType gestorAnimalType = new GestorAnimalType();
    private GestorAnimalBreed gestorAnimalBreed = new GestorAnimalBreed();
    private GestorAnimalAge gestorAnimalAge = new GestorAnimalAge();
    private GestorAnimalSize gestorAnimalSize = new GestorAnimalSize();
    private GestorVaccination gestorVaccination = new GestorVaccination();


    public static synchronized AppSingleton getInstance(Context context){
        if (instance == null){
            instance = new AppSingleton();
            volleyQueue = Volley.newRequestQueue(context);
        }
        return instance;
    }

    public void setMenuListener(MenuListener menuListener) {
        this.menuListener = menuListener;
    }

    // -------------------------
    // GESTOR ANIMAL
    // -------------------------
    public ArrayList<Animal> getAnimals() {

        return gestorAnimals.getAnimals();
    }

    public Animal getAnimal(int idAnimal){
        return gestorAnimals.getAnimal(idAnimal);
    }
    /*--> MÉTODOS DE ANIMAIS <--*/

    public void addAnimal(Animal animal) {
        gestorAnimals.addAnimal(animal);
    }

    public ArrayList<Animal> getAnimalsByUser(int userId) {
        return gestorAnimals.getAnimalsByUser(userId);
    }

    // -------------------------
    // GESTOR User
    // -------------------------

    public User getUserLogin(String username, String password){
        return gestorUsers.getUserLogin(username, password);
    }

    public User getUser(int idUser){
        return gestorUsers.getUser(idUser);
    }

    public void addUser(String username, String email, String password) {
        gestorUsers.addUser(username, email, password);
    }

    // -------------------------
    // GESTOR ANIMAL TYPE
    // -------------------------
    public ArrayList<AnimalType> getAnimalTypes() {
        return gestorAnimalType.getAnimalTypes();
    }

    public AnimalType getAnimalType(int id) {
        return gestorAnimalType.getAnimalType(id);
    }

    // -------------------------
    // GESTOR BREED
    // -------------------------
    public ArrayList<AnimalBreed> getBreedsByAnimalType(int tipoId) {
        return gestorAnimalBreed.getBreedsByType(tipoId);
    }

    public AnimalBreed getBreed(int id) {
        return gestorAnimalBreed.getAnimalBreed(id);
    }

    // -------------------------
    // GESTOR AGE
    // -------------------------
    public ArrayList<String> getAnimalAgesStrings() {
        return gestorAnimalAge.getAgesStrings();
    }

    // -------------------------
    // GESTOR SIZE
    // -------------------------
    public ArrayList<String> getAnimalSizesStrings() {
        return gestorAnimalSize.getSizesStrings();
    }

    public ArrayList<AnimalSize> getAnimalSizes() {
        return gestorAnimalSize.getSizes();
    }

    // -------------------------
    // GESTOR Vaccination
    // -------------------------
    public ArrayList<String> getVaccinationStrings() {
        return gestorVaccination.getVaccinationStrings();
    }
    public ArrayList<Vaccination> getVaccinations() {
        return gestorVaccination.getVaccinations();
    }

    // -------------------------
    // GESTOR Application
    // -------------------------
//    public void addApplication(String user_id, int animal_id, String description) {
//        int newId;
//        if(applications.isEmpty()){
//            newId = 1;
//        } else {
//            newId = applications.get(applications.size() -1).getId() + 1;
//        }
//
//        //Valores que foram feitos padrão:
//        int status = 0;
//        int type = 0;
//        int target_user_id = 1;
//
//        applications.add(new Application(
//                newId,              // id
//                status,             // status
//                description,        // description
//                user_id,            // userId
//                animal_id,          // animalId
//                type,               // type
//                "2023-01-01",       // createdAt (Valor provisório)
//                target_user_id,     // targetUserId
//                "{}",               // data (Valor provisório: JSON vazio)
//                "",                 // statusDate (Valor provisório)
//                0                   // isRead (0 = não lido)
//        ));
//    }

    public ArrayList<Application> getApplications() {
        return new ArrayList<>(applications);
    }

    // FUNÇÃO QUE PEDE AS CANDIDATURAS ENVIADAS/RECEBIDAS DO UTILIZADOR
    public void getApplicationsAPI(final Context context, String type, final ApplicationListener listener) {
        String url = endereco + "/applications/" + type;

        JsonArrayRequest req = new JsonArrayRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            // Tenta converter
                            ArrayList<Application> lista = ApplicationJsonParser.parserJsonApplications(response);
                            if (listener != null) {
                                listener.onRefreshList(lista);
                            }
                        } catch (Exception e) {
                            // Se falhar aqui, é erro no Parser (campos errados ou nulos)
                            e.printStackTrace();
                            Toast.makeText(context, "Erro ao processar dados: " + e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        String message = "Erro desconhecido";

                        if (error.networkResponse != null) {
                            // Se o servidor respondeu (ex: 404, 500, 401)
                            message = "Erro Servidor: " + error.networkResponse.statusCode;
                            try {
                                // Tenta ler o que o servidor escreveu no erro
                                String data = new String(error.networkResponse.data, "UTF-8");
                                android.util.Log.e("ERRO_API", data); // Vê no Logcat do Android Studio
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        } else if (error instanceof com.android.volley.ParseError) {
                            message = "Erro de Formato (ParseError): O JSON não veio como esperado.";
                        } else if (error instanceof com.android.volley.NoConnectionError) {
                            message = "Sem ligação à Internet";
                        } else if (error instanceof com.android.volley.TimeoutError) {
                            message = "O servidor demorou muito a responder";
                        }

                        Toast.makeText(context, message, Toast.LENGTH_LONG).show();
                        error.printStackTrace(); // Imprime o erro completo na consola
                    }
                }) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                String token = getToken(context);
                if (token != null) {
                    headers.put("Authorization", "Bearer " + token);
                }
                return headers;
            }
        };
        volleyQueue.add(req);
    }
    // -------------------------
    // FIM GESTOR Application
    // -------------------------




    // -------------------------
    // GESTOR Message
    // -------------------------

    public void setMessageListener(MessagesListener messageListener) {
        this.messagesListener = messageListener;
    }

    public void getAllMessagesAPI(final Context context) {

        JsonArrayRequest request = new JsonArrayRequest(
                Request.Method.GET,
                getMessageURL,
                null,
                (JSONArray response) -> {
                    ArrayList<Message> lista = MessageJsonParser.parserJsonMessages(response);
                    if (messagesListener != null) {
                        messagesListener.onRefreshListaMessages(lista);
                    }
                },
                error -> {
                    String msg;
                    if (error.networkResponse != null) {
                        msg = "Erro " + error.networkResponse.statusCode;
                    } else {
                        msg = "Erro de ligação ao servidor";
                    }

                    if (messagesListener != null) {
                        messagesListener.onErro(msg);
                    }
                }
        ) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                headers.put("Accept", "application/json");

                String token = getToken(context);
                if (token != null && !token.isEmpty()) {
                    headers.put("Authorization", "Bearer " + token);
                }

                return headers;
            }
        };
        volleyQueue.add(request);
    }

    public interface SendMessageListener {
        void onSuccess();
        void onError(String erro);
    }

    public void enviarMensagemAPI(final int receiverId,
                                  final String subject,
                                  final String text,
                                  final Context context,
                                  final SendMessageListener listener) {

        StringRequest request = new StringRequest(
                Request.Method.POST,
                getMessageURL,
                response -> {
                    if (listener != null) listener.onSuccess();
                },
                error -> {
                    String msg = (error.networkResponse != null)
                            ? "Erro " + error.networkResponse.statusCode
                            : "Erro de ligação ao servidor";
                    if (listener != null) listener.onError(msg);
                }
        ) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("receiver_user_id", String.valueOf(receiverId));
                params.put("subject", subject);
                params.put("text", text);
                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                headers.put("Accept", "application/json");

                String token = getToken(context);
                if (token != null && !token.isEmpty()) {
                    headers.put("Authorization", "Bearer " + token);
                }
                return headers;
            }
        };

        volleyQueue.add(request);
    }

    //*************************************** Fim Mensagens *************************************


    // FUNÇÃO UTILIZADA PARA FAZER O LOGIN, PARA DEPOIS GUARDAR O TOKEN NA SHARED PREFERENCES
    // RECEBE O TOKEN
    public void loginAPI(final String username, final String password, final Context context){
        if(!UserJsonParser.isConnectionInternet(context)){
            Toast.makeText(context, R.string.txt_nao_tem_internet, Toast.LENGTH_SHORT).show();
        }else{
            StringRequest request = new StringRequest(Request.Method.POST, getmUrlAPILogin, new Response.Listener<String>() {
                @Override
                public void onResponse(String s) {
                    String token = UserJsonParser.parserJsonLogin(s);
                    if(loginListener != null) {
                        loginListener.onValidateLogin(token);
                    }
                }
            }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        int statusCode = -1;

                        if (error.networkResponse != null) {
                            statusCode = error.networkResponse.statusCode;
                        }

                        String msg = "Erro desconhecido";

                        if (statusCode == 401) {
                            msg = "Credenciais inválidas";
                        } else if (statusCode == 404) {
                            msg = "Endpoint não encontrado";
                        } else if (statusCode == 500) {
                            msg = "Erro interno do servidor";
                        } else if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                            msg = "Sem ligação ao servidor";
                        }

                        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
                    }

            }){
                @Nullable
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {

                    String credentials = username + ":" + password;
                    String auth = "Basic " + Base64.encodeToString(
                            credentials.getBytes(),
                            Base64.NO_WRAP
                    );

                    Map<String, String> headers = new HashMap<>();
                    headers.put("Authorization", auth);
                    headers.put("Accept", "application/json");

                    return headers;
                }
            };
            volleyQueue.add(request);
        }
    }


    // FUNÇÃO QUE PEDE OS DADOS DO UTILIZADOR QUE ESTÁ LOGADO PARA DEPOIS GUARDAR NA SHARED PREFERENCES
    // ENVIA O TOKEN
    // RECEBE OS DADOS DO UTILIZADOR  
    public void getMe( final Context context){
        if(!UserJsonParser.isConnectionInternet(context)){
            Toast.makeText(context, R.string.txt_nao_tem_internet, Toast.LENGTH_SHORT).show();
        }else{
            JsonObjectRequest request = new JsonObjectRequest (Request.Method.GET, getmUrlAPIMe, null, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    //atualiza o singleton
                    me = UserJsonParser.parserJsonMe(response);

                    if (menuListener != null) {
                        menuListener.onRefreshMenu(me);
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                    String msg;

                    if (error.networkResponse != null) {
                        int statusCode = error.networkResponse.statusCode;

                        if (statusCode == 401) {
                            msg = "Sessão expirada ou não autorizado";
                        } else if (statusCode == 404) {
                            msg = "Endpoint não encontrado";
                        } else if (statusCode == 500) {
                            msg = "Erro interno do servidor";
                        } else {
                            msg = "Erro HTTP: " + statusCode;
                        }

                    } else if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                        msg = "Sem ligação ao servidor";
                    } else {
                        msg = "Erro desconhecido";
                    }

                    Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
                }
            }){
                @Nullable
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String, String> headers = new HashMap<>();
                    String token = getToken(context);
                    if (token != null && !token.isEmpty()) {
                        headers.put("Authorization", "Bearer " + token);
                    }
                    return headers;
                }
            };
            volleyQueue.add(request);
        }
    }

    public String getToken(Context context) {
        SharedPreferences sharedPreferences =
                context.getSharedPreferences("DADOS_USER", Context.MODE_PRIVATE);

        return sharedPreferences.getString(MenuMainActivity.TOKEN, null);
    }


    // FUNÇÃO PARA CRIAR UMA NOVA CANDIDATURA (POST)
    public void addApplicationAPI(final Context context, int userId, int animalId, String description, String data, final ApplicationListener listener) {

        // 1. Definir o Endpoint (URL)
        // Se o teu backend seguir o padrão REST, para criar é POST em /applications
        String url = endereco + "/applications";

        // 2. Criar o Objeto JSON com os dados para enviar
        JSONObject jsonBody = new JSONObject();
        try {
            jsonBody.put("user_id", userId);
            jsonBody.put("animal_id", animalId);
            jsonBody.put("description", description);

            // "data" contém aquele JSON extra (idade, habitação, etc.) convertido em String
            jsonBody.put("data", data);

            // Opcional: Se o backend exigir, podes enviar o status inicial (0 = pendente)
            // jsonBody.put("status", 0);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        // 3. Criar o Pedido Volley
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, jsonBody,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // Sucesso! O backend respondeu que criou a candidatura.
                        if (listener != null) {
                            // Chamamos o listener para avisar o Fragmento que correu tudo bem
                            listener.onRefreshList(null);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Erro
                        String mensagem = "Erro ao enviar candidatura";
                        if (error.networkResponse != null) {
                            mensagem += " (Código: " + error.networkResponse.statusCode + ")";
                        }
                        Toast.makeText(context, mensagem, Toast.LENGTH_SHORT).show();
                    }
                }) {

            // 4. Autenticação (Enviar o Token no cabeçalho)
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                String token = getToken(context); // Usamos o método auxiliar que já tens no Singleton
                if (token != null && !token.isEmpty()) {
                    headers.put("Authorization", "Bearer " + token);
                }
                headers.put("Content-Type", "application/json");
                return headers;
            }
        };

        // 5. Adicionar à fila de pedidos
        volleyQueue.add(request);
    }
}
