package pt.ipleiria.estg.dei.projetoandroid.modelo;

public class Application {
    private int id;
    private int animalId;
    private int status;
    private int type;
    private String description;

    private String userId;
    private String animalName;

    private String createdAt;
    private String targetUserId;

    private String statusDate;
    private int isRead;

    private String name;      // Nome do candidato (do form)
    private int age;          // Idade
    private String contact;
    private String motive;

    private String home;
    private String timeAlone;
    private String bills;
    private String children;
    private String followUp;

    public Application(int id, int animalId, int status, int type, String description, String userId, String animalName,
                       String createdAt, String targetUserId, String statusDate, int isRead,
                       /*String name,*/ int age, String contact, String motive, String home,
                       String timeAlone, String bills, String children, String followUp) {
        this.id = id;
        this.animalId = animalId;
        this.status = status;
        this.description = description;
        this.userId = userId;
        this.animalName = animalName;
        this.createdAt = createdAt;
        this.targetUserId = targetUserId;
        this.statusDate = statusDate;
        this.isRead = isRead;
        this.type = type;

        //ISTO VEM DO JSON
//        this.name = name;
        this.age = age;
        this.contact = contact;
        this.motive = motive;
        this.home = home;
        this.timeAlone = timeAlone;
        this.bills = bills;
        this.children = children;
        this.followUp = followUp;
        //ISTO VEM DO JSON
    }

    // --- Getters ---
    public int getId() { return id; }

    public int getAnimalId() {return animalId;}

    public int getType() {return type;}

    public String getName() {return name;}

    public int getStatus() { return status; }
    public String getDescription() { return description; }
    public String getUserId() { return userId; }
    public String getAnimalName() { return animalName; }
    public String getCreatedAt() { return createdAt; }
    public String getTargetUserId() { return targetUserId; }
    public String getStatusDate() { return statusDate; }
    public int getIsRead() { return isRead; }

//    public String getName() { return name; }
    public int getAge() { return age; }
    public String getContact() { return contact; }
    public String getMotive() { return motive; }
    public String getHome() { return home; }
    public String getTimeAlone() { return timeAlone; }
    public String getBills() { return bills; }
    public String getChildren() { return children; }
    public String getFollowUp() { return followUp; }
    // --- Getters ---


    // Setters
    public void setStatus(int status) { this.status = status; }
    public void setIsRead(int isRead) { this.isRead = isRead; }

    // Métodos auxiliares para receber parametros json em string
    // Helper para o Status
    public String getStatusTexto() {
        switch (this.status) {
            case 0: return "Enviado";
            case 1: return "Em Análise";
            case 2: return "Aprovada";
            case 3: return "Rejeitada";
            case 4: return "Cancelada";
            default: return "Desconhecido";
        }
    }
}