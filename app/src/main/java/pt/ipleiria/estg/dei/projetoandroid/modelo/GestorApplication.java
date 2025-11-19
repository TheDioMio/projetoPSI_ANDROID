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

        applications.add(new Application(newId, status, user_id, animal_id, type, target_user_id, description));
    }
}
