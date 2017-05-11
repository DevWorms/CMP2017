package com.cmp2017.devworms.cmp2017;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.app.ListFragment;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ListadoFragment extends Fragment {

    String seccion, nombre, diaProgram, userId, apiKey, cateId, fecha, resp;
    int tipoEvento;
    ProgressDialog pDialog;
    ListView lista;
    ConnectionDetector cd;
    Cursor cursor;
    ArrayList<HashMap<String, String>> albumsList;
    TextView txtTitulo;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_listado, container, false);
        SharedPreferences sp = getActivity().getSharedPreferences("prefe", Activity.MODE_PRIVATE);
        apiKey = sp.getString("APIkey","");
        userId = sp.getString("IdUser","");
        lista=  (ListView) view.findViewById(R.id.lvLista);
        nombre = getArguments() != null ? getArguments().getString("nombre"):"Resultados";
        txtTitulo= (TextView) view.findViewById(R.id.txtTituloListado);
        txtTitulo.setText(nombre);
        seccion =  getArguments().getString("seccion");
        cd = new ConnectionDetector(getActivity());
        if(seccion.equals("programas")){

            tipoEvento =  getArguments().getInt("tipoEvento");
            if(tipoEvento == 0){
                cateId = "";
            }else{
                cateId = tipoEvento +"";
            }

            Log.e("cateId" , cateId);

            diaProgram =  getArguments().getString("diaProgra");
            if(diaProgram.equals("Lunes 5 de Junio")){
                fecha = "2017-06-05";
            }else if(diaProgram.equals("Martes 6 de Junio")){
                fecha = "2017-06-06";
            }else if(diaProgram.equals("Miercoles 7 de Junio")){
                fecha = "2017-06-07";
            }else if(diaProgram.equals("Jueves 8 de Junio")){
                fecha = "2017-06-08";
            }else if(diaProgram.equals("Viernes 9 de Junio")){
                fecha = "2017-06-09";
            }else if(diaProgram.equals("Sabado 10 de Junio")){
                fecha = "2017-06-10";
            }else {
                fecha = "";
            }

            // llamamos el metodo que carga desde la BD
            getListaProgramas(cateId,fecha);

        }else if(seccion.equals("acomp") || seccion.equals("social")){
            new getListaAcompSocial().execute();
        }
        lista.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View view, int arg2,
                                    long arg3) {
                // on selecting a single album
                // TrackListActivity will be launched to show tracks inside the album
                String idProgram= ((TextView)view.findViewById(R.id.txtid)).getText().toString();
                Log.e("clickEVENTO",idProgram);
                Fragment fragment = new DetalleEventoFragment();

                Bundle parametro = new Bundle();


                parametro.putString("idProgram",idProgram);
                parametro.putString("seccion",seccion);
                parametro.putInt("posicion",arg2);

                fragment.setArguments(parametro);

                final FragmentTransaction ft = getFragmentManager()
                        .beginTransaction();
                ft.replace(R.id.actividad, fragment, "tag");

                ft.addToBackStack("tag");

                ft.commit();
            }
        });
        return view;



    }

    //metodo para obtener los programos de acuerdo a la busqeda, BASE DE DATOS
    public void getListaProgramas(String categoria, String fechaPrograma){
        txtTitulo.setText("Programas");
        // obtengo el json de la base de datos
        AdminSQLiteOffline dbHandlerOffline;
        dbHandlerOffline = new AdminSQLiteOffline(getActivity(), null, null, 1);
        Cursor crsProgramas = dbHandlerOffline.getJsonProgramas();

        // lo guardamos en un string para parsearlo
        String strProgramas = crsProgramas.getString(0);

        JSONObject jProgramas = null;
        JSONArray aProgramas = null;
        String actFecha="";

        try{
            jProgramas = new JSONObject(strProgramas);
            aProgramas = jProgramas.getJSONArray("programas");
            albumsList = new ArrayList<HashMap<String, String>>();
            for (int i = 0; i< aProgramas.length() ; i++) {
                JSONObject jTemp = aProgramas.getJSONObject(i);
                JSONObject jCategoria = new JSONObject(jTemp.getString("categoria"));
                // filtro misma categoria misma fecha
                if((jCategoria.getString("id").equals(categoria) || categoria.equals("")) && (jTemp.getString("fecha").equals(fechaPrograma) || fechaPrograma.equals(""))){

                    HashMap<String, String> map = new HashMap<String, String>();

                    // Storing each json item values in variable
                    String id = jTemp.getString("id");
                    String name = jTemp.getString("nombre");
                    String lugar = jTemp.getString("lugar");
                    String recomendaciones = jTemp.getString("recomendaciones");
                    String hora_inicio = jTemp.getString("hora_inicio");
                    String hora_fin = jTemp.getString("hora_fin");
                    String proFecha = jTemp.getString("fecha");
                    Log.e("FECHAFILTRADA",proFecha);
                    String fechaLetra = "";

                    if(proFecha.equals("2017-06-05")){
                        fechaLetra = "Lunes 5 de Junio";
                    }else if(proFecha.equals("2017-06-06")){
                        fechaLetra = "Martes 6 de Junio";
                    }else if(proFecha.equals("2017-06-07")){
                        fechaLetra = "Miercoles 7 de Junio";
                    }else if(proFecha.equals("2017-06-08")){
                        fechaLetra = "Jueves 8 de Junio";
                    }else if(proFecha.equals("2017-06-09")){
                        fechaLetra = "Viernes 9 de Junio";
                    }else if(proFecha.equals("2017-06-10")){
                        fechaLetra = "Sabado 10 de Junio";
                    }
                    if(diaProgram.equals("Todos")){

                        if (actFecha.equals("")||  !actFecha.equals(proFecha)){
                            map.put("progFecha", fechaLetra);
                            actFecha = proFecha;
                        }

                        map.put("progId", id);
                        map.put("progName", name);

                    }else{

                        map.put("progId", id);

                        if (i==0){
                            map.put("progFecha", diaProgram);
                        }

                        map.put("progName", name);
                    }

                    albumsList.add(map);
                }
            }

            // ya que seteamos los valores pasamos al adapter
            ListAdapter adapter = new SimpleAdapter(
                    getActivity(), albumsList,
                    R.layout.formato_lista_resultados, new String[] { "progFecha",
                    "progName","progId" }, new int[] {
                    R.id.txtFecha, R.id.txtTitulo , R.id.txtid});

            lista.setAdapter(adapter);

        }catch(JSONException jex){
            Log.e("Programas BD",jex.getMessage());
        }

    }

    class getListaAcompSocial extends AsyncTask<String, String, String> {

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
            AdminSQLiteOffline dbHandler;
            if( seccion.equals("acomp")) {
                //body = "http://cmp.devworms.com/api/acompanantes/all/" + userId + "/" + apiKey + "";
                dbHandler = new AdminSQLiteOffline(getActivity(), null, null, 1);
                SQLiteDatabase db = dbHandler.getWritableDatabase();
                cursor = dbHandler.jsonAcompa();

            }else if(seccion.equals("social")) {
                dbHandler = new AdminSQLiteOffline(getActivity(), null, null, 1);
                SQLiteDatabase db = dbHandler.getWritableDatabase();
                cursor = dbHandler.jsonSocialDepo();

            }else{
                //body = "http://cmp.devworms.com/api/deportivos/all/"+userId+"/"+apiKey+"";
            }
            JSONParser jsp = new JSONParser();


            //String respuesta = jsp.makeHttpRequest(body, "GET", body, "");
            String respuesta = cursor.getString(0);
            Log.d("LoginRes : ", "> " + respuesta);
            if (respuesta != "error") {
                try {
                    JSONObject json = new JSONObject(respuesta);
                    String elemBus = "";
                    if( seccion.equals("acomp")) {
                        elemBus = json.getString("acompanantes");

                    }else if(seccion.equals("social")) {
                        elemBus = json.getString("eventos");
                    }else{

                    }


                    JSONArray jsonProgramas = new JSONArray(elemBus);

                    int cuanto = jsonProgramas.length();
                    albumsList = new ArrayList<HashMap<String, String>>();

                    String actFecha="";
                    // looping through All albums
                    for (int i = 0; i <= jsonProgramas.length(); i++) {
                        JSONObject c = jsonProgramas.getJSONObject(i);


                        // creating new HashMap
                        HashMap<String, String> map = new HashMap<String, String>();

                        // Storing each json item values in variable
                        String id = c.getString("id");
                        String name = c.getString("nombre");

                        String proFecha = c.getString("fecha");

                        String fechaLetra = "";

                        if(proFecha.equals("2017-06-05")){
                            fechaLetra = "Lunes 5 de Junio";
                        }else if(proFecha.equals("2017-06-06")){
                            fechaLetra = "Martes 6 de Junio";
                        }else if(proFecha.equals("2017-06-07")){
                            fechaLetra = "Miercoles 7 de Junio";
                        }else if(proFecha.equals("2017-06-08")){
                            fechaLetra = "Jueves 8 de Junio";
                        }else if(proFecha.equals("2017-06-09")){
                            fechaLetra = "Viernes 9 de Junio";
                        }

                        if (actFecha.equals("")||  !actFecha.equals(proFecha)){
                            map.put("acompFecha", fechaLetra);
                            actFecha = proFecha;
                        }

                        map.put("acompId", id);


                        map.put("acompName", name);



                        // adding HashList to ArrayList
                        albumsList.add(map);

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
                ListAdapter adapter = new SimpleAdapter(
                        getActivity(), albumsList,
                        R.layout.formato_lista_resultados, new String[] { "acompFecha",
                        "acompName","acompId" }, new int[] {
                        R.id.txtFecha, R.id.txtTitulo , R.id.txtid});

                // updating listview

                lista.setAdapter(adapter);

            }
            // updating UI from Background Thread


        }
    }
}