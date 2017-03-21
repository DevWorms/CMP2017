package com.cmp2017.devworms.cmp2017;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class ExpositoresFrgament extends Fragment {
    String userId, apiKey,resp, nombre;
    ProgressDialog pDialog;
    ArrayList<HashMap<String, String>> albumsList;
    ListView lista;
    AutoCompleteTextView acTextView;
    TextView txtTitulo;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_expo, container, false);

        SharedPreferences sp = getActivity().getSharedPreferences("prefe", Activity.MODE_PRIVATE);
        apiKey = sp.getString("APIkey","");
        userId = sp.getString("IdUser","");
        nombre = getArguments() != null ? getArguments().getString("nombre"):"Expositores";
        txtTitulo= (TextView) view.findViewById(R.id.txtDescrip);
        txtTitulo.setText(nombre);
        lista=  (ListView) view.findViewById(R.id.lvExpo);
        new getListaAlfabetica().execute();

        Button btnAlfabetico = (Button)view.findViewById(R.id.btnOrdAlf);
        btnAlfabetico.setOnClickListener(new Alfabetico());

        Button btnNumerico = (Button)view.findViewById(R.id.btnNumStand);
        btnNumerico.setOnClickListener(new Numerico());

        acTextView = (AutoCompleteTextView) view.findViewById(R.id.autoCompleteEcpo);

        acTextView.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                lista.setAdapter(null);
                albumsList.clear();
                new getListaBuscar().execute();
            }
        });
        lista.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View view, int arg2,
                                    long arg3) {
                // on selecting a single album
                // TrackListActivity will be launched to show tracks inside the album
                String expoId= ((TextView)view.findViewById(R.id.txtid)).getText().toString();
                Fragment fragment = new DetalleExpoFragment();

                Bundle parametro = new Bundle();


                parametro.putString("expoId",expoId);


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

    class Alfabetico implements View.OnClickListener {
        public void onClick(View v) {



            new getListaAlfabetica().execute();



        }
    }

    class Numerico implements View.OnClickListener {
        public void onClick(View v) {



            new getListaNumerico().execute();



        }
    }

    class getListaAlfabetica extends AsyncTask<String, String, String> {

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

            if(nombre.equals("Expositores")){
                body = "http://cmp.devworms.com/api/expositor/order/name/"+userId+"/"+apiKey+"";
            } else if(nombre.equals("Patrocinadores")){
                body = "http://cmp.devworms.com/api/patrocinador/order/name/"+userId+"/"+apiKey+"";
            }


            JSONParser jsp = new JSONParser();



            String respuesta= jsp.makeHttpRequest(body,"GET",body,"");
            Log.d("LoginRes : ", "> " + respuesta);
            if (respuesta != "error") {
                try {
                    JSONObject json = new JSONObject(respuesta);
                    String exposi = "";
                    if(nombre.equals("Expositores")){
                        exposi = json.getString("expositores");
                    } else if(nombre.equals("Patrocinadores")){
                        exposi = json.getString("patrocinadores");
                    }


                    JSONArray jsonExpositores = new JSONArray(exposi);

                    int cuanto = jsonExpositores.length();
                    albumsList = new ArrayList<HashMap<String, String>>();
                    char actLetra = ' ';
                    // looping through All albums
                    for (int i = 0; i < jsonExpositores.length(); i++) {
                        JSONObject c = jsonExpositores.getJSONObject(i);


                        // creating new HashMap
                        HashMap<String, String> map = new HashMap<String, String>();

                        // Storing each json item values in variable
                        String id = c.getString("id");
                        String name = c.getString("nombre");
                        char letra = name.charAt(0);
                            if (letra == ' ' || letra != actLetra){
                                map.put("expoLetra", letra+" ");
                                actLetra = letra;

                            }


                            map.put("expoId", id);


                            map.put("expoName", name);



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
                        R.layout.formato_lista_alfabetica, new String[] { "expoLetra",
                        "expoName","expoId" }, new int[] {
                        R.id.txtLetra, R.id.txtNombre , R.id.txtid});

                // updating listview

                lista.setAdapter(adapter);

            }
            // updating UI from Background Thread


        }
    }

    class getListaBuscar extends AsyncTask<String, String, String> {




        /**
         * getting Albums JSON
         */
        protected String doInBackground(String... args) {
            // Building Parameters
            //add your data
            String body= "";
            String respuesta= "";
            String buscar = acTextView.getText().toString();
            JSONParser jsp = new JSONParser();

            if(nombre.equals("Expositores")){
                body = "{\n\t\"user_id\" : \""+userId+"\",\n\t\"api_key\" : \""+apiKey+"\",\n\t\"search\" : \""+buscar+"\"\n\t\n}";
                respuesta= jsp.makeHttpRequest("http://cmp.devworms.com/api/expositor/search","POST",body,"");
            } else if(nombre.equals("Patrocinadores")){
                body = "{\n\t\"user_id\" : \""+userId+"\",\n\t\"api_key\" : \""+apiKey+"\",\n\t\"search\" : \""+buscar+"\"\n\t\n}";
                respuesta= jsp.makeHttpRequest("http://cmp.devworms.com/api/patrocinador/search","POST",body,"");
            }



            Log.d("LoginRes : ", "> " + respuesta);
            if (respuesta != "error") {
                try {
                    JSONObject json = new JSONObject(respuesta);
                    String exposi = "";
                    if(nombre.equals("Expositores")){
                        exposi = json.getString("expositores");
                    } else if(nombre.equals("Patrocinadores")){
                        exposi = json.getString("patrocinadores");
                    }


                    JSONArray jsonExpositores = new JSONArray(exposi);

                    int cuanto = jsonExpositores.length();
                    albumsList = new ArrayList<HashMap<String, String>>();
                    char actLetra = ' ';
                    // looping through All albums
                    for (int i = 0; i < jsonExpositores.length(); i++) {
                        JSONObject c = jsonExpositores.getJSONObject(i);


                        // creating new HashMap
                        HashMap<String, String> map = new HashMap<String, String>();

                        // Storing each json item values in variable
                        String id = c.getString("id");
                        String name = c.getString("nombre");
                        char letra = name.charAt(0);
                        if (letra == ' ' || letra != actLetra){
                            map.put("expoLetra", letra+" ");
                            actLetra = letra;

                        }


                        map.put("expoId", id);


                        map.put("expoName", name);



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
                        R.layout.formato_lista_alfabetica, new String[] { "expoLetra",
                        "expoName","expoId" }, new int[] {
                        R.id.txtLetra, R.id.txtNombre , R.id.txtid});

                // updating listview

                lista.setAdapter(adapter);

            }
            // updating UI from Background Thread


        }
    }

    class getListaNumerico extends AsyncTask<String, String, String> {

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

            if(nombre.equals("Expositores")){
                body = "http://cmp.devworms.com/api/expositor/order/stand/"+userId+"/"+apiKey+"";
            } else if(nombre.equals("Patrocinadores")){
                body = "http://cmp.devworms.com/api/patrocinador/order/stand/"+userId+"/"+apiKey+"";
            }


            JSONParser jsp = new JSONParser();



            String respuesta= jsp.makeHttpRequest(body,"GET",body,"");
            Log.d("LoginRes : ", "> " + respuesta);
            if (respuesta != "error") {
                try {
                    JSONObject json = new JSONObject(respuesta);
                    String exposi = json.getString("expositores");

                    JSONArray jsonExpositores = new JSONArray(exposi);

                    int cuanto = jsonExpositores.length();
                    albumsList = new ArrayList<HashMap<String, String>>();
                    char actLetra = ' ';
                    // looping through All albums
                    for (int i = 0; i < jsonExpositores.length(); i++) {
                        JSONObject c = jsonExpositores.getJSONObject(i);


                        // creating new HashMap
                        HashMap<String, String> map = new HashMap<String, String>();

                        // Storing each json item values in variable
                        String id = c.getString("id");
                        String name = c.getString("nombre");
                        String stand = c.getString("stand");


                        map.put("expoId", id);

                        map.put("expoLetra", "Stand " + stand);
                        map.put("expoName", name);



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
                        R.layout.formato_lista_alfabetica, new String[] { "expoLetra",
                        "expoName","expoId" }, new int[] {
                        R.id.txtLetra, R.id.txtNombre , R.id.txtid});

                // updating listview

                lista.setAdapter(adapter);

            }
            // updating UI from Background Thread


        }
    }
}
