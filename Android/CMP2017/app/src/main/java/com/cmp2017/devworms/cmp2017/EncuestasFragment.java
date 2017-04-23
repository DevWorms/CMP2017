package com.cmp2017.devworms.cmp2017;

import android.app.Activity;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
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
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

public class EncuestasFragment extends Fragment{

    ListView listaEncuestas;
    String apiKey;
    String userId;
    Context aqui;
    EncuestaModel modelo[];
    String response;
    ProgressDialog pDialog;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_encuestas, container, false);
        SharedPreferences sp = getActivity().getSharedPreferences("prefe", Activity.MODE_PRIVATE);
        this.apiKey = sp.getString("APIkey", "");
        this.userId = sp.getString("IdUser", "");
        this.listaEncuestas = (ListView) view.findViewById(R.id.lvEncuestas);
        GetListaEncuestas generaLista = new GetListaEncuestas();
        generaLista.execute();
        return view;
    }

    class GetListaEncuestas extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(getActivity());
            pDialog.setMessage("Cargando encuestas...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {

            String fromUrl = "http://cmp.devworms.com/api/encuesta/all/"+userId+"/"+apiKey;

            JSONParser jsonParser = new JSONParser();

            String respuesta = jsonParser.makeHttpRequest(fromUrl, "GET", fromUrl, "");

            if (respuesta != "error") {
                try {
                    JSONObject json = new JSONObject(respuesta);
                    String strEncuestas = "";
                    strEncuestas = json.getString("encuestas");
                    JSONArray jEncuestas = new JSONArray(strEncuestas);

                    int cuentasEncuestas = jEncuestas.length();

                    modelo = new EncuestaModel[cuentasEncuestas];

                    for(int cont =0; cont< cuentasEncuestas ; cont++){

                        modelo[cont]= new EncuestaModel();
                        modelo[cont].setId(jEncuestas.getJSONObject(cont).getInt("id"));
                        JSONObject archivos = new JSONObject(jEncuestas.getJSONObject(cont).getString("filesm"));
                        modelo[cont].setImagen(getBitmapFromURL(archivos.getString("url")));
                    }

                    response = "OK";

                }catch (JSONException jex){
                    response = "NO";
                    jex.printStackTrace();
                }

            }else{
                response = "NO";
            }
            System.out.println(response + "<----");
            return response;
        }

        protected void onPostExecute(String file_url) {
            if ( response.equals("OK") ) {
                ListAdapterEncuestas adapter = new ListAdapterEncuestas(getActivity(),modelo);
                listaEncuestas.setAdapter(adapter);
                pDialog.dismiss();
            }
        }
    }

    public Bitmap getBitmapFromURL(String imageUrl) {
        Bitmap imagen = null;
        try{

            URL urlImage = new URL(imageUrl);
            HttpURLConnection conn = (HttpURLConnection) urlImage.openConnection();
            conn.connect();

            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inSampleSize = 2; // el factor de escala a minimizar la imagen, siempre es potencia de 2

            imagen = BitmapFactory.decodeStream(conn.getInputStream(), new Rect(0, 0, 0, 0), options);

        }catch (IOException iox){
            Log.e("Error imagen encuesta" , iox.getMessage());
        }
        return imagen;
    }
}
