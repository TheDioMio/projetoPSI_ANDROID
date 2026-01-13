package pt.ipleiria.estg.dei.projetoandroid.modelo;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.content.FileProvider;

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
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import pt.ipleiria.estg.dei.projetoandroid.listeners.AnimalDeleteListener;
import pt.ipleiria.estg.dei.projetoandroid.listeners.AvatarUploadListener;
import pt.ipleiria.estg.dei.projetoandroid.listeners.CommentActionListener;
import pt.ipleiria.estg.dei.projetoandroid.listeners.CommentCreateListener;
import pt.ipleiria.estg.dei.projetoandroid.listeners.CommentDeleteListener;
import pt.ipleiria.estg.dei.projetoandroid.listeners.CommentUpdateListener;
import pt.ipleiria.estg.dei.projetoandroid.listeners.CreateAnimalListener;
import pt.ipleiria.estg.dei.projetoandroid.listeners.DeleteAnimalPhotosListener;
import pt.ipleiria.estg.dei.projetoandroid.listeners.GetAnimalEditListener;
import pt.ipleiria.estg.dei.projetoandroid.listeners.MetaListener;
import pt.ipleiria.estg.dei.projetoandroid.listeners.MyAnimalsListener;
import pt.ipleiria.estg.dei.projetoandroid.listeners.SignupListener;
import pt.ipleiria.estg.dei.projetoandroid.listeners.UpdateAnimalListener;
import pt.ipleiria.estg.dei.projetoandroid.listeners.UploadAnimalPhotosListener;
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

    public static final int ROLE_ADMIN = 1;
    public static final int ROLE_USERPRO = 2;
    public static final int ROLE_USER  = 3;

    private Context context;

    private static RequestQueue volleyQueue;

    public AppDBHelper appBD = null;
    private Me me; // guarda os dados do utilizador logado
    private ArrayList<Application> applications;

    private ArrayList<Animal> animals;
    private ArrayList<Animal> myAnimals;

    private ArrayList<Comment> comments;



    //-------------------------------------------
    // Endpoints da API 10.104.146.191
    //--------------------------------------------
    //region ENDPOINTS
    public String endereco = "http://10.104.146.191/projetoPSI_WEB/backend/web/api";
    //endereço para as imagens
    public static final String FRONTEND_BASE_URL = "http://10.104.146.191/projetoPSI_WEB/frontend/web";

    //ENDPOINTS DOS USERS


    private String getmUrlAPILogin = endereco+"/auth/login";
    private String postmUrlAPISignup = endereco+"/auth/signup";

    private String getmUrlAPIMe = endereco+"/users/me";
    private String putmUrlAPIMe = endereco+"/users/me";
    private String getMessageURL = endereco+"/messages";
    private String getmUrlAPIApplication = endereco + "/application";

    //ENDPOINTS DOS FILES
    private String postAvatarURL = endereco+"/file/update-avatar";
    private String postmUrlAPIFilesDelete = endereco+"/file/delete"; 
    private String postmUrlAPIFilesCreate = endereco+"/file/create";
    private String getmUrlAPIAnimals = endereco+"/animals?expand=listing.comments.user.profileImage,user.profileImage";
    private String putmUrlAPIAnimalUpdate = endereco+"/animals/";
    private String deletemUrlAPIAnimalDelete = endereco+"/animals/";
    private String postmUrlAPIAnimalCreate = endereco+"/animals";
    private String getmUrlAPIMyAnimals = endereco+"/animals/my?expand=listing.comments.user.profileImage,user.profileImage";
    private String getmUrlAPIMeta = endereco+"/animals/meta";
    private String getmUrlAPIAnimalEdit = endereco+"/animals/edit/";

    //ENDPOINTS DOS COMMENTS
    private String postmUrlAPICommentCreate = endereco+"/comments";
    private String deletemUrlAPICommentDelete = endereco+"/comments";
    private String putmUrlAPICommentUpdate = endereco+"/comments";



    public static boolean isConnectionInternet(Context context){
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        //necessita de permissões de acesso a internet
        //e acesso ao estado la ligação
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();

        return activeNetwork != null && activeNetwork.isConnectedOrConnecting();
    }

    //endregion


    //region LISTENERS
    private LoginListener loginListener;
    private AvatarUploadListener avatarUploadListener;
    private MenuListener menuListener;
    private MetaListener metaListener;
    private UserUpdateListener userUpdateListener;
    private ApplicationsListener applicationsListener;
    private ApplicationListener applicationListener;
    private MessagesListener messagesListener;

    private AnimalsListener animalsListener;
    private AnimalDeleteListener animalDeleteListener;
    private CreateAnimalListener createAnimalListener;

    private GetAnimalEditListener getAnimalEditListener;

    private UpdateAnimalListener updateAnimalListener;

    private UploadAnimalPhotosListener uploadAnimalPhotosListener;

    private DeleteAnimalPhotosListener deleteAnimalPhotosListener;

    private MyAnimalsListener myAnimalsListener;
    private CommentCreateListener commentCreateListener;

    private CommentDeleteListener commentDeleteListener;
    private CommentActionListener commentActionListener;

    private CommentUpdateListener commentUpdateListener;

    private SignupListener signupListener;

    public void setSignupListener(SignupListener listener) {
        this.signupListener = listener;
    }
    public void setCommentUpdateListener(CommentUpdateListener commentUpdateListener) {
        this.commentUpdateListener = commentUpdateListener;
    }

    public void setCommentActionListener(CommentActionListener commentActionListener) {
        this.commentActionListener = commentActionListener;
    }

    public void setCommentDeleteListener(CommentDeleteListener commentDeleteListener) {
        this.commentDeleteListener = commentDeleteListener;
    }

    public void setCommentCreateListener(CommentCreateListener commentCreateListener) {
        this.commentCreateListener = commentCreateListener;
    }

    public void setMyAnimalsListener(MyAnimalsListener listener) {
        this.myAnimalsListener = listener;
    }

    public void setDeleteAnimalPhotosListener(DeleteAnimalPhotosListener listener) {
        this.deleteAnimalPhotosListener = listener;
    }
    public void setUploadAnimalPhotosListener(UploadAnimalPhotosListener listener) {
        this.uploadAnimalPhotosListener = listener;
    }
    public void setUpdateAnimalListener(UpdateAnimalListener updateAnimalListener) {
        this.updateAnimalListener = updateAnimalListener;
    }

    public void setGetAnimalEditListener(GetAnimalEditListener getAnimalEditListener) {
        this.getAnimalEditListener = getAnimalEditListener;
    }

    public void setCreateAnimalListener(CreateAnimalListener createAnimalListener) {
        this.createAnimalListener = createAnimalListener;
    }

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

    public void setApplicationsListener(ApplicationsListener applicationsListener){
        this.applicationsListener = applicationsListener;
    }

    //endregion



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
        myAnimals = new ArrayList<>();
        applications = new ArrayList<>();
    }


    //------------------------------
    //GESTOR DE COMMENTS
    //-----------------------------

    //gestor dos comments no singlerton (atualiza a lista dos animals e dos my animals)






    public void adicionarCommentsBD(ArrayList<Comment> comments){
        for (Comment c: comments){
            appBD.adicionarCommentBD(c);
        }
    }

    public void removerCommentBD(int commentId) {


        int animalId = appBD.getAnimalIdFromComment(commentId);

        if (animalId == -1) return; // segurança

        //atualiza a BD
        appBD.deleteCommentBD(commentId);

        ArrayList<Comment> updated = appBD.getCommentsByAnimalId(animalId);

        // Atualizar nos Animals
        for (Animal a : animals) {
            if (a.getId() == animalId) {
                a.setComments(updated);
            }
        }

        // Atualizar nos MyAnimals
        for (Animal a : myAnimals) {
            if (a.getId() == animalId) {
                a.setComments(updated);
            }
        }

    }

    public void editarCommentBD(Comment comment) {

        int commentId = comment.getIdComment();
        int animalId  = comment.getIdAnimal();

        //  Atualizar BD
        appBD.updateCommentBD(comment);

        // Recarregar comments do animal
        ArrayList<Comment> updated = appBD.getCommentsByAnimalId(animalId);

        // Atualizar lista Animals
        for (Animal a : animals) {
            if (a.getId() == animalId) {
                a.setComments(updated);
            }
        }

        // Atualizar lista MyAnimals
        for (Animal a : myAnimals) {
            if (a.getId() == animalId) {
                a.setComments(updated);
            }
        }

    }

    public void adicionarCommentBD(Comment comment) {
        appBD.adicionarCommentBD(comment);
        // atualiza a lista dos animals
        for (Animal a : animals) {
            if (a.getId() == comment.getIdAnimal()) {
                a.getComments().add(0, comment);
                break;
            }
        }

        // atualiza a lista dos myAnimals
        for (Animal a : myAnimals) {
            if (a.getId() == comment.getIdAnimal()) {
                a.getComments().add(0, comment);
                break;
            }
        }
    }


    public void createCommentAPI(Context context, int animalId, String text) {

        JSONObject body = new JSONObject();
        try {
            body.put("animal_id", animalId);
            body.put("text", text);
        } catch (JSONException e) {
            if (commentCreateListener != null)
                commentCreateListener.onCreateCommentError("Erro a criar pedido");
            return;
        }

        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.POST,
                postmUrlAPICommentCreate,
                body,
                response -> {
                    try {
                        if (response.getBoolean("success")) {

                            JSONObject commentJson = response.getJSONObject("comment");

                            // reutiliza o parser que já existe
                            JSONArray arr = new JSONArray();
                            arr.put(commentJson);

                            ArrayList<Comment> comments = AnimalJsonParser.parserJsonComments(arr);
                            //atualizar BD e no singleton
                            adicionarCommentBD(comments.get(0));


                            //procurar o animal, adicionar o comment ao animal
                            //adicionar o comment a BD


                            //commentCreateListener.onCreateCommentSuccess(comments.get(0));
                            if (commentCreateListener != null)
                                commentCreateListener.onCreateCommentSuccess(comments.get(0));
                        } else {
                            if (commentCreateListener != null)
                                commentCreateListener.onCreateCommentError("Erro ao criar comentário");
                        }

                    } catch (JSONException e) {
                        if (commentCreateListener != null)
                            commentCreateListener.onCreateCommentError("Erro a processar resposta");
                    }
                },
                error -> commentCreateListener.onCreateCommentError(
                        error.getMessage() != null ? error.getMessage() : "Erro de rede"
                )
        ) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                headers.put("Authorization", "Bearer " + getToken(context));
                headers.put("Content-Type", "application/json");
                return headers;
            }
        };

        Volley.newRequestQueue(context).add(request);
    }


    public void updateCommentAPI(Context context, int commentId, String text) {

        String url = putmUrlAPICommentUpdate + "/" + commentId;

        JSONObject body = new JSONObject();
        try {
            body.put("text", text);
        } catch (JSONException e) {
            if (commentUpdateListener != null)
                commentUpdateListener.onUpdateCommentError("Erro a criar pedido");
            return;
        }

        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.PUT,
                url,
                body,
                response -> {
                    try {
                        if (response.getBoolean("success")) {

                            JSONObject commentJson = response.getJSONObject("comment");

                            JSONArray arr = new JSONArray();
                            arr.put(commentJson);

                            ArrayList<Comment> comments =
                                    AnimalJsonParser.parserJsonComments(arr);

                            Comment updated = comments.get(0);

                            // Atualizar BD e singleton
                            editarCommentBD(updated);


                            if (commentUpdateListener != null)
                                commentUpdateListener.onUpdateCommentSuccess(updated);

                        } else {
                            if (commentUpdateListener != null)
                                commentUpdateListener.onUpdateCommentError("Erro ao atualizar comentário");
                        }

                    } catch (JSONException e) {
                        if (commentUpdateListener != null)
                            commentUpdateListener.onUpdateCommentError("Erro a processar resposta");
                    }
                },
                error -> {
                    if (commentUpdateListener != null)
                        commentUpdateListener.onUpdateCommentError(
                                error.getMessage() != null ? error.getMessage() : "Erro de rede"
                        );
                }
        ) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                headers.put("Authorization", "Bearer " + getToken(context));
                headers.put("Content-Type", "application/json");
                return headers;
            }
        };

        Volley.newRequestQueue(context).add(request);
    }


    public void deleteCommentAPI(Context context, int commentId) {

        String url = deletemUrlAPICommentDelete + "/" + commentId;

        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.DELETE,
                url,
                null,
                response -> {
                    try {
                        if (response.getBoolean("success")) {

                            //atualiza a BD e o singleton
                            removerCommentBD(commentId);

                            if (commentDeleteListener != null)
                                commentDeleteListener.onDeleteCommentSuccess(commentId);
                        } else {
                            if (commentDeleteListener != null)
                                commentDeleteListener.onDeleteCommentError("Erro ao apagar comentário");
                        }

                    } catch (JSONException e) {
                        if (commentDeleteListener != null)
                            commentDeleteListener.onDeleteCommentError("Erro a processar resposta");
                    }
                },
                error -> {
                    if (commentDeleteListener != null)
                        commentDeleteListener.onDeleteCommentError(
                                error.getMessage() != null ? error.getMessage() : "Erro de rede"
                        );
                }
        ) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                headers.put("Authorization", "Bearer " + getToken(context));
                return headers;
            }
        };

        Volley.newRequestQueue(context).add(request);
    }






    // -------------------------
    // GESTOR ANIMAL
    // -------------------------

    //region ANIMALS SINGLETON
    public ArrayList<Animal> getAnimals() {
        return new ArrayList<>(animals);
    }

    public Animal getAnimal(int idAnimal){

        for (Animal animal: animals) {
            if (animal.getId()==idAnimal){
                return animal;
            }
        }
        return null;
    }

    public Animal getMyAnimal(int idAnimal){

        for (Animal animal: myAnimals) {
            if (animal.getId()==idAnimal){
                return animal;
            }
        }
        return null;
    }

    public ArrayList<Animal> getMyAnimals() {
        return new ArrayList<>(myAnimals);
    }

    //endregion

    //region ANIMALS BD

    //FALTA o editar animal BD
    //falta o remover Animal BD

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

    //------------MY ANIMALS BD

    public void adicionarMyAnimalsBD(ArrayList<Animal> animals){
        AppDBHelper.getInstance(context).removerAllMyAnimalsBD();
        for (Animal a: animals){
            appBD.adicionarMyAnimalBD(a);
        }
    }

    public Animal getMyAnimalBD(int id) {
        for (Animal a : myAnimals) {
            if (a.getId() == id) {
                return a;
            }
        }
        return AppDBHelper.getInstance(context).getMyAnimalById(id);
    }

    public ArrayList<Animal> getAllMyAnimalsBD() {
        myAnimals = appBD.getAllMyAnimalsBD();
        return new ArrayList<>(myAnimals);
    }


    //endregion

    //region ANIMALS API

    public void getMyAnimalsAPI(final Context context) {

        if (!isConnectionInternet(context)) {

            ArrayList<Animal> local = appBD.getAllMyAnimalsBD();

            if (myAnimalsListener != null)
                myAnimalsListener.onMyAnimalsOffline(local);

            return;
        }

//        if (!isConnectionInternet(context)) {
//            Toast.makeText(context, R.string.txt_nao_tem_internet, Toast.LENGTH_SHORT).show();
//            return;
//        }

        JsonArrayRequest request = new JsonArrayRequest(
                Request.Method.GET,
                getmUrlAPIMyAnimals,
                null,
                response -> {

                    //  atualizar o singleton
                    myAnimals = AnimalJsonParser.parserJsonAnimals(response);
                    appBD.removerAllMyAnimalsBD();
                    //atualiza a BD
                    adicionarMyAnimalsBD(myAnimals);
//                    // 2️⃣ Guardar na BD local (my_animals)
                    for (Animal a: myAnimals){
                        adicionarCommentsBD(a.getComments());
                        adicionarFilesBD(a.getAnimalfiles());
                    }

                    // 3️⃣ Notificar UI correta
                    if (myAnimalsListener != null) {
                        myAnimalsListener.onRefreshMyAnimals(
                                new ArrayList<>(myAnimals)
                        );
                    }
                },
                error -> {

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

                    if (myAnimalsListener != null) {
                        myAnimalsListener.onErro(msg);
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

    public void deleteAnimalAPI(Context context, int animalId) {
        //utilizado nos Myanimals
        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.DELETE,
                deletemUrlAPIAnimalDelete + animalId,
                //endereco + "/animals/" + animalId,
                null,
                response -> {


                    //atualizar o singleton e a BD
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

    public void createAnimalAPI(Context context, AnimalEdit animal) {

        if (!isConnectionInternet(context)) {
            Toast.makeText(context, R.string.txt_nao_tem_internet, Toast.LENGTH_SHORT).show();
            return;
        }

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
                Request.Method.POST,
                postmUrlAPIAnimalCreate,
                body,
                response -> {
                    int animalId = response.optInt("animal_id", -1);

                    if (createAnimalListener != null) {
                        createAnimalListener.onCreateAnimalSuccess(animalId);
                    }
                },
                error -> {
                    if (createAnimalListener != null) {
                        createAnimalListener.onCreateAnimalError(error);
                    }
                }
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

    public void updateAnimalAPI(Context context, int animalId, AnimalEdit animal) {

        if (!isConnectionInternet(context)) {
            Toast.makeText(context, R.string.txt_nao_tem_internet, Toast.LENGTH_SHORT).show();

            if (updateAnimalListener != null) {
                updateAnimalListener.onUpdateAnimalError(null);
            }
            return;
        }

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
                putmUrlAPIAnimalUpdate + animalId,
                body,
                response -> {

                    if (updateAnimalListener != null) {
                        updateAnimalListener.onUpdateAnimalSuccess();
                    }
                },
                error -> {
                    if (updateAnimalListener != null) {
                        updateAnimalListener.onUpdateAnimalError(error);
                    }
                }
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

    public void getAnimalsAPI(final Context context) {

        if (!isConnectionInternet(context)) {

            ArrayList<Animal> local = appBD.getAllAnimalsBD();

            if (animalsListener != null)
                animalsListener.onAnimalsOffline(local);

            return;
        }

//        if (!isConnectionInternet(context)) {
//            Toast.makeText(context, R.string.txt_nao_tem_internet, Toast.LENGTH_SHORT).show();
//            return;
//        }

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

    public void getAnimalEditAPI(Context context, int animalId) {

        if (!isConnectionInternet(context)) {
            if (getAnimalEditListener != null) {
                getAnimalEditListener.onGetAnimalEditError(null);
            }
            return;
        }

        String url = getmUrlAPIAnimalEdit + animalId;

        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.GET,
                url,
                null,
                response -> {

                    AnimalEdit animal = AnimalEditJsonParser.parse(response);

                    if (getAnimalEditListener != null) {
                        getAnimalEditListener.onGetAnimalEditSuccess(animal);
                    }
                },
                error -> {
                    if (getAnimalEditListener != null) {
                        getAnimalEditListener.onGetAnimalEditError(error);
                    }
                }
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
                        HashMap<String, ArrayList<MetaItem>> meta = MetaJsonParser.parserMeta(response);
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

    //endregion






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


//    public ArrayList<Application> getAllApplicationsBD() {
//        applications = appBD.getAllApplicationsBD();
//        return new ArrayList<>(applications);
//    }




    public void adicionarFilesBD(ArrayList<AnimalFile> files){
        for (AnimalFile f: files){
            appBD.adicionarFileBD(f);
        }
    }

    public void uploadAnimalPhotosAPI(Context context, int animalId, ArrayList<Uri> photos) {

        if (!isConnectionInternet(context)) {
            Toast.makeText(context, R.string.txt_nao_tem_internet, Toast.LENGTH_SHORT).show();

            if (uploadAnimalPhotosListener != null) {
                uploadAnimalPhotosListener.onUploadAnimalPhotosError(null);
            }
            return;
        }

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
                        response -> {
                            if (uploadAnimalPhotosListener != null) {
                                uploadAnimalPhotosListener.onUploadAnimalPhotosSuccess();
                            }
                        },
                        error -> {
                            if (uploadAnimalPhotosListener != null) {
                                uploadAnimalPhotosListener.onUploadAnimalPhotosError(error);
                            }
                        }
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
            InputStream is = context.getContentResolver().openInputStream(uri);
            Bitmap bitmap = BitmapFactory.decodeStream(is);

            if (bitmap == null) return null;

            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 85, stream);

            return stream.toByteArray();

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    public void deleteAnimalPhotosAPI(Context context, ArrayList<Integer> removedPhotoIds) {

        if (!isConnectionInternet(context)) {
            Toast.makeText(context, R.string.txt_nao_tem_internet, Toast.LENGTH_SHORT).show();

            if (deleteAnimalPhotosListener != null) {
                deleteAnimalPhotosListener.onDeleteAnimalPhotosError(null);
            }
            return;
        }

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
                response -> {
                    if (deleteAnimalPhotosListener != null) {
                        deleteAnimalPhotosListener.onDeleteAnimalPhotosSuccess();
                    }
                },
                error -> {
                    if (deleteAnimalPhotosListener != null) {
                        deleteAnimalPhotosListener.onDeleteAnimalPhotosError(error);
                    }
                }
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

    // -------------------------
    // GESTOR User
    // -------------------------


    public User getUser(int idUser){
        //colocar aqui o que está no gestor de users
        return gestorUsers.getUser(idUser);
    }


    // -------------------------
    // GESTOR Application
    // -------------------------
    public ArrayList<Application> getApplications() {
        return new ArrayList<>(applications);
    }

    public Application getApplication(int id) {
        //Tenta procurar na memória (Se a lista existir)
        if (applications != null) {
            for (Application a : applications) {
                if (a.getId() == id) {
                    return a;
                }
            }
        }

        //Se não encontrou na memória (ou lista nula), vai à BD buscar
        // Isto impede o crash se a lista applications estiver a null
        return AppDBHelper.getInstance(context).getApplicationById(id);
    }

    public void getApplicationsAPI(final Context context, final String type) {
        String url = endereco + "/application/" + type;

        //Se não tem net, vai logo à BD
        if (!isConnectionInternet(context)) {
            System.out.println("DEBUG: Sem net detetada. A carregar cache...");
            tentaCarregarCache(type);
            return;
        }

        //Pedido API
        JsonArrayRequest req = new JsonArrayRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray jsonArray) {
                        try {
                            //Converter JSON
                            applications = ApplicationJsonParser.parserJsonApplications(jsonArray);

                            //Guardar dados vindos da API na BD local (para cache)
                            try {
                                //Limpa lista antiga e guarda a nova
                                adicionarApplicationsBD(applications, type);
                                System.out.println("DEBUG: Dados guardados na BD com sucesso!");
                            } catch (Exception ex) {
                                System.out.println("DEBUG: Erro ao guardar na BD: " + ex.getMessage());
                                ex.printStackTrace();
                            }

                            //Atualizar vista
                            if (applicationsListener != null) {
                                applicationsListener.onRefreshList(applications);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            // Se falhar o parse, tenta mostrar o que temos na BD
                            tentaCarregarCache(type);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //Se o pedido falhar por algum motivo, vai à BD local
                        System.out.println("DEBUG: Erro no Volley. A tentar cache local...");
                        tentaCarregarCache(type);
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
            //Pegar na string que vem do fragmento e torná-la no objeto raiz
            //Assim "age", "home", etc. ficam na raiz, como o PHP quer.
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

                        // TENTA LER O HTML QUE VEIO NO ERRO
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

    public void adicionarApplicationsBD(ArrayList<Application> applications, String type) {
        //Limpar dados antigos das applications anteriores da lista específica ao type
        appBD.removerAllApplicationsBD(type);
        for (Application app : applications) {
            appBD.adicionarApplicationBD(app, type);
        }
        // Atualiza a memória
        this.applications = new ArrayList<>(applications);
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

    private void tentaCarregarCache(String type) {
        try {
            ArrayList<Application> local = appBD.getAllApplicationsBD(type);

            if (local != null && !local.isEmpty()) {
                System.out.println("DEBUG: Recuperados " + local.size() + " itens da cache.");
                if (applicationsListener != null) {
                    applicationsListener.onApplicationsOffline(local);
                }
            } else {
                System.out.println("DEBUG: A cache está vazia.");
            }
        } catch (Exception e) {
            e.printStackTrace();
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



    public void signupAPI(final String username,
                          final String email,
                          final String password,
                          final Context context) {

        if (!isConnectionInternet(context)) {
            Toast.makeText(context, R.string.txt_nao_tem_internet, Toast.LENGTH_SHORT).show();
            return;
        }

        StringRequest request = new StringRequest(
                Request.Method.POST,
                postmUrlAPISignup,
                response -> {
                    try {
                        JSONObject json = new JSONObject(response);

                        boolean success = json.optBoolean("success", false);

                        if (success) {
                            if (signupListener != null) {
                                signupListener.onSignupResultListener(true, "Conta criada com sucesso");
                            }
                        } else {
                            if (signupListener != null) {
                                signupListener.onSignupResultListener(false, "Erro ao criar conta");
                            }
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                        if (signupListener != null) {
                            signupListener.onSignupResultListener(false, "Erro a processar resposta");
                        }
                    }
                },
                error -> {

                    String msg = "Erro desconhecido";

                    if (error.networkResponse != null) {
                        int code = error.networkResponse.statusCode;

                        if (code == 422) msg = "Dados inválidos";
                        else if (code == 409) msg = "Utilizador já existe";
                        else if (code == 500) msg = "Erro no servidor";
                    } else if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                        msg = "Sem ligação ao servidor";
                    }

                    if (signupListener != null) {
                        signupListener.onSignupResultListener(false, msg);
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("username", username);
                params.put("email", email);
                params.put("password", password);
                return params;
            }

            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                headers.put("Accept", "application/json");
                return headers;
            }
        };

        volleyQueue.add(request);
    }



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
                putmUrlAPIMe,
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
    public void uploadAvatar(Context context, Uri avatarUri, AvatarUploadListener listener) {

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

//    public void notifyMenuRefresh(Me me) {
//        this.me = me;
//
//        if (menuListener != null) {
//            menuListener.onRefreshMenu(me);
//        }
//    }


    public String getToken(Context context) {
        SharedPreferences sharedPreferences =
                context.getSharedPreferences("DADOS_USER", Context.MODE_PRIVATE);

        return sharedPreferences.getString(MenuMainActivity.TOKEN, null);
    }

    public int getUserLoggedId() {
        SharedPreferences sp = context.getSharedPreferences("DADOS_USER", Context.MODE_PRIVATE);
        return sp.getInt("USER_ID_INT", -1); // -1 = não autenticado
    }

    public interface SendMessageListener {
        void onSuccess();

        void onError(String msg);
    }
}

