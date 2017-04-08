package com.cmp2017.devworms.cmp2017;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by mac on 07/04/17.
 */

public class AdminSQLiteOffline extends SQLiteOpenHelper {
    //Se crean variables para la Base de Datos
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "Offline.db";
    public static final String TABLA_BANNER = "Banner";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_URL = "url";
    public static final String COLUMN_IMAGEN = "imagenStr";




    public AdminSQLiteOffline(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, DATABASE_NAME, factory, DATABASE_VERSION);
    }
    //Se construye y se crea la Base de Datos
    @Override
    public void onCreate(SQLiteDatabase db) {
        String query = "CREATE TABLE " + TABLA_BANNER + "(" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_URL + " TEXT, " +
                COLUMN_IMAGEN + " TEXT " +

                ");";

        db.execSQL(query);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLA_BANNER);
        onCreate(db);
    }

    //Añade los Banners a la Base de Datos

    public void addBanner(String url, String imagen) {

        ContentValues values = new ContentValues();
        values.put(COLUMN_URL, url);
        values.put(COLUMN_IMAGEN, imagen);


        SQLiteDatabase db = getWritableDatabase();
        db.insert(TABLA_BANNER, null, values);
        db.close();

    }


    // Borrar Banners de la Base de Datos

    public void borrarBanners(String persona_id){
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("DELETE FROM " + TABLA_BANNER + " ;");
        db.close();
    }


    //listar a todos los Banners
    public Cursor listarTodosBanner(){
        SQLiteDatabase db = getReadableDatabase();
        String query = ("SELECT imagenStr FROM " + TABLA_BANNER + " ;");
        Cursor c = db.rawQuery(query, null);

        if (c != null) {
            c.moveToFirst();
        }

        return c;
    }


}