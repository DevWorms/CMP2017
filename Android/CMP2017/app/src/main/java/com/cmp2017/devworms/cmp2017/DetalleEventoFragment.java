package com.cmp2017.devworms.cmp2017;

import android.app.Activity;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class DetalleEventoFragment extends Fragment {
    ProgressDialog pDialog;
    String resp,userId, apiKey,idProgram, strNomEven,strLugEven,strRecomEven;
    TextView txtNombreEven, txtLugarEven, txtRecomendaEven;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_detalle_evento, container, false);
        SharedPreferences sp = getActivity().getSharedPreferences("prefe", Activity.MODE_PRIVATE);
        apiKey = sp.getString("APIkey","");
        userId = sp.getString("IdUser","");
        idProgram = getArguments().getString("idProgram");
        txtNombreEven = (TextView)view.findViewById(R.id.txtNombreEven);
        txtLugarEven = (TextView)view.findViewById(R.id.txtLugarEven);
        txtRecomendaEven = (TextView)view.findViewById(R.id.txtRecomEven);
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
}
