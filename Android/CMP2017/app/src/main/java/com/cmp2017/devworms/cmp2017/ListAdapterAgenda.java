package com.cmp2017.devworms.cmp2017;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.os.AsyncTask;
import android.support.annotation.IdRes;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.internal.bind.ArrayTypeAdapter;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by AndrewAlan on 23/04/2017.
 */

public class ListAdapterAgenda extends ArrayAdapter<AgendaModel> {

    // declaramos los elementos del view y la lista que recibiremos
    private List<AgendaModel> rowsAgenda;
    private ImageView miniatura;
    private TextView descripcionAgenda;
    private TextView horarioAgenda;
    private ProgressDialog pDialog;
    private Context contex;
    private Bitmap imagen;
    private Activity actividad;
    private int posicionGeneral;
    //constructoe recibimos la lsita de objetos
    public ListAdapterAgenda(Context context, List<AgendaModel> objetos, Activity a) {
        super(context, R.layout.formato_lista_agenda, objetos);
        this.contex = context;
        this.actividad = a;
        this.rowsAgenda = objetos;
    }


    @Override
    public View getView(final int position, View vista, ViewGroup parent) {

        LayoutInflater inflater = this.actividad.getLayoutInflater();
        View formato = inflater.inflate(R.layout.formato_lista_agenda, null, true);

        this.posicionGeneral = position;
        this.miniatura = (ImageView) formato.findViewById(R.id.miniAgendaImg);
        this.descripcionAgenda = (TextView) formato.findViewById(R.id.descAgenda);
        this.horarioAgenda = (TextView) formato.findViewById(R.id.horarioAgenda);

        // ponemos de manera directa los textos
        this.descripcionAgenda.setText(this.rowsAgenda.get(position).getNombreEvento());
        this.horarioAgenda.setText(this.rowsAgenda.get(position).getHorarioEvento());
        //las imagenes son cargadas por url
        CargarImagen cargar = new CargarImagen();
        cargar.execute(this.rowsAgenda.get(position).getUrlImagen());

        // asignamos el evento onclick para que lleve al detalle
        formato.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String ruta = "Seccion: " + rowsAgenda.get(posicionGeneral).getTipoEvento() + " Id evento " +  rowsAgenda.get(posicionGeneral).getIdEvento();
                Toast.makeText(actividad, ruta, Toast.LENGTH_LONG).show();
            }
        });

        return formato;
    }

    public void mostrarImagen(String url) {

        try {

            URL imageUrl = new URL(url);
            HttpURLConnection conn = (HttpURLConnection) imageUrl.openConnection();
            conn.connect();

            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inSampleSize = 2; // el factor de escala a minimizar la imagen, siempre es potencia de 2

            imagen = BitmapFactory.decodeStream(conn.getInputStream(), new Rect(0, 0, 0, 0), options);

            actividad.runOnUiThread(new Runnable() {
                public void run() {

                    miniatura.setImageBitmap(imagen);

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
            pDialog = new ProgressDialog(actividad);
            pDialog.setMessage("Cargando imagenes de eventos");
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
