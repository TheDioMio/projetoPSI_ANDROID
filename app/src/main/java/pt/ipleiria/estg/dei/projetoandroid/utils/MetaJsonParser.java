package pt.ipleiria.estg.dei.projetoandroid.utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import pt.ipleiria.estg.dei.projetoandroid.modelo.MetaItem;

public class MetaJsonParser {

    public static HashMap<String, ArrayList<MetaItem>> parserMeta(JSONObject response)
            throws JSONException {

        HashMap<String, ArrayList<MetaItem>> meta = new HashMap<>();

        meta.put("types", parseSimpleArray(response.getJSONArray("types")));
        meta.put("ages", parseSimpleArray(response.getJSONArray("ages")));
        meta.put("sizes", parseSimpleArray(response.getJSONArray("sizes")));
        meta.put("vaccinations", parseSimpleArray(response.getJSONArray("vaccinations")));
        meta.put("breeds", parseBreeds(response.getJSONArray("breeds")));

        return meta;
    }

    private static ArrayList<MetaItem> parseSimpleArray(JSONArray array)
            throws JSONException {

        ArrayList<MetaItem> list = new ArrayList<>();

        for (int i = 0; i < array.length(); i++) {
            JSONObject obj = array.getJSONObject(i);
            list.add(new MetaItem(
                    obj.getInt("id"),
                    obj.getString("description"),
                    null
            ));
        }
        return list;
    }

    private static ArrayList<MetaItem> parseBreeds(JSONArray array)
            throws JSONException {

        ArrayList<MetaItem> list = new ArrayList<>();

        for (int i = 0; i < array.length(); i++) {
            JSONObject obj = array.getJSONObject(i);
            list.add(new MetaItem(
                    obj.getInt("id"),
                    obj.getString("description"),
                    obj.getInt("animal_type_id")
            ));
        }
        return list;
    }
}


