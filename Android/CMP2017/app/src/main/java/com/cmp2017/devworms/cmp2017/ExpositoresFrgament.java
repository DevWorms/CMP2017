package com.cmp2017.devworms.cmp2017;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.system.ErrnoException;
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
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.SimpleCursorAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class ExpositoresFrgament extends Fragment implements View.OnClickListener,AdapterView.OnItemClickListener {

    private String userId, apiKey;
    private String origen;
    private String ordenamiento;
    private ListView lista;
    private AutoCompleteTextView acTextView;
    private TextView txtTitulo;
    private ImageTools tools;
    private LinearLayout fragExpo;
    private Button btnOrdenByAlf;
    private Button btnOrdenByNum;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_expo, container, false);

        //procesamos las imagenes bajo manejador
        tools = new ImageTools(getActivity());
        fragExpo = (LinearLayout) view.findViewById(R.id.fragExpo);
        tools.loadBackground(R.drawable.fondo,fragExpo);

        //INICIALIZAMOS LOS VALORES PARA EL USUARIO
        SharedPreferences sp = getActivity().getSharedPreferences("prefe", Activity.MODE_PRIVATE);
        String inicioComo = sp.getString("Nombre","");

        if (inicioComo.equals("invi")){
           apiKey = "0";
           userId = "1";
        } else{
           apiKey = sp.getString("APIkey","");
           userId = sp.getString("IdUser","");
        }

        // INICIALIZAMOS COMPONENTES DE LA PANTALLA
        txtTitulo= (TextView) view.findViewById(R.id.txtDescrip);
        lista=  (ListView) view.findViewById(R.id.lvExpo);
        btnOrdenByAlf = (Button) view.findViewById(R.id.btnOrdAlf);
        btnOrdenByNum = (Button) view.findViewById(R.id.btnNumStand);
        acTextView = (AutoCompleteTextView) view.findViewById(R.id.autoCompleteEcpo);

        //AGREGAMOS LISTENERS DE CLICK
        btnOrdenByAlf.setOnClickListener(this);
        btnOrdenByNum.setOnClickListener(this);
        lista.setOnItemClickListener(this);

        // COMO SE OCUPA EL MISMO FRAGMENT Y DETALLA FRAGMEN OBTENEMOS EL ORIGEN
        this.origen = getArguments().getString("origen");
        //CONFIGURAMOS INICIO SEGUN ORIGEN
        if(this.origen.equals("e")){
            txtTitulo.setText("Expositores");
            this.getExpositoresAlf("");
        }else if(this.origen.equals("p")){
            txtTitulo.setText("Patrocinadores");
            this.getPatrocinadoresAlf("");
        }else if(this.origen.equals("me")){
            this.txtTitulo.setText("Mis expositores");
            this.getMisExpo("alf","");

        }



        this.ordenamiento = "alf";

        //inicializamos buscador
        searchInit();

        return view;

    }


    // IMPLEMENTACION METODO CLICK, DEPENDE EL BOTON CLICKEADO
    @Override
    public void onClick(View clicked) {
        //obtenemos el texto de ese momento en el buscador
        String busqueda = acTextView.getEditableText().toString();
        if(clicked == btnOrdenByAlf){
            if(this.origen.equals("e")){
                getExpositoresAlf(busqueda);
            }else if(this.origen.equals("p")){
                getPatrocinadoresAlf(busqueda);
            }else if(this.origen.equals("me")){
                this.getMisExpo("alf","");
            }

            this.ordenamiento = "alf";

        }else if(clicked == btnOrdenByNum){

            if(this.origen.equals("e")){
                getExpositoresNum(busqueda);
            }else if(this.origen.equals("p")){
                getPatrocinadoresNum(busqueda);
            }else if(this.origen.equals("me")){

                this.getMisExpo("num","");

            }

            this.ordenamiento = "num";
        }
    }

    // IMPLEMENTACION METODO CLICK ITEM LISTA
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        String strId = ((TextView) view.findViewById(R.id.txtid)).getText().toString();
        Fragment fragment = new DetalleExpoFragment();
        Bundle parametro = new Bundle();
        parametro.putString("origen", this.origen);
        parametro.putString("ordenamiento", this.ordenamiento);
        parametro.putString("id",strId);
        fragment.setArguments(parametro);

        final FragmentTransaction ft = getFragmentManager()
                .beginTransaction();

        ft.replace(R.id.actividad, fragment, "tag");

        ft.addToBackStack("tag");

        ft.commit();
    }

    //OBTIENE EXPOSITORES ALFABETICO Y LLENA LA LISTA
    public void getExpositoresAlf(String busqueda){

        //OBTENEMOS DE LA BASE DE DATOS
        AdminSQLiteOffline dbHandler;
        dbHandler = new AdminSQLiteOffline(getActivity(), null, null, 1);
        SQLiteDatabase db = dbHandler.getWritableDatabase();
        Cursor cExpAlf=  dbHandler.jsonAlfa();

        //Lista y MAP PARA SETEAR VALORES
        ArrayList<HashMap<String, String>> listValues;
        HashMap<String,String> value;


        listValues = new ArrayList<HashMap<String, String>>();

        String[] headers = new String[]{"id", "nombre","letra"};
        int[] outputs =new int[]{R.id.txtid, R.id.txtNombre,R.id.txtLetra};
        try{
            JSONObject jExpAlf = new JSONObject(cExpAlf.getString(0));

            JSONArray aExpAlf = new JSONArray(jExpAlf.getString("expositores"));
            String inicial = "";
            String lastIncial = "";

            for(int i=0 ; i<aExpAlf.length() ; i++){

                JSONObject jExpoTemp = aExpAlf.getJSONObject(i);
                value = new HashMap<String,String>();
                value.put("id",jExpoTemp.getString("id"));
                value.put("nombre",jExpoTemp.getString("nombre"));



                if(!busqueda.equals("")){
                    if(jExpoTemp.getString("nombre").contains(busqueda)){

                        lastIncial = jExpoTemp.getString("nombre").substring(0,1);

                        if(!inicial.equals(lastIncial)){

                            inicial = lastIncial;

                            String mayusInicial = inicial.toUpperCase();
                            value.put("letra",mayusInicial);

                        }else{
                            value.put("letra","");
                        }

                        listValues.add(value);
                    }
                }else{

                    lastIncial = jExpoTemp.getString("nombre").substring(0,1);

                    if(!inicial.equals(lastIncial)){

                        inicial = lastIncial;

                        String mayusInicial = inicial.toUpperCase();
                        value.put("letra",mayusInicial);

                    }else{
                        value.put("letra","");
                    }

                    listValues.add(value);
                }

            }

            ListAdapter adapter = new SimpleAdapter(
                    getActivity(), listValues,
                    R.layout.formato_lista_alfabetica,
                    headers,
                    outputs);

            this.lista.setAdapter(adapter);


        }catch(JSONException jex){
            jex.printStackTrace();
        }


    }
    
    //OBTIENE EXPOSITORES NUMERICO Y LLENA LA LISTA
    public void getExpositoresNum(String busqueda){
        //OBTENEMOS DE LA BASE DE DATOS
        AdminSQLiteOffline dbHandler;
        dbHandler = new AdminSQLiteOffline(getActivity(), null, null, 1);
        SQLiteDatabase db = dbHandler.getWritableDatabase();
        Cursor cExpNum=  dbHandler.jsonNume();

        //Lista y MAP PARA SETEAR VALORES
        ArrayList<HashMap<String, String>> listValues;
        HashMap<String,String> value;


        listValues = new ArrayList<HashMap<String, String>>();

        String[] headers = new String[]{"id", "nombre","letra"};
        int[] outputs =new int[]{R.id.txtid, R.id.txtNombre,R.id.txtLetra};
        try{
            JSONObject jExpNum = new JSONObject(cExpNum.getString(0));

            JSONArray aExpNum = new JSONArray(jExpNum.getString("expositores"));
            String inicial = "";
            String lastIncial = "";

            for(int i=0 ; i<aExpNum.length() ; i++){

                JSONObject jExpoTemp = aExpNum.getJSONObject(i);
                value = new HashMap<String,String>();
                value.put("id",jExpoTemp.getString("id"));
                value.put("nombre",jExpoTemp.getString("nombre"));



                if(!busqueda.equals("")){
                    if(jExpoTemp.getString("nombre").contains(busqueda)){

                        lastIncial = jExpoTemp.getString("stand");

                        if(!inicial.equals(lastIncial)){

                            inicial = lastIncial;

                            String mayusInicial = inicial.toUpperCase();
                            value.put("letra","Stand " + mayusInicial);

                        }else{
                            value.put("letra","");
                        }

                        listValues.add(value);
                    }
                }else{

                    lastIncial = jExpoTemp.getString("stand");

                    if(!inicial.equals(lastIncial)){

                        inicial = lastIncial;

                        String mayusInicial = inicial.toUpperCase();
                        value.put("letra","Stand " + mayusInicial);

                    }else{
                        value.put("letra","");
                    }

                    listValues.add(value);
                }

            }

            ListAdapter adapter = new SimpleAdapter(
                    getActivity(), listValues,
                    R.layout.formato_lista_alfabetica,
                    headers,
                    outputs);

            this.lista.setAdapter(adapter);


        }catch(JSONException jex){
            jex.printStackTrace();
        }
    }

    //OBTIENE PATROCINADORES ALFABETICO Y LLENA LISTA
    public void getPatrocinadoresAlf(String busqueda){
        //OBTENEMOS DE LA BASE DE DATOS
        AdminSQLiteOffline dbHandler;
        dbHandler = new AdminSQLiteOffline(getActivity(), null, null, 1);
        SQLiteDatabase db = dbHandler.getWritableDatabase();
        Cursor cPatAlf=  dbHandler.jsonPatroAlfa();

        //Lista y MAP PARA SETEAR VALORES
        ArrayList<HashMap<String, String>> listValues;
        HashMap<String,String> value;


        listValues = new ArrayList<HashMap<String, String>>();

        String[] headers = new String[]{"id", "nombre","letra"};
        int[] outputs =new int[]{R.id.txtid, R.id.txtNombre,R.id.txtLetra};
        try{
            JSONObject jPatAlf = new JSONObject(cPatAlf.getString(0));

            JSONArray aPatAlf = new JSONArray(jPatAlf.getString("patrocinadores"));
            String inicial = "";
            String lastIncial = "";

            for(int i=0 ; i<aPatAlf.length() ; i++){

                JSONObject jExpoTemp = aPatAlf.getJSONObject(i);
                value = new HashMap<String,String>();
                value.put("id",jExpoTemp.getString("id"));
                value.put("nombre",jExpoTemp.getString("nombre"));



                if(!busqueda.equals("")){
                    if(jExpoTemp.getString("nombre").contains(busqueda)){

                        lastIncial = jExpoTemp.getString("nombre").substring(0,1);

                        if(!inicial.equals(lastIncial)){

                            inicial = lastIncial;

                            String mayusInicial = inicial.toUpperCase();
                            value.put("letra",mayusInicial);

                        }else{
                            value.put("letra","");
                        }

                        listValues.add(value);
                    }
                }else{

                    lastIncial = jExpoTemp.getString("nombre").substring(0,1);

                    if(!inicial.equals(lastIncial)){

                        inicial = lastIncial;

                        String mayusInicial = inicial.toUpperCase();
                        value.put("letra",mayusInicial);

                    }else{
                        value.put("letra","");
                    }

                    listValues.add(value);
                }

            }

            ListAdapter adapter = new SimpleAdapter(
                    getActivity(), listValues,
                    R.layout.formato_lista_alfabetica,
                    headers,
                    outputs);

            this.lista.setAdapter(adapter);


        }catch(JSONException jex){
            jex.printStackTrace();
        }

    }

    //OBTIENE PATROCINADORES NUMERICO Y LLENA LISTA
    public void getPatrocinadoresNum(String busqueda){
        //OBTENEMOS DE LA BASE DE DATOS
        AdminSQLiteOffline dbHandler;
        dbHandler = new AdminSQLiteOffline(getActivity(), null, null, 1);
        SQLiteDatabase db = dbHandler.getWritableDatabase();
        Cursor cPatNum=  dbHandler.jsonPatroNume();

        //Lista y MAP PARA SETEAR VALORES
        ArrayList<HashMap<String, String>> listValues;
        HashMap<String,String> value;


        listValues = new ArrayList<HashMap<String, String>>();

        String[] headers = new String[]{"id", "nombre","letra"};
        int[] outputs =new int[]{R.id.txtid, R.id.txtNombre,R.id.txtLetra};
        try{
            JSONObject jPatNum = new JSONObject(cPatNum.getString(0));

            JSONArray aPatNum = new JSONArray(jPatNum.getString("patrocinadores"));
            String inicial = "";
            String lastIncial = "";

            for(int i=0 ; i<aPatNum.length() ; i++){

                JSONObject jExpoTemp = aPatNum.getJSONObject(i);
                value = new HashMap<String,String>();
                value.put("id",jExpoTemp.getString("id"));
                value.put("nombre",jExpoTemp.getString("nombre"));



                if(!busqueda.equals("")){
                    if(jExpoTemp.getString("nombre").contains(busqueda)){

                        lastIncial = jExpoTemp.getString("stand");

                        if(!inicial.equals(lastIncial)){

                            inicial = lastIncial;

                            String mayusInicial = inicial.toUpperCase();
                            value.put("letra","Stand " + mayusInicial);

                        }else{
                            value.put("letra","");
                        }

                        listValues.add(value);
                    }
                }else{

                    lastIncial = jExpoTemp.getString("stand");

                    if(!inicial.equals(lastIncial)){

                        inicial = lastIncial;

                        String mayusInicial = inicial.toUpperCase();
                        value.put("letra","Stand " + mayusInicial);

                    }else{
                        value.put("letra","");
                    }

                    listValues.add(value);
                }

            }

            ListAdapter adapter = new SimpleAdapter(
                    getActivity(), listValues,
                    R.layout.formato_lista_alfabetica,
                    headers,
                    outputs);

            this.lista.setAdapter(adapter);


        }catch(JSONException jex){
            jex.printStackTrace();
        }
    }

    public void getMisExpo(String ordenamiento, String busqueda){

        AdminSQLiteOpenHelper dbHandler;
        dbHandler = new AdminSQLiteOpenHelper(getActivity(), null, null, 2);
        SQLiteDatabase db = dbHandler.getWritableDatabase();
        Cursor cursor = null;
        String[] from = null;
        int[] to= null;
        if(busqueda.equals("")){

            if(ordenamiento.equals("alf")){
                cursor = dbHandler.listarpersonas();
                from = new String[]{"idExpo", "nombre"};
                to = new int[]{
                        R.id.txtid,
                        R.id.txtNombre
                };

            }else if(ordenamiento.equals("num")){
                cursor = dbHandler.listarpersonasStand();
                from = new String[]{"idExpo", "nombre", "stand"};
                to = new int[]{
                        R.id.txtid,
                        R.id.txtNombre,
                        R.id.txtLetra
                };
            }

        }else{
            cursor = dbHandler.listarpersonasb(busqueda);
            from = new String[]{"idExpo", "nombre"};
            to = new int[]{
                    R.id.txtid,
                    R.id.txtNombre
            };
        }


        cursor.getColumnCount();

        SimpleCursorAdapter cursorAdapter = new SimpleCursorAdapter(getActivity(), R.layout.formato_lista_alfabetica, cursor, from, to);
        lista.setAdapter(cursorAdapter);

    }

   //METODO CREACION BUSCADOR implementacion
    public void searchInit(){
        this.acTextView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String busqueda = s.toString();
                if(origen.equals("e")){
                    if(ordenamiento.equals("alf")){
                        getExpositoresAlf(busqueda);
                    }else if(ordenamiento.equals("num")){
                       getExpositoresNum(busqueda);
                    }
                }else if(origen.equals("p")){

                    if(ordenamiento.equals("alf")){
                        getPatrocinadoresAlf(busqueda);
                    }else if(ordenamiento.equals("num")){
                        getPatrocinadoresNum(busqueda);
                    }
                }else if(origen.equals("me")){
                    getMisExpo("alf",busqueda);
                }
            }
        });
    }

}
