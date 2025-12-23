package pt.ipleiria.estg.dei.projetoandroid.utils;

import org.json.JSONArray;
import org.json.JSONException; // Falta este import
import org.json.JSONObject;

import java.util.ArrayList;

import pt.ipleiria.estg.dei.projetoandroid.modelo.Application;

public class ApplicationJsonParser {
    public static ArrayList<Application> parserJsonApplications(JSONArray response) {
        ArrayList<Application> lista = new ArrayList<>();

        try {
            for (int i = 0; i < response.length(); i++) {
                JSONObject obj = response.getJSONObject(i);

                Application app = new Application(
                        obj.getInt("id"),
                        obj.getInt("animal_id"),
                        obj.getInt("status"),
                        obj.getInt("type"),
                        obj.getString("description"),
                        obj.getString("user_id"),
                        obj.getString("animal_name"),
                        obj.getString("created_at"),
                        obj.getString("target_user_id"),
                        obj.getString("statusDate"),
                        obj.getInt("isRead"),
                        obj.getInt("age"),
                        obj.getString("contact"),
                        obj.getString("motive"),
                        obj.getString("home"),
                        obj.getString("timeAlone"),
                        obj.getString("bills"),
                        obj.getString("children"),
                        obj.getString("followUp")
                );

                lista.add(app);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return lista;
    }
}