package com.cmp2017.devworms.cmp2017;

import android.app.Activity;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
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
    String resp, userId, apiKey, idProgram, strNomEven, strLugEven, strRecomEven, urlImage, horaIni, horaFin, strdia, seccion;
    TextView txtNombreEven, txtLugarEven, txtRecomendaEven, txtHorario;
    int strcursorEncontrado, posJson;
    ImageView imgFoto;
    URL imageUrl;
    Bitmap imagen;
    HttpURLConnection conn;
    Button btnLocalizar, btnAgreAgenda;
    Cursor cursor;

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

        ////Se busca si este evento ya se encuentra en la base de agenda
        AdminSQLiteAgenda dbHandlerAgenda;
        dbHandlerAgenda = new AdminSQLiteAgenda(getActivity(), null, null, 1);
        SQLiteDatabase db = dbHandlerAgenda.getWritableDatabase();
        Cursor cursor = dbHandlerAgenda.BuscarpoId(idProgram);
        strcursorEncontrado = cursor.getCount();
        ///////////////////////////

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

        switch(cualSeccion){
            case "social" :

                cursor = dbHandler.jsonSocialDepo();
                respuesta = cursor.getString(0);
                cualDetalle = "eventos";

                break;
            case "programas":

                cursor = dbHandler.getJsonProgramas();
                respuesta = cursor.getString(0);
                cualDetalle = "programas";

                break;
            case "acomp":

                cursor = dbHandler.jsonAcompa();
                respuesta = cursor.getString(0);
                cualDetalle = "acompanantes";

                break;
            default:
                Toast.makeText(getActivity(), "Ocurrio un erro inesperado", Toast.LENGTH_SHORT).show();
        }

        try{
            tempEventos = new JSONObject(respuesta);
            detalles = new JSONArray(tempEventos.getString(cualDetalle));
            for (int c=0; c < detalles.length() ; c++) {

                JSONObject detalle = detalles.getJSONObject(c);


                if(detalle.getString("id").equals(idProgram)){

                    strNomEven = detalle.getString("nombre");
                    strLugEven = detalle.getString("lugar");
                    strRecomEven = detalle.getString("recomendaciones");
                    horaIni = detalle.getString("hora_inicio");
                    horaFin = detalle.getString("hora_fin");
                    strdia = detalle.getString("fecha");
                    try{
                        JSONObject foto = new JSONObject(detalle.getString("foto"));
                        new CargarImagen().execute(foto.getString("url"));
                    }catch (JSONException x){
                        Log.e("FOTO","no tenia foto");
                    }


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

            if (strcursorEncontrado == 0) {
                AgregarAgendaEvento();
                strcursorEncontrado = 1;
            } else {
                Toast.makeText(getActivity(), "Ya se encuentra en tu agenda",
                        Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void AgregarAgendaEvento() {

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

        Toast.makeText(getActivity(), "Se guardo en Mi AgendaFragment",
                Toast.LENGTH_SHORT).show();
    }

    class Localizar implements View.OnClickListener {
        public void onClick(View v) {
            Toast.makeText(getActivity(), "Próximamente", Toast.LENGTH_SHORT).show();
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
}
