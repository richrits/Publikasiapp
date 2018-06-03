package com.skripsi.android.publikasiapp.helper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.skripsi.android.publikasiapp.model.Publikasi;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Harits on 2/26/2018.
 */


public class SQLiteHandler extends SQLiteOpenHelper {

    private static final String TAG = SQLiteHandler.class.getSimpleName();

    // Semua Static variables
    // Database Version
    private static final int DATABASE_VERSION = 2;

    // Nama database
    private static final String DATABASE_NAME = "db_publikasi";

    // Nama tabel-tabel
    private static final String TABLE_USER = "tb_user";
    private static final String TABLE_PUBLIKASI = "tb_publikasi";
    private static final String TABLE_USER_ACTIVITY = "tb_user_activity";

    // Nama Kolom Tabel Login
    private static final String KEY_USER_ID = "id";
    private static final String KEY_NAME = "name";
    private static final String KEY_EMAIL = "email";
    private static final String KEY_UID = "uid";
    private static final String KEY_CREATED_AT = "created_at";
    private static final String KEY_PEKERJAAN = "pekerjaan";
    private static final String KEY_PENDIDIKAN_TERAKHIR = "pendidikan_terakhir";

    // Nama Kolom Tabel User Activity
    private static final String KEY_EVENT = "event";
    private static final String KEY_HALAMAN_PUBLIKASI = "halaman_publikasi";
    private static final String KEY_TIME_STAMP = "time_stamp";

    // Nama Kolom Tabel Publikasi
    private static final String KEY_PUBLIKASI_ID = "id";
    private static final String KEY_PUBLIKASI_TITLE = "title";
    private static final String KEY_PUBLIKASI_KAT_NO = "kat_no";
    private static final String KEY_PUBLIKASI_PUB_NO = "pub_no";
    private static final String KEY_PUBLIKASI_ISSN = "issn";
    private static final String KEY_PUBLIKASI_ABSTRACT = "abstract";
    private static final String KEY_PUBLIKASI_SCH_DATE = "sch_date";
    private static final String KEY_PUBLIKASI_RL_DATE = "rl_date";
    private static final String KEY_PUBLIKASI_COVER = "cover";
    private static final String KEY_PUBLIKASI_PDF = "pdf";
    private static final String KEY_PUBLIKASI_SIZE = "size";



    public SQLiteHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Membuat Tabel Tabel
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_LOGIN_TABLE = "CREATE TABLE " + TABLE_USER
                + "("
                + KEY_USER_ID + " INTEGER PRIMARY KEY,"
                + KEY_NAME + " TEXT,"
                + KEY_EMAIL + " TEXT UNIQUE,"
                + KEY_UID + " TEXT,"
                + KEY_CREATED_AT + " TEXT,"
                + KEY_PEKERJAAN + " TEXT,"
                + KEY_PENDIDIKAN_TERAKHIR + " TEXT"
                + ")";
        db.execSQL(CREATE_LOGIN_TABLE);


        String CREATE_PUBLIKASI_TABLE = "CREATE TABLE " + TABLE_PUBLIKASI
                + "("
                + KEY_PUBLIKASI_ID + " INTEGER PRIMARY KEY,"
                + KEY_PUBLIKASI_TITLE + " TEXT,"
                + KEY_PUBLIKASI_KAT_NO + " TEXT,"
                + KEY_PUBLIKASI_PUB_NO + " TEXT,"
                + KEY_PUBLIKASI_ISSN + " TEXT,"
                + KEY_PUBLIKASI_ABSTRACT + " TEXT,"
                + KEY_PUBLIKASI_SCH_DATE + " TEXT,"
                + KEY_PUBLIKASI_RL_DATE + " TEXT,"
                + KEY_PUBLIKASI_COVER + " TEXT,"
                + KEY_PUBLIKASI_PDF + " TEXT,"
                + KEY_PUBLIKASI_SIZE + " TEXT"
                + ")";
        db.execSQL(CREATE_PUBLIKASI_TABLE);


        String CREATE_ACTIVITY_TABLE = "CREATE TABLE " + TABLE_USER_ACTIVITY
                + "("
                + KEY_EMAIL + " TEXT,"
                + KEY_PUBLIKASI_TITLE + " TEXT,"
                + KEY_EVENT + " TEXT,"
                + KEY_HALAMAN_PUBLIKASI + " INTEGER,"
                + KEY_TIME_STAMP + " TEXT"
                + ")";
        db.execSQL(CREATE_ACTIVITY_TABLE);

        Log.d(TAG, "Database tables created");
    }



    // Upgrade database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USER);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PUBLIKASI);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USER_ACTIVITY);

        // Create tables again
        onCreate(db);
    }

    /**
     * Menyimpan detail user ke database
     * */
    public void addUser(String name, String email, String uid, String created_at, String pekerjaan, String pendidikan_terakhir) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_NAME, name); // Name
        values.put(KEY_EMAIL, email); // Email
        values.put(KEY_UID, uid); // Email
        values.put(KEY_CREATED_AT, created_at); // Created At
        values.put(KEY_PEKERJAAN, pekerjaan); // pekerjaan
        values.put(KEY_PENDIDIKAN_TERAKHIR, pendidikan_terakhir); // pendidikan terakhir

        // Inserting Row
        long id = db.insert(TABLE_USER, null, values);
        db.close(); // Closing database connection

        Log.d(TAG, "New user inserted into sqlite: " + id);
    }

    /**
     * Mendapatkan data user dari database
     * */
    public HashMap<String, String> getUserDetails() {
        HashMap<String, String> user = new HashMap<String, String>();
        String selectQuery = "SELECT  * FROM " + TABLE_USER;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        // Move to first row
        cursor.moveToFirst();
        if (cursor.getCount() > 0) {
            user.put("name", cursor.getString(1));
            user.put("email", cursor.getString(2));
            user.put("uid", cursor.getString(3));
            user.put("created_at", cursor.getString(4));
            user.put("pekerjaan", cursor.getString(5));
            user.put("pendidikan_terakhir", cursor.getString(6));

        }
        cursor.close();
        db.close();
        // return user
        Log.d(TAG, "Fetching user from Sqlite: " + user.toString());

        return user;
    }

    /**
     * delete user dan create lagi
     * */
    public void deleteUsers() {
        SQLiteDatabase db = this.getWritableDatabase();
        // Delete All Rows
        db.delete(TABLE_USER, null, null);
        db.close();

        Log.d(TAG, "Deleted all user info from sqlite");
    }


    /**
     * Menambahkan data publikasi ke database
      */
    public void addPublikasi(Publikasi publikasi) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_PUBLIKASI_TITLE,publikasi.getTitle());
        values.put(KEY_PUBLIKASI_KAT_NO,publikasi.getKat_no());
        values.put(KEY_PUBLIKASI_PUB_NO,publikasi.getPub_no());
        values.put(KEY_PUBLIKASI_ISSN,publikasi.getIssn());
        values.put(KEY_PUBLIKASI_ABSTRACT,publikasi.get_abstract());
        values.put(KEY_PUBLIKASI_SCH_DATE,publikasi.getSch_date());
        values.put(KEY_PUBLIKASI_RL_DATE,publikasi.getRl_date());
        values.put(KEY_PUBLIKASI_COVER,publikasi.getCover());
        values.put(KEY_PUBLIKASI_PDF,publikasi.getPdf());
        values.put(KEY_PUBLIKASI_SIZE,publikasi.getSize());

        //insert row
        long id = db.insert(TABLE_PUBLIKASI,null,values);
        db.close();

        Log.d(TAG, "Publikasi dimasukkan ke db sqlite" + id);
    }

    /**
     * Mendapatkan satu data publikasi dari database
     */
    public Publikasi getPublikasi(long id){
        SQLiteDatabase db = this.getReadableDatabase();

        String selectQuery = "SELECT * FROM " + TABLE_PUBLIKASI + " WHERE " +
                KEY_PUBLIKASI_ID + " = " + id;

        Cursor c = db.rawQuery(selectQuery, null);
        if(c != null)
            c.moveToFirst();

        Publikasi pb = new Publikasi();
        pb.setPub_id(c.getInt(c.getColumnIndex(KEY_PUBLIKASI_ID)));
        pb.setTitle(c.getString(c.getColumnIndex(KEY_PUBLIKASI_TITLE)));
        pb.setKat_no(c.getString(c.getColumnIndex(KEY_PUBLIKASI_KAT_NO)));
        pb.setPub_no(c.getString(c.getColumnIndex(KEY_PUBLIKASI_PUB_NO)));
        pb.setIssn(c.getString(c.getColumnIndex(KEY_PUBLIKASI_ISSN)));
        pb.set_abstract(c.getString(c.getColumnIndex(KEY_PUBLIKASI_ABSTRACT)));
        pb.setSch_date(c.getString(c.getColumnIndex(KEY_PUBLIKASI_SCH_DATE)));
        pb.setRl_date(c.getString(c.getColumnIndex(KEY_PUBLIKASI_RL_DATE)));
        pb.setCover(c.getString(c.getColumnIndex(KEY_PUBLIKASI_COVER)));
        pb.setPdf(c.getString(c.getColumnIndex(KEY_PUBLIKASI_PDF)));
        pb.setSize(c.getString(c.getColumnIndex(KEY_PUBLIKASI_SIZE)));

        return pb;

    }
    /**
     * Mendapatkan semua data publikasi dari database
     * */
    // TODO: 6/1/2018 Ini belum dicek apakah berhasil 
    public ArrayList<HashMap<String,String>> getAllPublikasi() {
        ArrayList<HashMap<String,String>> publikasiList = new ArrayList<HashMap<String, String>>();
        String selectQuery = "SELECT  * FROM " + TABLE_PUBLIKASI;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        // Move to first row
        cursor.moveToFirst();
        while(!cursor.isAfterLast()){
            HashMap hashmap = new HashMap<String,String>();
            hashmap.put("title",cursor.getString(cursor.getColumnIndex(KEY_PUBLIKASI_TITLE)));
            hashmap.put("kat_no",cursor.getString(cursor.getColumnIndex(KEY_PUBLIKASI_KAT_NO)));
            hashmap.put("pub_no",cursor.getString(cursor.getColumnIndex(KEY_PUBLIKASI_PUB_NO)));
            hashmap.put("issn",cursor.getString(cursor.getColumnIndex(KEY_PUBLIKASI_ISSN)));
            hashmap.put("abstract",cursor.getString(cursor.getColumnIndex(KEY_PUBLIKASI_ABSTRACT)));
            hashmap.put("sch_date",cursor.getString(cursor.getColumnIndex(KEY_PUBLIKASI_SCH_DATE)));
            hashmap.put("rl_date",cursor.getString(cursor.getColumnIndex(KEY_PUBLIKASI_RL_DATE)));
            hashmap.put("cover",cursor.getString(cursor.getColumnIndex(KEY_PUBLIKASI_COVER)));
            hashmap.put("pdf",cursor.getString(cursor.getColumnIndex(KEY_PUBLIKASI_PDF)));
            hashmap.put("size",cursor.getString(cursor.getColumnIndex(KEY_PUBLIKASI_SIZE)));

            publikasiList.add(hashmap);
            cursor.moveToNext();

        }

        db.close();
        // return user
        Log.d(TAG, "Fetching publikasi from Sqlite: " + publikasiList.toString());

        return publikasiList;
    }

    /**
     * Delete data publikasi dan buat lagi
     */
    public void deletePublikasi(){

        SQLiteDatabase db = this.getWritableDatabase();
        // Delete All Rows
        db.delete(TABLE_PUBLIKASI, null, null);
        db.close();

        Log.d(TAG, "Deleted all publikasi from sqlite");

    }


    /**
     *     Menambahkan data aktivitas user ke database sqlite
     */
    public void addUserActivity(String email, String publikasi_title, String event, Integer halaman_publikasi, String time_stamp) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_EMAIL, email); // email
        values.put(KEY_PUBLIKASI_TITLE, publikasi_title); // title
        values.put(KEY_EVENT, event); // email
        values.put(KEY_HALAMAN_PUBLIKASI, halaman_publikasi); // email
        values.put(KEY_TIME_STAMP, time_stamp); // email

        // Inserting Row
        long id = db.insert(TABLE_USER_ACTIVITY, null, values);
        db.close(); // Closing database connection

        Log.d(TAG, "New user activity inserted into sqlite: " + id);
    }

    /**
     * Delete data aktivitas dan create lagi
     */
    public void deleteUserActivity(){

        SQLiteDatabase db = this.getWritableDatabase();
        // Delete All Rows
        db.delete(TABLE_USER_ACTIVITY, null, null);
        db.close();

        Log.d(TAG, "Deleted all user activity from sqlite");

    }


}
