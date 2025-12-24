package pt.ipleiria.estg.dei.projetoandroid.modelo;

public class Application {
    private int id;
    private int animalId;
    private String status;
    private int type;
    private String description;

    private String candidateName;
    private String animalName;
    private String animalImage;

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

    public static final String STATUS_SENT = "Pendente";
    public static final String STATUS_PENDING = "Em análise";
    public static final String STATUS_APPROVED = "Aprovada";
    public static final String STATUS_REJECTED = "Rejeitada";
    public static final String STATUS_CANCELLED = "Cancelada";


    public Application(int id, int animalId, String status, int type, String description, String candidateName, String animalName,
                       String animalImage,String createdAt, String targetUserId, String statusDate, int isRead,
                       /*String name,*/ int age, String contact, String motive, String home,
                       String timeAlone, String bills, String children, String followUp) {
        this.id = id;
        this.animalId = animalId;
        this.status = status;
        this.description = description;
        this.candidateName = candidateName;
        this.animalName = animalName;
        this.animalImage = animalImage;
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

    public String getStatus() { return status; }
    public String getDescription() { return description; }
    public String getCandidateName() { return candidateName; }
    public String getAnimalName() { return animalName; }
    public String getAnimalImage() {return animalImage;}
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
    public void setIsRead(int isRead) { this.isRead = isRead; }
}