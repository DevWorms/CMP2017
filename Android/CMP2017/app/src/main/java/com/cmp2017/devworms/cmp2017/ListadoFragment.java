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
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
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
    ImageTools tools;
    ConstraintLayout fragListado;
    private AutoCompleteTextView acSearchProgramas;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_listado, container, false);
        tools = new ImageTools(getActivity());
        fragListado = (ConstraintLayout) view.findViewById(R.id.fragListado);
        tools.loadBackground(R.drawable.fondo, fragListado);
        SharedPreferences sp = getActivity().getSharedPreferences("prefe", Activity.MODE_PRIVATE);
        apiKey = sp.getString("APIkey", "");
        userId = sp.getString("IdUser", "");
        lista = (ListView) view.findViewById(R.id.lvLista);
        nombre = getArguments() != null ? getArguments().getString("nombre") : "Resultados";
        txtTitulo = (TextView) view.findViewById(R.id.txtTituloListado);
        txtTitulo.setText(nombre);
        seccion = getArguments().getString("seccion");
        cd = new ConnectionDetector(getActivity());
        if (seccion.equals("programas")) {

            tipoEvento = getArguments().getInt("tipoEvento");
            if (tipoEvento == 0) {
                cateId = "";
            } else {
                cateId = tipoEvento + "";
            }

            Log.e("cateId", cateId);

            diaProgram = getArguments().getString("diaProgra");

            if (diaProgram.equals("Lunes 5 de Junio")) {
                fecha = "2017-06-05";
            } else if (diaProgram.equals("Martes 6 de Junio")) {
                fecha = "2017-06-06";
            } else if (diaProgram.equals("Miercoles 7 de Junio")) {
                fecha = "2017-06-07";
            } else if (diaProgram.equals("Jueves 8 de Junio")) {
                fecha = "2017-06-08";
            } else if (diaProgram.equals("Viernes 9 de Junio")) {
                fecha = "2017-06-09";
            } else if (diaProgram.equals("Sabado 10 de Junio")) {
                fecha = "2017-06-10";
            } else {
                fecha = "";
            }

            // llamamos el metodo que carga desde la BD
            getListaProgramas(cateId, fecha, "");

        } else if (seccion.equals("acomp")) {

            this.getListAcompanantes("");

        } else if (seccion.equals("social")) {

            this.getListSocialDepo("");
        }

        lista.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View view, int arg2,
                                    long arg3) {
                // on selecting a single album
                // TrackListActivity will be launched to show tracks inside the album
                String idProgram = ((TextView) view.findViewById(R.id.txtid)).getText().toString();
                Log.e("clickEVENTO", idProgram);
                Fragment fragment = new DetalleEventoFragment();

                Bundle parametro = new Bundle();


                parametro.putString("idProgram", idProgram);
                parametro.putString("seccion", seccion);
                parametro.putInt("posicion", arg2);

                fragment.setArguments(parametro);

                final FragmentTransaction ft = getFragmentManager()
                        .beginTransaction();
                ft.replace(R.id.actividad, fragment, "tag");

                ft.addToBackStack("tag");

                ft.commit();
            }
        });

        // inicializamos el buscador

        this.acSearchProgramas = (AutoCompleteTextView) view.findViewById(R.id.acSearchProgramas);
        initSearch();
        return view;


    }

    public void initSearch() {
        this.acSearchProgramas.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String search = s.toString();
                if (seccion.equals("programas")) {
                    getListaProgramas(cateId, fecha, search);
                } else if (seccion.equals("acomp")) {
                    getListAcompanantes(search);
                } else if (seccion.equals("social")) {
                    getListSocialDepo(search);
                }
            }
        });
    }

    //metodo para obtener los programos de acuerdo a la busqeda, BASE DE DATOS
    public void getListaProgramas(String categoria, String fechaPrograma, String search) {
        txtTitulo.setText("Programas");
        // obtengo el json de la base de datos
        AdminSQLiteOffline dbHandlerOffline;
        dbHandlerOffline = new AdminSQLiteOffline(getActivity(), null, null, 1);
        Cursor crsProgramas = dbHandlerOffline.getJsonProgramas();

        // lo guardamos en un string para parsearlo
        String strProgramas = crsProgramas.getString(0);

        JSONObject jProgramas = null;
        JSONArray aProgramas = null;
        String actFecha = "";

        try {
            jProgramas = new JSONObject(strProgramas);
            aProgramas = jProgramas.getJSONArray("programas");
            albumsList = new ArrayList<HashMap<String, String>>();
            for (int i = 0; i < aProgramas.length(); i++) {
                JSONObject jTemp = aProgramas.getJSONObject(i);
                JSONObject jCategoria = new JSONObject(jTemp.getString("categoria"));
                // filtro misma categoria misma fecha
                if ((jCategoria.getString("id").equals(categoria) || categoria.equals("")) && (jTemp.getString("fecha").equals(fechaPrograma) || fechaPrograma.equals(""))) {

                    HashMap<String, String> map = new HashMap<String, String>();
                    //aplicamos criterio de busqueda
                    String id = "";
                    String name = "";
                    String proFecha = "";
                    String fechaLetra = "";

                    if (!search.equals("")) {

                        if (jTemp.getString("nombre").contains(search)) {

                            id = jTemp.getString("id");
                            name = jTemp.getString("nombre");
                            proFecha = jTemp.getString("fecha");

                            fechaLetra = this.getFechaLetra(proFecha);

                            if (diaProgram.equals("Todos")) {

                                if (actFecha.equals("") || !actFecha.equals(proFecha)) {
                                    map.put("progFecha", fechaLetra);
                                    actFecha = proFecha;
                                }
                                map.put("progId", id);
                                map.put("progName", name);

                            } else {

                                if (i == 0) {
                                    map.put("progFecha", diaProgram);
                                }

                                map.put("progId", id);
                                map.put("progName", name);
                            }

                            albumsList.add(map);
                        }

                    } else {

                        id = jTemp.getString("id");
                        name = jTemp.getString("nombre");
                        proFecha = jTemp.getString("fecha");

                        fechaLetra = this.getFechaLetra(proFecha);

                        if (diaProgram.equals("Todos")) {

                            if (actFecha.equals("") || !actFecha.equals(proFecha)) {
                                map.put("progFecha", fechaLetra);
                                actFecha = proFecha;
                            }
                            map.put("progId", id);
                            map.put("progName", name);

                        } else {


                            if (i == 0) {
                                map.put("progFecha", diaProgram);
                            }

                            map.put("progId", id);
                            map.put("progName", name);
                        }

                        albumsList.add(map);

                    }

                }
            }

            // ya que seteamos los valores pasamos al adapter
            ListAdapter adapter = new SimpleAdapter(
                    getActivity(), albumsList,
                    R.layout.formato_lista_resultados, new String[]{"progFecha",
                    "progName", "progId"}, new int[]{
                    R.id.txtFecha, R.id.txtTitulo, R.id.txtid});

            lista.setAdapter(adapter);

        } catch (JSONException jex) {
            Log.e("Programas BD", jex.getMessage());
        }

    }

    //metodo para obtener los eventos acompanantes

    public void getListAcompanantes(String search) {

        AdminSQLiteOffline dbHandler;
        dbHandler = new AdminSQLiteOffline(getActivity(), null, null, 1);
        SQLiteDatabase db = dbHandler.getWritableDatabase();
        cursor = dbHandler.jsonAcompa();

        String respuesta = cursor.getString(0);



        try {

            JSONObject jAcomp = new JSONObject(respuesta);
            String sAcomp = jAcomp.getString("acompanantes");
            JSONArray arAcomp = new JSONArray(sAcomp);

            int cuanto = arAcomp.length();
            albumsList = new ArrayList<HashMap<String, String>>();

            String actFecha = "";
            for (int i = 0; i < arAcomp.length(); i++) {
                JSONObject c = arAcomp.getJSONObject(i);

                HashMap<String, String> map = new HashMap<String, String>();

                String id = "";
                String name = "";
                String proFecha = "";
                String fechaLetra = "";


                if (!search.equals("")) {

                    if (c.getString("nombre").contains(search)) {

                        id = c.getString("id");
                        name = c.getString("nombre");
                        proFecha = c.getString("fecha");

                        fechaLetra = this.getFechaLetra(proFecha);
                        if (actFecha.equals("") || !actFecha.equals(proFecha)) {
                            map.put("acompFecha", fechaLetra);
                            actFecha = proFecha;
                        }
                        map.put("acompId", id);
                        map.put("acompName", name);
                        albumsList.add(map);

                    }
                } else {

                    id = c.getString("id");
                    name = c.getString("nombre");
                    proFecha = c.getString("fecha");

                    fechaLetra = this.getFechaLetra(proFecha);
                    if (actFecha.equals("") || !actFecha.equals(proFecha)) {
                        map.put("acompFecha", fechaLetra);
                        actFecha = proFecha;
                    }
                    map.put("acompId", id);
                    map.put("acompName", name);
                    albumsList.add(map);
                }

            }

            ListAdapter adapter = new SimpleAdapter(
                    getActivity(), albumsList,
                    R.layout.formato_lista_resultados, new String[]{"acompFecha",
                    "acompName", "acompId"}, new int[]{
                    R.id.txtFecha, R.id.txtTitulo, R.id.txtid});

            // updating listview

            lista.setAdapter(adapter);

        } catch (JSONException ex) {
            Log.e("JSON", ex.getMessage());
            ex.printStackTrace();
        }


    }

    //metodo para obtenerl a lista de eventos sociales y deportivos
    public void getListSocialDepo(String search) {
        Log.e("SOCIALDEPO","entro " + search);
        AdminSQLiteOffline dbHandler;
        dbHandler = new AdminSQLiteOffline(getActivity(), null, null, 1);
        SQLiteDatabase db = dbHandler.getWritableDatabase();
        cursor = dbHandler.jsonSocialDepo();

        String respuesta = cursor.getString(0);

        try {

            JSONObject jSocioD = new JSONObject(respuesta);
            String sSocioD = jSocioD.getString("eventos");
            JSONArray arjSocioD = new JSONArray(sSocioD);


            albumsList = new ArrayList<HashMap<String, String>>();

            String actFecha = "";
            // looping through All albums
            for (int i = 0; i < arjSocioD.length(); i++) {
                JSONObject c = arjSocioD.getJSONObject(i);
                HashMap<String, String> map = new HashMap<String, String>();

                String id = "";
                String name = "";
                String proFecha = "";
                String fechaLetra = "";


                if (!search.equals("")) {

                    if (c.getString("nombre").contains(search)) {

                        id = c.getString("id");
                        name = c.getString("nombre");
                        proFecha = c.getString("fecha");

                        fechaLetra = this.getFechaLetra(proFecha);
                        if (actFecha.equals("") || !actFecha.equals(proFecha)) {
                            map.put("acompFecha", fechaLetra);
                            actFecha = proFecha;
                        }
                        map.put("acompId", id);
                        map.put("acompName", name);
                        albumsList.add(map);

                    }
                } else {

                    id = c.getString("id");
                    name = c.getString("nombre");
                    proFecha = c.getString("fecha");

                    fechaLetra = this.getFechaLetra(proFecha);
                    if (actFecha.equals("") || !actFecha.equals(proFecha)) {
                        map.put("acompFecha", fechaLetra);
                        actFecha = proFecha;
                    }
                    map.put("acompId", id);
                    map.put("acompName", name);
                    albumsList.add(map);
                }

            }

            ListAdapter adapter = new SimpleAdapter(
                    getActivity(), albumsList,
                    R.layout.formato_lista_resultados, new String[]{"acompFecha",
                    "acompName", "acompId"}, new int[]{
                    R.id.txtFecha, R.id.txtTitulo, R.id.txtid});

            // updating listview

            lista.setAdapter(adapter);

        } catch (JSONException jex) {
            Log.e("LISTEVENT", jex.getMessage());
            jex.printStackTrace();
        }
    }

    //obtiene la fecha e letras.
    public String getFechaLetra(String fecha) {

        String fechaLetra = "";

        if (fecha.equals("2017-06-05")) {
            fechaLetra = "Lunes 5 de Junio";
        } else if (fecha.equals("2017-06-06")) {
            fechaLetra = "Martes 6 de Junio";
        } else if (fecha.equals("2017-06-07")) {
            fechaLetra = "Miercoles 7 de Junio";
        } else if (fecha.equals("2017-06-08")) {
            fechaLetra = "Jueves 8 de Junio";
        } else if (fecha.equals("2017-06-09")) {
            fechaLetra = "Viernes 9 de Junio";
        } else if (fecha.equals("2017-06-10")) {
            fechaLetra = "Sábado 10 de Junio";
        }

        return fechaLetra;

    }

}