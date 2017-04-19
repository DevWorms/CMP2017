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
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
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
import java.util.ArrayList;
import java.util.HashMap;

public class DetalleEventoFragment extends Fragment {
    ProgressDialog pDialog;
    String resp,userId, apiKey,idProgram, strNomEven,strLugEven,strRecomEven,urlImage, horaIni,horaFin,strdia, seccion;
    TextView txtNombreEven, txtLugarEven, txtRecomendaEven, txtHorario;
    int strcursorEncontrado, posJson;
    ImageView imgFoto;
    URL imageUrl ;
    Bitmap imagen;
    HttpURLConnection conn;
    Button btnLocalizar,btnAgreAgenda;
    Cursor cursor;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_detalle_evento, container, false);
        SharedPreferences sp = getActivity().getSharedPreferences("prefe", Activity.MODE_PRIVATE);
        apiKey = sp.getString("APIkey","");
        userId = sp.getString("IdUser","");
        imgFoto = (ImageView)view.findViewById(R.id.imgFoto);
        idProgram = getArguments().getString("idProgram");
        seccion = getArguments().getString("seccion");
        posJson = getArguments().getInt("posicion");
        txtNombreEven = (TextView)view.findViewById(R.id.txtNombreEven);
        txtLugarEven = (TextView)view.findViewById(R.id.txtLugarEven);
        txtRecomendaEven = (TextView)view.findViewById(R.id.txtRecomEven);
        txtHorario = (TextView)view.findViewById(R.id.txtHorario);
        btnLocalizar = (Button)view.findViewById(R.id.btnLocalizar);
        btnLocalizar.setOnClickListener(new Localizar());
        btnAgreAgenda = (Button)view.findViewById(R.id.btnAgreExpo);
        btnAgreAgenda.setOnClickListener(new AgregarAgenda());
        ////Se busca si este evento ya se encuentra en la base de agenda
        AdminSQLiteAgenda dbHandlerAgenda;
        dbHandlerAgenda = new AdminSQLiteAgenda(getActivity(), null, null, 1);
        SQLiteDatabase db = dbHandlerAgenda.getWritableDatabase();
        Cursor cursor = dbHandlerAgenda.BuscarpoId(idProgram);
        strcursorEncontrado = cursor.getCount();
        ///////////////////////////

        new getDetalle().execute();
        return view;



    }
    class AgregarAgenda implements View.OnClickListener {
        public void onClick(View v) {



            if(strcursorEncontrado == 0){
                AgregarAgendaEvento();
                strcursorEncontrado = 1;
            }
            else{
                Toast.makeText(getActivity(), "Ya se encuentra en tu agenda",
                        Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void AgregarAgendaEvento(){
        AdminSQLiteAgenda dbHandlerAgenda;
        dbHandlerAgenda = new AdminSQLiteAgenda(getActivity(), null, null, 1);
        SQLiteDatabase db = dbHandlerAgenda.getWritableDatabase();

        dbHandlerAgenda.addEvento(idProgram,txtNombreEven.getText().toString(),strdia,horaIni,horaFin);

        Toast.makeText(getActivity(), "Se guardo en Mi Agenda",
                Toast.LENGTH_SHORT).show();
    }
    class Localizar implements View.OnClickListener {
        public void onClick(View v) {

            Toast.makeText(getActivity(),"Próximamente",Toast.LENGTH_SHORT).show();
            /*getFragmentManager().beginTransaction()
                    .replace(R.id.actividad, new MapaFragment()).addToBackStack(null).commit();*/


        }
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
            AdminSQLiteOffline dbHandler;
            String respuesta ="";
            if (seccion.equals("acomp")){
                dbHandler = new AdminSQLiteOffline(getActivity(), null, null, 1);
                SQLiteDatabase db = dbHandler.getWritableDatabase();
                cursor = dbHandler.jsonAcompa();
                respuesta = cursor.getString(0);
            }else if(seccion.equals("social")) {
                dbHandler = new AdminSQLiteOffline(getActivity(), null, null, 1);
                SQLiteDatabase db = dbHandler.getWritableDatabase();
                cursor = dbHandler.jsonSocialDepo();
                respuesta = cursor.getString(0);
            }else {
                JSONParser jsp = new JSONParser();
                String body = "http://cmp.devworms.com/api/programa/detail/" + userId + "/" + apiKey + "/" + idProgram + "";
                 respuesta= jsp.makeHttpRequest(body,"GET",body,"");
            }





            Log.d("LoginRes : ", "> " + respuesta);
            if (respuesta != "error") {
                try {
                    JSONObject json = new JSONObject(respuesta);
                    String exposi = "";
                    if(seccion.equals("acomp")){
                        exposi = json.getString("acompanantes");
                    } else if(seccion.equals("social")) {
                        exposi = json.getString("eventos");
                    } else {
                        exposi = json.getString("programa");
                    }

                    JSONArray jsonExpositores = new JSONArray(exposi);

                    JSONObject jsonExpo = jsonExpositores.getJSONObject(posJson);


                    ////////////////////////////////////////////
                    strNomEven = jsonExpo.getString("nombre");
                    strLugEven = jsonExpo.getString("lugar");
                    strRecomEven = jsonExpo.getString("recomendaciones");
                    horaIni = jsonExpo.getString("hora_inicio");
                    horaFin = jsonExpo.getString("hora_fin");
                    strdia = jsonExpo.getString("fecha");


                    dbHandler = new AdminSQLiteOffline(getActivity(), null, null, 1);
                    SQLiteDatabase db = dbHandler.getWritableDatabase();

                    if(seccion.equals("acomp")){
                        cursor = dbHandler.ImagenPorIdAco(idProgram);
                    } else if(seccion.equals("social")) {
                        cursor = dbHandler.ImagenPorIdSocialDepo(idProgram);
                    }

                    if(cursor.getCount()>0) {
                        imagen = getImage(cursor.getString(0));

                    }




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
                        txtNombreEven.setText(strNomEven);
                        txtLugarEven.setText(strLugEven);
                        txtRecomendaEven.setText(strRecomEven);
                        txtHorario.setText(horaIni + " - " + horaFin);
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
