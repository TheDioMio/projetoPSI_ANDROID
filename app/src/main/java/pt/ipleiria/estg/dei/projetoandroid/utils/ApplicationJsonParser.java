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
                        obj.getInt("status"),
                        obj.optString("description", "Sem descrição"),
                        obj.getInt("user_id"),
                        obj.optInt("animal_id", 0),
                        obj.optInt("type", 0),
                        obj.optString("created_at", ""),
                        obj.optInt("target_user_id", 0),
                        obj.optString("data", ""),
                        obj.optString("statusDate", ""),
                        obj.optInt("isRead", 0)
                );

                lista.add(app);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return lista;
    }
}