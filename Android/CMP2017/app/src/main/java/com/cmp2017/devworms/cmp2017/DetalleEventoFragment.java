package com.cmp2017.devworms.cmp2017;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.Base64;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

public class DetalleEventoFragment extends Fragment {
    ProgressDialog pDialog;
    String resp, userId, apiKey, idProgram, strNomEven, strLugEven, strRecomEven, urlMapa, horaIni, horaFin, strdia, seccion;
    TextView txtNombreEven, txtLugarEven, txtRecomendaEven, txtHorario;
    int strcursorEncontrado, posJson;
    ImageView imgFoto;
    URL imageUrl;
    Bitmap imagen;
    HttpURLConnection conn;
    Button btnLocalizar, btnAgreAgenda;
    Cursor cursor;
    ImageTools tools;
    LinearLayout linearDetalleEvent;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_detalle_evento, container, false);
        SharedPreferences sp = getActivity().getSharedPreferences("prefe", Activity.MODE_PRIVATE);
        apiKey = sp.getString("APIkey", "");
        userId = sp.getString("IdUser", "");
        imgFoto = (ImageView) view.findViewById(R.id.imgFoto);
        idProgram = getArguments().getString("idProgram");
        seccion = getArguments().getString("seccion");
        Log.e("SECCION RECIBIDA",seccion);
        posJson = getArguments().getInt("posicion");
        txtNombreEven = (TextView) view.findViewById(R.id.txtNombreEven);
        txtLugarEven = (TextView) view.findViewById(R.id.txtLugarEven);
        txtRecomendaEven = (TextView) view.findViewById(R.id.txtRecomEven);
        txtHorario = (TextView) view.findViewById(R.id.txtHorario);
        btnLocalizar = (Button) view.findViewById(R.id.btnLocalizar);
        btnLocalizar.setOnClickListener(new Localizar());
        btnAgreAgenda = (Button) view.findViewById(R.id.btnAgreExpo);
        btnAgreAgenda.setOnClickListener(new AgregarAgenda());
        linearDetalleEvent = (LinearLayout) view.findViewById(R.id.linearDetalleEvent);
        tools = new ImageTools(getActivity());
        tools.loadBackground(R.drawable.fondo,linearDetalleEvent);
        ////Se busca si este evento ya se encuentra en la base de agenda
        // y si esta entonces cambiamos a boton borrar
        if(estaEnAgenda()){
            this.btnAgreAgenda.setBackgroundResource(R.drawable.btnborraragenda);
        }

        loadDetalleEventos(seccion);

        return view;


    }

    public void loadDetalleEventos(String cualSeccion){
        AdminSQLiteOffline dbHandler;
        String respuesta = "";
        JSONObject tempEventos;
        JSONArray detalles;
        String cualDetalle = "";
        dbHandler = new AdminSQLiteOffline(getActivity(), null, null, 1);
        SQLiteDatabase db = dbHandler.getWritableDatabase();
        Cursor cImg;
        switch(cualSeccion){
            case "social" :

                cursor = dbHandler.jsonSocialDepo();
                respuesta = cursor.getString(0);
                cualDetalle = "eventos";

                cImg = dbHandler.ImagenPorIdSocialDepo(idProgram);
                if(cImg.getCount()>0){
                    tools.loadByBytesToImageViewCenterCrop(cImg.getString(0),imgFoto);
                }


                break;
            case "programas":

                cursor = dbHandler.getJsonProgramas();
                respuesta = cursor.getString(0);
                cualDetalle = "programas";
                Log.e("CURSORPROGRAMA",cursor.getCount()+"");
                Log.e("PROGRGRAL",respuesta);
                cImg = dbHandler.imagenPorPrograma(idProgram);

                if(cImg.getCount() > 0){
                    tools.loadByBytesToImageViewCenterCrop(cImg.getString(0),imgFoto);
                }


                break;
            case "acomp":

                cursor = dbHandler.jsonAcompa();
                respuesta = cursor.getString(0);

                cualDetalle = "acompanantes";
                cImg = dbHandler.ImagenPorIdAco(idProgram);
                if(cImg.getCount() > 0){
                    tools.loadByBytesToImageViewCenterCrop(cImg.getString(0),imgFoto);
                }


                break;
            default:
                Toast.makeText(getActivity(), "Ocurrio un erro inesperado", Toast.LENGTH_SHORT).show();
        }

        try{
            tempEventos = new JSONObject(respuesta);
            detalles = new JSONArray(tempEventos.getString(cualDetalle));
            Log.e("DETALLES",detalles.toString());
            for (int c=0; c < detalles.length() ; c++) {

                JSONObject detalle = detalles.getJSONObject(c);


                if(detalle.getString("id").equals(idProgram)){

                    strNomEven = detalle.getString("nombre");
                    strLugEven = detalle.getString("lugar");
                    strRecomEven = detalle.getString("recomendaciones");
                    horaIni = detalle.getString("hora_inicio");
                    horaFin = detalle.getString("hora_fin");
                    strdia = detalle.getString("fecha");
                    urlMapa =  detalle.getString("maps_url");



                }

            }

            txtNombreEven.setText(strNomEven);
            txtLugarEven.setText(strLugEven);
            txtRecomendaEven.setText(strRecomEven);
            txtHorario.setText(strdia + " \nDe " + horaIni + " a" + horaFin);


        }catch(JSONException jex){
            Log.e("Carga Programa deta",jex.getMessage());
        }
    }

    class AgregarAgenda implements View.OnClickListener {
        public void onClick(View v) {
            if (!estaEnAgenda()) {
                agregarAgendaEvento();
            } else {
                borrarAgendaEvento();
            }
        }
    }

    public boolean estaEnAgenda(){

        AdminSQLiteAgenda dbHandlerAgenda;
        dbHandlerAgenda = new AdminSQLiteAgenda(getActivity(), null, null, 1);
        SQLiteDatabase db = dbHandlerAgenda.getWritableDatabase();
        Cursor cursor = dbHandlerAgenda.BuscarpoId(idProgram);
        if(cursor.getCount() > 0){
            return true;
        }
        return false;
    }

    public void agregarAgendaEvento() {

        AdminSQLiteAgenda dbHandlerAgenda;
        dbHandlerAgenda = new AdminSQLiteAgenda(getActivity(), null, null, 1);
        SQLiteDatabase db = dbHandlerAgenda.getWritableDatabase();
        String strurl = "drawable://" + R.drawable.noimagebox;
        try{
            strurl = imageUrl.toString();
        }catch (NullPointerException nex){
            Log.e("IMAGEN","no tiene");
        }
        dbHandlerAgenda.addEvento(idProgram, txtNombreEven.getText().toString(), strdia, horaIni, horaFin,seccion,strurl);
        //this.btnAgreAgenda.setBackgroundResource(R.drawable.btnborraragenda);
        tools.loadBackground(R.drawable.btnborraragenda,btnAgreAgenda);
        Toast.makeText(getActivity(), "Se guardo en Mi AgendaFragment",
                Toast.LENGTH_SHORT).show();
    }


    public void borrarAgendaEvento(){

        AdminSQLiteAgenda dbHandlerAgenda;
        dbHandlerAgenda = new AdminSQLiteAgenda(getActivity(), null, null, 1);
        SQLiteDatabase db = dbHandlerAgenda.getWritableDatabase();
        //borramos el evento
        dbHandlerAgenda.borrarEvento(idProgram,seccion);
        //this.btnAgreAgenda.setBackgroundResource(R.drawable.btnagregaragenda);
        tools.loadBackground(R.drawable.btnagregaragenda,btnAgreAgenda);
        // si la borramos ponemos el boton en agregar de nuevo
        Toast.makeText(getActivity(), "Evento removido de la agenda",
                Toast.LENGTH_SHORT).show();
    }
    class Localizar implements View.OnClickListener {
        public void onClick(View v) {
            if (urlMapa== null || urlMapa == "null") {
                Toast.makeText(getActivity(), "No tiene ninguna localización",
                        Toast.LENGTH_SHORT).show();
            }else{
                Fragment fragment = new ClimaFragment();

                Bundle parametro = new Bundle();


                parametro.putString("url", urlMapa);
                parametro.putString("tipo", "3");


                fragment.setArguments(parametro);

                final FragmentTransaction ft = getActivity().getFragmentManager()
                        .beginTransaction();
                ft.replace(R.id.actividad, fragment, "tag");

                ft.addToBackStack("tag");

                ft.commit();
            }
        }
    }

    public void mostrarImagen(String url) {


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

    class CargarImagen extends AsyncTask<String,String,String>{

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(getActivity());
            pDialog.setMessage("Cargando imagen del evento");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
            mostrarImagen(params[0]);
            return null;
        }

        @Override
        protected void onPostExecute(String str) {
            pDialog.dismiss();

        }
    }

    public static Bitmap getImage(String imageS) {
        byte[] b = Base64.decode(imageS , Base64.DEFAULT);
        Bitmap bmp = BitmapFactory.decodeByteArray(b, 0, b.length);
        return  bmp;
    }
}
