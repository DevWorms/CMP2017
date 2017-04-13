package com.cmp2017.devworms.cmp2017;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by AndrewAlan on 11/04/2017.
 */

public class ListAdapterEncuestas extends ArrayAdapter<EncuestaModel> {

    private EncuestaModel[] modelo;
    private Activity contexto;
    private ImageView imagenEncuesta;
    private Button botonEncuesta;

    public ListAdapterEncuestas(Activity contexto, EncuestaModel[] modelo) {
        super(contexto, R.layout.formato_lista_encuestas, modelo);
        this.modelo = modelo;
        this.contexto = contexto;

    }

    public View getView(final int posicion, View view, ViewGroup parent) {

        LayoutInflater inflater = this.contexto.getLayoutInflater();
        View rowView = inflater.inflate(R.layout.formato_lista_encuestas, null, true);
        this.imagenEncuesta = (ImageView) rowView.findViewById(R.id.imagenEncuesta);
        this.botonEncuesta = (Button) rowView.findViewById(R.id.btnEncuesta);

        this.botonEncuesta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment = new EncuestaDetalleFragment();

                Bundle parametro = new Bundle();
                parametro.putInt("idEncuesta",modelo[posicion].getId());
                fragment.setArguments(parametro);

                FragmentTransaction ft = contexto.getFragmentManager()
                        .beginTransaction();

                ft.replace(R.id.actividad, fragment, "tag");

                ft.addToBackStack("tag");

                ft.commit();
            }
        });
        contexto.runOnUiThread(new Runnable() {
            public void run() {

                imagenEncuesta.setImageBitmap(modelo[posicion].getImagen());

            }
        });


        return rowView;
    }


}