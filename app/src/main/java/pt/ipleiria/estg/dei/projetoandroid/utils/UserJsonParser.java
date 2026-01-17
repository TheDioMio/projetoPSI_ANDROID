package pt.ipleiria.estg.dei.projetoandroid.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import pt.ipleiria.estg.dei.projetoandroid.modelo.Me;

public class UserJsonParser {


    public static Me parserJsonMe(JSONObject resp){
        Me auxMe = null;
        try{
            //JSONObject resp = new JSONObject(response);
            if (resp.getBoolean("success")){

                String email = resp.getString("email");
                String name = resp.getString("name");
                String username = resp.getString("username");
                String address = resp.getString("address");
                String avatar = resp.getString("avatar");
                int user_id = resp.getInt("id");

                auxMe = new Me(user_id, name, username, avatar, address, email);
            }

        }catch (JSONException e){
            e.printStackTrace();
        }

        return auxMe;
    }


    public static String parserJsonLogin(String response){
        String token = null, email = null, name = null, username= null, avatar = null;
        int user_id = -1;
        try{
            JSONObject login = new JSONObject(response);
            if (login.getBoolean("success")){
                token = login.getString("token");

            }

        }catch (JSONException e){
            e.printStackTrace();
        }

        return token;
    }

    public static boolean isConnectionInternet(Context context){
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        //necessita de permissões de acesso a internet
        //e acesso ao estado la ligação
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();

        return activeNetwork != null && activeNetwork.isConnectedOrConnecting();
    }



}
