package com.cmp2017.devworms.cmp2017;

import android.app.Activity;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

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

public class PDFFragment extends Fragment

{
    ImageView imgRuta;
    String url, idRuta;
    Cursor cursor;
    int posJson;
    Bitmap imagen;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_pdffragment, container, false);
        url =  getArguments().getString("url");
        imgRuta = (ImageView) view.findViewById(R.id.imgRuta);
        posJson = getArguments().getInt("posicion");

        new LoadSingleTrack().execute();
        return view;


    }
    class LoadSingleTrack extends AsyncTask<String, String, String> {

        /**
         * Before starting background thread Show Progress Dialog
         * */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        /**
         * getting song json and parsing
         * */
        protected String doInBackground(String... args) {

            AdminSQLiteOffline dbHandler;

                    dbHandler = new AdminSQLiteOffline(getActivity(), null, null, 1);
                    SQLiteDatabase db = dbHandler.getWritableDatabase();
                    cursor = dbHandler.jsonTrans();


            String respuesta = cursor.getString(0);

            Log.d("LoginRes : ", "> " + respuesta);
            if (respuesta != "error") {
                try {
                    JSONObject json = new JSONObject(respuesta);
                    String ruta = "";

                    ruta = json.getString("rutas");



                    JSONArray jsonExpositores = new JSONArray(ruta);





                    JSONObject jsonExpo = jsonExpositores.getJSONObject(posJson);





                    ////////////////////////////////////////////


                    idRuta = jsonExpo.getString("id");


                    dbHandler = new AdminSQLiteOffline(getActivity(), null, null, 1);
                    SQLiteDatabase dbP = dbHandler.getWritableDatabase();


                        cursor = dbHandler.ImagenPorIdRuta(idRuta);


                    if(cursor.getCount()>0) {
                        imagen = getImage(cursor.getString(0));
                    }




                } catch (JSONException e) {
                    e.printStackTrace();
                }



            } else {

            }


            return null;
        }

        /**
         * After completing background task Dismiss the progress dialog
         * **/
        protected void onPostExecute(String file_url) {
            // dismiss the dialog after getting song information

            // updating UI from Background Thread
            getActivity().runOnUiThread(new Runnable() {
                public void run() {


                        imgRuta.setImageBitmap(imagen);




                }
            });


        }

    }

    // convert from bitmap to byte array
    public static String getBytes(Bitmap bitmap) {
        ByteArrayOutputStream blob = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 0 /* Ignored for PNGs */, blob);
        byte[] bitmapdata = blob.toByteArray();

        String encodedImage = Base64.encodeToString(bitmapdata, Base64.DEFAULT);
        //---------------
       /* ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);*/
        return encodedImage;
    }

    // convert from byte array to bitmap
    public static Bitmap getImage(String imageS) {

        byte[] b = Base64.decode(imageS , Base64.DEFAULT);
        //-----------------
        Bitmap bmp = BitmapFactory.decodeByteArray(b, 0, b.length);


        return  bmp;

    }

}
