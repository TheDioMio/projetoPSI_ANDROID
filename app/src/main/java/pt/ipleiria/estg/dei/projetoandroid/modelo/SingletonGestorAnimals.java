package pt.ipleiria.estg.dei.projetoandroid.modelo;

import android.os.Build;

import androidx.annotation.RequiresApi;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.chrono.ChronoLocalDateTime;
import java.util.ArrayList;
import java.util.Date;

public class SingletonGestorAnimals {

    private static SingletonGestorAnimals instance = null;

    private ArrayList<Animal> animals;

    @RequiresApi(api = Build.VERSION_CODES.O)
    public static synchronized SingletonGestorAnimals getInstance(){
        if (instance == null){
            instance = new SingletonGestorAnimals();
        }
        return instance;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public SingletonGestorAnimals() {
        animals = new ArrayList<>();
        gerarDadosDinamicos();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void gerarDadosDinamicos() {
        Date dataFixa = Date.from(Instant.now());

        //está a carregar as imagens do disco local, mas no futuro vem da API
        ArrayList<String> imagens = new ArrayList<>();
        imagens.add("https:\\localhost\\www\\projetoPSI_WEB\\backend\\web\\assets\\af788a85\\img\\avatar.pmg");
        imagens.add("C:\\Users\\Igor\\Desktop\\img\\cao2.png");
        imagens.add("C:\\Users\\Igor\\Desktop\\img\\cao3.png");
        imagens.add("C:\\Users\\Igor\\Desktop\\img\\cao4.png");
        imagens.add("C:\\Users\\Igor\\Desktop\\img\\cao5.png");

        animals.add(new Animal(
                1,                  // id
                2,                  // size (ex: 1=pequeno, 2=médio, 3=grande)
                3,                  // age (em anos)
                1,                  // animal_type_id (ex: 1=cão)
                5,                  // breed_id (ex: 5=labrador)
                1,                  // vaccines (ex: número de vacinas)
                10,                 // user_id (dono ou criador)
                "Rex",              // name
                "Cão muito amigável e brincalhão",  // description
                "Lisboa",           // location
                true,               // neutered
                dataFixa,// created_at
                imagens             // lista de imagens
        ));

        animals.add(new Animal(
                2,                  // id
                2,                  // size (ex: 1=pequeno, 2=médio, 3=grande)
                3,                  // age (em anos)
                1,                  // animal_type_id (ex: 1=cão)
                5,                  // breed_id (ex: 5=labrador)
                1,                  // vaccines (ex: número de vacinas)
                10,                 // user_id (dono ou criador)
                "Mia",              // name
                "Gato muito amigável e brincalhão",  // description
                "Lisboa",           // location
                true,               // neutered
                dataFixa,// created_at
                imagens             // lista de imagens
        ));

        animals.add(new Animal(
                3,                  // id
                2,                  // size (ex: 1=pequeno, 2=médio, 3=grande)
                3,                  // age (em anos)
                1,                  // animal_type_id (ex: 1=cão)
                5,                  // breed_id (ex: 5=labrador)
                1,                  // vaccines (ex: número de vacinas)
                10,                 // user_id (dono ou criador)
                "Bobi",              // name
                "Cão muito amigável e brincalhão",  // description
                "Lisboa",           // location
                true,               // neutered
                dataFixa,// created_at
                imagens             // lista de imagens
        ));

        //animals.add(new Animal(1, 1, 1, 1, 1, 1, 1, "Rex", "Animal Meigo, gosta de crianças", "Leiria", true, dataFixa, ""));
    }

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

}
