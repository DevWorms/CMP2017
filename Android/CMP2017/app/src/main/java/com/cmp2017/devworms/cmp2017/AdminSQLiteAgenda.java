package com.cmp2017.devworms.cmp2017;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by mac on 07/04/17.
 */

public class AdminSQLiteAgenda extends SQLiteOpenHelper {
    //Se crean variables para la Base de Datos
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "Agenda.db";
    public static final String TABLA_AGENDA = "agenda";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_IDEVENTO = "idEvento";
    public static final String COLUMN_NOMBRE = "nombre";
    public static final String COLUMN_DIA = "dia";
    public static final String COLUMN_HORA_INICIO = "horainicio";
    public static final String COLUMN_HORA_FIN = "horafin";



    public AdminSQLiteAgenda(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, DATABASE_NAME, factory, DATABASE_VERSION);
    }
    //Se construye y se crea la Base de Datos
    @Override
    public void onCreate(SQLiteDatabase db) {
        String query = "CREATE TABLE " + TABLA_AGENDA + "(" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_IDEVENTO + " TEXT," +
                COLUMN_NOMBRE + " TEXT, " +
                COLUMN_DIA + " TEXT, " +
                COLUMN_HORA_INICIO + " TEXT," +
                COLUMN_HORA_FIN + " TEXT " +

                ");";

        db.execSQL(query);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLA_AGENDA);
        onCreate(db);
    }

    //Añade un nuevo Row de evento a la Base de Datos

    public void addEvento(String idEvento,String nombre, String dia, String horaini, String horafin) {

        ContentValues values = new ContentValues();
        values.put(COLUMN_IDEVENTO, idEvento);
        values.put(COLUMN_NOMBRE, nombre);
        values.put(COLUMN_DIA, dia);
        values.put(COLUMN_HORA_INICIO, horaini);
        values.put(COLUMN_HORA_FIN, horafin);

        SQLiteDatabase db = getWritableDatabase();
        db.insert(TABLA_AGENDA, null, values);
        db.close();

    }



    // Borrar una evento por su id de la Base de Datos

    public void borrarEvento(String persona_id){
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("DELETE FROM " + TABLA_AGENDA + " WHERE " + COLUMN_IDEVENTO + " = " + persona_id + ";");
        db.close();
    }

    //Regresa todos los eventos por dia y los acomoda segun su hora de inicio

    public Cursor listaPorDia(String dia){
        SQLiteDatabase db = getWritableDatabase();
        String query = "SELECT * FROM " + TABLA_AGENDA + " WHERE " + COLUMN_DIA + " = " + dia + "ORDER BY horainicio ASC ;";
        Cursor c = db.rawQuery(query, null);

        if (c != null) {
            c.moveToFirst();
        }

        return c;
    }

    //listar a todos los eventos guardados en la base
    public Cursor listarTodos(){
        SQLiteDatabase db = getReadableDatabase();
        String query = ("SELECT * FROM " + TABLA_AGENDA + " ORDER BY horainicio ASC ;");
        Cursor c = db.rawQuery(query, null);

        if (c != null) {
            c.moveToFirst();
        }

        return c;
    }

    //busca evento por id en la base
    public Cursor BuscarpoId(String id){
        SQLiteDatabase db = getReadableDatabase();
        String query = "SELECT * FROM " + TABLA_AGENDA + " WHERE " + COLUMN_IDEVENTO + " = " + id + " ;";

        Cursor c = db.rawQuery(query, null);

        if (c != null) {
            c.moveToFirst();
        }

        return c;
    }

}





