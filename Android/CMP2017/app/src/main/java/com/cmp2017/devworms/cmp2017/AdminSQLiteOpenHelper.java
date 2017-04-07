package com.cmp2017.devworms.cmp2017;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


public class AdminSQLiteOpenHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "MisExpositores.db";
    public static final String TABLA_PERSONAS = "persona";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_IDEXPO = "idExpo";
    public static final String COLUMN_NOMBRE = "nombre";
    public static final String COLUMN_DESCRIP = "descripcion";
    public static final String COLUMN_TELEFONO = "telefono";
    public static final String COLUMN_EMAIL = "email";
    public static final String COLUMN_WEB = "web";
    public static final String COLUMN_PRESENTA = "presentacion";


    public AdminSQLiteOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, DATABASE_NAME, factory, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String query = "CREATE TABLE " + TABLA_PERSONAS + "(" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_IDEXPO + " TEXT," +
                COLUMN_NOMBRE + " TEXT, " +
                COLUMN_DESCRIP + " TEXT," +
                COLUMN_TELEFONO + " TEXT, " +
                COLUMN_EMAIL + " TEXT, " +
                COLUMN_WEB + " TEXT," +
                COLUMN_PRESENTA + " TEXT" +

                ");";

        db.execSQL(query);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLA_PERSONAS);
        onCreate(db);
    }

    //Añade un nuevo Row a la Base de Datos

    public void addExpo(String idExpo,String nombre, String descri, String tel, String email, String web, String prese) {

        ContentValues values = new ContentValues();
        values.put(COLUMN_IDEXPO, idExpo);
        values.put(COLUMN_NOMBRE, nombre);
        values.put(COLUMN_DESCRIP, descri);
        values.put(COLUMN_TELEFONO, tel);
        values.put(COLUMN_EMAIL, email);
        values.put(COLUMN_WEB, web);
        values.put(COLUMN_PRESENTA, prese);
        SQLiteDatabase db = getWritableDatabase();
        db.insert(TABLA_PERSONAS, null, values);
        db.close();

    }



    // Borrar una persona de la Base de Datos

    public void borrarPersona(int persona_id){
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("DELETE FROM " + TABLA_PERSONAS + " WHERE " + COLUMN_ID + " = " + persona_id + ";");
        db.close();
    }

    //listar por id

    public Cursor personabyid(String id){
        SQLiteDatabase db = getWritableDatabase();
        String query = "SELECT * FROM " + TABLA_PERSONAS + " WHERE " + COLUMN_IDEXPO + " = " + id + ";";
        Cursor c = db.rawQuery(query, null);

        if (c != null) {
            c.moveToFirst();
        }

        return c;
    }

    //listar a todas las personas
    public Cursor listarpersonas(){
        SQLiteDatabase db = getReadableDatabase();
        String query = ("SELECT * FROM " + TABLA_PERSONAS + " ORDER BY nombre ASC ;");
        Cursor c = db.rawQuery(query, null);

        if (c != null) {
            c.moveToFirst();
        }

        return c;
    }
    public Cursor listarpersonasb(String b){
        SQLiteDatabase db = getReadableDatabase();
        String query = ("SELECT * FROM " + TABLA_PERSONAS + " where nombre LIKE  '"+ b +"%';");
        Cursor c = db.rawQuery(query, null);

        if (c != null) {
            c.moveToFirst();
        }

        return c;
    }
    public Cursor listarpersonasid(Integer id){
        SQLiteDatabase db = getReadableDatabase();
        String query = ("SELECT * FROM " + TABLA_PERSONAS + " where _id =  '"+ id +"';");
        Cursor c = db.rawQuery(query, null);

        if (c != null) {
            c.moveToFirst();
        }

        return c;
    }
}




