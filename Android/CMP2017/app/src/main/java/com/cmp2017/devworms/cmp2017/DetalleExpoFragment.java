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
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

public class DetalleExpoFragment extends Fragment {
    String userId, apiKey,resp,expoId,strNomEmpre,strPagina,strTelefono,strCorreo,strAcercaDe,urlImage;
    TextView txtNomEmpre, txtPagina,txtTelefono,txtCorreo, txtAcercaDe;
    ProgressDialog pDialog;
    ImageView imgFoto;
    URL imageUrl ;
    Bitmap imagen;
    HttpURLConnection conn;
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_detalle_expo, container, false);
        SharedPreferences sp = getActivity().getSharedPreferences("prefe", Activity.MODE_PRIVATE);
        String inicioComo = sp.getString("Nombre","");

        if (inicioComo.equals("invi")){
            apiKey = "0";
            userId = "1";
        } else{
            apiKey = sp.getString("APIkey","");
            userId = sp.getString("IdUser","");
        }
        expoId = getArguments().getString("expoId");
        txtNomEmpre = (TextView)view.findViewById(R.id.txtNomEmpre);
        txtPagina = (TextView)view.findViewById(R.id.txtPaginaExpo);
        txtTelefono = (TextView)view.findViewById(R.id.txtTelefono);
        txtCorreo = (TextView)view.findViewById(R.id.txtEmail);
        txtAcercaDe = (TextView)view.findViewById(R.id.txtAcercaDe);
        imgFoto = (ImageView)view.findViewById(R.id.imgFoto);
        new getDetalle().execute();
        return view;



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


            String body = "http://cmp.devworms.com/api/expositor/detail/"+userId+"/"+apiKey+"/"+expoId+"";
            JSONParser jsp = new JSONParser();



            String respuesta= jsp.makeHttpRequest(body,"GET",body,"");
            Log.d("LoginRes : ", "> " + respuesta);
            if (respuesta != "error") {
                try {
                    JSONObject json = new JSONObject(respuesta);
                    String expositor = json.getString("expositor");

                    JSONObject jsonExpo = new JSONObject(expositor);


                    strNomEmpre = jsonExpo.getString("nombre");
                    strPagina = jsonExpo.getString("url");
                    strTelefono = jsonExpo.getString("telefono");
                    strCorreo = jsonExpo.getString("email");
                    strAcercaDe = jsonExpo.getString("acerca");
                    String urlImageJson = jsonExpo.getString("logo");
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
                        txtNomEmpre.setText(strNomEmpre);
                        txtPagina.setText(strPagina);
                        txtTelefono.setText(strTelefono);
                        txtCorreo.setText(strCorreo);
                        txtAcercaDe.setText(strAcercaDe);
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
