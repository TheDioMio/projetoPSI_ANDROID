package pt.ipleiria.estg.dei.projetoandroid.modelo;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
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

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import pt.ipleiria.estg.dei.projetoandroid.listeners.AnimalDeleteListener;
import pt.ipleiria.estg.dei.projetoandroid.listeners.AvatarUploadListener;
import pt.ipleiria.estg.dei.projetoandroid.listeners.MetaListener;
import pt.ipleiria.estg.dei.projetoandroid.utils.FileUtils;
import pt.ipleiria.estg.dei.projetoandroid.MenuMainActivity;
import pt.ipleiria.estg.dei.projetoandroid.R;
import pt.ipleiria.estg.dei.projetoandroid.listeners.ApplicationListener;
import pt.ipleiria.estg.dei.projetoandroid.listeners.ApplicationsListener;
import pt.ipleiria.estg.dei.projetoandroid.listeners.AnimalsListener;
import pt.ipleiria.estg.dei.projetoandroid.listeners.LoginListener;
import pt.ipleiria.estg.dei.projetoandroid.listeners.MenuListener;
import pt.ipleiria.estg.dei.projetoandroid.listeners.MessagesListener;
import pt.ipleiria.estg.dei.projetoandroid.listeners.UserUpdateListener;
import pt.ipleiria.estg.dei.projetoandroid.utils.ApplicationJsonParser;
import pt.ipleiria.estg.dei.projetoandroid.utils.AnimalJsonParser;
import pt.ipleiria.estg.dei.projetoandroid.utils.MessageJsonParser;
import pt.ipleiria.estg.dei.projetoandroid.utils.MetaJsonParser;
import pt.ipleiria.estg.dei.projetoandroid.utils.UserJsonParser;
import pt.ipleiria.estg.dei.projetoandroid.utils.VolleyMultipartRequest;
import pt.ipleiria.estg.dei.projetoandroid.utils.AnimalEditJsonParser;

public class AppSingleton {

    private Context context;

    private static RequestQueue volleyQueue;

    //-------------------------------------------
    // Endpoints da API
    //--------------------------------------------
    //region ENDPOINTS
    public String endereco = "http://172.20.10.2/PSI/projetoPSI_WEB/backend/web/api";
    //endereço para as imagens
    public static final String FRONTEND_BASE_URL = "http://172.20.10.2/PSI/projetoPSI_WEB/frontend/web";
    private String getmUrlAPILogin = endereco+"/auth/login";
    private String getmUrlAPIMe = endereco+"/users/me";
    private String getMessageURL = endereco+"/messages";
    private String getmUrlAPIApplication = endereco + "/application";
    private String postAvatarURL = endereco+"/file/update-avatar";
    private String postmUrlAPIFilesDelete = endereco+"/files/delete";
    private String postmUrlAPIFilesCreate = endereco+"/file/create";
    private String getSentApplications = endereco+"application/sent"; // <- Diogo penso que o link deve começar por / não quis alterar pois podes estar a tratar isto de outra forma
    private String getmUrlAPIAnimals = endereco+"/animals?expand=listing.comments.user.profileImage,user.profileImage";
    private String putmUrlAPIAnimalUpdate = endereco+"/animals/";
    private String postmUrlAPIAnimalCreate = endereco+"/animals";
    private String getmUrlAPIMyAnimals = endereco+"/animals/my?expand=listing.comments.user.profileImage,user.profileImage";
    private String getmUrlAPIMeta = endereco+"/animals/meta";
    private String getmUrlAPIAnimalEdit = endereco+"/animals/edit/";

    public static boolean isConnectionInternet(Context context){
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        //necessita de permissões de acesso a internet
        //e acesso ao estado la ligação
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();

        return activeNetwork != null && activeNetwork.isConnectedOrConnecting();
    }

    //endregion



    private LoginListener loginListener;
    private AvatarUploadListener avatarUploadListener;
    private MenuListener menuListener;
    private MetaListener metaListener;
    private UserUpdateListener userUpdateListener;
    private ApplicationsListener applicationsListener;
    private ApplicationListener applicationListener;

    private AnimalsListener animalsListener;
    private AnimalDeleteListener animalDeleteListener;

    public void setAnimalDeleteListener(AnimalDeleteListener listener) {
        this.animalDeleteListener = listener;
    }

    public void setAvatarUploadListener(AvatarUploadListener avatarUploadListener) {
        this.avatarUploadListener = avatarUploadListener;
    }

    public void setLoginListener(LoginListener loginListener) {
        this.loginListener = loginListener;
    }

    public void setAnimalsListener(AnimalsListener animalsListener) {
        this.animalsListener = animalsListener;
    }

    public void setMenuListener(MenuListener menuListener) {
        this.menuListener = menuListener;
    }

    public void setMetaListener(MetaListener metaListener) {
        this.metaListener = metaListener;
    }

    private AppDBHelper appBD = null;

    private Me me;
    private ArrayList<Application> applications;

    private MessagesListener messagesListener;

    private ArrayList<Comment> comments;




    private static AppSingleton instance = null;
//    private GestorAnimals gestorAnimals = new GestorAnimals();
    private GestorUsers gestorUsers = new GestorUsers();


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

    public void deleteAnimalAPI(Context context, int animalId) {

        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.DELETE,
                endereco + "/animals/" + animalId,
                null,
                response -> {
                    if (animalDeleteListener != null) {
                        animalDeleteListener.onDeleteAnimalSuccess(animalId);
                    }
                },
                error -> {
                    if (animalDeleteListener != null) {
                        String msg = "Erro ao apagar animal";

                        if (error.networkResponse != null) {
                            msg += " (" + error.networkResponse.statusCode + ")";
                        }

                        animalDeleteListener.onDeleteAnimalError(msg);
                    }
                }
        ) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                headers.put("Authorization", "Bearer " + getToken(context));
                headers.put("Accept", "application/json");
                return headers;
            }
        };

        volleyQueue.add(request);
    }


    public void createAnimalAPI(Context context,
                                AnimalEdit animal,
                                Response.Listener<Integer> listener,
                                Response.ErrorListener errorListener) {

        JSONObject body = new JSONObject();
        try {
            body.put("name", animal.getName());
            body.put("description", animal.getDescription());
            body.put("location", animal.getLocation());

            body.put("animal_type_id", animal.getTypeId());
            body.put("breed_id", animal.getBreedId());
            body.put("age_id", animal.getAgeId());
            body.put("size_id", animal.getSizeId());
            body.put("vaccination_id", animal.getVaccinationId());
            body.put("neutered", animal.getNeutered());

            // listing
            body.put("listing_description", animal.getListingDescription());
            body.put("listing_status", animal.getListingStatus());

        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.POST,
                postmUrlAPIAnimalCreate,
                body,
                response -> {
                    int animalId = response.optInt("animal_id", -1);
                    listener.onResponse(animalId);
                },
                errorListener
        ) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                headers.put("Authorization", "Bearer " + getToken(context));
                headers.put("Accept", "application/json");
                headers.put("Content-Type", "application/json");
                return headers;
            }
        };

        volleyQueue.add(request);
    }

    public void updateAnimalAPI(Context context,
                                int animalId,
                                AnimalEdit animal,
                                Response.Listener<Void> listener,
                                Response.ErrorListener errorListener) {


        JSONObject body = new JSONObject();
        try {
            body.put("name", animal.getName());
            body.put("description", animal.getDescription());
            body.put("location", animal.getLocation());

            body.put("animal_type_id", animal.getTypeId());
            body.put("breed_id", animal.getBreedId());
            body.put("age_id", animal.getAgeId());
            body.put("size_id", animal.getSizeId());
            body.put("vaccination_id", animal.getVaccinationId());
            body.put("neutered", animal.getNeutered());

            body.put("listing_description", animal.getListingDescription());
            body.put("listing_status", animal.getListingStatus());

        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.PUT,
                putmUrlAPIAnimalUpdate+animalId,
                body,
                response -> listener.onResponse(null),
                errorListener
        ) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                headers.put("Authorization", "Bearer " + getToken(context));
                headers.put("Accept", "application/json");
                headers.put("Content-Type", "application/json");
                return headers;
            }
        };

        volleyQueue.add(request);
    }

    // Na tua AppSingleton (ou onde crias o pedido)
    public void uploadAnimalPhotosAPI(Context context,
                                      int animalId,
                                      ArrayList<Uri> photos,
                                      Response.Listener<Void> listener,
                                      Response.ErrorListener errorListener) {

        Map<String, String> stringParams = new HashMap<>();
        stringParams.put("animal_id", String.valueOf(animalId));

        Map<String, VolleyMultipartRequest.DataPart> fileParams = new HashMap<>();

        for (int i = 0; i < photos.size(); i++) {
            byte[] bytes = getBytesFromUri(context, photos.get(i));

            if (bytes == null) continue;

            fileParams.put(
                    "files[" + i + "]",
                    new VolleyMultipartRequest.DataPart(
                            "photo_" + i + ".jpg",
                            bytes,
                            "image/jpeg"
                    )
            );
        }

        VolleyMultipartRequest request =
                new VolleyMultipartRequest(
                        Request.Method.POST,
                        postmUrlAPIFilesCreate,
                        stringParams,
                        fileParams,
                        response -> listener.onResponse(null),
                        errorListener
                ) {

                    @Override
                    public Map<String, String> getHeaders() {
                        Map<String, String> headers = new HashMap<>();
                        headers.put("Authorization", "Bearer " + getToken(context));
                        return headers;
                    }
                };

        volleyQueue.add(request);
    }

    private byte[] getBytesFromUri(Context context, Uri uri) {
        try {
            InputStream inputStream =
                    context.getContentResolver().openInputStream(uri);

            ByteArrayOutputStream buffer = new ByteArrayOutputStream();
            int nRead;
            byte[] data = new byte[4096];

            while ((nRead = inputStream.read(data)) != -1) {
                buffer.write(data, 0, nRead);
            }

            return buffer.toByteArray();

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }



    public void deleteAnimalPhotosAPI(Context context,
                                      ArrayList<Integer> removedPhotoIds,
                                      Response.Listener<Void> listener,
                                      Response.ErrorListener errorListener) {



        JSONObject body = new JSONObject();
        JSONArray ids = new JSONArray();

        for (int id : removedPhotoIds) {
            ids.put(id);
        }

        try {
            body.put("photo_ids", ids);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.POST,
                postmUrlAPIFilesDelete,
                body,
                response -> listener.onResponse(null),
                errorListener
        ) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                headers.put("Authorization", "Bearer " + getToken(context));
                return headers;
            }
        };

        volleyQueue.add(request);
    }



    public void getMyAnimalsAPI(final Context context) {

        //esta tarefa fica para o gestor
        if (!isConnectionInternet(context)) {
            Toast.makeText(context, R.string.txt_nao_tem_internet, Toast.LENGTH_SHORT).show();
            return;
        }

        JsonArrayRequest request = new JsonArrayRequest(
                Request.Method.GET,
                getmUrlAPIMyAnimals,
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


    //recebe o animal que vamos editar
    public void getAnimalEditAPI(Context context, int animalId,
                                 Response.Listener<AnimalEdit> listener,
                                 Response.ErrorListener errorListener) {

        if (!isConnectionInternet(context)) {
            errorListener.onErrorResponse(null);
            return;
        }

        String url = getmUrlAPIAnimalEdit + animalId;

        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.GET,
                url,
                null,
                response -> {
                    AnimalEdit animal = AnimalEditJsonParser.parse(response);
                    listener.onResponse(animal);
                },
                errorListener
        ) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                String token = getToken(context);

                if (token != null) {
                    headers.put("Authorization", "Bearer " + token);
                }
                return headers;
            }
        };

        volleyQueue.add(request);
    }

    //recebe os dados para carreagar nos spinners da form do animal
    public void getAnimalMetaAPI(final Context context, final MetaListener listener) {

        if (!isConnectionInternet(context)) {
            listener.onMetaError("Sem ligação à internet");
            return;
        }



        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.GET,
                getmUrlAPIMeta,
                null,
                response -> {
                    try {
                        HashMap<String, ArrayList<MetaItem>> meta =
                                MetaJsonParser.parserMeta(response);
                        listener.onMetaLoaded(meta);
                    } catch (Exception e) {
                        e.printStackTrace();
                        listener.onMetaError("Erro a processar meta");
                    }
                },
                error -> {
                    listener.onMetaError("Erro ao carregar dados");
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

    public void addApplicationAPI(final Context context, int userId,int animalId, String motive, String dataJsonString, final ApplicationsListener listener) {

        if (!isConnectionInternet(context)) {
            Toast.makeText(context, "Sem ligação à internet", Toast.LENGTH_SHORT).show();
            return;
        }

        String url = endereco + "/application";

        JSONObject jsonBody;
        try {
            // 1. O SEGREDO: Pegar na string que vem do fragmento e torná-la no objeto raiz
            // Assim "age", "home", etc. ficam na raiz, como o PHP quer.
            jsonBody = new JSONObject(dataJsonString);

            // 2. Adicionar o que falta
            jsonBody.put("animal_id", animalId);
            jsonBody.put("user_id", userId);
            jsonBody.put("motive", motive);
            jsonBody.put("description", motive);

            if (jsonBody.has("age")) {
                int idadeInt = jsonBody.optInt("age");
                jsonBody.put("age", String.valueOf(idadeInt)); // Força "25" em vez de 25
            }

        } catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(context, "Erro ao processar dados", Toast.LENGTH_SHORT).show();
            return;
        }

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, jsonBody,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        if (listener != null) {
                            listener.onRefreshList(null);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        String message = "Erro ao enviar candidatura";

                        // LOG DE DEBUG PARA VERES O ERRO REAL NO LOGCAT
                        if (error.networkResponse != null) {
                            message += " (" + error.networkResponse.statusCode + ")";
                            try {
                                String data = new String(error.networkResponse.data, "UTF-8");
                                android.util.Log.e("ERRO_PHP", data); // <--- Procura por isto no Logcat se falhar
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        } else if (error instanceof com.android.volley.ParseError) {
                        message += " (Erro de Leitura)";

                        // TENTA LER O HTML QUE VEIO NO ERRO (NOVO)
                        if (error.networkResponse != null && error.networkResponse.data != null) {
                            try {
                                String htmlErro = new String(error.networkResponse.data, "UTF-8");
                                android.util.Log.e("HTML_DO_ERRO", htmlErro);
                            } catch (Exception e) {}
                        }

                        android.util.Log.e("DEBUG_ERRO", "ParseError: O servidor devolveu HTML.");
                    }
                        error.printStackTrace();
                        Toast.makeText(context, message, Toast.LENGTH_LONG).show();
                    }
                }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                String token = getToken(context);
                if (token != null) {
                    headers.put("Authorization", "Bearer " + token);
                }
                return headers;
            }
        };

        volleyQueue.add(request);
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
        String url = endereco + "/application/" + application.getId();

        JSONObject jsonBody = new JSONObject();
        try {
            int statusInt = converterStatusParaInt(application.getStatus());

            jsonBody.put("status", statusInt);
            //Envia {"status": 2} em vez de {"status": "Aprovada"}

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

                            try {
                                String responseBody = new String(error.networkResponse.data, "UTF-8");
                                // ISTO VAI IMPRIMIR O MOTIVO EXATO NO LOGCAT (Ex: "Status cannot be blank")
                                android.util.Log.e("ERRO_422_DETALHE", responseBody);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }

                        if (listener != null) listener.onError(message);
                    }
                }) {

            //ISTO AQUI ENVIA O TOKEN
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

    public void removerApplicationAPI(final Application application, final Context context, final ApplicationListener listener) {
        if (!isConnectionInternet(context)) {
            Toast.makeText(context, "Sem ligação à internet", Toast.LENGTH_SHORT).show();
            return;
        }

        // URL: .../api/application/5
        String url = getmUrlAPIApplication + "/" + application.getId();

        StringRequest request = new StringRequest(Request.Method.DELETE, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //Remove da BD Local
                        removerApplicationBD(application.getId());

                        //AVISAR O ADAPTADOR QUE O DELETE FOI UM SUCESSO
                        if (listener != null) {
                            listener.onRefreshDetails(application);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        Toast.makeText(context, volleyError.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                String token = getToken(context);
                if (token != null) {
                    headers.put("Authorization", "Bearer " + token);
                }
                return headers;
            }
        };
        volleyQueue.add(request);
    }
    public void removerApplicationBD(int idApplication) {
        Application app = getApplication(idApplication);

        if (app != null) {
            appBD.removerApplicationBD(idApplication);

            // Remove da lista em memória
            applications.remove(app);
        }
    }

    // -------------------------
    // FIM GESTOR Application
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


    // METODO UTILIZADO PARA ALTERAR OS DADOS DO USER
    public void updateMe(final Context context, Me me, final UserUpdateListener listener) {

        if (!isConnectionInternet(context)) {
            Toast.makeText(context, R.string.txt_nao_tem_internet, Toast.LENGTH_SHORT).show();
            return;
        }

        JSONObject body = new JSONObject();
        try {
            body.put("name", me.getName());
            body.put("username", me.getUsername());
            body.put("email", me.getEmail());
            body.put("address", me.getAddress());
        } catch (JSONException e) {
            listener.onUpdateError("Dados inválidos");
            return;
        }

        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.PUT,
                getmUrlAPIMe,
                body,
                response -> {

                    // Atualiza o singleton com os dados do servidor
                    this.me = UserJsonParser.parserJsonMe(response);

                    // Atualiza menu / UI
                    if (menuListener != null) {
                        menuListener.onRefreshMenu(this.me);
                    }

                    listener.onUpdateSuccess(this.me);
                },
                error -> {

                    String msg;

                    if (error.networkResponse != null) {
                        int statusCode = error.networkResponse.statusCode;

                        if (statusCode == 401) {
                            msg = "Sessão expirada ou não autorizado";
                        } else if (statusCode == 422) {
                            msg = "Dados inválidos";
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

                    listener.onUpdateError(msg);
                }
        ) {
            @Nullable
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                String token = getToken(context);
                if (token != null && !token.isEmpty()) {
                    headers.put("Authorization", "Bearer " + token);
                    headers.put("Content-Type", "application/json");
                }
                return headers;
            }
        };

        volleyQueue.add(request);
    }


    // UTILIZADO PARA EDITAR O AVATAR DO USER
    public void uploadAvatar(
            Context context,
            Uri avatarUri,
            AvatarUploadListener listener
    ) {

        byte[] imageBytes = FileUtils.readBytes(context, avatarUri);

        Map<String, VolleyMultipartRequest.DataPart> file = new HashMap<>();
        file.put("file", new VolleyMultipartRequest.DataPart(
                "avatar.jpg",
                imageBytes,
                "image/jpeg"
        ));

        Map<String, String> headers = new HashMap<>();
        headers.put("Authorization", "Bearer " + getToken(context));

        VolleyMultipartRequest request = new VolleyMultipartRequest(
                Request.Method.POST,
                postAvatarURL,
                headers,
                file,
                response -> {
                    try {
                        String json = new String(response.data);
                        JSONObject resp = new JSONObject(json);

                        if (resp.getBoolean("success")) {

                            String avatarPath = resp.getString("path");

                            // ✅ guardar avatar localmente
                            saveAvatarPath(context, avatarPath);

                            // ✅ notificar quem chamou
                            listener.onSuccess(avatarPath);

                        } else {
                            listener.onError("Erro ao atualizar avatar");
                        }

                    } catch (Exception e) {
                        listener.onError("Resposta inválida do servidor");
                    }
                },
                error -> listener.onError("Erro de comunicação com o servidor")
        );

        volleyQueue.add(request);
    }


    private void saveAvatarPath(Context context, String path) {
        SharedPreferences sp =
                context.getSharedPreferences("DADOS_USER", Context.MODE_PRIVATE);

        sp.edit()
                .putString(MenuMainActivity.IMGAVATAR, path)
                .apply();
    }

    public void notifyMenuRefresh(Me me) {
        this.me = me;

        if (menuListener != null) {
            menuListener.onRefreshMenu(me);
        }
    }


    public String getToken(Context context) {
        SharedPreferences sharedPreferences =
                context.getSharedPreferences("DADOS_USER", Context.MODE_PRIVATE);

        return sharedPreferences.getString(MenuMainActivity.TOKEN, null);
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
