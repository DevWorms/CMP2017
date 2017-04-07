package com.cmp2017.devworms.cmp2017;

import android.app.Activity;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.SharedPreferences;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

public class DetalleEventoFragment extends Fragment {
    ProgressDialog pDialog;
    String resp,userId, apiKey,idProgram, strNomEven,strLugEven,strRecomEven,urlImage;
    TextView txtNombreEven, txtLugarEven, txtRecomendaEven;
    ImageView imgFoto;
    URL imageUrl ;
    Bitmap imagen;
    HttpURLConnection conn;
    Button btnLocalizar,btnAgreExpo;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_detalle_evento, container, false);
        SharedPreferences sp = getActivity().getSharedPreferences("prefe", Activity.MODE_PRIVATE);
        apiKey = sp.getString("APIkey","");
        userId = sp.getString("IdUser","");
        imgFoto = (ImageView)view.findViewById(R.id.imgFoto);
        idProgram = getArguments().getString("idProgram");
        txtNombreEven = (TextView)view.findViewById(R.id.txtNombreEven);
        txtLugarEven = (TextView)view.findViewById(R.id.txtLugarEven);
        txtRecomendaEven = (TextView)view.findViewById(R.id.txtRecomEven);
        btnLocalizar = (Button)view.findViewById(R.id.btnLocalizar);
        btnLocalizar.setOnClickListener(new Localizar());
        btnAgreExpo = (Button)view.findViewById(R.id.btnAgreExpo);
        btnAgreExpo.setOnClickListener(new AgregarExpo());
        new getDetalle().execute();
        return view;



    }
    class AgregarExpo implements View.OnClickListener {
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


            String body ="http://cmp.devworms.com/api/programa/detail/"+userId+"/"+apiKey+"/"+idProgram+"";
            JSONParser jsp = new JSONParser();



            String respuesta= jsp.makeHttpRequest(body,"GET",body,"");
            Log.d("LoginRes : ", "> " + respuesta);
            if (respuesta != "error") {
                try {
                    JSONObject json = new JSONObject(respuesta);
                    String programs = json.getString("programa");

                    JSONObject jsonProgramas = new JSONObject(programs);

                   strNomEven = jsonProgramas.getString("nombre");
                    strLugEven = jsonProgramas.getString("lugar");
                    strRecomEven = jsonProgramas.getString("recomendaciones");
                    String urlImageJson = jsonProgramas.getString("foto");
                    JSONObject jsonImagen = new JSONObject(urlImageJson);
                    urlImage = jsonImagen.getString("url");

                    mostrarImagen(urlImage);


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

                    }



                }
            });

        }
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
