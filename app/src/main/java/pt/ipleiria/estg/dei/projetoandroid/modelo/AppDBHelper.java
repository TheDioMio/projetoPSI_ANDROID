package pt.ipleiria.estg.dei.projetoandroid.modelo;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

public class AppDBHelper extends SQLiteOpenHelper {

    private static AppDBHelper instance;
    private static final String DB_NAME = "petpanionDB";
    private static final int DB_VERSION = 1;
    private final SQLiteDatabase database;

    //1.PARA JA VAMOS APENAS BUSCAR OS DADOS DOS ANIMAIS, MAS APENAS OS DADOS PARA SEREM EXIBIDOS, NÃO VAI DAR PARA EDITAR (NÃO VAI TRAZER AS CHAVES, VAI TRAZER OS TEXTOS)
    //2.CAMPOS DA TABELA ANIMAIS (VAI CONTER OS DADOS DOS LISTINGS E DO USER OWNER(DONO)) FAZER NUMA TABELA DIFERENTE ------> APENAS PARA NÃO ME ESQUECER
    //Tabela Animals
    // VAMOS RECEBER OS IDs DAS CHAVES ESTRANGEIRAS POIS VAMOS QUERER EDITAR O ANIMAL E TAMBÉM VAMOS QUERER FILTRAR

    private static final String TABLE_ANIMALS = "animals";
    public static final String ID = "id";
    public static final String NAME = "name";
    public static final String DESCRIPTION = "description";
    public static final String CREATED_AT = "created_at";
    public static final String AGE = "capa";
    public static final String SIZE = "size";
    public static final String TYPE = "type";
    public static final String BREED = "breed";
    public static final String NEUTERED = "neutered";
    public static final String VACINATION = "vacination";
    public static final String LOCATION = "location";
    public static final String OWNER_NAME = "owner_name";
    public static final String OWNER_ADDRESS = "owner_address";
    public static final String OWNER_EMAIL = "owner_email";
    public static final String OWNER_AVATAR = "owner_avatar";

    //DADOS QUE VEM DOS LISTING
    public static final String LISTING_DESCRIPTION = "listing_description";
    public static final String LISTING_VIEWS = "listing_views";


    //TABELA DOS FILES (PARA GUARDAR O CAMINHO DAS FOTOS DOS ANIMAIS)
    private static final String TABLE_ANIMALS_FILES = "animals_files";
    public static final String ID_FILE = "id_file";
    public static final String ID_ANIMAL = "id_animal";
    public static final String FILE_ADDRESS = "file_address";



    //TABELA DAS APPLICATIONS
    private static final String TABLE_APPLICATIONS = "applications";
    public static final String APPLICATION_ID = "id";
    public static final String STATUS = "status";
    public static final String APPLICATION_TYPE = "type";
    public static final String APPLICATION_CREATED_AT = "created_at";
    public static final String STATUS_DATE = "statusDate";
    public static final String IS_READ = "isRead";
    public static final String APPLICATION_DESCRIPTION = "description";
    public static final String CANDIDATE_NAME = "candidate_name";
    public static final String TARGET_USER_ID = "target_user_id";
    public static final String APPLICATION_ANIMAL_ID = "animal_id";
    public static final String APPLICATION_ANIMAL_NAME = "animal_name";
    public static final String APPLICATION_ANIMAL_IMAGE = "animal_image";
    public static final String CANDIDATE_AGE = "candidate_age";
    public static final String CANDIDATE_CONTACT = "candidate_contact";
    public static final String MOTIVE = "motive";
    public static final String HOME = "home";
    public static final String BILLS = "bills";
    public static final String TIME_ALONE = "timeAlone";
    public static final String CHILDREN = "children";
    public static final String FOLLOW_UP = "followUp";



    public static synchronized AppDBHelper getInstance(Context context) {
        if (instance == null) {
            instance = new AppDBHelper(context.getApplicationContext());
        }
        return instance;
    }



    //TABELA DOS COMENTÁRIOS
    private static final String TABLE_ANIMALS_COMMENTS = "animal_comments";
    public static final String ID_COMMENT = "id_comment";
    //VAI CONTER O ID DO ANIMAL MAS A CONSTANTE JÁ SE ENCONTRA DECLARADA
    //public static final String ID_ANIMAL = "id_animal";
    public static final String COMMENT_TEXT = "comment_text";
    public static final String COMMENT_DATE = "comment_date";
    public static final String NAME_USER = "name_user";
    public static final String AVATAR_USER = "avatar_user";



    private AppDBHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
        database = getWritableDatabase();
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        String createAnimalsTable = "CREATE TABLE " + TABLE_ANIMALS + " (" +
                ID + " INTEGER PRIMARY KEY, " +
                NAME + " TEXT, " +
                DESCRIPTION + " TEXT, " +
                CREATED_AT + " TEXT, " +
                AGE + " TEXT, " +
                SIZE + " TEXT, " +
                TYPE + " TEXT, " +
                BREED + " TEXT, " +
                NEUTERED + " TEXT, " +
                VACINATION + " TEXT, " +
                LOCATION + " TEXT, " +
                OWNER_NAME + " TEXT, " +
                OWNER_ADDRESS + " TEXT, " +
                OWNER_EMAIL + " TEXT, " +
                OWNER_AVATAR + " TEXT, " +
                LISTING_DESCRIPTION + " TEXT, " +
                LISTING_VIEWS + " TEXT" +
                ");";
        db.execSQL(createAnimalsTable);


        String createFilesTable = "CREATE TABLE "+ TABLE_ANIMALS_FILES +" (" +
                ID_FILE + " INTEGER PRIMARY KEY, " +
                ID_ANIMAL + " INTEGER, " +
                FILE_ADDRESS + " TEXT" +
                ");";
        db.execSQL(createFilesTable);

        String createCommentsTable = "CREATE TABLE "+ TABLE_ANIMALS_COMMENTS + " (" +
                ID_COMMENT + " INTEGER PRIMARY KEY, " +
                ID_ANIMAL + " INTEGER, " +
                COMMENT_TEXT + " TEXT, " +
                COMMENT_DATE + " TEXT, " +
                NAME_USER + " TEXT, " +
                AVATAR_USER + " TEXT" +
                ");";
        db.execSQL(createCommentsTable);

        String createApplicationsTable = "CREATE TABLE " + TABLE_APPLICATIONS + " (" +
                APPLICATION_ID + " INTEGER PRIMARY KEY, " +
                STATUS + " TEXT, " +
                APPLICATION_TYPE + " INTEGER, " +
                APPLICATION_CREATED_AT + " TEXT, " +
                STATUS_DATE + " TEXT, " +
                IS_READ + " INTEGER, " +
                APPLICATION_DESCRIPTION + " TEXT, " +
                CANDIDATE_NAME + " TEXT, " +
                TARGET_USER_ID + " TEXT, " +
                APPLICATION_ANIMAL_ID + " INTEGER, " +
                APPLICATION_ANIMAL_NAME + " TEXT, " +
                APPLICATION_ANIMAL_IMAGE + " TEXT, " +

                //DADOS JSON DO FORMULÁRIO
                CANDIDATE_AGE + " INTEGER, " +
                CANDIDATE_CONTACT + " TEXT, " +
                MOTIVE + " TEXT, " +
                HOME + " TEXT, " +
                BILLS + " TEXT, " +
                TIME_ALONE + " TEXT, " +
                CHILDREN + " TEXT, " +
                FOLLOW_UP + " TEXT" +
                ");";
        db.execSQL(createApplicationsTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ANIMALS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ANIMALS_COMMENTS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ANIMALS_FILES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_APPLICATIONS);
        this.onCreate(db);
    }



    //INICIO-----------------------------------------------ANIMAIS-------------------------------------------------------------

    public ArrayList<Animal> getAllAnimalsBD() {
        ArrayList<Animal> animals = new ArrayList<>();

        Cursor cursor = database.query(
                TABLE_ANIMALS,
                new String[]{
                        ID, NAME, DESCRIPTION, CREATED_AT, AGE, SIZE, TYPE,
                        BREED, NEUTERED, VACINATION, LOCATION,
                        OWNER_NAME, OWNER_ADDRESS, OWNER_EMAIL, OWNER_AVATAR,
                        LISTING_DESCRIPTION, LISTING_VIEWS
                },
                null, null, null, null, null
        );

        if (cursor.moveToFirst()) {
            do {
                int animalId = cursor.getInt(0);

                ArrayList<Comment> comments = getCommentsByAnimalId(animalId);

                ArrayList<AnimalFile> files = getFilesByAnimalId(animalId);

                animals.add(new Animal(
                        animalId,
                        cursor.getString(1),  // NAME
                        cursor.getString(2),  // DESCRIPTION
                        cursor.getString(3),  // CREATED_AT
                        cursor.getString(4),  // AGE
                        cursor.getString(5),  // SIZE
                        cursor.getString(6),  // TYPE
                        cursor.getString(7),  // BREED
                        cursor.getString(8),  // NEUTERED
                        cursor.getString(9),  // VACINATION
                        cursor.getString(10), // LOCATION
                        cursor.getString(11), // OWNER_NAME
                        cursor.getString(12), // OWNER_ADDRESS
                        cursor.getString(13), // OWNER_EMAIL
                        cursor.getString(14), // OWNER_AVATAR
                        cursor.getString(15), // LISTING_DESCRIPTION
                        cursor.getString(16), // LISTING_VIEWS
                        comments,
                        files
                ));

            } while (cursor.moveToNext());
        }

        cursor.close();
        return animals;
    }

    public Animal adicionarAnimalBD(Animal animal) {

        ContentValues values = new ContentValues();

        values.put(ID, animal.getId());
        values.put(NAME, animal.getName());
        values.put(DESCRIPTION, animal.getDescription());
        values.put(CREATED_AT, animal.getCreatedAt());
        values.put(AGE, animal.getAge());
        values.put(SIZE, animal.getSize());
        values.put(TYPE, animal.getType());
        values.put(BREED, animal.getBreed());
        values.put(NEUTERED, animal.getNeutered());
        values.put(VACINATION, animal.getVacination());
        values.put(LOCATION, animal.getLocation());
        values.put(OWNER_NAME, animal.getOwnerName());
        values.put(OWNER_ADDRESS, animal.getOwnerAddress());
        values.put(OWNER_EMAIL, animal.getOwnerEmail());
        values.put(OWNER_AVATAR, animal.getOwnerAvatar());

        values.put(LISTING_DESCRIPTION, animal.getListingDescription());
        values.put(LISTING_VIEWS, animal.getListingViews());

        long id = database.insert(TABLE_ANIMALS, null, values);

        if (id > -1) {
            // se quiseres guardar o id no objeto
            // animal.setId((int) id);
            return animal;
        }

        return null;
    }

    public Animal getAnimalById(int idAnimal) {

        Animal animal = null;

        Cursor cursor = database.query(
                TABLE_ANIMALS, // animals
                null,
                ID + " = ?",
                new String[]{String.valueOf(idAnimal)},
                null, null, null
        );

        if (cursor.moveToFirst()) {

            animal = new Animal(
                    cursor.getInt(cursor.getColumnIndexOrThrow(ID)),
                    cursor.getString(cursor.getColumnIndexOrThrow(NAME)),
                    cursor.getString(cursor.getColumnIndexOrThrow(DESCRIPTION)),
                    cursor.getString(cursor.getColumnIndexOrThrow(CREATED_AT)),
                    cursor.getString(cursor.getColumnIndexOrThrow(AGE)),
                    cursor.getString(cursor.getColumnIndexOrThrow(SIZE)),
                    cursor.getString(cursor.getColumnIndexOrThrow(TYPE)),
                    cursor.getString(cursor.getColumnIndexOrThrow(BREED)),
                    cursor.getString(cursor.getColumnIndexOrThrow(NEUTERED)),
                    cursor.getString(cursor.getColumnIndexOrThrow(VACINATION)),
                    cursor.getString(cursor.getColumnIndexOrThrow(LOCATION)),
                    cursor.getString(cursor.getColumnIndexOrThrow(OWNER_NAME)),
                    cursor.getString(cursor.getColumnIndexOrThrow(OWNER_ADDRESS)),
                    cursor.getString(cursor.getColumnIndexOrThrow(OWNER_EMAIL)),
                    cursor.getString(cursor.getColumnIndexOrThrow(OWNER_AVATAR)),
                    cursor.getString(cursor.getColumnIndexOrThrow(LISTING_DESCRIPTION)),
                    cursor.getString(cursor.getColumnIndexOrThrow(LISTING_VIEWS)),
                    new ArrayList<>(), // comments
                    new ArrayList<>()  // animalfiles
            );
            animal.setAnimalfiles(getFilesByAnimalId(animal.getId()));
            animal.setComments(getCommentsByAnimalId(animal.getId()));
        }

        cursor.close();
        return animal;
    }

    public void removerAllAnimalsBD() {
        this.database.delete(TABLE_ANIMALS, null, null);
        this.database.delete(TABLE_ANIMALS_FILES, null, null);
        this.database.delete(TABLE_ANIMALS_COMMENTS, null, null);
    }


    //FIM-----------------------------------------------ANIMAIS-----------------------------------------------------


    //INICIO----------------------------------------------FOTOS-----------------------------------------------------

    public ArrayList<AnimalFile> getFilesBD(int idAnimal) {
        ArrayList<AnimalFile> files = new ArrayList<>();

        Cursor cursor = database.query(
                TABLE_ANIMALS_FILES,
                new String[]{ ID_FILE, ID_ANIMAL, FILE_ADDRESS },
                ID_ANIMAL + " = ?",
                new String[]{ String.valueOf(idAnimal) },
                null, null, null
        );

        if (cursor.moveToFirst()) {
            do {
                files.add(new AnimalFile(
                        cursor.getInt(0),    // ID
                        cursor.getInt(1),    // ID_ANIMAL
                        cursor.getString(2)  // FILE_ADDRESS
                ));
            } while (cursor.moveToNext());
        }

        cursor.close();
        return files;
    }


    public AnimalFile adicionarFileBD(AnimalFile file) {

        ContentValues values = new ContentValues();
        values.put(ID_FILE, file.getIdFile());
        values.put(ID_ANIMAL, file.getIdAnimal());
        values.put(FILE_ADDRESS, file.getFileAddress());

        long id = database.insert(TABLE_ANIMALS_FILES, null, values);

        if (id > -1) {
            return file;
        }

        return null;
    }


    public ArrayList<AnimalFile> getFilesByAnimalId(int idAnimal) {

        ArrayList<AnimalFile> files = new ArrayList<>();

        Cursor cursor = database.query(
                TABLE_ANIMALS_FILES,
                null,
                ID_ANIMAL + " = ?",
                new String[]{String.valueOf(idAnimal)},
                null, null, null
        );

        if (cursor.moveToFirst()) {
            do {
                files.add(new AnimalFile(
                        cursor.getInt(cursor.getColumnIndexOrThrow(ID_FILE)),
                        cursor.getInt(cursor.getColumnIndexOrThrow(ID_ANIMAL)),
                        cursor.getString(cursor.getColumnIndexOrThrow(FILE_ADDRESS))
                ));
            } while (cursor.moveToNext());
        }

        cursor.close();
        return files;
    }


    //FIM------------------------------------------------FOTOS-------------------------------------------

    //INICIO-------------------------------------------COMENTÁRIOS--------------------------------------------
    public ArrayList<Comment> getCommentsBD(int idAnimal) {
        ArrayList<Comment> comments = new ArrayList<>();

        Cursor cursor = database.query(
                TABLE_ANIMALS_COMMENTS,
                new String[]{
                        ID_COMMENT, ID_ANIMAL,
                        COMMENT_TEXT, COMMENT_DATE,
                        NAME_USER, AVATAR_USER
                },
                ID_ANIMAL + " = ?",
                new String[]{ String.valueOf(idAnimal) },
                null, null, COMMENT_DATE + " DESC"
        );

        if (cursor.moveToFirst()) {
            do {
                comments.add(new Comment(
                        cursor.getInt(0),    // ID_COMMENT
                        cursor.getInt(1),    // ID_ANIMAL
                        cursor.getString(2), // COMMENT_TEXT
                        cursor.getString(3), // COMMENT_DATE
                        cursor.getString(4), // NAME_USER
                        cursor.getString(5)  // AVATAR_USER
                ));
            } while (cursor.moveToNext());
        }

        cursor.close();
        return comments;
    }

    public Comment adicionarCommentBD(Comment comment) {

        ContentValues values = new ContentValues();
        values.put(ID_COMMENT, comment.getIdComment());
        values.put(ID_ANIMAL, comment.getIdAnimal());
        values.put(COMMENT_TEXT, comment.getText());
        values.put(COMMENT_DATE, comment.getDate());
        values.put(NAME_USER, comment.getUserName());
        values.put(AVATAR_USER, comment.getUserAvatar());

        long id = database.insert(TABLE_ANIMALS_COMMENTS, null, values);

        if (id > -1) {
            return comment;
        }

        return null;
    }

    public ArrayList<Comment> getCommentsByAnimalId(int idAnimal) {

        ArrayList<Comment> comments = new ArrayList<>();

        Cursor cursor = database.query(
                TABLE_ANIMALS_COMMENTS,
                null,
                ID_ANIMAL + " = ?",
                new String[]{String.valueOf(idAnimal)},
                null, null,
                COMMENT_DATE + " DESC"
        );

        if (cursor.moveToFirst()) {
            do {
                comments.add(new Comment(
                        cursor.getInt(cursor.getColumnIndexOrThrow(ID_COMMENT)),
                        cursor.getInt(cursor.getColumnIndexOrThrow(ID_ANIMAL)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COMMENT_TEXT)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COMMENT_DATE)),
                        cursor.getString(cursor.getColumnIndexOrThrow(NAME_USER)),
                        cursor.getString(cursor.getColumnIndexOrThrow(AVATAR_USER))
                ));
            } while (cursor.moveToNext());
        }

        cursor.close();
        return comments;
    }
//FIM---------------------------------------------------------COMENTÁRIOS---------------------------------------




    //INICIO-------------------------------------------APPLICATION--------------------------------------------

    public ArrayList<Application> getAllApplicationsBD() {
        ArrayList<Application> applications = new ArrayList<>();

        // Definimos as colunas que queremos buscar
        String[] columns = new String[]{
                APPLICATION_ID, APPLICATION_ANIMAL_ID, STATUS, APPLICATION_TYPE,
                APPLICATION_DESCRIPTION, CANDIDATE_NAME, APPLICATION_ANIMAL_NAME,
                APPLICATION_ANIMAL_IMAGE,
                APPLICATION_CREATED_AT, TARGET_USER_ID, STATUS_DATE, IS_READ,
                CANDIDATE_AGE, CANDIDATE_CONTACT, MOTIVE, HOME, TIME_ALONE,
                BILLS, CHILDREN, FOLLOW_UP
        };

        Cursor cursor = database.query(
                TABLE_APPLICATIONS,
                columns,
                null, null, null, null, null
        );

        if (cursor.moveToFirst()) {
            do {
                applications.add(new Application(
                        // 1. int id
                        cursor.getInt(cursor.getColumnIndexOrThrow(APPLICATION_ID)),
                        // 2. int animalId
                        cursor.getInt(cursor.getColumnIndexOrThrow(APPLICATION_ANIMAL_ID)),
                        // 3. String status
                        cursor.getString(cursor.getColumnIndexOrThrow(STATUS)),
                        // 4. int type
                        cursor.getInt(cursor.getColumnIndexOrThrow(APPLICATION_TYPE)),
                        // 5. String description
                        cursor.getString(cursor.getColumnIndexOrThrow(APPLICATION_DESCRIPTION)),
                        // 6. String candidateName
                        cursor.getString(cursor.getColumnIndexOrThrow(CANDIDATE_NAME)),
                        // 7. String animalName
                        cursor.getString(cursor.getColumnIndexOrThrow(APPLICATION_ANIMAL_NAME)),
                        // 8. String animalImage
                        cursor.getString(cursor.getColumnIndexOrThrow(APPLICATION_ANIMAL_IMAGE)),
                        // 9. String createdAt
                        cursor.getString(cursor.getColumnIndexOrThrow(APPLICATION_CREATED_AT)),
                        // 10. String targetUserId
                        cursor.getString(cursor.getColumnIndexOrThrow(TARGET_USER_ID)),
                        // 11. String statusDate
                        cursor.getString(cursor.getColumnIndexOrThrow(STATUS_DATE)),
                        // 12. int isRead
                        cursor.getInt(cursor.getColumnIndexOrThrow(IS_READ)),

                        // --- DADOS DO FORMULÁRIO ---
                        // 13. int age
                        cursor.getInt(cursor.getColumnIndexOrThrow(CANDIDATE_AGE)),
                        // 14. String contact
                        cursor.getString(cursor.getColumnIndexOrThrow(CANDIDATE_CONTACT)),
                        // 15. String motive
                        cursor.getString(cursor.getColumnIndexOrThrow(MOTIVE)),
                        // 16. String home
                        cursor.getString(cursor.getColumnIndexOrThrow(HOME)),
                        // 17. String timeAlone
                        cursor.getString(cursor.getColumnIndexOrThrow(TIME_ALONE)),
                        // 18. String bills
                        cursor.getString(cursor.getColumnIndexOrThrow(BILLS)),
                        // 19. String children
                        cursor.getString(cursor.getColumnIndexOrThrow(CHILDREN)),
                        // 20. String followUp
                        cursor.getString(cursor.getColumnIndexOrThrow(FOLLOW_UP))
                ));
            } while (cursor.moveToNext());
        }

        cursor.close();
        return applications;
    }

    public Application adicionarApplicationBD(Application application) {
        ContentValues values = new ContentValues();

        // Campos Base
        values.put(APPLICATION_ID, application.getId());
        values.put(STATUS, application.getStatus());
        values.put(APPLICATION_TYPE, application.getType());
        values.put(APPLICATION_CREATED_AT, application.getCreatedAt());
        values.put(STATUS_DATE, application.getStatusDate());
        values.put(IS_READ, application.getIsRead());
        values.put(APPLICATION_DESCRIPTION, application.getDescription());

        values.put(CANDIDATE_NAME, application.getCandidateName());
        values.put(TARGET_USER_ID, application.getTargetUserId());

        values.put(APPLICATION_ANIMAL_ID, application.getAnimalId());
        values.put(APPLICATION_ANIMAL_NAME, application.getAnimalName());

        // Dados do JSON (Formulário)
        values.put(CANDIDATE_AGE, application.getAge()); // int
        values.put(CANDIDATE_CONTACT, application.getContact());
        values.put(MOTIVE, application.getMotive());
        values.put(HOME, application.getHome());
        values.put(BILLS, application.getBills());
        values.put(TIME_ALONE, application.getTimeAlone());
        values.put(CHILDREN, application.getChildren());
        values.put(FOLLOW_UP, application.getFollowUp());

        long id = database.insert(TABLE_APPLICATIONS, null, values);

        if (id > -1) {
            return application;
        }
        return null;
    }

    public Application getApplicationById(int idApplication) {
        Application application = null;

        Cursor cursor = database.query(
                TABLE_APPLICATIONS,
                null, // Todas as colunas
                APPLICATION_ID + " = ?",
                new String[]{String.valueOf(idApplication)},
                null, null, null
        );

        if (cursor.moveToFirst()) {
            application = new Application(
                    // A ORDEM TEM DE SER IGUAL AO CONSTRUTOR DO MODELO
                    cursor.getInt(cursor.getColumnIndexOrThrow(APPLICATION_ID)),                // 1
                    cursor.getInt(cursor.getColumnIndexOrThrow(APPLICATION_ANIMAL_ID)),         // 2
                    cursor.getString(cursor.getColumnIndexOrThrow(STATUS)),                        // 3
                    cursor.getInt(cursor.getColumnIndexOrThrow(APPLICATION_TYPE)),              // 4
                    cursor.getString(cursor.getColumnIndexOrThrow(APPLICATION_DESCRIPTION)),    // 5
                    cursor.getString(cursor.getColumnIndexOrThrow(CANDIDATE_NAME)),             // 6
                    cursor.getString(cursor.getColumnIndexOrThrow(APPLICATION_ANIMAL_NAME)),    // 7
                    cursor.getString(cursor.getColumnIndexOrThrow(APPLICATION_ANIMAL_IMAGE)),   // 8
                    cursor.getString(cursor.getColumnIndexOrThrow(APPLICATION_CREATED_AT)),     // 9
                    cursor.getString(cursor.getColumnIndexOrThrow(TARGET_USER_ID)),             // 10
                    cursor.getString(cursor.getColumnIndexOrThrow(STATUS_DATE)),                // 11
                    cursor.getInt(cursor.getColumnIndexOrThrow(IS_READ)),                       // 12
                    cursor.getInt(cursor.getColumnIndexOrThrow(CANDIDATE_AGE)),                 // 13
                    cursor.getString(cursor.getColumnIndexOrThrow(CANDIDATE_CONTACT)),          // 14
                    cursor.getString(cursor.getColumnIndexOrThrow(MOTIVE)),                     // 15
                    cursor.getString(cursor.getColumnIndexOrThrow(HOME)),                       // 16
                    cursor.getString(cursor.getColumnIndexOrThrow(TIME_ALONE)),                 // 17
                    cursor.getString(cursor.getColumnIndexOrThrow(BILLS)),                      // 18
                    cursor.getString(cursor.getColumnIndexOrThrow(CHILDREN)),                   // 19
                    cursor.getString(cursor.getColumnIndexOrThrow(FOLLOW_UP))                   // 20
            );
        }

        cursor.close();
        return application;
    }

    public void removerAllApplicationsBD() {
        this.database.delete(TABLE_APPLICATIONS, null, null);
    }

    //FIM-----------------------------------------------APPLICATION---------------------------------------
}
