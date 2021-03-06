package com.cmp2017.devworms.cmp2017;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.internal.bind.ArrayTypeAdapter;
import com.loopj.android.http.Base64;
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

import static com.loopj.android.http.AsyncHttpClient.log;

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
    private ImageTools tools;
    //constructoe recibimos la lsita de objetos
    public ListAdapterAgenda(Context context, List<AgendaModel> objetos, Activity a) {
        super(context, R.layout.formato_lista_agenda, objetos);
        this.contex = context;
        this.actividad = a;
        this.rowsAgenda = objetos;
        this.tools = new ImageTools(context);
    }


    @Override
    public View getView(final int position, View vista, ViewGroup parent) {

        LayoutInflater inflater = this.actividad.getLayoutInflater();
        View formato = inflater.inflate(R.layout.formato_lista_agenda, null, true);

        if(posicionColor == 1){
            formato.setBackgroundColor(Color.parseColor("#d8ecff"));
            log.d("Color","uno");
            posicionColor =2;
        }else if(posicionColor == 2){
            formato.setBackgroundColor(Color.parseColor("#d8ffff"));
            posicionColor = 3;
            log.d("Color","dos");
        }else if(posicionColor == 3){
            formato.setBackgroundColor(Color.parseColor("#ecffd8"));
            posicionColor = 1;
            log.d("Color","tres");
        }

        this.posicionGeneral = position;
        this.miniatura = (ImageView) formato.findViewById(R.id.miniAgendaImg);
        this.descripcionAgenda = (TextView) formato.findViewById(R.id.descAgenda);
        this.horarioAgenda = (TextView) formato.findViewById(R.id.horarioAgenda);

        // ponemos de manera directa los textos
        this.descripcionAgenda.setText(this.rowsAgenda.get(position).getNombreEvento());
        this.horarioAgenda.setText(this.rowsAgenda.get(position).getHorarioEvento());


        AdminSQLiteOffline dbHandler;
        dbHandler = new AdminSQLiteOffline(actividad, null, null, 1);
        SQLiteDatabase db = dbHandler.getWritableDatabase();
        Cursor cImg;
        if(rowsAgenda.get(posicionGeneral).getTipoEvento().equals("social")){
            cImg = dbHandler.ImagenPorIdSocialDepo(rowsAgenda.get(posicionGeneral).getIdEvento());
            if(cImg.getCount() > 0){
                Bitmap   originalBitmap  = getImage(cImg.getString(0));
                //creamos el drawable redondeado
                RoundedBitmapDrawable roundedDrawable =
                        RoundedBitmapDrawableFactory.create(actividad.getResources(), originalBitmap);


                Matrix matrix = new Matrix();
                // resize the Bitmap
                matrix.postScale(2, 2);

                // volvemos a crear la imagen con los nuevos valores
                Bitmap resizedBitmap = Bitmap.createBitmap(originalBitmap, 0, 0,
                        200, 200, matrix, true);
                //asignamos el CornerRadius
                roundedDrawable.setCornerRadius(resizedBitmap.getHeight());
                roundedDrawable.setCircular(true);
                this.miniatura.setImageDrawable(roundedDrawable);
            }

        }else if(rowsAgenda.get(posicionGeneral).getTipoEvento().equals("programas")){
            cImg = dbHandler.imagenPorPrograma(rowsAgenda.get(posicionGeneral).getIdEvento());
            if(cImg.getCount() > 0){
                Bitmap   originalBitmap  = getImage(cImg.getString(0));
                //creamos el drawable redondeado
                RoundedBitmapDrawable roundedDrawable =
                        RoundedBitmapDrawableFactory.create(actividad.getResources(), originalBitmap);

                //asignamos el CornerRadius
                roundedDrawable.setCornerRadius(originalBitmap.getHeight());
                roundedDrawable.setCircular(true);
                this.miniatura.setImageDrawable(roundedDrawable);
            }
        }else if(rowsAgenda.get(posicionGeneral).getTipoEvento().equals("acomp")){
            cImg = dbHandler.ImagenPorIdAco(rowsAgenda.get(posicionGeneral).getIdEvento());
            if(cImg.getCount() > 0){
                Bitmap   originalBitmap  = getImage(cImg.getString(0));
                //creamos el drawable redondeado
                RoundedBitmapDrawable roundedDrawable =
                        RoundedBitmapDrawableFactory.create(actividad.getResources(), originalBitmap);

                //asignamos el CornerRadius
                roundedDrawable.setCornerRadius(originalBitmap.getHeight());
                roundedDrawable.setCircular(true);

                this.miniatura.setImageDrawable(roundedDrawable);

            }
        }
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
    public static Bitmap getImage(String imageS) {
        byte[] b = Base64.decode(imageS , Base64.DEFAULT);
        Bitmap bmp = BitmapFactory.decodeByteArray(b, 0, b.length);
        return  bmp;
    }

}
