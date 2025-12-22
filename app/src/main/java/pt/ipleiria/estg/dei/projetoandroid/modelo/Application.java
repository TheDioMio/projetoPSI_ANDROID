package pt.ipleiria.estg.dei.projetoandroid.modelo;

public class Application {
    private int id;
    private int status; // 0=Pendente, 1=Aceite, 2=Recusada (exemplo)
    private String description;
    private int userId; // Quem enviou
    private int animalId; // Animal associado (pode ser 0 se for null na BD)
    private int type; // Tipo de candidatura
    private String createdAt; // Data de criação (String para facilitar)
    private int targetUserId; // Quem recebe (dono do animal)
    private String data; // O campo JSON vem como String
    private String statusDate; // Data da mudança de estado
    private int isRead; // 0 = Não lida, 1 = Lida

    // Construtor Completo
    public Application(int id, int status, String description, int userId, int animalId,
                       int type, String createdAt, int targetUserId, String data,
                       String statusDate, int isRead) {
        this.id = id;
        this.status = status;
        this.description = description;
        this.userId = userId;
        this.animalId = animalId;
        this.type = type;
        this.createdAt = createdAt;
        this.targetUserId = targetUserId;
        this.data = data;
        this.statusDate = statusDate;
        this.isRead = isRead;
    }

    // Getters
    public int getId() { return id; }
    public int getStatus() { return status; }
    public String getDescription() { return description; }
    public int getUserId() { return userId; }
    public int getAnimalId() { return animalId; }
    public int getType() { return type; }
    public String getCreatedAt() { return createdAt; }
    public int getTargetUserId() { return targetUserId; }
    public String getData() { return data; }
    public String getStatusDate() { return statusDate; }
    public int getIsRead() { return isRead; } // Podes mudar para boolean se preferires converter

    // Setters (Caso precises de alterar valores na APP)
    public void setStatus(int status) { this.status = status; }
    public void setIsRead(int isRead) { this.isRead = isRead; }

    // Método auxiliar para obter o estado em texto (Opcional, mas útil para a Lista)
    public String getStatusTexto() {
        switch (this.status) {
            case 0: return "Pendente";
            case 1: return "Aceite";
            case 2: return "Recusada";
            default: return "Desconhecido";
        }
    }
}