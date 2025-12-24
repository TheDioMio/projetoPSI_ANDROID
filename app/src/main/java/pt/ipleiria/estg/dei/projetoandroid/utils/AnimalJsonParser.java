package pt.ipleiria.estg.dei.projetoandroid.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import pt.ipleiria.estg.dei.projetoandroid.modelo.Animal;
import pt.ipleiria.estg.dei.projetoandroid.modelo.AnimalFile;
import pt.ipleiria.estg.dei.projetoandroid.modelo.Comment;

public class AnimalJsonParser {


    public static ArrayList<Animal> parserJsonAnimals(JSONArray response) {

        ArrayList<Animal> animals = new ArrayList<>();

        try {
            for (int i = 0; i < response.length(); i++) {

                JSONObject animalJson = response.getJSONObject(i);

                int id = animalJson.getInt("id");
                String name = animalJson.getString("name");
                String description = animalJson.getString("description");
                String createdAt = animalJson.getString("created_at");

                String age = animalJson.isNull("age") ? null : animalJson.getString("age");
                String size = animalJson.getString("size");
                String type = animalJson.getString("type");
                String breed = animalJson.getString("breed");
                String location = animalJson.isNull("location")
                        ? null
                        : animalJson.getString("location");

                String ownerAddress = animalJson.isNull("location")                    //<- futuramente receber o address do dono do animal, mas para já fica com a localização do animal
                        ? null
                        : animalJson.getString("location");

                String neutered = animalJson.getInt("neutered") == 1 ? "Sim" : "Não";
                String vaccination = animalJson.getString("vaccination");

                String ownerName = animalJson.getString("owner_name");
                String ownerEmail = animalJson.getString("owner_email");
                String ownerAvatar = animalJson.isNull("owner_avatar")
                        ? null
                        : animalJson.getString("owner_avatar");

                ArrayList<AnimalFile> animalFiles = new ArrayList<>();
                if (animalJson.has("files")) {
                    JSONArray filesArray = animalJson.getJSONArray("files");
                    animalFiles = AnimalJsonParser.parserJsonFiles(filesArray);
                }

                // -------------------
                // LISTING
                // -------------------
                JSONObject listingJson = animalJson.getJSONObject("listing");

                String listingDescription = listingJson.isNull("description")
                        ? null
                        : listingJson.getString("description");

                String listingViews = String.valueOf(listingJson.getInt("views"));

                // -------------------
                // COMMENTS aqui guardamos os comentários
                // -------------------
                ArrayList<Comment> comments = new ArrayList<>();

                if (listingJson.has("comments")) {
                    JSONArray commentsArray = listingJson.getJSONArray("comments");
                    comments = AnimalJsonParser.parserJsonComments(commentsArray);
                }



                Animal animal = new Animal(
                        id,
                        name,
                        description,
                        createdAt,
                        age,
                        size,
                        type,
                        breed,
                        neutered,
                        vaccination,
                        location,
                        ownerName,
                        ownerAddress,
                        ownerEmail,
                        ownerAvatar,
                        listingDescription,
                        listingViews,
                        comments,
                        animalFiles
                );

                animals.add(animal);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return animals;
    }

    public static ArrayList<Comment> parserJsonComments(JSONArray commentsArray) {

        ArrayList<Comment> comments = new ArrayList<>();

        try {
            for (int i = 0; i < commentsArray.length(); i++) {

                JSONObject commentJson = commentsArray.getJSONObject(i);

                int idComment = commentJson.getInt("id");
                int idAnimal = commentJson.getInt("animal_id");

                String text = commentJson.getString("comment_text");

                String date = commentJson.isNull("comment_date") ? null : commentJson.getString("comment_date");

                String userName = commentJson.getString("name_user");

                String userAvatar = commentJson.isNull("avatar_user") ? null : commentJson.getString("avatar_user");

                Comment comment = new Comment(
                        idComment,
                        idAnimal,
                        text,
                        date,
                        userName,
                        userAvatar
                );

                comments.add(comment);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return comments;
    }


    public static ArrayList<AnimalFile> parserJsonFiles(JSONArray filesArray) {

        ArrayList<AnimalFile> animalFiles = new ArrayList<>();

        try {
            for (int i = 0; i < filesArray.length(); i++) {

                JSONObject fileJson = filesArray.getJSONObject(i);

                int idFile = fileJson.getInt("id_file");
                int idAnimal = fileJson.getInt("id_animal");

                String fileAddress = fileJson.getString("file_address");

                AnimalFile file = new AnimalFile(
                        idFile,
                        idAnimal,
                        fileAddress
                );

                animalFiles.add(file);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return animalFiles;
    }


//    public static boolean isConnectionInternet(Context context){
//        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
//
//        //necessita de permissões de acesso a internet
//        //e acesso ao estado la ligação
//        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
//
//        return activeNetwork != null && activeNetwork.isConnectedOrConnecting();
//    }




}
