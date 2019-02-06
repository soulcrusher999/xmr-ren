package com.iit.rentals.utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.iit.rentals.models.Category;
import com.iit.rentals.models.User;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {

    //database singleton instance
    private static DatabaseHelper instance = null;
    // Database Version
    private static final int DATABASE_VERSION = 8;

    // Database Name
    private static final String DATABASE_NAME = "UserManager.db";

    // User table name
    private static final String TABLE_USER = "user";
    // User Table Columns names
    private static final String COLUMN_USER_ID = "user_id";
    private static final String COLUMN_USER_NAME = "user_name";
    private static final String COLUMN_USER_EMAIL = "user_email";
    private static final String COL_USER_IMAGE = "user_password";
    private static final String COL_USER_TYPE = "type";
    private static final String COL_USER_ID = "user_id";

    // create table user sql query

    private String DROP_USER_TABLE = String.format("DROP TABLE IF EXISTS %s", TABLE_USER);
    private String CREATE_USER_TABLE = String.format("CREATE TABLE %s(" +
                    "%s INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "%s TEXT," +
                    "%s TEXT," +
                    "%s TEXT)",
            TABLE_USER,
            COLUMN_USER_ID,
            COLUMN_USER_NAME,
            COLUMN_USER_EMAIL,
            COL_USER_IMAGE
    );


    // CATEGORY table name
    private static final String TABLE_CATEGORY = "CATEGORY";
    // CATEGORY Table Columns names
    private static final String COLUMN_CATEGORY_ID = "category_id";
    private static final String COLUMN_CATEGORY_NAME = "name";
    private static final String COLUMN_CATEGORY_DESC = "description";
    private static final String COL_CATEGORY_IMAGE = "picture";
    private static final String COL_CATEGORY_TYPE = "isCategoryPrimary";


    // create table CATEGORY sql query

    private String DROP_CATEGORY_TABLE = String.format("DROP TABLE IF EXISTS %s", TABLE_CATEGORY);
    private String CREATE_CATEGORY_TABLE = String.format("CREATE TABLE %s(" +
                    "%s VARCHAR(255) PRIMARY KEY," +
                    "%s VARCHAR(100)," +
                    "%s TEXT," +
                    "%s TEXT," +
                    "%s INTEGER DEFAULT 0" +
                    ")",
            TABLE_CATEGORY,
            COLUMN_CATEGORY_ID,
            COLUMN_CATEGORY_NAME,
            COLUMN_CATEGORY_DESC,
            COL_CATEGORY_IMAGE,
            COL_CATEGORY_TYPE
    );

//    // HOTEL table name
//    private static final String TABLE_HOTEL = "hotels";
//    // HOTEL Table Columns names
//    private static final String COLUMN_HOTEL_ID = "id";
//    private static final String COLUMN_HOTEL_NAME = "name";
//    private static final String COLUMN_HOTEL_DESC = "description";
//    private static final String COL_HOTEL_IMAGE = "location";
//    private static final String COL_HOTEL_IMAGE = "picture";
//    private static final String COL_HOTEL_TYPE = "isHOTELPrimary";
//
//
//    // create table HOTEL sql query
//
//    private String DROP_HOTEL_TABLE = String.format("DROP TABLE IF EXISTS %s", TABLE_HOTEL);
//    private String CREATE_HOTEL_TABLE = String.format("CREATE TABLE %s(" +
//                    "%s VARCHAR(255) PRIMARY KEY," +
//                    "%s VARCHAR(100)," +
//                    "%s TEXT," +
//                    "%s TEXT," +
//                    "%s INTEGER DEFAULT 0" +
//                    ")",
//            TABLE_HOTEL,
//            COLUMN_HOTEL_ID,
//            COLUMN_HOTEL_NAME,
//            COLUMN_HOTEL_DESC,
//            COL_HOTEL_IMAGE,
//            COL_HOTEL_TYPE
//    );
//


    /**
     * singleton instance of database
     */
    public static DatabaseHelper getInstance(Context context) {
        if (instance == null)
            instance = new DatabaseHelper(context);
        return instance;
    }

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
        db.execSQL(CREATE_CATEGORY_TABLE);
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        //Drop User Table if exist
        db.execSQL(DROP_USER_TABLE);
        db.execSQL(DROP_CATEGORY_TABLE);

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
        values.put(COL_USER_ID, user.getUser_id());
        values.put(COLUMN_USER_NAME, user.getUsername());
        values.put(COLUMN_USER_EMAIL, user.getEmail());
        values.put(COL_USER_IMAGE, user.getAvatar_img_link());
        values.put(COL_USER_TYPE, user.getUser_type());

        // Inserting Row
        db.insert(TABLE_USER, null, values);
        db.close();
    }

    /**
     * This method is to fetch all user and return the list of user records
     *
     * @return list
     */
    public List<User> getAllUser() {
        // array of columns to fetch
        String[] columns = {
                COL_USER_ID,
                COLUMN_USER_EMAIL,
                COLUMN_USER_NAME,
                COL_USER_IMAGE,
                COL_USER_TYPE
        };
        // sorting orders
        String sortOrder =
                COLUMN_USER_NAME + " ASC";
        List<User> userList = new ArrayList<User>();

        SQLiteDatabase db = this.getReadableDatabase();

        // query the user table
        /**
         * Here query function is used to fetch records from user table this function works like we use sql query.
         * SQL query equivalent to this query function is
         * SELECT user_id,user_name,user_email,user_password FROM user ORDER BY user_name;
         */
        Cursor cursor = db.query(TABLE_USER, //Table to query
                columns,    //columns to return
                null,        //columns for the WHERE clause
                null,        //The values for the WHERE clause
                null,       //group the rows
                null,       //filter by row groups
                sortOrder); //The sort order


        // Traversing through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                User user = new User();
                user.setUser_id(cursor.getString(cursor.getColumnIndex(COLUMN_USER_ID)));
                user.setUsername(cursor.getString(cursor.getColumnIndex(COLUMN_USER_NAME)));
                user.setEmail(cursor.getString(cursor.getColumnIndex(COLUMN_USER_EMAIL)));
                user.setUser_type(Integer.parseInt(cursor.getString(cursor.getColumnIndex(COL_USER_TYPE))));
                user.setAvatar_img_link(cursor.getString(cursor.getColumnIndex(COL_USER_IMAGE)));
                // Adding user record to list
                Log.v("USER", userList.toString());
                userList.add(user);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();

        // return user list
        return userList;
    }


    /**
     * This method is to fetch all user and return the list of user records
     *
     * @return list
     */


    /**
     * This method to update user record
     *
     * @param user
     */
    public void updateUser(User user, int id) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_USER_NAME, user.getUsername());
        values.put(COLUMN_USER_EMAIL, user.getEmail());


        // updating row
        db.update(TABLE_USER, values, COLUMN_USER_ID + " = ?",
                new String[]{String.valueOf(id)});
        db.close();
    }

    /**
     * This method is to delete user record
     *
     * @param user
     */
    public void deleteUser(User user) {
        SQLiteDatabase db = this.getWritableDatabase();
        // delete user record by id
        db.delete(TABLE_USER, COLUMN_USER_ID + " = ?",
                new String[]{String.valueOf(user.getUser_id())});
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
                COLUMN_USER_ID
        };
        SQLiteDatabase db = this.getReadableDatabase();

        // selection criteria
        String selection = COLUMN_USER_EMAIL + " = ?";

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
    public boolean checkUser(String email, String password, Context context) {

        // array of columns to fetch
        String[] columns = {
                COLUMN_USER_ID,
                COLUMN_USER_NAME,
                COLUMN_USER_EMAIL
        };
        SQLiteDatabase db = this.getReadableDatabase();
        // selection criteria
        String selection = COLUMN_USER_EMAIL + " = ?" + " AND " + COL_USER_IMAGE + " = ?";

        // selection arguments
        String[] selectionArgs = {email, password};

        // query user table with conditions
        /**
         * Here query function is used to fetch records from user table this function works like we use sql query.
         * SQL query equivalent to this query function is
         * SELECT user_id, name, email FROM user WHERE user_email = 'manish@manish.com' AND user_password = 'qwerty';
         */
        Cursor cursor = db.query(TABLE_USER, //Table to query
                columns,                    //columns to return
                selection,                  //columns for the WHERE clause
                selectionArgs,              //The values for the WHERE clause
                null,                       //group the rows
                null,                       //filter by row groups
                null);                      //The sort order

        int cursorCount = cursor.getCount();


        if (cursorCount > 0) {

            cursor.moveToLast();
            SharedPreferenceHelper manager = SharedPreferenceHelper.getInstance(context);
            User user = new User();
            user.setUser_id(cursor.getString(cursor.getColumnIndex(COLUMN_USER_ID)));
            user.setUsername(cursor.getString(cursor.getColumnIndex(COLUMN_USER_NAME)));
            user.setEmail(cursor.getString(cursor.getColumnIndex(COLUMN_USER_EMAIL)));
            user.setUser_type(Integer.parseInt(cursor.getString(cursor.getColumnIndex(COL_USER_IMAGE))));
            user.setAvatar_img_link(cursor.getString(cursor.getColumnIndex(COL_USER_IMAGE)));
            manager.saveUserInfo(user);
            //                    manager.setName(email);
            //                    manager.setEmail(email);


            cursor.close();
            db.close();
            return true;
        }
        db.close();
        return false;
    }

    /**
     * This method is to create category record
     * TABLE_CATEGORY,
     * COLUMN_CATEGORY_ID,
     * COLUMN_CATEGORY_NAME,
     * COLUMN_CATEGORY_DESC,
     * COL_CATEGORY_IMAGE,
     * COL_CATEGORY_TYPE
     *
     * @param post
     */
    public void addCategory(Category post) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_CATEGORY_ID, post.getId());
        values.put(COLUMN_CATEGORY_NAME, post.getName());
        values.put(COLUMN_CATEGORY_DESC, post.getDescription());
        values.put(COL_CATEGORY_IMAGE, post.getPicture());
        values.put(COL_CATEGORY_TYPE, post.isCategoryPrimary());

        // Inserting Row
        db.insert(TABLE_CATEGORY, null, values);
        db.close();
    }

    /**
     * This method is to fetch all user and return the list of user records
     * TABLE_CATEGORY,
     * COLUMN_CATEGORY_ID,
     * COLUMN_CATEGORY_NAME,
     * COLUMN_CATEGORY_DESC,
     * COL_CATEGORY_IMAGE,
     * COL_CATEGORY_TYPE
     *
     * @return list
     */
    public List<Category> getAllCategories() {
        // array of columns to fetch
        String[] columns = {
                COL_USER_ID,
                COLUMN_CATEGORY_ID,
                COLUMN_CATEGORY_NAME,
                COLUMN_CATEGORY_DESC,
                COL_CATEGORY_IMAGE,
                COL_CATEGORY_TYPE
        };
        // sorting orders
        String sortOrder =
                COLUMN_CATEGORY_NAME + " ASC";
        List<Category> mList = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();

        // query the user table
        /**
         * Here query function is used to fetch records from user table this function works like we use sql query.
         * SQL query equivalent to this query function is
         * SELECT user_id,user_name,user_email,user_password FROM user ORDER BY user_name;
         */
        Cursor cursor = db.query(TABLE_CATEGORY, //Table to query
                columns,    //columns to return
                null,        //columns for the WHERE clause
                null,        //The values for the WHERE clause
                null,       //group the rows
                null,       //filter by row groups
                sortOrder); //The sort order


        // Traversing through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Category post = new Category();
                post.setId(cursor.getString(cursor.getColumnIndex(COLUMN_CATEGORY_ID)));
                post.setName(cursor.getString(cursor.getColumnIndex(COLUMN_CATEGORY_NAME)));
                post.setDescription(cursor.getString(cursor.getColumnIndex(COLUMN_CATEGORY_DESC)));
                post.setPicture(cursor.getString(cursor.getColumnIndex(COL_CATEGORY_IMAGE)));
                post.setCategoryPrimary(cursor.getInt(cursor.getColumnIndex(COL_CATEGORY_TYPE)) == 1);
                // Adding post record to list
                Log.v("post", mList.toString());
                mList.add(post);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();

        // return user list
        return mList;
    }


    /**
     * This method is to delete user record
     *
     * @param post
     */
    public void deleteCategory(Category post) {
        SQLiteDatabase db = this.getWritableDatabase();
        // delete user record by id
        db.delete(TABLE_CATEGORY, COLUMN_CATEGORY_ID + " = ?",
                new String[]{String.valueOf(post.getId())});
        db.close();
    }

    /**
     * This method to update user record
     * TABLE_CATEGORY,
     * COLUMN_CATEGORY_ID,
     * COLUMN_CATEGORY_NAME,
     * COLUMN_CATEGORY_DESC,
     * COL_CATEGORY_IMAGE,
     * COL_CATEGORY_TYPE
     *
     * @param post
     */
    public void updateCategory(Category post, int id) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_CATEGORY_ID, post.getId());
        values.put(COLUMN_CATEGORY_NAME, post.getName());
        values.put(COLUMN_CATEGORY_DESC, post.getDescription());
        values.put(COL_CATEGORY_IMAGE, post.getPicture());
        values.put(COL_CATEGORY_TYPE, post.isCategoryPrimary());


        // updating row
        db.update(TABLE_CATEGORY, values, COLUMN_CATEGORY_ID + " = ?",
                new String[]{String.valueOf(id)});
        db.close();
    }

//
//    /**
//     * This method is to create category record
//     * TABLE_CATEGORY,
//     * COLUMN_CATEGORY_ID,
//     * COLUMN_CATEGORY_NAME,
//     * COLUMN_CATEGORY_DESC,
//     * COL_CATEGORY_IMAGE,
//     * COL_CATEGORY_TYPE
//     *
//     * @param post
//     */
//    public void addCategory(Category post) {
//        SQLiteDatabase db = this.getWritableDatabase();
//
//        ContentValues values = new ContentValues();
//        values.put(COLUMN_CATEGORY_ID, post.getId());
//        values.put(COLUMN_CATEGORY_NAME, post.getName());
//        values.put(COLUMN_CATEGORY_DESC, post.getDescription());
//        values.put(COL_CATEGORY_IMAGE, post.getPicture());
//        values.put(COL_CATEGORY_TYPE, post.isCategoryPrimary());
//
//        // Inserting Row
//        db.insert(TABLE_CATEGORY, null, values);
//        db.close();
//    }
//
//    /**
//     * This method is to fetch all user and return the list of user records
//     * TABLE_CATEGORY,
//     * COLUMN_CATEGORY_ID,
//     * COLUMN_CATEGORY_NAME,
//     * COLUMN_CATEGORY_DESC,
//     * COL_CATEGORY_IMAGE,
//     * COL_CATEGORY_TYPE
//     *
//     * @return list
//     */
//    public List<Category> getAllCategories() {
//        // array of columns to fetch
//        String[] columns = {
//                COL_USER_ID,
//                COLUMN_CATEGORY_ID,
//                COLUMN_CATEGORY_NAME,
//                COLUMN_CATEGORY_DESC,
//                COL_CATEGORY_IMAGE,
//                COL_CATEGORY_TYPE
//        };
//        // sorting orders
//        String sortOrder =
//                COLUMN_CATEGORY_NAME + " ASC";
//        List<Category> mList = new ArrayList<>();
//
//        SQLiteDatabase db = this.getReadableDatabase();
//
//        // query the user table
//        /**
//         * Here query function is used to fetch records from user table this function works like we use sql query.
//         * SQL query equivalent to this query function is
//         * SELECT user_id,user_name,user_email,user_password FROM user ORDER BY user_name;
//         */
//        Cursor cursor = db.query(TABLE_CATEGORY, //Table to query
//                columns,    //columns to return
//                null,        //columns for the WHERE clause
//                null,        //The values for the WHERE clause
//                null,       //group the rows
//                null,       //filter by row groups
//                sortOrder); //The sort order
//
//
//        // Traversing through all rows and adding to list
//        if (cursor.moveToFirst()) {
//            do {
//                Category post = new Category();
//                post.setId(cursor.getString(cursor.getColumnIndex(COLUMN_CATEGORY_ID)));
//                post.setName(cursor.getString(cursor.getColumnIndex(COLUMN_CATEGORY_NAME)));
//                post.setDescription(cursor.getString(cursor.getColumnIndex(COLUMN_CATEGORY_DESC)));
//                post.setPicture(cursor.getString(cursor.getColumnIndex(COL_CATEGORY_IMAGE)));
//                post.setCategoryPrimary(cursor.getInt(cursor.getColumnIndex(COL_CATEGORY_TYPE)) == 1);
//                // Adding post record to list
//                Log.v("post", mList.toString());
//                mList.add(post);
//            } while (cursor.moveToNext());
//        }
//        cursor.close();
//        db.close();
//
//        // return user list
//        return mList;
//    }
//
//
//    /**
//     * This method is to delete user record
//     *
//     * @param post
//     */
//    public void deleteCategory(Category post) {
//        SQLiteDatabase db = this.getWritableDatabase();
//        // delete user record by id
//        db.delete(TABLE_CATEGORY, COLUMN_CATEGORY_ID + " = ?",
//                new String[]{String.valueOf(post.getId())});
//        db.close();
//    }

}