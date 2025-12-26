package pt.ipleiria.estg.dei.projetoandroid.modelo;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
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
import pt.ipleiria.estg.dei.projetoandroid.listeners.ApplicationsListener;
import pt.ipleiria.estg.dei.projetoandroid.listeners.AnimalsListener;
import pt.ipleiria.estg.dei.projetoandroid.listeners.LoginListener;
import pt.ipleiria.estg.dei.projetoandroid.listeners.MenuListener;
import pt.ipleiria.estg.dei.projetoandroid.listeners.MessagesListener;
import pt.ipleiria.estg.dei.projetoandroid.utils.ApplicationJsonParser;
import pt.ipleiria.estg.dei.projetoandroid.utils.AnimalJsonParser;
import pt.ipleiria.estg.dei.projetoandroid.utils.MessageJsonParser;
import pt.ipleiria.estg.dei.projetoandroid.utils.UserJsonParser;

public class AppSingleton {

    private Context context;

    private static RequestQueue volleyQueue;

    //-------------------------------------------
    // Endpoints da API
    //--------------------------------------------
    //region ENDPOINTS
    public String endereco = "http://10.0.2.2/projetoPSI_WEB/backend/web/api";
    //endereço para as imagens
    public static final String FRONTEND_BASE_URL = "http://10.0.2.2/projetoPSI_WEB/frontend/web";
    private String getmUrlAPILogin = endereco+"/auth/login";
    private String getmUrlAPIMe = endereco+"/users/me";
    private String getMessageURL = endereco+"/messages";
    private String getSentApplications = endereco+"application/sent";

    private String getmUrlAPIAnimals = endereco+"/animals?expand=listing.comments.user.profileImage,user.profileImage";

    public static boolean isConnectionInternet(Context context){
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        //necessita de permissões de acesso a internet
        //e acesso ao estado la ligação
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();

        return activeNetwork != null && activeNetwork.isConnectedOrConnecting();
    }

    //endregion




    public void setLoginListener(LoginListener loginListener) {
        this.loginListener = loginListener;
    }

    public void setAnimalsListener(AnimalsListener animalsListener) {
        this.animalsListener = animalsListener;
    }

    public void setMenuListener(MenuListener menuListener) {
        this.menuListener = menuListener;
    }


    private LoginListener loginListener;
    private MenuListener menuListener;
    private ApplicationsListener applicationsListener;
    private ApplicationListener applicationListener;

    private AnimalsListener animalsListener;

    private AppDBHelper appBD = null;

    private Me me;
    private ArrayList<Application> applications;

    private MessagesListener messagesListener;

    private ArrayList<Comment> comments;




    private static AppSingleton instance = null;
//    private GestorAnimals gestorAnimals = new GestorAnimals();
    private GestorUsers gestorUsers = new GestorUsers();
//    private GestorAnimalType gestorAnimalType = new GestorAnimalType();
//    private GestorAnimalBreed gestorAnimalBreed = new GestorAnimalBreed();
//    private GestorAnimalAge gestorAnimalAge = new GestorAnimalAge();
//    private GestorAnimalSize gestorAnimalSize = new GestorAnimalSize();
//    private GestorVaccination gestorVaccination = new GestorVaccination();
    //private GestorApplication gestorApplication = new GestorApplication();
//    private GestorMessage gestorMessage = new GestorMessage();


    public static synchronized AppSingleton getInstance(Context context){
        if (instance == null){
            instance = new AppSingleton(context);
            volleyQueue = Volley.newRequestQueue(context);
        }
        return instance;
    }


    public AppSingleton(Context context) {
        this.context = context.getApplicationContext();
        appBD = AppDBHelper.getInstance(this.context);
        animals = new ArrayList<>();
    }




    // -------------------------
    // GESTOR ANIMAL
    // -------------------------

    private ArrayList<Animal> animals = new ArrayList<>();

    public ArrayList<Animal> getAnimals() {
        return animals;
    }



//    /*--> MÉTODOS DE ANIMAIS <--*/
//
//    public void addAnimal(Animal animal) {
//        gestorAnimals.addAnimal(animal);
//    }

//    public ArrayList<Animal> getAnimalsByUser(int userId) {
//        return gestorAnimals.getAnimalsByUser(userId);
//    }



    //--------------------------------------
    //----------------ANIMALS BD
    //--------------------------------------
    public void adicionarAnimalsBD(ArrayList<Animal> animals){
        AppDBHelper.getInstance(context).removerAllAnimalsBD();
        for (Animal a: animals){
            appBD.adicionarAnimalBD(a);
        }
    }

    public Animal getAnimalBD(int id) {
        for (Animal a : animals) {
            if (a.getId() == id) {
                return a;
            }
        }
        return AppDBHelper.getInstance(context).getAnimalById(id);
    }

    public ArrayList<Animal> getAllAnimalsBD() {
        animals = appBD.getAllAnimalsBD();
        return new ArrayList<>(animals);
    }

    public ArrayList<Application> getAllApplicationsBD() {
        applications = appBD.getAllApplicationsBD();
        return new ArrayList<>(applications);
    }



    public void adicionarCommentsBD(ArrayList<Comment> comments){
        for (Comment c: comments){
            appBD.adicionarCommentBD(c);
        }
    }

    public void adicionarFilesBD(ArrayList<AnimalFile> files){
        for (AnimalFile f: files){
            appBD.adicionarFileBD(f);
        }
    }



    public void getAnimalsAPI(final Context context) {

        //esta tarefa fica para o gestor
        if (!isConnectionInternet(context)) {
            Toast.makeText(context, R.string.txt_nao_tem_internet, Toast.LENGTH_SHORT).show();
            return;
        }

        JsonArrayRequest request = new JsonArrayRequest(
                Request.Method.GET,
                getmUrlAPIAnimals,
                null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {

                        //atualiza o singleton
                         animals = AnimalJsonParser.parserJsonAnimals(response);

                        //Guardar na BD local
                        appBD.removerAllAnimalsBD();          // limpa dados antigos dos animais dos comments e dos files
                        adicionarAnimalsBD(animals);
                        for (Animal a: animals){
                            adicionarCommentsBD(a.getComments());
                            adicionarFilesBD(a.getAnimalfiles());
                        }

                        // 4️⃣ Notificar UI (RecyclerView, etc.)
                        if (animalsListener != null) {
                            animalsListener.onRefreshAnimalsList(animals);
                        }
                    }
                },
                new Response.ErrorListener() {
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
                }
        ) {
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


    // -------------------------
    // GESTOR User
    // -------------------------

//    public User getUserLogin(String username, String password){
//        return gestorUsers.getUserLogin(username, password);
//    }
//
    public User getUser(int idUser){
        return gestorUsers.getUser(idUser);
    }
//
//    public void addUser(String username, String email, String password) {
//        gestorUsers.addUser(username, email, password);
//    }
//
//    // -------------------------
//    // GESTOR ANIMAL TYPE
//    // -------------------------
//    public ArrayList<AnimalType> getAnimalTypes() {
//        return gestorAnimalType.getAnimalTypes();
//    }
//
//    public AnimalType getAnimalType(int id) {
//        return gestorAnimalType.getAnimalType(id);
//    }
//
//    // -------------------------
//    // GESTOR BREED
//    // -------------------------
//    public ArrayList<AnimalBreed> getBreedsByAnimalType(int tipoId) {
//        return gestorAnimalBreed.getBreedsByType(tipoId);
//    }
//
//    public AnimalBreed getBreed(int id) {
//        return gestorAnimalBreed.getAnimalBreed(id);
//    }
//
//    // -------------------------
//    // GESTOR AGE
//    // -------------------------
//    public ArrayList<String> getAnimalAgesStrings() {
//        return gestorAnimalAge.getAgesStrings();
//    }
//
//    // -------------------------
//    // GESTOR SIZE
//    // -------------------------
//    public ArrayList<String> getAnimalSizesStrings() {
//        return gestorAnimalSize.getSizesStrings();
//    }
//
//    public ArrayList<AnimalSize> getAnimalSizes() {
//        return gestorAnimalSize.getSizes();
//    }
//
//    // -------------------------
//    // GESTOR Vaccination
//    // -------------------------
//    public ArrayList<String> getVaccinationStrings() {
//        return gestorVaccination.getVaccinationStrings();
//    }
//    public ArrayList<Vaccination> getVaccinations() {
//        return gestorVaccination.getVaccinations();
//    }
//
//

    // -------------------------
    // GESTOR Application
    // -------------------------
    public ArrayList<Application> getApplications() {
        return new ArrayList<>(applications);
    }

    public Application getApplication(int id) {
        for (Application a : applications) {
            if (a.getId() == id) {
                return a;
            }
        }

        // fallback BD
        return AppDBHelper.getInstance(context).getApplicationById(id);
    }

    public void getApplicationsAPI(final Context context, String type, final ApplicationsListener listener) {
        String url = endereco + "/application/" + type;

        JsonArrayRequest req = new JsonArrayRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray jsonArray) {
                        try {
                            // Tenta converter
                            applications = ApplicationJsonParser.parserJsonApplications(jsonArray);
                            //Limpar dados antigos das applications anteriores
                            appBD.removerAllApplicationsBD();
                            //Guardar na BD local
                            adicionarApplicationsBD(applications);
                            if (listener != null) {
                                listener.onRefreshList(applications);
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
                                android.util.Log.e("ERRO_API", data); // VEJAM NO LOGCAT!
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

    public void adicionarApplicationBD(Application application) {
        //Guarda na Base de Dados
        Application auxApp = appBD.adicionarApplicationBD(application);

        //Atualiza a lista em memória (Singleton)
        if (auxApp != null) {
            // Procura se já existe na lista para substituir (Update)
            Application existente = getApplication(auxApp.getId());

            if (existente != null) {
                applications.remove(existente);
                applications.add(auxApp);
            } else {
                applications.add(auxApp);
            }
        }
    }
    public void adicionarApplicationsBD(ArrayList<Application> listaApplications) {
        appBD.removerAllApplicationsBD();
        for (Application app : listaApplications) {
            appBD.adicionarApplicationBD(app);
        }
        // Atualiza a memória
        this.applications = new ArrayList<>(listaApplications);
    }
    public void editarApplicationAPI(final Application application, final Context context, final ApplicationListener listener) {
        if (!isConnectionInternet(context)) {
            Toast.makeText(context, "Sem ligação à internet", Toast.LENGTH_SHORT).show();
            return;
        }

        // A URL no SINGULAR (como configurámos no passo anterior)
        String url = endereco + "/application/" + application.getId();

        JSONObject jsonBody = new JSONObject();
        try {
            // converterStatusParaInt: Uma função auxiliar que vamos criar já a seguir
            int statusInt = converterStatusParaInt(application.getStatus());

            jsonBody.put("status", statusInt);
            // Agora envia {"status": 2} em vez de {"status": "Aprovada"}

        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.PUT, url, jsonBody,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        editarApplicationBD(application);
                        if (listener != null) {
                            listener.onRefreshDetails(application);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        String message = "Erro desconhecido";

                        if (error.networkResponse != null) {
                            message = "Erro API: " + error.networkResponse.statusCode;

                            // --- CÓDIGO NOVO PARA VER O ERRO 422 ---
                            try {
                                String responseBody = new String(error.networkResponse.data, "UTF-8");
                                // ISTO VAI IMPRIMIR O MOTIVO EXATO NO LOGCAT (Ex: "Status cannot be blank")
                                android.util.Log.e("ERRO_422_DETALHE", responseBody);

                                // Opcional: Mostra no Toast também para ser mais fácil veres já
                                message += " | " + responseBody;
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            // ---------------------------------------
                        }

                        if (listener != null) listener.onError(message);
                    }
                }) { // <--- ABRIR CHAVETA AQUI PARA O OVERRIDE

            // ESTA É A PARTE QUE FALTAVA: ENVIAR O TOKEN
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                String token = getToken(context);
                if (token != null && !token.isEmpty()) {
                    headers.put("Authorization", "Bearer " + token);
                }
                return headers;
            }
        }; // <--- FECHAR CHAVETA AQUI

        volleyQueue.add(request);
    }

    private int converterStatusParaInt(String statusTexto) {
        if (statusTexto == null) return 0; // Default (Pendente)

        switch (statusTexto) {
            case Application.STATUS_SENT:      // "Pendente"
                return 0;
            case Application.STATUS_PENDING:   // "Em análise"
                return 1;
            case Application.STATUS_APPROVED:  // "Aprovada"
                return 2;
            case Application.STATUS_REJECTED:  // "Rejeitada"
                return 3;
            case Application.STATUS_CANCELLED: // "Cancelada"
                return 4;
            default:
                return 0; // Se não reconhecer, manda 0
        }
    }

    public void editarApplicationBD(Application app) {
        // Tenta editar na BD
        if (appBD.editarApplicationBD(app)) {
            // Se funcionou na BD, atualiza na lista em memória
            Application auxApp = getApplication(app.getId());
            if (auxApp != null) {
                // Atualiza os campos necessários
                auxApp.setStatus(app.getStatus());
                auxApp.setIsRead(app.getIsRead());
                auxApp.setStatusDate(app.getStatusDate());
            }
        }
    }

    public void removerApplicationBD(int idApplication) {
        Application app = getApplication(idApplication);

        if (app != null) {
            appBD.removerApplicationBD(idApplication);

            // Remove da lista em memória
            applications.remove(app);
        }
    }

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


                    removerAllMessagesBD();
                    adicionarMessagesBD(lista);

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


    public void editarMensagemAPI(int id, String subject, String text, Context context, SendMessageListener listener) {
        String url = getMessageURL + "/" + id; // ex: /messages/9

        StringRequest request = new StringRequest(
                Request.Method.PUT,    // se o backend usar POST para update, troca para Request.Method.POST
                url,
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



    public void removerAllMessagesBD() {
        AppDBHelper.getInstance(context).removerAllMessagesBD();
    }

    public void adicionarMessagesBD(ArrayList<Message> messages) {
        AppDBHelper db = AppDBHelper.getInstance(context);
        for (Message m : messages) {
            db.adicionarMessageBD(m);
        }
    }

    public ArrayList<Message> getAllMessagesBD() {
        return AppDBHelper.getInstance(context).getAllMessagesBD();
    }



    //*************************************** Fim Mensagens *************************************


    // FUNÇÃO UTILIZADA PARA FAZER O LOGIN, PARA DEPOIS GUARDAR O TOKEN NA SHARED PREFERENCES
    // RECEBE O TOKEN


    /*
    public void loginAPI(final String username, final String password, final Context context){
        if(!isConnectionInternet(context)){
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


     */


    public void loginAPI(final String username, final String password, final Context context){
        if(!isConnectionInternet(context)){
            Toast.makeText(context, R.string.txt_nao_tem_internet, Toast.LENGTH_SHORT).show();
        }else{
            StringRequest request = new StringRequest(
                    Request.Method.POST,
                    getmUrlAPILogin,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String s) {
                            try {
                                // s é o JSON: {"success":true,"token":"...","id":11}
                                JSONObject json = new JSONObject(s);

                                String token = json.optString("token", null);
                                int userId   = json.optInt("id", -1);

                                // se ainda quiseres usar o parser antigo do token:
                                // String token = UserJsonParser.parserJsonLogin(s);

                                if (token != null) {
                                    SharedPreferences sp = context.getSharedPreferences("DADOS_USER", Context.MODE_PRIVATE);
                                    sp.edit()
                                            .putString(MenuMainActivity.TOKEN, token)
                                            .putInt("USER_ID_INT", userId)   // <-- ADICIONADO
                                            .apply();

                                    System.out.println("DEBUG SP SAVE USER_ID_INT=" + userId);
                                }

                                if(loginListener != null) {
                                    loginListener.onValidateLogin(token);
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                                Toast.makeText(context, "Erro a processar login", Toast.LENGTH_SHORT).show();
                            }
                        }
                    },
                    new Response.ErrorListener() {
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
                    }
            ){
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
        if(!isConnectionInternet(context)){
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
    public void addApplicationAPI(final Context context, int userId, int animalId, String description, String data, final ApplicationsListener listener) {

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

    public interface SendMessageListener {
        void onSuccess();

        void onError(String msg);
    }
}

// -------------------------
    // FIM GESTOR Application
    // -------------------------




    // -------------------------
    // GESTOR Message
    // -------------------------
//    public ArrayList<Message> getMessagesForUser (int userId){
//        return gestorMessage.getMessageForUser(userId);
//    }
//
//
//    public Message getMessage(int idMessage) {
//        return gestorMessage.getMessageById(idMessage);
//    }

//*************************************** Fim Mensagens *************************************
