package com.cmp2017.devworms.cmp2017;

import android.app.Activity;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.Base64;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.HashMap;

import static android.R.attr.bitmap;

public class DetalleExpoFragment extends Fragment {

    String userId, apiKey,nombreSec,resp,expoId,strNomEmpre,strStand,strPagina,strTelefono,strCorreo,strAcercaDe,urlImage,urlPresenta,misExpo,miExpo,tipoOrden;
    TextView txtNomEmpre, txtPagina,txtTelefono,txtCorreo, txtAcercaDe;
    ProgressDialog pDialog;
    ImageView imgFoto;
    URL imageUrl ;
    Bitmap imagen;
    View viewR;
    int cursorEncontrado,posJson;
    byte[] imageArray;
    HttpURLConnection conn;
    String inicioComo;
    Button btnLocalizar,btnAgreExpo,btnPresent;
    Cursor cursor;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_detalle_expo, container, false);
        viewR = view;
        SharedPreferences sp = getActivity().getSharedPreferences("prefe", Activity.MODE_PRIVATE);
        inicioComo = sp.getString("Nombre","");

        if (inicioComo.equals("invi")){
            apiKey = "0";
            userId = "1";
        } else{
            apiKey = sp.getString("APIkey","");
            userId = sp.getString("IdUser","");
        }
        expoId = getArguments().getString("expoId");
        misExpo = getArguments().getString("miExpositores");
        nombreSec = getArguments().getString("nombre");
        tipoOrden = getArguments().getString("tipooreden");
        posJson = getArguments().getInt("posicion");
        txtNomEmpre = (TextView)view.findViewById(R.id.txtNomEmpre);
        txtPagina = (TextView)view.findViewById(R.id.txtPaginaExpo);
        txtTelefono = (TextView)view.findViewById(R.id.txtTelefono);
        txtCorreo = (TextView)view.findViewById(R.id.txtEmail);
        txtAcercaDe = (TextView)view.findViewById(R.id.txtAcercaDe);
        imgFoto = (ImageView)view.findViewById(R.id.imgFoto);
        btnLocalizar = (Button)view.findViewById(R.id.btnLocalizar);
        btnLocalizar.setOnClickListener(new Localizar());
        btnAgreExpo = (Button)view.findViewById(R.id.btnAgreExpo);
        btnAgreExpo.setOnClickListener(new AgregarExpo());
        if(nombreSec.equals("Patrocinadores")){
            btnAgreExpo.setVisibility(View.INVISIBLE);
        }
        btnPresent = (Button)view.findViewById(R.id.btnPresent);
        btnPresent.setOnClickListener(new Presentacion());

        AdminSQLiteOpenHelper dbHandler;
        dbHandler = new AdminSQLiteOpenHelper(getActivity(), null, null, 1);
        SQLiteDatabase db = dbHandler.getWritableDatabase();
        Cursor cursor = dbHandler.personabyid(expoId);
        cursorEncontrado = cursor.getCount();

        if(cursorEncontrado == 1){
            btnAgreExpo.setBackground(getResources().getDrawable(R.drawable.btneliminarexpo));

        }

        if(misExpo.equals("si")){

            txtNomEmpre.setText(cursor.getString(2));
            txtAcercaDe.setText(cursor.getString(3));
            txtTelefono.setText(cursor.getString(4));
            txtCorreo.setText(cursor.getString(5));
            txtPagina.setText(cursor.getString(6));
            String imageF = cursor.getString(8);


            if(!imageF.equals("no")){
                Bitmap bmimage = getImage(imageF);
                imgFoto.setImageBitmap(bmimage);
            }



        }else {
            new getDetalle().execute();
        }
        return view;



    }
    class AgregarExpo implements View.OnClickListener {
        public void onClick(View v) {
            if (inicioComo.equals("invi")){
                Toast.makeText(getActivity(),"Registrate para activar esta función",Toast.LENGTH_SHORT).show();
            }else {
                if (cursorEncontrado == 1) {
                    EliminarExpoFav();
                } else {
                    AgregarExpoFav();
                }
            }
        }
    }
    class Presentacion implements View.OnClickListener {
        public void onClick(View v) {

            Toast.makeText(getActivity(),"Próximamente",Toast.LENGTH_SHORT).show();
            /*getFragmentManager().beginTransaction()
                    .replace(R.id.actividad, new MapaFragment()).addToBackStack(null).commit();*/


        }
    }
    class Localizar implements View.OnClickListener {
        public void onClick(View v) {

            Toast.makeText(getActivity(),"Próximamente",Toast.LENGTH_SHORT).show();
            /*getFragmentManager().beginTransaction()
                    .replace(R.id.actividad, new MapaFragment()).addToBackStack(null).commit();*/


        }
    }

   public void AgregarExpoFav(){
        AdminSQLiteOpenHelper dbHandler;
        dbHandler = new AdminSQLiteOpenHelper(getActivity(), null, null, 1);
        SQLiteDatabase db = dbHandler.getWritableDatabase();
       String imageString = "no";
       if(imagen != null) {
           imageString = getBytes(imagen);
       }

        dbHandler.addExpo(expoId,txtNomEmpre.getText().toString(),txtAcercaDe.getText().toString(),txtTelefono.getText().toString(),txtCorreo.getText().toString(),txtPagina.getText().toString(), urlPresenta, imageString,"Stand "+strStand);


       btnAgreExpo.setBackground(getResources().getDrawable(R.drawable.btneliminarexpo));
       cursorEncontrado = 1;
        Toast.makeText(getActivity(), "Se guardo en Mis expositores",
                Toast.LENGTH_SHORT).show();
    }

    public void EliminarExpoFav(){
        AdminSQLiteOpenHelper dbHandler;
        dbHandler = new AdminSQLiteOpenHelper(getActivity(), null, null, 1);
        SQLiteDatabase db = dbHandler.getWritableDatabase();
        String imageString = "no";
        if(imagen != null) {
            imageString = getBytes(imagen);
        }

        dbHandler.borrarPersona(expoId);

        btnAgreExpo.setBackground(getResources().getDrawable(R.drawable.btnagregarespo));
        cursorEncontrado = 0;
        Toast.makeText(getActivity(), "Se Elimino de Mis expositores",
                Toast.LENGTH_SHORT).show();
    }

    class getDetalle extends AsyncTask<String, String, String> {

        /**
         * Before starting background thread Show Progress Dialog
         */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(getActivity());
            pDialog.setMessage("Buscando");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        /**
         * getting Albums JSON
         */
        protected String doInBackground(String... args) {
            // Building Parameters
            //add your data
            //add your data


           /* String body = "http://cmp.devworms.com/api/expositor/detail/"+userId+"/"+apiKey+"/"+expoId+"";
            JSONParser jsp = new JSONParser();



            String respuesta= jsp.makeHttpRequest(body,"GET",body,"");*/
            AdminSQLiteOffline dbHandler;
            if (tipoOrden.equals("alfa")){
                if(nombreSec.equals("Expositores")) {
                    dbHandler = new AdminSQLiteOffline(getActivity(), null, null, 1);
                    SQLiteDatabase db = dbHandler.getWritableDatabase();
                    cursor = dbHandler.jsonAlfa();
                } else if(nombreSec.equals("Patrocinadores")){
                    dbHandler = new AdminSQLiteOffline(getActivity(), null, null, 1);
                    SQLiteDatabase db = dbHandler.getWritableDatabase();
                    cursor = dbHandler.jsonPatroAlfa();
                }
            }else if (tipoOrden.equals("nume")){
                if(nombreSec.equals("Expositores")) {
                    dbHandler = new AdminSQLiteOffline(getActivity(), null, null, 1);
                    SQLiteDatabase db = dbHandler.getWritableDatabase();
                    cursor = dbHandler.jsonNume();
                } else if(nombreSec.equals("Patrocinadores")){
                    dbHandler = new AdminSQLiteOffline(getActivity(), null, null, 1);
                    SQLiteDatabase db = dbHandler.getWritableDatabase();
                    cursor = dbHandler.jsonPatroNume();
                }
            }

            String respuesta = cursor.getString(0);

            Log.d("LoginRes : ", "> " + respuesta);
            if (respuesta != "error") {
                try {
                    JSONObject json = new JSONObject(respuesta);
                    String exposi = "";
                    if(nombreSec.equals("Expositores")){
                        exposi = json.getString("expositores");
                    } else if(nombreSec.equals("Patrocinadores")){
                        exposi = json.getString("patrocinadores");
                    }


                    JSONArray jsonExpositores = new JSONArray(exposi);

                    int cuanto = jsonExpositores.length();



                    JSONObject jsonExpo = jsonExpositores.getJSONObject(posJson);





                    ////////////////////////////////////////////


                    strNomEmpre = jsonExpo.getString("nombre");
                    strPagina = jsonExpo.getString("url");
                    strTelefono = jsonExpo.getString("telefono");
                    strCorreo = jsonExpo.getString("email");
                    strAcercaDe = jsonExpo.getString("acerca");
                    strStand = jsonExpo.getString("stand");

                    dbHandler = new AdminSQLiteOffline(getActivity(), null, null, 1);
                    SQLiteDatabase db = dbHandler.getWritableDatabase();
                    cursor = dbHandler.ImagenPorId(expoId);
                    if(cursor.getCount()>0) {
                        imagen = getImage(cursor.getString(0));
                    }
                    /*
                    String urlImageJson = jsonExpo.getString("logo");
                    JSONObject jsonImagen = new JSONObject(urlImageJson);
                    urlImage = jsonImagen.getString("url");
                    mostrarImagen(urlImage);*/

                    String urlPreseJson = jsonExpo.getString("pdf");
                    JSONObject jsonPrese = new JSONObject(urlPreseJson);
                    urlPresenta = jsonPrese.getString("url");



                } catch (JSONException e) {
                    e.printStackTrace();
                }
                resp = "ok";


            } else {
                resp = "No hay resultados";
            }


            return null;
        }


        /**
         * After completing background task Dismiss the progress dialog
         **/
        protected void onPostExecute(String file_url) {
            // dismiss the dialog after getting all albums
            Log.d("Login : ", "> " + resp);
            pDialog.dismiss();

            // updating UI from Background Thread
            getActivity().runOnUiThread(new Runnable() {
                public void run() {
                    if (resp.equals("ok")) {
                        txtNomEmpre.setText(strNomEmpre);
                        txtPagina.setText(strPagina);
                        txtTelefono.setText(strTelefono);
                        txtCorreo.setText(strCorreo);
                        txtAcercaDe.setText(strAcercaDe);
                        imgFoto.setImageBitmap(imagen);
                    }



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

    public void mostrarImagen(String url){


        try {

            imageUrl = new URL(url);
            conn = (HttpURLConnection) imageUrl.openConnection();
            conn.connect();

            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inSampleSize = 2; // el factor de escala a minimizar la imagen, siempre es potencia de 2

            imagen = BitmapFactory.decodeStream(conn.getInputStream(), new Rect(0, 0, 0, 0), options);

            getActivity().runOnUiThread(new Runnable() {
                public void run() {

                    imgFoto.setImageBitmap(imagen);

                }
            });


        } catch (IOException e) {

            e.printStackTrace();

        }
    }
}
