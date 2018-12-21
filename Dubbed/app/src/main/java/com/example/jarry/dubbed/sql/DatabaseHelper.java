package com.example.jarry.dubbed.sql;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


import com.example.jarry.dubbed.model.PetVisits;
import com.example.jarry.dubbed.model.Pets;
import com.example.jarry.dubbed.model.User;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {

    // Database Version
    private static final int DATABASE_VERSION = 3;

    // Database Name
    private static final String DATABASE_NAME = "VetManager.db";

    // User table name
    private static final String TABLE_USER = "vet";
    private static final String TABLE_PET = "pet";
    private static final String TABLE_VISITS = "visits";

    // User Table Columns names
    private static final String COLUMN_VET_ID = "vet_id";
    private static final String COLUMN_VET_NAME = "vet_name";
    private static final String COLUMN_VET_EMAIL = "vet_email";
    private static final String COLUMN_VET_PASSWORD = "vet_password";
    private static final String COLUMN_VET_CONTACT = "vet_phone_number";

    // Pet Table Columns names
    private static final String COLUMN_PET_ID = "pet_id";
    private static final String COLUMN_PET_NAME = "pet_name";
    private static final String COLUMN_PET_BREED = "pet_breed";
    private static final String COLUMN_PET_ANIMAL = "pet_animal";
    private static final String COLUMN_PET_OWNER = "pet_owner";
    private static final String COLUMN_PET_VET_ID = "vet_pet_id";
    private static final String COLUMN_PET_IMAGE = "pet_image";
    private static final String COLUMN_PET_AGE = "pet_age";

    //Pet Visits Column names
    private static final String COLUMN_PET_ID_VISITS = "visits_pet_id";
    private static final String COLUMN_VISIT_DATE = "date_visited";
    private static final String COLUMN_VISIT_CAUSE= "cause_of_visit";




    // create table sql query
    private String CREATE_USER_TABLE = "CREATE TABLE " + TABLE_USER + "("
            + COLUMN_VET_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + COLUMN_VET_NAME + " TEXT,"
            + COLUMN_VET_EMAIL + " TEXT," + COLUMN_VET_PASSWORD + " TEXT," + COLUMN_VET_CONTACT + " TEXT " + ")";

    private String CREATE_PET_TABLE = "CREATE TABLE " + TABLE_PET + "("
            + COLUMN_PET_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + COLUMN_PET_NAME + " TEXT,"
            + COLUMN_PET_BREED + " TEXT," + COLUMN_PET_VET_ID + " INTEGER," + COLUMN_PET_ANIMAL + " TEXT," + COLUMN_PET_AGE + " INTEGER ," + COLUMN_PET_OWNER + " TEXT ," + COLUMN_PET_IMAGE + " TEXT" + ")";

    private String CREATE_VISITS_TABLE = "CREATE TABLE " + TABLE_VISITS + "(" + COLUMN_PET_ID_VISITS + " TEXT," +
            COLUMN_VISIT_DATE + " TEXT," + COLUMN_VISIT_CAUSE + " TEXT" + ")";

    // drop table sql query
    private String DROP_USER_TABLE = "DROP TABLE IF EXISTS " + TABLE_USER;
    private String DROP_PET_TABLE = "DROP TABLE IF EXISTS " + TABLE_PET;
    private String DROP_VISITS_TABLE = "DROP TABLE IF EXISTS " + TABLE_VISITS;


    /**
     * Constructor
     *
     * @param context
     */
    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL(CREATE_USER_TABLE);
        db.execSQL(CREATE_PET_TABLE);
        db.execSQL(CREATE_VISITS_TABLE);
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        //Drop User Table if exist
        db.execSQL(DROP_USER_TABLE);
        db.execSQL(DROP_PET_TABLE);
        db.execSQL(DROP_VISITS_TABLE);

        // Create tables again
        onCreate(db);

    }

    /**
     * This method is to create user record
     *
     * @param user
     */
    public void addUser(User user) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_VET_NAME, user.getName());
        values.put(COLUMN_VET_EMAIL, user.getEmail());
        values.put(COLUMN_VET_PASSWORD, user.getPassword());
        values.put(COLUMN_VET_CONTACT, user.getContact());

        // Inserting Row
        db.insert(TABLE_USER, null, values);
        db.close();
    }

    /**
     * This method is to create user record
     *
     * @param pets
     */
    public void addPet(Pets pets) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_PET_NAME, pets.getName());
        values.put(COLUMN_PET_VET_ID, pets.getPet_vet_id());
        values.put(COLUMN_PET_ANIMAL, pets.getAnimal());
        values.put(COLUMN_PET_IMAGE, pets.getImagePath());
        values.put(COLUMN_PET_BREED, pets.getBreed());
        values.put(COLUMN_PET_OWNER, pets.getOwner_name());
        values.put(COLUMN_PET_AGE, pets.getPetAge());

        // Inserting Row
        db.insert(TABLE_PET, null, values);
        db.close();
    }

    /**
     * This method is to create user record
     *
     * @param visits
     */
    public void addVisits(PetVisits visits) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_PET_ID_VISITS, visits.getPet_id());
        values.put(COLUMN_VISIT_DATE, visits.getDate_of_visit());
        values.put(COLUMN_VISIT_CAUSE, visits.getCause_of_visit());

        // Inserting Row
        db.insert(TABLE_VISITS, null, values);
        db.close();
    }


    /**
     * This method is to fetch all user and return the list of user records
     *
     * @return list
     */
    public List<Pets> getAllPet(String pet_vet_id) {
        // array of columns to fetch
        String[] columns = {
                COLUMN_PET_ID,
                COLUMN_PET_NAME,
                COLUMN_PET_BREED,
                COLUMN_PET_ANIMAL,
                COLUMN_PET_IMAGE,
                COLUMN_PET_AGE,
                COLUMN_PET_VET_ID,
                COLUMN_PET_OWNER
        };
        // sorting orders
        String sortOrder =
                COLUMN_PET_NAME + " ASC";
        String whereClause = COLUMN_PET_VET_ID + " = ?";
        String[] whereArgs = new String[] {
                pet_vet_id
        };
        List<Pets> petList = new ArrayList<Pets>();

        SQLiteDatabase db = this.getReadableDatabase();

        // query the user table
        /**
         * Here query function is used to fetch records from user table this function works like we use sql query.
         * SQL query equivalent to this query function is
         * SELECT user_id,user_name,user_email,user_password FROM user ORDER BY user_name;
         */
        Cursor cursor = db.query(TABLE_PET, //Table to query
                columns,    //columns to return
                whereClause,        //columns for the WHERE clause
                whereArgs,        //The values for the WHERE clause
                null,       //group the rows
                null,       //filter by row groups
                sortOrder); //The sort order


        // Traversing through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Pets pets = new Pets();
                pets.setPet_id(Integer.parseInt(cursor.getString(cursor.getColumnIndex(COLUMN_PET_ID))));
                pets.setPet_vet_id(Integer.parseInt(cursor.getString(cursor.getColumnIndex(COLUMN_PET_VET_ID))));
                pets.setName(cursor.getString(cursor.getColumnIndex(COLUMN_PET_NAME)));
                pets.setImagePath(cursor.getString(cursor.getColumnIndex(COLUMN_PET_IMAGE)));
                pets.setAnimal(cursor.getString(cursor.getColumnIndex(COLUMN_PET_ANIMAL)));
                pets.setBreed(cursor.getString(cursor.getColumnIndex(COLUMN_PET_BREED)));
                pets.setOwner_name(cursor.getString(cursor.getColumnIndex(COLUMN_PET_OWNER)));
                pets.setPetAge(Integer.parseInt(cursor.getString(cursor.getColumnIndex(COLUMN_PET_AGE))));
                // Adding user record to list
                petList.add(pets);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();

        // return user list
        return petList;
    }

    /**
     * This method is to fetch all user and return the list of user records
     *
     * @return list
     */
    public List<PetVisits> getPetVisits(String pet_id) {
        // array of columns to fetch
        String[] columns = {

                COLUMN_PET_ID_VISITS,
                COLUMN_VISIT_DATE,
                COLUMN_VISIT_CAUSE
        };
        // sorting orders
        String sortOrder =
                COLUMN_VISIT_DATE + " DESC";
        String whereClause = COLUMN_PET_ID_VISITS + " = ?";
        String[] whereArgs = new String[] {
                pet_id
        };
        List<PetVisits> petVisits = new ArrayList<PetVisits>();

        SQLiteDatabase db = this.getReadableDatabase();

        // query the user table
        /**
         * Here query function is used to fetch records from user table this function works like we use sql query.
         * SQL query equivalent to this query function is
         * SELECT user_id,user_name,user_email,user_password FROM user ORDER BY user_name;
         */
        Cursor cursor = db.query(TABLE_VISITS, //Table to query
                columns,    //columns to return
                whereClause,        //columns for the WHERE clause
                whereArgs,        //The values for the WHERE clause
                null,       //group the rows
                null,       //filter by row groups
                sortOrder); //The sort order


        // Traversing through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                PetVisits visits = new PetVisits();
                visits.setPet_id(Integer.parseInt(cursor.getString(cursor.getColumnIndex(COLUMN_PET_ID_VISITS))));
                visits.setDate_of_visit(cursor.getString(cursor.getColumnIndex(COLUMN_VISIT_DATE)));
                visits.setCause_of_visit(cursor.getString(cursor.getColumnIndex(COLUMN_VISIT_CAUSE)));

                // Adding user record to list
                petVisits.add(visits);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();

        // return user list
        return petVisits;
    }

    /**
     * This method to update user record
     *
     * @param user
     */
    public void updateUser(User user) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_VET_NAME, user.getName());
        values.put(COLUMN_VET_EMAIL, user.getEmail());
        values.put(COLUMN_VET_PASSWORD, user.getPassword());
        values.put(COLUMN_VET_CONTACT, user.getContact());

        // updating row
        db.update(TABLE_USER, values, COLUMN_VET_ID + " = ?",
                new String[]{String.valueOf(user.getId())});
        db.close();
    }

    public User getUser(String email) {
        // array of columns to fetch
        String[] columns = {
                COLUMN_VET_ID,
                COLUMN_VET_NAME,
                COLUMN_VET_EMAIL,
                COLUMN_VET_PASSWORD,
                COLUMN_VET_CONTACT
        };
        SQLiteDatabase db = this.getReadableDatabase();

        List<User> userList = new ArrayList<User>();
        User user = new User();

        // selection criteria
        String selection = COLUMN_VET_EMAIL + " = ?";

        // selection argument
        String[] selectionArgs = {email};

        // query user table with condition
        /**
         * Here query function is used to fetch records from user table this function works like we use sql query.
         * SQL query equivalent to this query function is
         * SELECT user_id FROM user WHERE user_email = 'jack@androidtutorialshub.com';
         */
        Cursor cursor = db.query(TABLE_USER, //Table to query
                columns,                    //columns to return
                selection,                  //columns for the WHERE clause
                selectionArgs,              //The values for the WHERE clause
                null,                       //group the rows
                null,                      //filter by row groups
                null);                      //The sort order
        int id = 0;

        if (cursor.moveToFirst()) {
            do {
                user.setId(Integer.parseInt(cursor.getString(cursor.getColumnIndex(COLUMN_VET_ID))));
                user.setName(cursor.getString(cursor.getColumnIndex(COLUMN_VET_NAME)));
                user.setEmail(cursor.getString(cursor.getColumnIndex(COLUMN_VET_EMAIL)));
                user.setPassword(cursor.getString(cursor.getColumnIndex(COLUMN_VET_PASSWORD)));
                user.setContact(cursor.getString(cursor.getColumnIndex(COLUMN_VET_CONTACT)));
                // Adding user record to list
                userList.add(user);
                id = user.getId();
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();

        return user;
    }

    public Pets getPet(String pet_id) {
        // array of columns to fetch
        String[] columns = {
                COLUMN_PET_ID,
                COLUMN_PET_NAME,
                COLUMN_PET_BREED,
                COLUMN_PET_ANIMAL,
                COLUMN_PET_IMAGE,
                COLUMN_PET_AGE,
                COLUMN_PET_VET_ID,
                COLUMN_PET_OWNER
        };
        SQLiteDatabase db = this.getReadableDatabase();

        List<Pets> petList = new ArrayList<Pets>();
        Pets pet = new Pets();

        // selection criteria
        String selection = COLUMN_PET_ID + " = ?";

        // selection argument
        String[] selectionArgs = {pet_id};

        // query user table with condition
        /**
         * Here query function is used to fetch records from user table this function works like we use sql query.
         * SQL query equivalent to this query function is
         * SELECT user_id FROM user WHERE user_email = 'jack@androidtutorialshub.com';
         */
        Cursor cursor = db.query(TABLE_PET, //Table to query
                columns,                    //columns to return
                selection,                  //columns for the WHERE clause
                selectionArgs,              //The values for the WHERE clause
                null,                       //group the rows
                null,                      //filter by row groups
                null);                      //The sort order
        int id = 0;

        if (cursor.moveToFirst()) {
            do {
                pet.setPet_id(Integer.parseInt(cursor.getString(cursor.getColumnIndex(COLUMN_PET_ID))));
                pet.setPet_vet_id(Integer.parseInt(cursor.getString(cursor.getColumnIndex(COLUMN_PET_VET_ID))));
                pet.setName(cursor.getString(cursor.getColumnIndex(COLUMN_PET_NAME)));
                pet.setBreed(cursor.getString(cursor.getColumnIndex(COLUMN_PET_BREED)));
                pet.setAnimal(cursor.getString(cursor.getColumnIndex(COLUMN_PET_ANIMAL)));
                pet.setPetAge(Integer.parseInt(cursor.getString(cursor.getColumnIndex(COLUMN_PET_AGE))));
                pet.setOwner_name(cursor.getString(cursor.getColumnIndex(COLUMN_PET_OWNER)));
                pet.setImagePath(cursor.getString(cursor.getColumnIndex(COLUMN_PET_IMAGE)));
                // Adding user record to list
                petList.add(pet);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();

        return pet;
    }

    /**
     * This method is to delete user record
     *
     * @param user
     */
    public void deleteUser(User user) {
        SQLiteDatabase db = this.getWritableDatabase();
        // delete user record by id
        db.delete(TABLE_USER, COLUMN_VET_ID + " = ?",
                new String[]{String.valueOf(user.getId())});
        db.close();
    }

    /**
     * This method to check user exist or not
     *
     * @param email
     * @return true/false
     */
    public boolean checkUser(String email) {

        // array of columns to fetch
        String[] columns = {
                COLUMN_VET_ID
        };
        SQLiteDatabase db = this.getReadableDatabase();

        // selection criteria
        String selection = COLUMN_VET_EMAIL + " = ?";

        // selection argument
        String[] selectionArgs = {email};

        // query user table with condition
        /**
         * Here query function is used to fetch records from user table this function works like we use sql query.
         * SQL query equivalent to this query function is
         * SELECT user_id FROM user WHERE user_email = 'jack@androidtutorialshub.com';
         */
        Cursor cursor = db.query(TABLE_USER, //Table to query
                columns,                    //columns to return
                selection,                  //columns for the WHERE clause
                selectionArgs,              //The values for the WHERE clause
                null,                       //group the rows
                null,                      //filter by row groups
                null);                      //The sort order
        int cursorCount = cursor.getCount();
        cursor.close();
        db.close();

        if (cursorCount > 0) {
            return true;
        }

        return false;
    }

    /**
     * This method to check user exist or not
     *
     * @param email
     * @param password
     * @return true/false
     */
    public boolean checkUser(String email, String password) {

        // array of columns to fetch
        String[] columns = {
                COLUMN_VET_ID
        };
        SQLiteDatabase db = this.getReadableDatabase();
        // selection criteria
        String selection = COLUMN_VET_EMAIL + " = ?" + " AND " + COLUMN_VET_PASSWORD + " = ?";

        // selection arguments
        String[] selectionArgs = {email, password};

        // query user table with conditions
        /**
         * Here query function is used to fetch records from user table this function works like we use sql query.
         * SQL query equivalent to this query function is
         * SELECT user_id FROM user WHERE user_email = 'jack@androidtutorialshub.com' AND user_password = 'qwerty';
         */
        Cursor cursor = db.query(TABLE_USER, //Table to query
                columns,                    //columns to return
                selection,                  //columns for the WHERE clause
                selectionArgs,              //The values for the WHERE clause
                null,                       //group the rows
                null,                       //filter by row groups
                null);                      //The sort order

        int cursorCount = cursor.getCount();

        cursor.close();
        db.close();
        if (cursorCount > 0) {
            return true;
        }

        return false;
    }

}
