package pt.ipleiria.estg.dei.projetoandroid.utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import pt.ipleiria.estg.dei.projetoandroid.modelo.AnimalEdit;
import pt.ipleiria.estg.dei.projetoandroid.modelo.AnimalFile;

public class AnimalEditJsonParser {

    public static AnimalEdit parse(JSONObject response) {

        AnimalEdit animal = new AnimalEdit();

        try {
            // ===== ANIMAL =====
            animal.setId(response.getInt("id"));
            animal.setName(response.getString("name"));
            animal.setDescription(response.getString("description"));
            animal.setLocation(response.getString("location"));

            animal.setTypeId(response.getInt("type_id"));
            animal.setBreedId(response.getInt("breed_id"));
            animal.setAgeId(response.getInt("age_id"));
            animal.setSizeId(response.getInt("size_id"));
            animal.setVaccinationId(response.getInt("vaccination_id"));
            animal.setNeutered(response.getInt("neutered"));

            // ===== LISTING =====
            JSONObject listingObj = response.getJSONObject("listing");

            animal.setListingId(listingObj.getInt("id"));
            animal.setListingDescription(listingObj.getString("description"));
            animal.setListingStatus(listingObj.getInt("status"));

            // ===== FILES =====
            JSONArray filesArr = response.getJSONArray("files");
            List<AnimalFile> files = new ArrayList<>();

            for (int i = 0; i < filesArr.length(); i++) {
                JSONObject f = filesArr.getJSONObject(i);

                AnimalFile file = new AnimalFile(
                        f.getInt("id_file"),
                        f.getInt("id_animal"),
                        f.getString("file_address")
                );

                files.add(file);
            }

            animal.setFiles(files);

        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }

        return animal;
    }
}


