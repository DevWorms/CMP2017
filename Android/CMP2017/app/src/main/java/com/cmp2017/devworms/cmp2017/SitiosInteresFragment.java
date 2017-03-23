package com.cmp2017.devworms.cmp2017;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

public class SitiosInteresFragment extends Fragment

{
    String  pagianweb, nombre, userId, apiKey, resp,urlImage;
    String []  idAr, nameAr, descrpAr,mapaAr, paginaAr;
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
        View view = inflater.inflate(R.layout.fragment_sitios_interes, container, false);
        lista=  (ListView) view.findViewById(R.id.lvLista);
        SharedPreferences sp = getActivity().getSharedPreferences("prefe", Activity.MODE_PRIVATE);
        apiKey = sp.getString("APIkey","");
        userId = sp.getString("IdUser","");
        new getListaSitios().execute();

        lista.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View view, int arg2,
                                    long arg3) {
                // on selecting a single album
                // TrackListActivity will be launched to show tracks inside the album
                TextView txtSitio= (TextView)view.findViewById(R.id.txtSitioweb);
                nume= arg2;
                pagianweb = txtSitio.getText().toString();
                txtSitio.setOnClickListener(new SitiosInteres());
                TextView txtMaps= (TextView)view.findViewById(R.id.txtUrlMapLugar);

                txtMaps.setOnClickListener(new SitiosMaps());

            }
        });
        return view;


    }
    class SitiosInteres implements View.OnClickListener {
        public void onClick(View v) {

            String link = pagianweb;
            Intent intent = null;
            intent = new Intent(intent.ACTION_VIEW, Uri.parse(link));
            startActivity(intent);


        }
    }

    class SitiosMaps implements View.OnClickListener {
        public void onClick(View v) {


            String link = linksMaps[nume];
            Intent intent = null;
            intent = new Intent(intent.ACTION_VIEW, Uri.parse(link));
            startActivity(intent);


        }
    }
    class getListaSitios extends AsyncTask<String, String, String> {

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


                body = "http://cmp.devworms.com/api/puebla/sitio/all/"+userId+"/"+apiKey+"";

            JSONParser jsp = new JSONParser();


            String respuesta = jsp.makeHttpRequest(body, "GET", body, "");

            if (respuesta != "error") {
                try {
                    JSONObject json = new JSONObject(respuesta);
                    String elemBus = "";

                        elemBus = json.getString("sitios");




                    JSONArray jsonProgramas = new JSONArray(elemBus);

                    int cuanto = jsonProgramas.length();
                    albumsList = new ArrayList<HashMap<String, Object>>();
                    Log.d("ListadoProgra : ", "> " + cuanto);
                    String actFecha="";
                    linksMaps = new String[jsonProgramas.length()];
                    idAr = new String[jsonProgramas.length()];
                    nameAr = new String[jsonProgramas.length()];
                    descrpAr = new String[jsonProgramas.length()];
                    paginaAr = new String[jsonProgramas.length()];
                    imagen = new Bitmap[jsonProgramas.length()];
                    mapaAr = new String[jsonProgramas.length()];
                    // looping through All albums
                    for (int i = 0; i <= jsonProgramas.length(); i++) {
                        JSONObject c = jsonProgramas.getJSONObject(i);




                        // Storing each json item values in variable
                         idAr[i] = c.getString("id");
                         nameAr[i] = c.getString("titulo");
                         descrpAr[i] = c.getString("descripcion");
                         mapaAr[i] = c.getString("maps_link");
                         paginaAr[i] = c.getString("url");
                        String urlImageJson = c.getString("imagen");

                        JSONObject jsonImagen = new JSONObject(urlImageJson);
                        urlImage = jsonImagen.getString("url");

                        linksMaps[i] = mapaAr[i];


                        mostrarImagen(urlImage,i);


                        Log.d("sitioImage : ", "> " + imagen.toString());
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
                ListAdapterCustom adapter=new ListAdapterCustom(getActivity(),nameAr,descrpAr,mapaAr,paginaAr,imagen);

                // updating listview

                lista.setAdapter(adapter);

            }
            // updating UI from Background Thread


        }
    }

    public void mostrarImagen(String url, int cont){


        try {

            imageUrl = new URL(url);
            conn = (HttpURLConnection) imageUrl.openConnection();
            conn.connect();

            BitmapFactory.Options options = new BitmapFactory.Options();
            //options.inSampleSize = 2; // el factor de escala a minimizar la imagen, siempre es potencia de 2

            imagen[cont] = BitmapFactory.decodeStream(conn.getInputStream(), new Rect(0, 0, 0, 0), options);



        } catch (IOException e) {

            e.printStackTrace();

        }

    }
}
