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

    public static final String TABLA_EXPOSITORES = "Expositores";
    public static final String COLUMN_IDEX = "_id";
    public static final String COLUMN_JSONEXPOALFABETICO = "jsonExpoAlfa";
    public static final String COLUMN_JSONEXPONumerico = "jsonExpoNume";

    public static final String TABLA_EXPOSITORESIMAGENES = "ExpositoresImagenes";
    public static final String COLUMN_IDEXIMA = "_id";
    public static final String COLUMN_IDEXPOSITOR = "idExpositor";
    public static final String COLUMN_EXPOIMAGESTR = "imageExpo";


    public static final String TABLA_PATROCINADORES = "Patrocinadores";
    public static final String COLUMN_IDPA = "_id";
    public static final String COLUMN_JSONPATROALFABETICO = "jsonPatroAlfa";
    public static final String COLUMN_JSONPATRONumerico = "jsonPatroNume";


    public static final String TABLA_PATROIMAGENES = "PatroImagenes";
    public static final String COLUMN_IDPATRO = "_id";
    public static final String COLUMN_IDPATROCINADOR = "idPatro";
    public static final String COLUMN_PATROIMAGESTR = "imagePatro";

    public static final String TABLA_ACOMPAÑANTE = "Acompañantes";
    public static final String COLUMN_IDACO = "_id";
    public static final String COLUMN_JSONACOMPAÑANTE = "jsonAcomp";

    public static final String TABLA_ACOMPAÑANTEIMAGENES = "AcompImagenes";
    public static final String COLUMN_IDACOIMA = "_id";
    public static final String COLUMN_IDACOMPAÑANTES = "idAcomp";
    public static final String COLUMN_ACOMPIMAGESTR = "imageAcomp";

    public static final String TABLA_SOCIALDEPO = "SocialDeportivo";
    public static final String COLUMN_IDSOCIADEPO = "_id";
    public static final String COLUMN_JSONSOCIALDEPORTIVO= "jsonSocialDepo";

    public static final String TABLA_SOCIALDEPOIMAGENES = "SociaDepoImagenes";
    public static final String COLUMN_IDSOCIA = "_id";
    public static final String COLUMN_IDSOCIADEPORTIVO = "idSociaDepo";
    public static final String COLUMN_SOCIALDEPOIMAGESTR = "imageSociaDepo";

    public static final String TABLA_CATEGORIAS = "Categorias";
    public static final String COLUMN_IDCAT = "idCategoria";
    public static final String COLUMN_JSONCATEGORIA= "jsonCategoria";

    public static final String TABLA_TRANSP = "Transportacion";
    public static final String COLUMN_IDTRANS = "idTransportacion";
    public static final String COLUMN_JSONTRANS= "jsonTranspo";

    public static final String TABLA_TRANSPOIMAGENES = "TransportacionImagenes";
    public static final String COLUMN_IDTRANSRUTA = "_id";
    public static final String COLUMN_IDRUTA = "idRuta";
    public static final String COLUMN_RUTAIMAGESTR = "imageRuta";


    public AdminSQLiteOffline(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, DATABASE_NAME, factory, DATABASE_VERSION);
    }
    //Se construye y se crea la Base de Datos
    @Override
    public void onCreate(SQLiteDatabase db) {
        String queryBanner = "CREATE TABLE " + TABLA_BANNER + "(" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_URL + " TEXT, " +
                COLUMN_IMAGEN + " TEXT " +

                ");";

        db.execSQL(queryBanner);

        String queryExpo = "CREATE TABLE " + TABLA_EXPOSITORES + "(" +
                COLUMN_IDEX + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_JSONEXPOALFABETICO + " TEXT," +
                COLUMN_JSONEXPONumerico + " TEXT" +
                ");";

        db.execSQL(queryExpo);


        String queryExpoIma = "CREATE TABLE " + TABLA_EXPOSITORESIMAGENES + "(" +
                COLUMN_IDEXIMA + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_IDEXPOSITOR + " TEXT," +
                COLUMN_EXPOIMAGESTR + " TEXT" +
                ");";

        db.execSQL(queryExpoIma);

        String queryPatro = "CREATE TABLE " + TABLA_PATROCINADORES + "(" +
                COLUMN_IDPA + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_JSONPATROALFABETICO + " TEXT," +
                COLUMN_JSONPATRONumerico + " TEXT" +
                ");";

        db.execSQL(queryPatro);

        String queryPatroIma = "CREATE TABLE " + TABLA_PATROIMAGENES + "(" +
                COLUMN_IDPATRO + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_IDPATROCINADOR + " TEXT," +
                COLUMN_PATROIMAGESTR + " TEXT" +
                ");";

        db.execSQL(queryPatroIma);

        String queryAco = "CREATE TABLE " + TABLA_ACOMPAÑANTE + "(" +
                COLUMN_IDACO + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_JSONACOMPAÑANTE + " TEXT" +
                ");";

        db.execSQL(queryAco);

        String queryAcoIma = "CREATE TABLE " + TABLA_ACOMPAÑANTEIMAGENES + "(" +
                COLUMN_IDACOIMA + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_IDACOMPAÑANTES + " TEXT," +
                COLUMN_ACOMPIMAGESTR + " TEXT" +
                ");";
        db.execSQL(queryAcoIma);


        String querySocia = "CREATE TABLE " + TABLA_SOCIALDEPO + "(" +
                COLUMN_IDSOCIADEPO + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_JSONSOCIALDEPORTIVO + " TEXT" +
                ");";

        db.execSQL(querySocia);

        String querySociaIma = "CREATE TABLE " + TABLA_SOCIALDEPOIMAGENES + "(" +
                COLUMN_IDSOCIA + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_IDSOCIADEPORTIVO + " TEXT," +
                COLUMN_SOCIALDEPOIMAGESTR + " TEXT" +
                ");";
        db.execSQL(querySociaIma);

        String queryCategoria = "CREATE TABLE " + TABLA_CATEGORIAS + "(" +
                COLUMN_IDCAT+ " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_JSONCATEGORIA + " TEXT" +
                ");";

        db.execSQL(queryCategoria);

        String queryTrans = "CREATE TABLE " + TABLA_TRANSP+ "(" +
                COLUMN_IDTRANS+ " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_JSONTRANS + " TEXT" +
                ");";

        db.execSQL(queryTrans);

        String queryTransIma = "CREATE TABLE " + TABLA_TRANSPOIMAGENES + "(" +
                COLUMN_IDTRANSRUTA + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_IDRUTA + " TEXT," +
                COLUMN_RUTAIMAGESTR + " TEXT" +
                ");";
        db.execSQL(queryTransIma);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLA_BANNER);
        onCreate(db);

        db.execSQL("DROP TABLE IF EXISTS " + TABLA_EXPOSITORES);
        onCreate(db);

        db.execSQL("DROP TABLE IF EXISTS " + TABLA_EXPOSITORESIMAGENES);
        onCreate(db);

        db.execSQL("DROP TABLE IF EXISTS " + TABLA_PATROCINADORES);
        onCreate(db);

        db.execSQL("DROP TABLE IF EXISTS " + TABLA_PATROIMAGENES);
        onCreate(db);

        db.execSQL("DROP TABLE IF EXISTS " + TABLA_ACOMPAÑANTE);
        onCreate(db);

        db.execSQL("DROP TABLE IF EXISTS " + TABLA_ACOMPAÑANTEIMAGENES);
        onCreate(db);

        db.execSQL("DROP TABLE IF EXISTS " + TABLA_SOCIALDEPO);
        onCreate(db);

        db.execSQL("DROP TABLE IF EXISTS " + TABLA_SOCIALDEPOIMAGENES);
        onCreate(db);

        db.execSQL("DROP TABLE IF EXISTS " + TABLA_CATEGORIAS);
        onCreate(db);

        db.execSQL("DROP TABLE IF EXISTS " + TABLA_TRANSP);
        onCreate(db);

        db.execSQL("DROP TABLE IF EXISTS " + TABLA_TRANSPOIMAGENES);
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

    //Añade El JSON Expositores a la Base de Datos

    public void addExpo(String jsonAlfa, String jsonNume) {

        ContentValues values = new ContentValues();
        values.put(COLUMN_JSONEXPOALFABETICO, jsonAlfa);
        values.put(COLUMN_JSONEXPONumerico, jsonNume);
        SQLiteDatabase db = getWritableDatabase();
        db.insert(TABLA_EXPOSITORES, null, values);
        db.close();

    }
    //Regresa el jsonAlfanumerico de la Base de Datos
    public Cursor jsonAlfa(){
        SQLiteDatabase db = getWritableDatabase();
        String query = "SELECT jsonExpoAlfa FROM " + TABLA_EXPOSITORES + " ;";
        Cursor c = db.rawQuery(query, null);

        if (c != null) {
            c.moveToFirst();
        }

        return c;
    }

    //Regresa el jsonNumerico de la Base de Datos
    public Cursor jsonNume(){
        SQLiteDatabase db = getWritableDatabase();
        String query = "SELECT jsonExpoNume FROM " + TABLA_EXPOSITORES + " ;";
        Cursor c = db.rawQuery(query, null);

        if (c != null) {
            c.moveToFirst();
        }

        return c;
    }

    //Añade Las Imagenes de Los expositores

    public void addExpoImag(String idImage, String image) {

        ContentValues values = new ContentValues();
        values.put(COLUMN_IDEXPOSITOR, idImage);
        values.put(COLUMN_EXPOIMAGESTR, image);
        SQLiteDatabase db = getWritableDatabase();
        db.insert(TABLA_EXPOSITORESIMAGENES, null, values);
        db.close();

    }
    //Regresa la imagen por el id de expositor
    public Cursor ImagenPorId(String idexpo){
        SQLiteDatabase db = getWritableDatabase();
        String query = "SELECT imageExpo FROM " + TABLA_EXPOSITORESIMAGENES + " WHERE idExpositor = "+idexpo+";";
        Cursor c = db.rawQuery(query, null);

        if (c != null) {
            c.moveToFirst();
        }

        return c;
    }


    //Añade El JSON Patrocinadores  a la Base de Datos

    public void addPatro(String jsonAlfa, String jsonNume) {

        ContentValues values = new ContentValues();
        values.put(COLUMN_JSONPATROALFABETICO, jsonAlfa);
        values.put(COLUMN_JSONPATRONumerico, jsonNume);
        SQLiteDatabase db = getWritableDatabase();
        db.insert(TABLA_PATROCINADORES, null, values);
        db.close();

    }
    //Regresa el jsonAlfanumerico de la Base de Datos
    public Cursor jsonPatroAlfa(){
        SQLiteDatabase db = getWritableDatabase();
        String query = "SELECT jsonPatroAlfa FROM " + TABLA_PATROCINADORES + " ;";
        Cursor c = db.rawQuery(query, null);

        if (c != null) {
            c.moveToFirst();
        }

        return c;
    }

    //Regresa el jsonNumerico de la Base de Datos
    public Cursor jsonPatroNume(){
        SQLiteDatabase db = getWritableDatabase();
        String query = "SELECT jsonPatroNume FROM " + TABLA_PATROCINADORES + " ;";
        Cursor c = db.rawQuery(query, null);

        if (c != null) {
            c.moveToFirst();
        }

        return c;
    }

    public void addPatroImag(String idImage, String image) {

        ContentValues values = new ContentValues();
        values.put(COLUMN_IDPATROCINADOR, idImage);
        values.put(COLUMN_PATROIMAGESTR, image);
        SQLiteDatabase db = getWritableDatabase();
        db.insert(TABLA_PATROIMAGENES, null, values);
        db.close();

    }
    public Cursor ImagenPorIdPatro(String idPatro){
        SQLiteDatabase db = getWritableDatabase();
        String query = "SELECT imagePatro FROM " + TABLA_PATROIMAGENES + " WHERE idPatro = "+idPatro+";";
        Cursor c = db.rawQuery(query, null);

        if (c != null) {
            c.moveToFirst();
        }

        return c;
    }
    // INSERTAMOS EL JSON DE PROGRAMAS
    public void addJsonCategorias(String jsonPrograma){
        ContentValues values = new ContentValues();
        values.put(COLUMN_JSONCATEGORIA, jsonPrograma);
        SQLiteDatabase db = getWritableDatabase();
        db.insert(TABLA_CATEGORIAS, null, values);
        db.close();
    }

    public Cursor getJsonCategorias(){
        SQLiteDatabase db = getWritableDatabase();
        String query = "SELECT "+COLUMN_JSONCATEGORIA+" FROM " + TABLA_CATEGORIAS + " ;";
        Cursor cursorRs = db.rawQuery(query, null);

        if (cursorRs != null) {
            cursorRs.moveToFirst();
        }

        return cursorRs;
    }

    //Añade El JSON ACOMPAÑANTE  a la Base de Datos

    public void addAcomp(String jsonAlfa) {

        ContentValues values = new ContentValues();
        values.put(COLUMN_JSONACOMPAÑANTE, jsonAlfa);
        SQLiteDatabase db = getWritableDatabase();
        db.insert(TABLA_ACOMPAÑANTE, null, values);
        db.close();

    }
    //Regresa el jsonAcomp de la Base de Datos
    public Cursor jsonAcompa(){
        SQLiteDatabase db = getWritableDatabase();
        String query = "SELECT jsonAcomp FROM " + TABLA_ACOMPAÑANTE + " ;";
        Cursor c = db.rawQuery(query, null);

        if (c != null) {
            c.moveToFirst();
        }

        return c;


    }

    //Añade Las Imagenes de Los acompañantes

    public void addAcoImag(String idImage, String image) {

        ContentValues values = new ContentValues();
        values.put(COLUMN_IDACOMPAÑANTES, idImage);
        values.put(COLUMN_ACOMPIMAGESTR, image);
        SQLiteDatabase db = getWritableDatabase();
        db.insert(TABLA_ACOMPAÑANTEIMAGENES, null, values);
        db.close();

    }

    public Cursor ImagenPorIdAco(String idAco){
        SQLiteDatabase db = getWritableDatabase();
        String query = "SELECT imageAcomp FROM " + TABLA_ACOMPAÑANTEIMAGENES + " WHERE idAcomp = "+idAco+";";
        Cursor c = db.rawQuery(query, null);

        if (c != null) {
            c.moveToFirst();
        }

        return c;
    }

    //Añade El JSON SOCIAL DEPO  a la Base de Datos

    public void addSocialDepo(String jsonAlfa) {

        ContentValues values = new ContentValues();
        values.put(COLUMN_JSONSOCIALDEPORTIVO, jsonAlfa);
        SQLiteDatabase db = getWritableDatabase();
        db.insert(TABLA_SOCIALDEPO, null, values);
        db.close();

    }
    //Regresa el jsonAcomp de la Base de Datos
    public Cursor jsonSocialDepo(){
        SQLiteDatabase db = getWritableDatabase();
        String query = "SELECT jsonSocialDepo FROM " + TABLA_SOCIALDEPO + " ;";
        Cursor c = db.rawQuery(query, null);

        if (c != null) {
            c.moveToFirst();
        }

        return c;


    }

    //Añade Las Imagenes de Social depo

    public void addSociaDepoImag(String idImage, String image) {

        ContentValues values = new ContentValues();
        values.put(COLUMN_IDSOCIADEPORTIVO, idImage);
        values.put(COLUMN_SOCIALDEPOIMAGESTR, image);
        SQLiteDatabase db = getWritableDatabase();
        db.insert(TABLA_SOCIALDEPOIMAGENES, null, values);
        db.close();

    }

    public Cursor ImagenPorIdSocialDepo(String idSocial){
        SQLiteDatabase db = getWritableDatabase();
        String query = "SELECT "+ COLUMN_SOCIALDEPOIMAGESTR +" FROM " + TABLA_SOCIALDEPOIMAGENES + " WHERE "+ COLUMN_IDSOCIADEPORTIVO +" = "+idSocial+";";
        Cursor c = db.rawQuery(query, null);

        if (c != null) {
            c.moveToFirst();
        }

        return c;
    }


    //Añade El JSON TRANSPORTACION  a la Base de Datos

    public void addTrans(String jsonAlfa) {

        ContentValues values = new ContentValues();
        values.put(COLUMN_JSONTRANS, jsonAlfa);
        SQLiteDatabase db = getWritableDatabase();
        db.insert(TABLA_TRANSP, null, values);
        db.close();

    }

    public Cursor jsonTrans(){
        SQLiteDatabase db = getWritableDatabase();
        String query = "SELECT jsonTranspo FROM " + TABLA_TRANSP + " ;";
        Cursor c = db.rawQuery(query, null);

        if (c != null) {
            c.moveToFirst();
        }

        return c;


    }

    //Añade Las Imagenes de Social depo

    public void addRutaImag(String idImage, String image) {

        ContentValues values = new ContentValues();
        values.put(COLUMN_IDRUTA, idImage);
        values.put(COLUMN_RUTAIMAGESTR, image);
        SQLiteDatabase db = getWritableDatabase();
        db.insert(TABLA_TRANSPOIMAGENES, null, values);
        db.close();

    }

    public Cursor ImagenPorIdRuta(String idRuta){
        SQLiteDatabase db = getWritableDatabase();
        String query = "SELECT "+ COLUMN_RUTAIMAGESTR +" FROM " + TABLA_TRANSPOIMAGENES + " WHERE "+ COLUMN_IDRUTA +" = "+idRuta+";";
        Cursor c = db.rawQuery(query, null);

        if (c != null) {
            c.moveToFirst();
        }

        return c;
    }

}