package com.cmp2017.devworms.cmp2017;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by mac on 17/03/17.
 */

public class AdminSqlSecciones extends SQLiteOpenHelper {


        private static final int DATABASE_VERSION = 1;
        private static final String DATABASE_NAME = "cmp2017.db";
        public static final String TABLA_SECCION = "Programas";
        public static final String COLUMN_ID = "_id";
        public static final String COLUMN_ID_EVENTOPROGRAMAS = "id_programas_eventos";
        public static final String COLUMN_NOMBRE = "nombre";
        public static final String COLUMN_LUGAR = "lugar";
        public static final String COLUMN_RECOMENDACIONES = "recomendaciones";
        public static final String COLUMN_FECHA = "fecha";
        public static final String COLUMN_HORA_INICIO = "hora_inicio";
        public static final String COLUMN_HORA_FIN = "hora_fin";
        public static final String COLUMN_Categoria = "categoria";






        public AdminSqlSecciones(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
            super(context, DATABASE_NAME, factory, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            String query = "CREATE TABLE " + TABLA_SECCION + "(" +
                    COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_NOMBRE + " TEXT, " +
                    COLUMN_ID_EVENTOPROGRAMAS + " TEXT, " +

                    COLUMN_LUGAR + " TEXT, " +
                    COLUMN_FECHA + " TEXT, " +
                    COLUMN_HORA_INICIO + " TEXT, " +
                    COLUMN_HORA_FIN + " TEXT " +

                    ");";

            db.execSQL(query);

        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("DROP TABLE IF EXISTS " + TABLA_SECCION);
            onCreate(db);
        }

        //Añade un nuevo Row a la Base de Datos

        public void addEvento(String titulo, String conferencista, String descri, String lugar, String fecha, String hr,String event) {
            if (hr.length()<=4){
                hr="0"+hr;
            }
           /* ContentValues values = new ContentValues();
            values.put(COLUMN_NOMBRE, titulo);
            values.put(COLUMN_CONFERENCISTA, conferencista);
            values.put(COLUMN_DESCRIP, descri);
            values.put(COLUMN_LUGAR, lugar);
            values.put(COLUMN_FECHA, fecha);
            values.put(COLUMN_HORA, hr);
            values.put(COLUMN_DESCRIPEVENT,event);
            SQLiteDatabase db = getWritableDatabase();
            db.insert(TABLA_PERSONAS, null, values);
            db.close();*/

        }


}
