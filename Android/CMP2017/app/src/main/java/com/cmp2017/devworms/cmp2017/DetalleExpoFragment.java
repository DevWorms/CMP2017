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
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.HashMap;

import static android.R.attr.bitmap;

public class DetalleExpoFragment extends Fragment implements View.OnClickListener {

    private String userId, apiKey;
    private TextView txtNomEmpre, txtPagina, txtTelefono, txtCorreo, txtAcercaDe;
    private ProgressDialog pDialog;
    private ImageView imgFoto;
    private URL imageUrl;
    private String imagen;
    private String inicioComo;
    private Button btnLocalizar, btnAgreExpo, btnPresent;
    private ImageTools tools;
    private LinearLayout linearExpo;
    private String ordenamiento;
    private String origen;
    private String strId;
    private String nombre;
    private String pagina;
    private String telefono;
    private String correo;
    private String acercaDe;
    private String stand;
    private String urlPresenta;
    private int cursorEncontrado;
    private String strImagen;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        // inflamos vista
        View view = inflater.inflate(R.layout.fragment_detalle_expo, container, false);


        //inicializamos los atributos del usuario
        SharedPreferences sp = getActivity().getSharedPreferences("prefe", Activity.MODE_PRIVATE);
        inicioComo = sp.getString("Nombre", "");
        if (inicioComo.equals("invi")) {
            apiKey = "0";
            userId = "1";
        } else {
            apiKey = sp.getString("APIkey", "");
            userId = sp.getString("IdUser", "");
        }

        //OBTENEMOS ARGUMENTOS
        this.origen = getArguments().getString("origen");
        this.ordenamiento = getArguments().getString("ordenamiento");
        this.strId = getArguments().getString("id");

        //IMAGENES CARGADAS POR MANEJADOR
        tools = new ImageTools(getActivity());
        linearExpo = (LinearLayout) view.findViewById(R.id.linearExpo);
        tools.loadBackground(R.drawable.fondo, linearExpo);


        // INICIALIZAMOS LOS COMPONENTES DE LA VISTA
        txtNomEmpre = (TextView) view.findViewById(R.id.txtNomEmpre);
        txtPagina = (TextView) view.findViewById(R.id.txtPaginaExpo);
        txtTelefono = (TextView) view.findViewById(R.id.txtTelefono);
        txtCorreo = (TextView) view.findViewById(R.id.txtEmail);
        txtAcercaDe = (TextView) view.findViewById(R.id.txtAcercaDe);
        imgFoto = (ImageView) view.findViewById(R.id.imgFoto);
        btnLocalizar = (Button) view.findViewById(R.id.btnLocalizar);
        btnLocalizar.setOnClickListener(this);
        btnAgreExpo = (Button) view.findViewById(R.id.btnAgreExpo);
        btnAgreExpo.setOnClickListener(this);
        btnPresent = (Button) view.findViewById(R.id.btnPresent);
        btnPresent.setOnClickListener(this);

        //SI ES PATROCINADOR NO HAY BOTON AGREGAR
        if (this.origen.equals("e") || this.origen.equals("me")) {
            btnAgreExpo.setVisibility(View.VISIBLE);

            // VEMOS SI EL EXPOSITOR YA ESTA AGREGADO
            AdminSQLiteOpenHelper dbHandler;
            dbHandler = new AdminSQLiteOpenHelper(getActivity(), null, null, 1);
            SQLiteDatabase db = dbHandler.getWritableDatabase();
            Cursor cursor = dbHandler.personabyid(this.strId);
            cursorEncontrado = cursor.getCount();

            if (cursorEncontrado == 1) {
                btnAgreExpo.setBackground(getResources().getDrawable(R.drawable.btneliminarexpo));
            }

        } else if (this.origen.equals("p")) {
            btnAgreExpo.setVisibility(View.INVISIBLE);
        }

        // ponemos lso valores en la vista
        setPatroExpo(origen, strId, ordenamiento);

        return view;


    }

    //METODO QUE LLENA LA VISTA CON VALORES SEGUN CONFIGURACION
    public void setPatroExpo(String origen, String id, String ordenamiento) {
        if (origen.equals("me")) {

            AdminSQLiteOpenHelper dbHandler;
            dbHandler = new AdminSQLiteOpenHelper(getActivity(), null, null, 1);
            SQLiteDatabase db = dbHandler.getWritableDatabase();
            Cursor cursor = dbHandler.personabyid(id);

            txtNomEmpre.setText(cursor.getString(2));
            txtAcercaDe.setText(cursor.getString(3));
            txtTelefono.setText(cursor.getString(4));
            txtCorreo.setText(cursor.getString(5));
            txtPagina.setText(cursor.getString(6));

            String strImagen = cursor.getString(8);

            tools.loadByBytesToImageView(strImagen,imgFoto);

        } else {
            //OBTENEMOS DE LA BASE DE DATOS
            AdminSQLiteOffline dbHandler;
            dbHandler = new AdminSQLiteOffline(getActivity(), null, null, 1);
            SQLiteDatabase db = dbHandler.getWritableDatabase();
            Cursor cursor = null;
            Cursor cImg = null;
            String data = "";
            if (origen.equals("e")) {
                if (ordenamiento.equals("alf")) {
                    cursor = dbHandler.jsonAlfa();

                } else if (ordenamiento.equals("num")) {
                    cursor = dbHandler.jsonNume();

                }
                cImg = dbHandler.ImagenPorId(strId);
                data = "expositores";

            } else if (origen.equals("p")) {
                if (ordenamiento.equals("alf")) {
                    cursor = dbHandler.jsonPatroAlfa();
                } else if (ordenamiento.equals("num")) {
                    cursor = dbHandler.jsonPatroNume();
                }

                cImg = dbHandler.ImagenPorIdPatro(strId);
                data = "patrocinadores";
            }

            try {

                JSONObject jObj = new JSONObject(cursor.getString(0));

                JSONArray jArr = new JSONArray(jObj.getString(data));


                for (int i = 0; i < jArr.length(); i++) {
                    JSONObject temp = jArr.getJSONObject(i);

                    if (strId.equals(temp.getString("id"))) {

                        nombre = temp.getString("nombre");
                        pagina = temp.getString("url");
                        telefono = temp.getString("telefono");
                        correo = temp.getString("email");
                        acercaDe = temp.getString("acerca");
                        stand = temp.getString("stand");

                        String urlPreseJson = temp.getString("pdf");
                        if (!urlPreseJson.equals("[]")) {
                            JSONObject jsonPrese = new JSONObject(urlPreseJson);
                            this.urlPresenta = jsonPrese.getString("url");
                        }


                    }
                }

                // seteamos valores
                this.txtAcercaDe.setText(acercaDe);
                this.txtCorreo.setText(correo);
                this.txtPagina.setText(pagina);
                this.txtNomEmpre.setText(nombre);
                this.txtTelefono.setText(telefono);

                //seteo la imagen
                if (cImg.getCount() > 0) {
                    this.strImagen = cImg.getString(0);
                    this.tools.loadByBytesToImageView(this.strImagen, this.imgFoto);
                }

                // obtenemso el pdf de presentacion


            } catch (JSONException jex) {
                jex.printStackTrace();
            }
        }


    }

    //IMPLEMENTACION DE EVENTO CLICK PARA BOTONES
    @Override
    public void onClick(View clicked) {
        if (clicked == btnAgreExpo) {

            if (inicioComo.equals("invi")) {
                Toast.makeText(getActivity(), "Registrate para activar esta función", Toast.LENGTH_SHORT).show();
            } else {
                if (cursorEncontrado == 1) {
                    EliminarExpoFav();
                } else {
                    AgregarExpoFav();
                }
            }

        } else if (clicked == btnLocalizar) {

            Fragment fragment = new ClimaFragment();

            Bundle parametro = new Bundle();


            parametro.putString("url", "http://congreso.digital/public-map.php#" + strId);
            parametro.putString("tipo", "3");


            fragment.setArguments(parametro);

            final FragmentTransaction ft = getActivity().getFragmentManager()
                    .beginTransaction();
            ft.replace(R.id.actividad, fragment, "tag");

            ft.addToBackStack("tag");

            ft.commit();

        } else if (clicked == btnPresent) {

            Fragment fragment = new ClimaFragment();

            Bundle parametro = new Bundle();

            Log.d("Url Presentacion : ", "> " + urlPresenta);
            parametro.putString("url", urlPresenta);
            parametro.putString("tipo", "2");


            fragment.setArguments(parametro);

            final FragmentTransaction ft = getActivity().getFragmentManager()
                    .beginTransaction();
            ft.replace(R.id.actividad, fragment, "tag");

            ft.addToBackStack("tag");

            ft.commit();

        } else {
            Toast.makeText(getActivity(), "Sin accion", Toast.LENGTH_SHORT).show();
        }

    }

    public void EliminarExpoFav() {

        AdminSQLiteOpenHelper dbHandler;
        dbHandler = new AdminSQLiteOpenHelper(getActivity(), null, null, 1);
        SQLiteDatabase db = dbHandler.getWritableDatabase();
        dbHandler.borrarPersona(strId);
        btnAgreExpo.setBackground(getResources().getDrawable(R.drawable.btnagregarespo));
        cursorEncontrado = 0;
        Toast.makeText(getActivity(), "Se Elimino de Mis expositores", Toast.LENGTH_SHORT).show();
    }

    public void AgregarExpoFav() {
        AdminSQLiteOpenHelper dbHandler;
        dbHandler = new AdminSQLiteOpenHelper(getActivity(), null, null, 1);
        SQLiteDatabase db = dbHandler.getWritableDatabase();

        if(this.strImagen.equals("") || this.strImagen == null){
            this.strImagen = "";
        }

        dbHandler.addExpo(strId, txtNomEmpre.getText().toString(), txtAcercaDe.getText().toString(), txtTelefono.getText().toString(), txtCorreo.getText().toString(), txtPagina.getText().toString(), urlPresenta, this.strImagen, "Stand " + stand);


        btnAgreExpo.setBackground(getResources().getDrawable(R.drawable.btneliminarexpo));
        cursorEncontrado = 1;
        Toast.makeText(getActivity(), "Se guardo en Mis expositores", Toast.LENGTH_SHORT).show();
    }

}
