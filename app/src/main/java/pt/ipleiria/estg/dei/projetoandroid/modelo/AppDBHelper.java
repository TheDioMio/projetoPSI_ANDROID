package pt.ipleiria.estg.dei.projetoandroid.modelo;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

public class AppDBHelper extends SQLiteOpenHelper {

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



    //TABELA DOS COMENTÁRIOS
    private static final String TABLE_ANIMALS_COMMENTS = "animal_comments";
    public static final String ID_COMMENT = "id_comment";
    //VAI CONTER O ID DO ANIMAL MAS A CONSTANTE JÁ SE ENCONTRA DECLARADA
    //public static final String ID_ANIMAL = "id_animal";
    public static final String COMMENT_TEXT = "comment_text";
    public static final String COMMENT_DATE = "comment_date";
    public static final String NAME_USER = "name_user";
    public static final String AVATAR_USER = "avatar_user";



    public AppDBHelper(Context context) {
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

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ANIMALS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ANIMALS_COMMENTS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ANIMALS_FILES);
        this.onCreate(db);
    }



    //INICIO-----------------------------------------------ANIMAIS-------------------------------------------------------------

    public ArrayList<Animal> getAllAnimalsBD() {
        ArrayList<Animal> animals = new ArrayList<>();

        Cursor cursor = database.query(
                TABLE_ANIMALS,
                new String[]{
                        ID, NAME, DESCRIPTION, CREATED_AT, AGE, SIZE, TYPE,
                        BREED, NEUTERED, VACINATION,
                        OWNER_NAME, OWNER_ADDRESS, OWNER_EMAIL, OWNER_AVATAR,
                        LISTING_DESCRIPTION, LISTING_VIEWS
                },
                null, null, null, null, null
        );

        if (cursor.moveToFirst()) {
            do {
                animals.add(new Animal(
                        cursor.getInt(0),     // ID
                        cursor.getString(1),  // NAME
                        cursor.getString(2),  // DESCRIPTION
                        cursor.getString(3),  // CREATED_AT
                        cursor.getString(4),  // AGE
                        cursor.getString(5),  // SIZE
                        cursor.getString(6),  // TYPE
                        cursor.getString(7),  // BREED
                        cursor.getString(8),  // NEUTERED
                        cursor.getString(9),  // VACINATION
                        cursor.getString(10), // OWNER_NAME
                        cursor.getString(11), // OWNER_ADDRESS
                        cursor.getString(12), // OWNER_EMAIL
                        cursor.getString(13), // OWNER_AVATAR
                        cursor.getString(14), // LISTING_DESCRIPTION
                        cursor.getString(15)  // LISTING_VIEWS
                ));
            } while (cursor.moveToNext());
        }

        cursor.close();
        return animals;
    }

    public Animal adicionarAnimalBD(Animal animal) {

        ContentValues values = new ContentValues();

        //penso que tenho de passar o id que vem do animal
        values.put(NAME, animal.getName());
        values.put(DESCRIPTION, animal.getDescription());
        values.put(CREATED_AT, animal.getCreatedAt());
        values.put(AGE, animal.getAge());
        values.put(SIZE, animal.getSize());
        values.put(TYPE, animal.getType());
        values.put(BREED, animal.getBreed());
        values.put(NEUTERED, animal.getNeutered());
        values.put(VACINATION, animal.getVacination());

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
                    cursor.getString(cursor.getColumnIndexOrThrow(OWNER_NAME)),
                    cursor.getString(cursor.getColumnIndexOrThrow(OWNER_ADDRESS)),
                    cursor.getString(cursor.getColumnIndexOrThrow(OWNER_EMAIL)),
                    cursor.getString(cursor.getColumnIndexOrThrow(OWNER_AVATAR)),
                    cursor.getString(cursor.getColumnIndexOrThrow(LISTING_DESCRIPTION)),
                    cursor.getString(cursor.getColumnIndexOrThrow(LISTING_VIEWS))
            );
        }

        cursor.close();
        return animal;
    }


    //FIM-----------------------------------------------ANIMAIS-----------------------------------------------------


    //INICIO----------------------------------------------FOTOS-----------------------------------------------------

    public ArrayList<AnimalFile> getFotosBD(int idAnimal) {
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


    public AnimalFile adicionarFotoBD(AnimalFile file) {

        ContentValues values = new ContentValues();

        values.put(ID_ANIMAL, file.getIdAnimal());
        values.put(FILE_ADDRESS, file.getFileAddress());

        long id = database.insert(TABLE_ANIMALS_FILES, null, values);

        if (id > -1) {
            return file;
        }

        return null;
    }


    public ArrayList<AnimalFile> getFotosByAnimalId(int idAnimal) {

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

}
