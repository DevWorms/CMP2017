package com.cmp2017.devworms.cmp2017;

import android.app.Activity;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.Base64;
import com.nostra13.universalimageloader.utils.L;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

public class Updates extends Activity implements View.OnClickListener {

    private String apiKey, urlImage, ImageDecoExpo, resp;
    private String resFinal;
    private String userId;
    private ProgressDialog pDialog;
    private ArrayList<Integer> modulosParaActulizar;
    private Activity yo;
    private Button btnActualizar;
    private TextView descActualizaciones;
    private URL imageUrl;
    private Bitmap[] imagen;
    private Bitmap expoImg;
    private int numModulos;
    private HttpURLConnection conn;
    private String[] ArrayBanners, ImagenDeco;
    private AdminSQLiteOffline dbHandlerOffline;
    private String errorUpate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_updates);

        SharedPreferences sp = this.getSharedPreferences("prefe", Activity.MODE_PRIVATE);
        this.apiKey = sp.getString("APIkey", "");
        this.userId = sp.getString("IdUser", "");
        this.errorUpate = sp.getString("ERROR-UPDATE", "");
        Log.e("PREFERENCES", apiKey + " " + userId + " " + errorUpate);
        // ESTOS PARAMETROS SON PARA QUE SE PUEDA VER LA VENTANA COMO POP UP
        DisplayMetrics metrics = new DisplayMetrics();

        getWindowManager().getDefaultDisplay().getMetrics(metrics);

        int width = (int) (metrics.widthPixels * 0.9);
        int height = (int) (metrics.heightPixels * 0.38);

        getWindow().setLayout(width, height);

        this.descActualizaciones = (TextView) findViewById(R.id.descActualizaciones);
        this.btnActualizar = (Button) findViewById(R.id.btnActualizar);
        this.modulosParaActulizar = new ArrayList<Integer>();
        this.btnActualizar.setOnClickListener(this);
        this.yo = this;
        GetUpdates getUpdates = new GetUpdates();
        getUpdates.execute();

        resFinal = "";
    }

    @Override
    public void onClick(View v) {
        if (numModulos > 0) {
            try {
                UpdateModulos update = new UpdateModulos();
                update.execute();
            } catch (Exception ex) {
                Log.e("ALGOPASO", ex.getMessage());
            }
        } else {
            this.finish();
        }
    }

    class GetUpdates extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //ABRIMOS EL DIALOG DE ESPERA
            pDialog = new ProgressDialog(yo);
            pDialog.setMessage("Buscando Actulizaciones...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected String doInBackground(String... args) {

            /*DEBIDO A QUE NO SE TIENE UN INDICADOR PARA BANNERS Y PARA MAPA RESINTO SIEMRPE SE ACTUALIZA */

            updateBanners();
            updateMapas();

            String fromUrl = "http://cmp.devworms.com/api/updates/check/" + userId + "/" + apiKey;

            JSONParser jsonParser = new JSONParser();

            String respuesta = jsonParser.makeHttpRequest(fromUrl, "GET", fromUrl, "");

            if (respuesta != "error") {
                try {
                    JSONObject jUpdates = new JSONObject(respuesta);
                    String strUpdates = jUpdates.getString("actualizaciones");
                    JSONArray aUpdates = new JSONArray(strUpdates);
                    for (int c = 0; c < aUpdates.length(); c++) {
                        JSONObject jModulos = aUpdates.getJSONObject(c);
                        modulosParaActulizar.add(jModulos.getInt("modulo"));
                    }
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (errorUpate.equals("1")) {
                                descActualizaciones.setText("Obtener actualizaciones nuevamente.");
                                modulosParaActulizar.clear();

                                for (int i = 1; i <= 10; i++) {
                                    modulosParaActulizar.add(i);
                                }
                                numModulos = modulosParaActulizar.size();
                            } else {
                                if (modulosParaActulizar.size() > 0) {
                                    numModulos = modulosParaActulizar.size();
                                    descActualizaciones.setText("Existe " + numModulos + " actualizaciones disponibles.");

                                } else {
                                    descActualizaciones.setText("No hay actualizaciones por el momento");
                                    btnActualizar.setText("Cerrar");
                                }
                            }

                        }
                    });
                } catch (JSONException j) {
                    Log.e("UPDATESWRONS", j.getMessage());
                }
            }

            return null;
        }


        @Override
        protected void onPostExecute(String str) {
            //CERRAMOS EL DIALOG E ESPERA
            pDialog.dismiss();

        }

    }

    class UpdateModulos extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //ABRIMOS EL DIALOG DE ESPERA
            pDialog = new ProgressDialog(yo);
            pDialog.setMessage("Actualizando,esto puede durar unos minutos...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected String doInBackground(String... args) {
            //primero restauramos las tablas
            dbHandlerOffline = new AdminSQLiteOffline(Updates.this, null, null, 1);
            SQLiteDatabase db = dbHandlerOffline.getWritableDatabase();

            for (Integer modulo : modulosParaActulizar) {
                Log.e("MODULOSQUEVIENEN", modulo + "");
                if (modulo == 1) { //Eventos de acompañantes
                    dbHandlerOffline.resetAcompanantes();
                    resFinal += updateAcompanate();
                } else if (modulo == 4) { // Expositores
                    dbHandlerOffline.resetExpositores();
                    resFinal += updateExpositores();
                } else if (modulo == 5) { // Patrocinadores
                    dbHandlerOffline.resetPatrocinadores();
                    resFinal += updatePatrocinadores();
                } else if (modulo == 6) { //Programas.
                    dbHandlerOffline.resetProgramas();
                    resFinal += updateCategorias();
                    resFinal += updateProgramas();
                } else if (modulo == 7) { // Rutas / Transportación
                    dbHandlerOffline.resetTransportacion();
                    resFinal += updateTransportacion();
                } else if (modulo == 8) { // Eventos Sociales y Deportivos
                    dbHandlerOffline.resetSocialDepo();
                    resFinal += updateSocialDepo();
                } else if (modulo == 9) { // Conoce Puebla, teléfonos
                    // code son fijos
                } else if (modulo == 10) { // Conoce Puebla, sitios interes
                    dbHandlerOffline.resetSitiosInt();
                    resFinal += updateSitiosPuebla();
                }
            }

            Log.e("RESFINAL", resFinal);
            return resFinal;
        }

        @Override
        protected void onPostExecute(String str) {
            //CERRAMOS EL DIALOG E ESPERA
            pDialog.dismiss();
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (resFinal.equals("")) {
                        // refrescamos las actualizaciones una vez realizadas
                        numModulos = 0;
                        descActualizaciones.setText("No hay actualizaciones por el momento");
                        btnActualizar.setText("Cerrar");
                        Toast.makeText(yo, "Actualizacion finalizada", Toast.LENGTH_SHORT).show();

                        SharedPreferences sp = getSharedPreferences("prefe", Activity.MODE_PRIVATE);
                        SharedPreferences.Editor editor = sp.edit();
                        editor.putString("ERROR-UPDATE", "0");

                        editor.commit();
                        errorUpate = "0";

                        if (pDialog.isShowing()) {
                            pDialog.dismiss();
                        }

                    } else {

                        String str = "No se actualizó correctamente \n verifique su conexión de internet \n  e intentelo de nuevo.";
                        btnActualizar.setText("Cerar");
                        numModulos = 0;
                        Toast.makeText(yo, str, Toast.LENGTH_LONG).show();


                        SharedPreferences sp = getSharedPreferences("prefe", Activity.MODE_PRIVATE);
                        SharedPreferences.Editor editor = sp.edit();
                        editor.putString("ERROR-UPDATE", "1");

                        editor.commit();
                        errorUpate = "1";
                        if (pDialog.isShowing()) {
                            pDialog.dismiss();
                        }
                    }
                }
            });

        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        pDialog.dismiss();
    }

    public static String getBytes(Bitmap bitmap) {
        ByteArrayOutputStream blob = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 0 /* Ignored for PNGs */, blob);
        byte[] bitmapdata = blob.toByteArray();

        String encodedImage = Base64.encodeToString(bitmapdata, Base64.DEFAULT);

        return encodedImage;
    }

    // metodos para actualizar secciones
    public String updateAcompanate() {
        String resolucion = "";
        String respuesta = "";
        String bodyAco = "http://cmp.devworms.com/api/acompanantes/all/" + userId + "/" + apiKey + "";
        JSONParser jspAco = new JSONParser();
        String respuestaAco = jspAco.makeHttpRequest(bodyAco, "GET", bodyAco, "");
        if (respuesta != "error") {
            dbHandlerOffline = new AdminSQLiteOffline(Updates.this, null, null, 1);
            dbHandlerOffline.addAcomp(respuestaAco);
            JSONObject jsonimageA = null;
            try {
                jsonimageA = new JSONObject(respuestaAco);
                String acomp = "";
                acomp = jsonimageA.getString("acompanantes");
                JSONArray jsonAcoimage = null;
                jsonAcoimage = new JSONArray(acomp);
                for (int i = 0; i < jsonAcoimage.length(); i++) {
                    JSONObject c = null;
                    c = jsonAcoimage.getJSONObject(i);


                    String urlImageJson = c.getString("foto");
                    if (urlImageJson.equals("[]")) {

                    } else {
                        JSONObject jsonImagen = new JSONObject(urlImageJson);
                        urlImage = jsonImagen.getString("url");

                        imageUrl = new URL(urlImage);
                        conn = (HttpURLConnection) imageUrl.openConnection();
                        conn.connect();

                        BitmapFactory.Options options = new BitmapFactory.Options();
                        options.inSampleSize = 2; // el factor de escala a minimizar la imagen, siempre es potencia de 2

                        expoImg = BitmapFactory.decodeStream(conn.getInputStream(), new Rect(0, 0, 0, 0), options);
                        ImageDecoExpo = getBytes(expoImg);

                        dbHandlerOffline = new AdminSQLiteOffline(Updates.this, null, null, 1);
                        SQLiteDatabase dbP = dbHandlerOffline.getWritableDatabase();

                        String idAcoImagen = c.getString("id");

                        dbHandlerOffline.addAcoImag(idAcoImagen, ImageDecoExpo);
                    }

                }
            } catch (JSONException e) {
                e.printStackTrace();
                resolucion += "x";
            } catch (MalformedURLException e) {
                resolucion += "x";
                e.printStackTrace();
            } catch (IOException e) {
                resolucion += "x";
                e.printStackTrace();
            } catch (NullPointerException n) {
                resolucion += "x";
            }

        } else {
            resolucion += "x";
        }

        return resolucion;
    }

    public String updateExpositores() {
        String resolucion = "";
        String body = "";
        String bodyExpo = "http://cmp.devworms.com/api/expositor/order/name/" + userId + "/" + apiKey + "";

        JSONParser jspExpo = new JSONParser();


        String respuestaAlfa = jspExpo.makeHttpRequest(bodyExpo, "GET", bodyExpo, "");

        if (respuestaAlfa != "error") {


        } else {
            resp = "No hay resultados";
        }

        bodyExpo = "http://cmp.devworms.com/api/expositor/order/stand/" + userId + "/" + apiKey + "";
        JSONParser jspNumerico = new JSONParser();


        String respuestaNumerico = jspNumerico.makeHttpRequest(bodyExpo, "GET", body, "");

        if (respuestaNumerico != "error") {


        }
        dbHandlerOffline = new AdminSQLiteOffline(Updates.this, null, null, 1);
        SQLiteDatabase dbExpo = dbHandlerOffline.getWritableDatabase();

        dbHandlerOffline.addExpo(respuestaAlfa, respuestaNumerico);
        try {
            JSONObject json = new JSONObject(respuestaAlfa);
            String exposi = "";

            exposi = json.getString("expositores");


            JSONArray jsonExpositores = new JSONArray(exposi);

            int cuanto = jsonExpositores.length();

            for (int i = 0; i < jsonExpositores.length(); i++) {
                JSONObject c = jsonExpositores.getJSONObject(i);


                String urlImageJson = c.getString("logo");
                if (urlImageJson.equals("[]")) {
                    //resolucion += "x";
                } else {
                    JSONObject jsonImagen = new JSONObject(urlImageJson);
                    urlImage = jsonImagen.getString("url");

                    imageUrl = new URL(urlImage);
                    conn = (HttpURLConnection) imageUrl.openConnection();
                    conn.connect();

                    BitmapFactory.Options options = new BitmapFactory.Options();
                    options.inSampleSize = 2; // el factor de escala a minimizar la imagen, siempre es potencia de 2

                    expoImg = BitmapFactory.decodeStream(conn.getInputStream(), new Rect(0, 0, 0, 0), options);
                    ImageDecoExpo = getBytes(expoImg);

                    dbHandlerOffline = new AdminSQLiteOffline(Updates.this, null, null, 1);
                    SQLiteDatabase db = dbHandlerOffline.getWritableDatabase();

                    String idExpoImagen = c.getString("id");

                    dbHandlerOffline.addExpoImag(idExpoImagen, ImageDecoExpo);
                }
            }


        } catch (JSONException e) {
            resolucion += "x";
            e.printStackTrace();
        } catch (MalformedURLException e) {
            resolucion += "x";
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
            resolucion += "x";
        } catch (NullPointerException n) {
            resolucion += "x";
        }

        return resolucion;
    }

    public String updateCategorias() {
        String resolucion = "";
        try {
            String fromUrlCat = "http://cmp.devworms.com/api/categoria/all/" + userId + "/" + apiKey + "";
            JSONParser jspCat = new JSONParser();
            String strCat = jspCat.makeHttpRequest(fromUrlCat, "GET", fromUrlCat, "");
            dbHandlerOffline = new AdminSQLiteOffline(Updates.this, null, null, 1);
            dbHandlerOffline.addJsonCategorias(strCat);
            Log.d("Descarga: ", "> Datos Programa completo");
        } catch (Exception ex) {
            resolucion += "x";
        }
        return resolucion;
    }

    public String updateTransportacion() {
        String resolucion = "";
        String bodyT = "";


        bodyT = "http://cmp.devworms.com/api/ruta/all/" + userId + "/" + apiKey + "";

        JSONParser jspT = new JSONParser();


        String respuestaT = jspT.makeHttpRequest(bodyT, "GET", bodyT, "");
        Log.e("Tabla Trans", respuestaT);

        if (respuestaT != "error") {

            dbHandlerOffline = new AdminSQLiteOffline(Updates.this, null, null, 1);
            dbHandlerOffline.addTrans(respuestaT);

            try {


                JSONObject jsonimageT = null;

                jsonimageT = new JSONObject(respuestaT);

                String ruta = "";

                ruta = jsonimageT.getString("rutas");


                JSONArray jsonSDimage = null;
                jsonSDimage = new JSONArray(ruta);

                for (int i = 0; i < jsonSDimage.length(); i++) {
                    JSONObject c = null;
                    try {
                        c = jsonSDimage.getJSONObject(i);

                        String urlImageJson = c.getString("image");
                        if (urlImageJson.equals("[]")) {
                            //resolucion += "x";
                        } else {
                            JSONObject jsonImagen = new JSONObject(urlImageJson);
                            urlImage = jsonImagen.getString("url");

                            imageUrl = new URL(urlImage);
                            conn = (HttpURLConnection) imageUrl.openConnection();
                            conn.connect();

                            BitmapFactory.Options options = new BitmapFactory.Options();
                            options.inSampleSize = 2; // el factor de escala a minimizar la imagen, siempre es potencia de 2

                            expoImg = BitmapFactory.decodeStream(conn.getInputStream(), new Rect(0, 0, 0, 0), options);
                            ImageDecoExpo = getBytes(expoImg);

                            dbHandlerOffline = new AdminSQLiteOffline(Updates.this, null, null, 1);
                            SQLiteDatabase dbP = dbHandlerOffline.getWritableDatabase();

                            String idTRImagen = c.getString("id");

                            dbHandlerOffline.addRutaImag(idTRImagen, ImageDecoExpo);
                        }
                    } catch (JSONException e) {
                        resolucion += "x";
                        e.printStackTrace();
                    } catch (MalformedURLException e) {
                        resolucion += "x";
                        e.printStackTrace();
                    } catch (IOException e) {
                        resolucion += "x";
                        e.printStackTrace();
                    } catch (NullPointerException n) {
                        resolucion += "x";
                    }
                }


            } catch (JSONException e) {
                resolucion += "x";
                e.printStackTrace();
            }
        }
        return resolucion;
    }

    public String updateSocialDepo() {
        String resolucion = "";
        String bodySD = "";


        bodySD = "http://cmp.devworms.com/api/deportivos/all/" + userId + "/" + apiKey + "";


        JSONParser jspSoci = new JSONParser();


        String respuestaSocialDepo = jspSoci.makeHttpRequest(bodySD, "GET", bodySD, "");

        if (respuestaSocialDepo != "error") {
            dbHandlerOffline = new AdminSQLiteOffline(Updates.this, null, null, 1);
            dbHandlerOffline.addSocialDepo(respuestaSocialDepo);

            JSONObject jsonimageS = null;

            try {
                jsonimageS = new JSONObject(respuestaSocialDepo);
                String socialdepo = "";
                socialdepo = jsonimageS.getString("eventos");
                JSONArray jsonSDimage = null;
                jsonSDimage = new JSONArray(socialdepo);

                for (int i = 0; i < jsonSDimage.length(); i++) {
                    JSONObject c = null;
                    c = jsonSDimage.getJSONObject(i);


                    String urlImageJson = c.getString("foto");
                    if (urlImageJson.equals("[]")) {
                        //resolucion += "x";
                    } else {
                        JSONObject jsonImagen = new JSONObject(urlImageJson);
                        urlImage = jsonImagen.getString("url");

                        imageUrl = new URL(urlImage);
                        conn = (HttpURLConnection) imageUrl.openConnection();
                        conn.connect();

                        BitmapFactory.Options options = new BitmapFactory.Options();
                        options.inSampleSize = 2; // el factor de escala a minimizar la imagen, siempre es potencia de 2

                        expoImg = BitmapFactory.decodeStream(conn.getInputStream(), new Rect(0, 0, 0, 0), options);
                        ImageDecoExpo = getBytes(expoImg);

                        dbHandlerOffline = new AdminSQLiteOffline(Updates.this, null, null, 1);
                        SQLiteDatabase dbP = dbHandlerOffline.getWritableDatabase();

                        String idSDImagen = c.getString("id");

                        dbHandlerOffline.addSociaDepoImag(idSDImagen, ImageDecoExpo);
                    }

                }
            } catch (JSONException e) {
                resolucion += "x";
                e.printStackTrace();
            } catch (MalformedURLException e) {
                resolucion += "x";
                e.printStackTrace();
            } catch (IOException e) {
                resolucion += "x";
                e.printStackTrace();
            } catch (NullPointerException n) {
                resolucion += "x";
            }


        } else {
            resolucion += "x";
        }

        return resolucion;
    }

    public String updateProgramas() {
        String resolucion = "";
        String fromUrlPrograma = "http://cmp.devworms.com/api/programa/all/" + userId + "/" + apiKey + "";
        JSONParser jspPrograma = new JSONParser();
        String strProgramas = jspPrograma.makeHttpRequest(fromUrlPrograma, "GET", fromUrlPrograma, "");
        dbHandlerOffline = new AdminSQLiteOffline(Updates.this, null, null, 1);
        Log.e("JSONPROGRAMAS", strProgramas);
        dbHandlerOffline.addJsonProgramas(strProgramas);
        try {
            JSONObject jProgramas = new JSONObject(strProgramas);
            String strProg = jProgramas.getString("programas");
            JSONArray aProgramas = new JSONArray(strProg);
            for (int c = 0; c < aProgramas.length(); c++) {
                try {
                    JSONObject programa = aProgramas.getJSONObject(c);

                    String strFoto = programa.getString("foto");
                    if (strFoto.equals("[]")) {
                        // resolucion += "x";
                    } else {

                        JSONObject jFoto = new JSONObject(strFoto);

                        urlImage = jFoto.getString("url");

                        imageUrl = new URL(urlImage);
                        conn = (HttpURLConnection) imageUrl.openConnection();
                        conn.connect();

                        BitmapFactory.Options options = new BitmapFactory.Options();
                        options.inSampleSize = 2; // el factor de escala a minimizar la imagen, siempre es potencia de 2

                        expoImg = BitmapFactory.decodeStream(conn.getInputStream(), new Rect(0, 0, 0, 0), options);
                        ImageDecoExpo = getBytes(expoImg);

                        dbHandlerOffline = new AdminSQLiteOffline(Updates.this, null, null, 1);
                        SQLiteDatabase dbP = dbHandlerOffline.getWritableDatabase();

                        String idProgImge = programa.getString("id");

                        dbHandlerOffline.addProgramaImage(idProgImge, ImageDecoExpo);
                    }

                } catch (JSONException e) {
                    resolucion += "x";
                    e.printStackTrace();
                } catch (MalformedURLException e) {
                    resolucion += "x";
                    e.printStackTrace();
                } catch (IOException e) {
                    resolucion += "x";
                    e.printStackTrace();
                } catch (NullPointerException n) {
                    resolucion += "x";
                }
            }
        } catch (JSONException jex) {
            resolucion += "x";
            Log.e("DescargaPrograma", jex.getMessage());
        } catch (NullPointerException n) {
            resolucion += "x";
        }
        return resolucion;
    }

    public String updatePatrocinadores() {
        String resolucion = "";
        String bodyPatro = "";
        String respuestaAlfa = "";
        String respuestaNumerico = "";

        bodyPatro = "http://cmp.devworms.com/api/patrocinador/order/name/" + userId + "/" + apiKey + "";


        JSONParser jspPatro = new JSONParser();


        String respuestaAlfaPatro = jspPatro.makeHttpRequest(bodyPatro, "GET", bodyPatro, "");


        bodyPatro = "http://cmp.devworms.com/api/patrocinador/order/stand/" + userId + "/" + apiKey + "";
        JSONParser jspNumericoPatro = new JSONParser();


        String respuestaNumericoPatro = jspNumericoPatro.makeHttpRequest(bodyPatro, "GET", bodyPatro, "");


        dbHandlerOffline = new AdminSQLiteOffline(Updates.this, null, null, 1);
        SQLiteDatabase db = dbHandlerOffline.getWritableDatabase();
        dbHandlerOffline.addPatro(respuestaAlfaPatro, respuestaNumericoPatro);

        JSONObject jsonimageP = null;
        try {
            jsonimageP = new JSONObject(respuestaAlfaPatro);
            String patro = "";
            patro = jsonimageP.getString("patrocinadores");
            JSONArray jsonPatroimage = null;
            jsonPatroimage = new JSONArray(patro);
            for (int i = 0; i < jsonPatroimage.length(); i++) {
                JSONObject c = null;
                c = jsonPatroimage.getJSONObject(i);


                String urlImageJson = c.getString("logo");
                if (urlImageJson.equals("[]")) {
                    //resolucion += "3x";
                } else {
                    JSONObject jsonImagen = new JSONObject(urlImageJson);
                    urlImage = jsonImagen.getString("url");

                    imageUrl = new URL(urlImage);
                    conn = (HttpURLConnection) imageUrl.openConnection();
                    conn.connect();

                    BitmapFactory.Options options = new BitmapFactory.Options();
                    options.inSampleSize = 2; // el factor de escala a minimizar la imagen, siempre es potencia de 2

                    expoImg = BitmapFactory.decodeStream(conn.getInputStream(), new Rect(0, 0, 0, 0), options);
                    ImageDecoExpo = getBytes(expoImg);

                    dbHandlerOffline = new AdminSQLiteOffline(Updates.this, null, null, 1);
                    SQLiteDatabase dbP = dbHandlerOffline.getWritableDatabase();

                    String idExpoImagen = c.getString("id");

                    dbHandlerOffline.addPatroImag(idExpoImagen, ImageDecoExpo);
                }

            }
        } catch (JSONException e) {
            resolucion += "0x";
            e.printStackTrace();
        } catch (NullPointerException n) {
            resolucion += "x";
        } catch (MalformedURLException e) {
            resolucion += "5x";
            e.printStackTrace();
        } catch (IOException e) {
            resolucion += "6x";
            e.printStackTrace();
        }


        return resolucion;
    }

    public String updateSitiosPuebla() {
        String resolucion = "";
        String fromUrlSitio = "http://cmp.devworms.com/api/puebla/sitio/all/" + userId + "/" + apiKey + "";
        JSONParser jspSitio = new JSONParser();
        String strSitio = jspSitio.makeHttpRequest(fromUrlSitio, "GET", fromUrlSitio, "");
        dbHandlerOffline = new AdminSQLiteOffline(Updates.this, null, null, 1);

        dbHandlerOffline.addJsonSitiosPuebla(strSitio);
        try {
            JSONObject jSitios = new JSONObject(strSitio);
            String strSit = jSitios.getString("sitios");
            JSONArray aSitios = new JSONArray(strSit);
            for (int c = 0; c < aSitios.length(); c++) {
                try {
                    JSONObject sitio = aSitios.getJSONObject(c);

                    String strFoto = sitio.getString("imagen");
                    if (strFoto.equals("[]")) {
                        //resolucion += "x";
                    } else {

                        JSONObject jFoto = new JSONObject(strFoto);

                        urlImage = jFoto.getString("url");

                        imageUrl = new URL(urlImage);
                        conn = (HttpURLConnection) imageUrl.openConnection();
                        conn.connect();

                        BitmapFactory.Options options = new BitmapFactory.Options();
                        options.inSampleSize = 2; // el factor de escala a minimizar la imagen, siempre es potencia de 2

                        expoImg = BitmapFactory.decodeStream(conn.getInputStream(), new Rect(0, 0, 0, 0), options);
                        ImageDecoExpo = getBytes(expoImg);

                        dbHandlerOffline = new AdminSQLiteOffline(Updates.this, null, null, 1);
                        SQLiteDatabase dbP = dbHandlerOffline.getWritableDatabase();

                        String idProgImge = sitio.getString("id");

                        dbHandlerOffline.addImgeSitio(idProgImge, ImageDecoExpo);
                    }

                } catch (JSONException e) {
                    resolucion += "x";
                    e.printStackTrace();
                } catch (MalformedURLException e) {
                    resolucion += "x";
                    e.printStackTrace();
                } catch (IOException e) {
                    resolucion += "x";
                    e.printStackTrace();
                } catch (NullPointerException n) {
                    resolucion += "x";
                }
            }
        } catch (JSONException jex) {
            resolucion += "x";
            Log.e("DescargaPrograma", jex.getMessage());
        } catch (NullPointerException n) {
            resolucion += "x";
        }
        return resolucion;
    }

    public void updateBanners() {
        String body = "http://cmp.devworms.com/api/banners/all/" + userId + "/" + apiKey + "";
        JSONParser jsp = new JSONParser();
        String respuesta = "";
        respuesta = jsp.makeHttpRequest(body, "GET", body, "");
        if (respuesta != "error") {
            try {

                JSONObject json = new JSONObject(respuesta);
                String banners = "";

                banners = json.getString("banners");

                JSONArray jsonBanner = new JSONArray(banners);

                ArrayBanners = new String[jsonBanner.length()];

                // looping through All albums
                for (int i = 0; i < jsonBanner.length(); i++) {
                    JSONObject c = jsonBanner.getJSONObject(i);
                    // creating new HashMap
                    ArrayBanners[i] = c.getString("url");

                }
                try {
                    imagen = new Bitmap[ArrayBanners.length];
                    ImagenDeco = new String[ArrayBanners.length];

                    for (int cou = 0; cou < imagen.length; cou++) {
                        imageUrl = new URL(ArrayBanners[cou]);
                        conn = (HttpURLConnection) imageUrl.openConnection();
                        conn.connect();

                        BitmapFactory.Options options = new BitmapFactory.Options();
                        options.inSampleSize = 2; // el factor de escala a minimizar la imagen, siempre es potencia de 2

                        imagen[cou] = BitmapFactory.decodeStream(conn.getInputStream(), new Rect(0, 0, 0, 0), options);
                        ImagenDeco[cou] = getBytes(imagen[cou]);

                        dbHandlerOffline = new AdminSQLiteOffline(Updates.this, null, null, 1);
                        SQLiteDatabase db = dbHandlerOffline.getWritableDatabase();

                        dbHandlerOffline.addBanner(ArrayBanners[cou], ImagenDeco[cou]);

                    }


                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (NullPointerException n) {
                    Log.e("NULO", n.getMessage());
                }


            } catch (JSONException e) {
                e.printStackTrace();

            } catch (NullPointerException n) {
                Log.e("NULO", n.getMessage());
            }
        }


    }

    public void updateMapas() {
        String fromMapa = "http://cmp.devworms.com/api/mapa/recinto/" + userId + "/" + apiKey;
        JSONParser jpMapa = new JSONParser();

        String rspmapa = jpMapa.makeHttpRequest(fromMapa, "GET", fromMapa, "");
        try {

            JSONObject jMapa = new JSONObject(rspmapa);
            JSONObject jResinto = new JSONObject(jMapa.getString("mapa"));
            String urlResinto = jResinto.getString("url");
            urlImage = urlResinto;

            imageUrl = new URL(urlImage);
            conn = (HttpURLConnection) imageUrl.openConnection();
            conn.connect();

            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inSampleSize = 2; // el factor de escala a minimizar la imagen, siempre es potencia de 2

            expoImg = BitmapFactory.decodeStream(conn.getInputStream(), new Rect(0, 0, 0, 0), options);
            ImageDecoExpo = getBytes(expoImg);

            dbHandlerOffline = new AdminSQLiteOffline(Updates.this, null, null, 1);
            SQLiteDatabase dbP = dbHandlerOffline.getWritableDatabase();


            dbHandlerOffline.addMapaResinto(ImageDecoExpo);
        } catch (JSONException jex) {
            Log.e("Mapa", jex.getMessage());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (NullPointerException n) {
            Log.e("NULO", n.getMessage());
        }
    }

}
