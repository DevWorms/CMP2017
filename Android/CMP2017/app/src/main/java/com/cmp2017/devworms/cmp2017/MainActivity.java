package com.cmp2017.devworms.cmp2017;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.SimpleAdapter;

import com.amazonaws.AmazonClientException;
import com.cmp2017.devworms.cmp2017.mobile.AWSMobileClient;
import com.cmp2017.devworms.cmp2017.mobile.push.PushManager;
import com.loopj.android.http.Base64;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity {


    private PushManager pushManager;

    String resp, userId, apiKey, urlImage, ImageDecoExpo;
    ProgressDialog pDialog;
    ImageView imgFoto;
    URL imageUrl;
    Bitmap[] imagen;
    Bitmap expoImg;
    HttpURLConnection conn;
    String[] ArrayBanners,ImagenDeco;
    ImageView imageAnim;
    View viewBanner;

    AdminSQLiteOffline dbHandlerOffline;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        SharedPreferences sp = getSharedPreferences("prefe", Activity.MODE_PRIVATE);
       String nombre = sp.getString("APIkey","");
        apiKey = "0";
        userId = "1";

        if(nombre != ""){
            Intent intent = new Intent(getApplicationContext(), MenuPrincipal.class);
            intent.putExtra("parametro", "no");
            startActivity(intent);
        }

        new getDescaragaDatosOffline().execute();


        Button btnIniSesion = (Button) findViewById(R.id.btnIniSe);
        btnIniSesion.setOnClickListener(new IniciSesion());

        Button btnIniInv = (Button) findViewById(R.id.btnIngreInvi);
        btnIniInv.setOnClickListener(new IniciInvi());

        // Obtain a reference to the mobile client. It is created in the Application class,
        // but in case a custom Application class is not used, we initialize it here if necessary.
        AWSMobileClient.initializeMobileClientIfNecessary(this);

        pushManager = AWSMobileClient.defaultMobileClient().getPushManager();

        new AsyncTask<Void, Void, String>() {
            @Override
            protected String doInBackground(final Void... params) {
                // register device first to ensure we have a push endpoint.
                pushManager.registerDevice();

                // if registration succeeded.
                if (pushManager.isRegistered()) {
                    try {
                        pushManager.setPushEnabled(true);
                        // Automatically subscribe to the default SNS topic
                        if (true) {
                            pushManager.subscribeToTopic(pushManager.getDefaultTopic());
                        }
                        return null;
                    } catch (final AmazonClientException ace) {
                        Log.e("erro", "Failed to change push notification status", ace);
                        return ace.getMessage();
                    }
                }
                return "Failed to register for push notifications.";
            }

            @Override
            protected void onPostExecute(final String errorMessage) {
                /*dialog.dismiss();
                topicsAdapter.notifyDataSetChanged();
                enablePushCheckBox.setChecked(pushManager.isPushEnabled());

                if (errorMessage != null) {
                    showErrorMessage(R.string.push_demo_error_message_update_notification,
                            errorMessage);
                }*/
            }
        }.execute();


    }

    class IniciSesion implements View.OnClickListener {
        public void onClick(View v) {


            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
            startActivity(intent);






        }
    }

    class IniciInvi implements View.OnClickListener {
        public void onClick(View v) {


            SharedPreferences sp = getSharedPreferences("prefe", Activity.MODE_PRIVATE);
            SharedPreferences.Editor editor = sp.edit();

            editor.putString("Nombre", "invi");


            editor.commit();
            Intent intent = new Intent(getApplicationContext(), MenuPrincipal.class);
            startActivity(intent);





        }
    }

    class getDescaragaDatosOffline extends AsyncTask<String, String, String> {

        /**
         * Before starting background thread Show Progress Dialog
         */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(MainActivity.this);
            pDialog.setMessage("Descargando Datos CMP...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        /**
         * getting Albums JSON
         */
        protected String doInBackground(String... args) {
            String body= "";

////////////////////////////////////////////////////Descarga de Datos Banner

            body = "http://cmp.devworms.com/api/banners/all/"+userId+"/"+apiKey+"";


            JSONParser jsp = new JSONParser();
            SharedPreferences sp = getSharedPreferences("prefe", Activity.MODE_PRIVATE);

            SharedPreferences.Editor editor = sp.edit();

            String respuesta= "";

            respuesta = jsp.makeHttpRequest(body, "GET", body, "");

            Log.d("LoginRes : ", "> " + respuesta);
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

                            dbHandlerOffline = new AdminSQLiteOffline(MainActivity.this, null, null, 1);
                            SQLiteDatabase db = dbHandlerOffline.getWritableDatabase();
                            Log.d("Imagen deco : ", "> " + ImagenDeco[cou]);
                            dbHandlerOffline.addBanner(ArrayBanners[cou],ImagenDeco[cou]);

                        }


                    } catch (MalformedURLException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    resp = "ok";


                } catch (JSONException e) {
                    e.printStackTrace();

                }
            }
//////////////////////////////////////// Descarga de Datos Expositores
            String bodyExpo= "";


            bodyExpo = "http://cmp.devworms.com/api/expositor/order/name/"+userId+"/"+apiKey+"";



            JSONParser jspExpo = new JSONParser();



            String respuestaAlfa= jspExpo.makeHttpRequest(bodyExpo,"GET",bodyExpo,"");
            Log.d("RespuestaALfa : ", "> " + respuestaAlfa);
            if (respuestaAlfa != "error") {


            } else {
                resp = "No hay resultados";
            }
            bodyExpo = "http://cmp.devworms.com/api/expositor/order/stand/"+userId+"/"+apiKey+"";
            JSONParser jspNumerico = new JSONParser();



            String respuestaNumerico= jspNumerico.makeHttpRequest(bodyExpo,"GET",body,"");
            Log.d("RespuestaNumerico : ", "> " + respuestaNumerico);
            if (respuestaNumerico != "error") {


            }
            dbHandlerOffline = new AdminSQLiteOffline(MainActivity.this, null, null, 1);
            SQLiteDatabase dbExpo = dbHandlerOffline.getWritableDatabase();

            dbHandlerOffline.addExpo(respuestaAlfa,respuestaNumerico);
            try {
                JSONObject json = new JSONObject(respuestaAlfa);
                String exposi = "";

                    exposi = json.getString("expositores");



                JSONArray jsonExpositores = new JSONArray(exposi);

                int cuanto = jsonExpositores.length();

                for (int i = 0; i <= jsonExpositores.length(); i++) {
                    JSONObject c = jsonExpositores.getJSONObject(i);


                    String urlImageJson = c.getString("logo");
                    JSONObject jsonImagen = new JSONObject(urlImageJson);
                    urlImage = jsonImagen.getString("url");

                    imageUrl = new URL(urlImage);
                    conn = (HttpURLConnection) imageUrl.openConnection();
                    conn.connect();

                    BitmapFactory.Options options = new BitmapFactory.Options();
                    options.inSampleSize = 2; // el factor de escala a minimizar la imagen, siempre es potencia de 2

                    expoImg = BitmapFactory.decodeStream(conn.getInputStream(), new Rect(0, 0, 0, 0), options);
                    ImageDecoExpo = getBytes(expoImg);

                    dbHandlerOffline = new AdminSQLiteOffline(MainActivity.this, null, null, 1);
                    SQLiteDatabase db = dbHandlerOffline.getWritableDatabase();

                    String idExpoImagen = c.getString("id");

                    dbHandlerOffline.addExpoImag(idExpoImagen,ImageDecoExpo);
                }







                ////////////////////////////////////////////








            } catch (JSONException e) {
                e.printStackTrace();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }


////////////////////////////////////////////// Descarga de Datos Patrocinadores

            String bodyPatro= "";


            bodyPatro = "http://cmp.devworms.com/api/patrocinador/order/name/"+userId+"/"+apiKey+"";



            JSONParser jspPatro = new JSONParser();



            String respuestaAlfaPatro= jspPatro.makeHttpRequest(bodyPatro,"GET",bodyPatro,"");
            Log.d("RespuestaALfa : ", "> " + respuestaAlfaPatro);
            if (respuestaAlfa != "error") {


            } else {
                resp = "No hay resultados";
            }
            bodyPatro = "http://cmp.devworms.com/api/patrocinador/order/stand/"+userId+"/"+apiKey+"";
            JSONParser jspNumericoPatro = new JSONParser();



            String respuestaNumericoPatro= jspNumericoPatro.makeHttpRequest(bodyPatro,"GET",bodyPatro,"");
            Log.d("RespuestaNumerico : ", "> " + respuestaNumerico);
            if (respuestaNumerico != "error") {


            }

            dbHandlerOffline = new AdminSQLiteOffline(MainActivity.this, null, null, 1);
            SQLiteDatabase db = dbHandlerOffline.getWritableDatabase();
            dbHandlerOffline.addPatro(respuestaAlfaPatro,respuestaNumericoPatro);

            /************************************************ DESCARGAR DATOS PROGRAMA +*************************/
            String fromUrlCat= "http://cmp.devworms.com/api/categoria/all/"+userId+"/"+apiKey+"";
            JSONParser jspPrograma = new JSONParser();
            String strProgramas = jspPrograma.makeHttpRequest(fromUrlCat,"GET",fromUrlCat,"");
            dbHandlerOffline = new AdminSQLiteOffline(MainActivity.this, null, null, 1);
            dbHandlerOffline.addJsonCategorias(strProgramas);

            return null;
        }


        /**
         * After completing background task Dismiss the progress dialog
         **/
        protected void onPostExecute(String file_url) {
            // dismiss the dialog after getting all albums
            Log.d("Login : ", "> " + resp);
            pDialog.dismiss();


        }
    }



    public static String getBytes(Bitmap bitmap) {
        ByteArrayOutputStream blob = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 0 /* Ignored for PNGs */, blob);
        byte[] bitmapdata = blob.toByteArray();

        String encodedImage = Base64.encodeToString(bitmapdata, Base64.DEFAULT);
        //---------------

        return encodedImage;
    }
}
