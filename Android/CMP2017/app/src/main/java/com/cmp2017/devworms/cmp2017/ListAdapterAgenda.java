package com.cmp2017.devworms.cmp2017;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Rect;
import android.os.AsyncTask;
import android.os.Bundle;
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
import com.nostra13.universalimageloader.cache.disc.naming.HashCodeFileNameGenerator;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;

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
    private static int posicionColor = 1;
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

        if(posicionColor == 1){
            formato.setBackgroundColor(Color.CYAN);
            posicionColor++;
        }else if(posicionColor == 2){
            formato.setBackgroundColor(Color.LTGRAY);
            posicionColor++;
        }else if(posicionColor == 3){
            formato.setBackgroundColor(Color.YELLOW);
            posicionColor = 1;
        }

        this.posicionGeneral = position;
        this.miniatura = (ImageView) formato.findViewById(R.id.miniAgendaImg);
        this.descripcionAgenda = (TextView) formato.findViewById(R.id.descAgenda);
        this.horarioAgenda = (TextView) formato.findViewById(R.id.horarioAgenda);

        // ponemos de manera directa los textos
        this.descripcionAgenda.setText(this.rowsAgenda.get(position).getNombreEvento());
        this.horarioAgenda.setText(this.rowsAgenda.get(position).getHorarioEvento());

        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(this.actividad)
                .discCacheFileNameGenerator(new HashCodeFileNameGenerator())
                .build();

        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .displayer(new RoundedBitmapDisplayer(350)) //rounded corner bitmap
                .cacheInMemory(true)
                .cacheOnDisc(true)
                .build();

        ImageLoader imageLoader = ImageLoader.getInstance();
        imageLoader.init(config);
        imageLoader.displayImage(this.rowsAgenda.get(position).getUrlImagen(),this.miniatura, options);

        // asignamos el evento onclick para que lleve al detalle
        formato.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String idProgram= rowsAgenda.get(posicionGeneral).getIdEvento();
                Fragment fragment = new DetalleEventoFragment();

                Bundle parametro = new Bundle();


                parametro.putString("idProgram",idProgram);
                parametro.putString("seccion",rowsAgenda.get(posicionGeneral).getTipoEvento());
                parametro.putInt("posicion",0);

                fragment.setArguments(parametro);

                final FragmentTransaction ft = actividad.getFragmentManager()
                        .beginTransaction();
                ft.replace(R.id.actividad, fragment, "tag");

                ft.addToBackStack("tag");

                ft.commit();
            }
        });

        return formato;
    }


}
