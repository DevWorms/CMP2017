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
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

public class TransportacionFragment extends Fragment

{
    String  pagianweb, nombre, userId, apiKey, resp,urlImage;
    String []  idAr, nameAr, descrpAr, urlAr;
    ProgressDialog pDialog;
    ListView lista;
    int nume;
    ArrayList<HashMap<String, Object>> albumsList;
    String [] linksMaps;
    ImageView imgFoto;
    URL imageUrl ;
    Bitmap []imagen;
    HttpURLConnection conn;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_transportacion, container, false);
        SharedPreferences sp = getActivity().getSharedPreferences("prefe", Activity.MODE_PRIVATE);


        apiKey = sp.getString("APIkey", "");
        userId = sp.getString("IdUser", "");
        lista = (ListView) view.findViewById(R.id.lvListaTrans);
        new getListaTrans().execute();
        return view;


    }

    class getListaTrans extends AsyncTask<String, String, String> {

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
            String body= "";


            body = "http://cmp.devworms.com/api/ruta/all/"+userId+"/"+apiKey+"";

            JSONParser jsp = new JSONParser();


            String respuesta = jsp.makeHttpRequest(body, "GET", body, "");

            if (respuesta != "error") {
                try {
                    JSONObject json = new JSONObject(respuesta);
                    String elemBus = "";

                    elemBus = json.getString("rutas");




                    JSONArray jsonRutas = new JSONArray(elemBus);

                    int cuanto = jsonRutas.length();
                    albumsList = new ArrayList<HashMap<String, Object>>();

                    String actFecha="";

                    idAr = new String[jsonRutas.length()];
                    nameAr = new String[jsonRutas.length()];
                    descrpAr = new String[jsonRutas.length()];
                    urlAr = new String[jsonRutas.length()];


                    // looping through All albums
                    for (int i = 0; i <= jsonRutas.length(); i++) {
                        JSONObject c = jsonRutas.getJSONObject(i);




                        // Storing each json item values in variable
                        idAr[i] = c.getString("id");
                        nameAr[i] = c.getString("titulo");
                        descrpAr[i] = c.getString("descripcion");
                        String urlTrans = c.getString("image");
                        JSONObject jsonUrl = new JSONObject(urlTrans);

                        urlAr[i] = jsonUrl.getString("url");
                        // adding HashList to ArrayList


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
            if (resp.equals("ok") ) {
                ListAdapterTransportacion adapter=new ListAdapterTransportacion(getActivity(),nameAr,descrpAr,urlAr);

                // updating listview

                lista.setAdapter(adapter);

            }
            // updating UI from Background Thread


        }
    }


}
