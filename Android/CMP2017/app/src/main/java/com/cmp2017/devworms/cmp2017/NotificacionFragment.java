package com.cmp2017.devworms.cmp2017;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class NotificacionFragment extends Fragment

{    ProgressDialog pDialog;
    private ListView lvNoti;
    private ImageView ivImagen;
    private ArrayList<NotificacionModelo> arrayNoti;
    private ListViewAdapter adapter;
    private int leido;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.notificacion_fragment, container, false);
        lvNoti = (ListView)view.findViewById(R.id.lvNoti);
        ivImagen = (ImageView)view.findViewById(R.id.ivImagen);
        arrayNoti = new ArrayList<>();

        new SendRespuestasEncuesta().execute();


        return view;


    }
    class SendRespuestasEncuesta extends AsyncTask<String, String, String> {
        String mensajeRespuestas;
        NotificacionModelo modelo[];
        String coso[];


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(getActivity());
            pDialog.setMessage("Cargando notificaciones...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }
        @Override
        protected String doInBackground(String... params) {
            String response = "";
            JSONParser jsonParser = new JSONParser();

            String toUrl = "http://cmp.devworms.com/api/notificacion/all/5/5f3a4f132f40dcd7924ec795133c540f";
            String respuesta = jsonParser.makeHttpRequest(toUrl, "GET", "", "");
            mensajeRespuestas = "No se pudieron enviar las respuesta";
            try{
                JSONObject jRespuesta = new JSONObject(respuesta);
                if(jRespuesta.getInt("estado") == 1){
                    mensajeRespuestas = jRespuesta.getString("notificaciones");
                    JSONArray jEncuestas = new JSONArray(mensajeRespuestas);
                    int cuentasEncuestas = jEncuestas.length();
                    //modelo = new NotificacionModelo[cuentasEncuestas];
                    //coso = new String[cuentasEncuestas];
                    for(int cont =0; cont< cuentasEncuestas ; cont++){

                        //modelo[cont]= new NotificacionModelo(jEncuestas.getJSONObject(cont).getString("notificacion"),jEncuestas.getJSONObject(cont).getInt("leido"));
                        arrayNoti.add(new NotificacionModelo(jEncuestas.getJSONObject(cont).getString("notificacion"),jEncuestas.getJSONObject(cont).getInt("leido")));
                        leido= jEncuestas.getJSONObject(cont).getInt("leido");
                        //modelo[cont].setNotificacion(jEncuestas.getJSONObject(cont).getString("notificacion"));
                        //JSONObject archivos = new JSONObject(jEncuestas.getJSONObject(cont).getString("notificacion"));

                        //modelo[cont].setImagen(getBitmapFromURL(archivos.getString("url")));
                        //coso[cont]= modelo[cont].getNotificacion();


                    }
                    response = "OK";
                }else{
                    mensajeRespuestas = jRespuesta.getString("mensaje");
                    response = "NO";
                }
            }catch(JSONException jex){
                mensajeRespuestas = jex.getMessage();
                response = "NO";
            }

            return response;
        }
        protected void onPostExecute(String file_url) {
            int cuentasEncuestas = arrayNoti.size();
            adapter = new ListViewAdapter(getActivity(),arrayNoti);
            lvNoti.setAdapter(adapter);
            pDialog.dismiss();
        }
    }

}
