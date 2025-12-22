package pt.ipleiria.estg.dei.projetoandroid.modelo;

import java.util.ArrayList;
import java.util.Objects;

public class GestorApplication {
    private ArrayList<Application> applications;


    public GestorApplication() {
        applications = new ArrayList<>();
    }

    public Application getApplication(int idApplication){
        for (Application application: applications) {
            if (application.getId()==idApplication){
                return application;
            }
        }
        return null;
    }

    public ArrayList<Application> getApplications() {
        return new ArrayList<>(applications);
    }

    public void addApplication(int user_id, int animal_id, String description) {
        int newId;
        if(applications.isEmpty()){
            newId = 1;
        } else {
            newId = applications.get(applications.size() -1).getId() + 1;
        }

        //Valores que foram feitos padrão:
        int status = 0;
        int type = 0;
        int target_user_id = 1;

        applications.add(new Application(
                newId,              // id
                status,             // status
                description,        // description (ATENÇÃO: Agora é o 3º parâmetro!)
                user_id,            // userId
                animal_id,          // animalId
                type,               // type
                "2023-01-01",       // createdAt (Valor provisório)
                target_user_id,     // targetUserId
                "{}",               // data (Valor provisório: JSON vazio)
                "",                 // statusDate (Valor provisório)
                0                   // isRead (0 = não lido)
        ));
    }
}
