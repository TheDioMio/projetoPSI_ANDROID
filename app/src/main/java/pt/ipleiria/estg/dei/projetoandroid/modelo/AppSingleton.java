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
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import pt.ipleiria.estg.dei.projetoandroid.R;
import pt.ipleiria.estg.dei.projetoandroid.listeners.LoginListener;
import pt.ipleiria.estg.dei.projetoandroid.listeners.MenuListener;
import pt.ipleiria.estg.dei.projetoandroid.listeners.MessagesListener;
import pt.ipleiria.estg.dei.projetoandroid.utils.MessageJsonParser;
import pt.ipleiria.estg.dei.projetoandroid.utils.UserJsonParser;

public class AppSingleton {

    private static RequestQueue volleyQueue;

    public String endereco = "http://10.0.2.2/PSI/projetoPSI_WEB/backend/web/api";
    //endereço para as imagens
    public static final String FRONTEND_BASE_URL = "http://10.0.2.2/PSI/projetoPSI_WEB/frontend/web";
    private String getmUrlAPILogin = endereco+"/auth/login";
    private String getmUrlAPIMe = endereco+"/users/me";
    private String getMessageURL = endereco+"/messages";

    public void setLoginListener(LoginListener loginListener) {
        this.loginListener = loginListener;
    }
    private LoginListener loginListener;
    private MenuListener menuListener;

    private Me me;

    private MessagesListener messagesListener;



    private static AppSingleton instance = null;
    private GestorAnimals gestorAnimals = new GestorAnimals();
    private GestorUsers gestorUsers = new GestorUsers();
    private GestorAnimalType gestorAnimalType = new GestorAnimalType();
    private GestorAnimalBreed gestorAnimalBreed = new GestorAnimalBreed();
    private GestorAnimalAge gestorAnimalAge = new GestorAnimalAge();
    private GestorAnimalSize gestorAnimalSize = new GestorAnimalSize();
    private GestorVaccination gestorVaccination = new GestorVaccination();
    private GestorApplication gestorApplication = new GestorApplication();
    private GestorMessage gestorMessage = new GestorMessage();

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

    public void addApplication(int user_id, int animal_id, String description) {
        gestorApplication.addApplication(user_id, animal_id, description);
    }
    public ArrayList<Application> getApplications() {
        return gestorApplication.getApplications();
    }

    // -------------------------
    // GESTOR Message
    // -------------------------

    public ArrayList<Message> getMessagesForUser (int userId){
        return gestorMessage.getMessageForUser(userId);
    }


    public Message getMessage(int idMessage) {
        return gestorMessage.getMessageById(idMessage);
    }

    public void setMessageListener(MessagesListener messageListener) {
        this.messagesListener = messageListener;
    }

    public void getAllMessagesAPI(final Context context) {

        JsonArrayRequest request = new JsonArrayRequest(
                Request.Method.GET,
                getMessageURL,   // já tens: endereco + "/messages"
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



    //codigo da ficha books

//    public void loginAPI(final String email, final String password, final Context context){
//        if(!LivroJsonParser.isConnectionInternet(context)){
//            Toast.makeText(context, R.string.txt_nao_tem_internet, Toast.LENGTH_SHORT).show();
//        }else{
//            StringRequest request = new StringRequest(Request.Method.POST, getmUrlAPILogin, new Response.Listener<String>() {
//                @Override
//                public void onResponse(String s) {
//                    String token = LivroJsonParser.parserJsonLogin(s);
//                    if(loginListener != null) {
//                        loginListener.onValidateLogin(token, email);
//                    }
//                }
//            }, new Response.ErrorListener() {
//                @Override
//                public void onErrorResponse(VolleyError volleyError) {
//                    if (volleyError.networkResponse != null){
//                        int statusCode = volleyError.networkResponse.statusCode;
//                        System.out.println("-->STATUS: " + statusCode);
//                    }else{
//                        System.out.println("-->Erro: TIMEOUT OU SEM NET");
//                    }
//                    Toast.makeText(context, volleyError.getMessage(), Toast.LENGTH_SHORT).show();
//                }
//            }){
//                @Nullable
//                @Override
//                protected Map<String, String> getParams() throws AuthFailureError {
//                    Map<String, String> params = new HashMap<>();
//                    params.put("email",email);
//                    params.put("password", password);
//                    return params;
//                }
//            };
//            volleyQueue.add(request);
//        }
//    }


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
//                @Override
//                public void onErrorResponse(VolleyError volleyError) {
//                    if (volleyError.networkResponse != null){
//                        int statusCode = volleyError.networkResponse.statusCode;
//                        System.out.println("-->STATUS: " + statusCode);
//                    }else{
//                        System.out.println("-->Erro: TIMEOUT OU SEM NET");
//                    }
//                    Toast.makeText(context, volleyError.getMessage(), Toast.LENGTH_SHORT).show();
//                }
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


    public void getMe( final Context context){
        if(!UserJsonParser.isConnectionInternet(context)){
            Toast.makeText(context, R.string.txt_nao_tem_internet, Toast.LENGTH_SHORT).show();
        }else{
            JsonObjectRequest request = new JsonObjectRequest (Request.Method.GET, getmUrlAPIMe, null, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    //atualiza o singleton
                    me = UserJsonParser.parserJsonMe(response);
                    //atualiza a BD
                    //adicionarLivrosBD(livros);
                    if (menuListener != null) {
                        menuListener.onRefreshMenu(me);
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    String msg;

                    if (error.networkResponse != null) {
                        msg = "Erro " + error.networkResponse.statusCode;
                    } else {
                        msg = "Erro de ligação ao servidor";
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

        return sharedPreferences.getString("TOKEN", null);
    }


}
