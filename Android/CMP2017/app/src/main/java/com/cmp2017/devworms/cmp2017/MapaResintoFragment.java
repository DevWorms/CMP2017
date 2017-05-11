package com.cmp2017.devworms.cmp2017;

import android.app.Activity;
import android.app.Fragment;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.loopj.android.http.Base64;

import org.json.JSONArray;
import org.json.JSONObject;

import uk.co.senab.photoview.PhotoViewAttacher;

public class MapaResintoFragment extends Fragment {
    private ImageView mapaResinto;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_mapa_resinto, container, false);
        this.mapaResinto = (ImageView) view.findViewById(R.id.imgMapaResinto);
        loadMapa();
        return view;
    }

    public void  loadMapa(){
        AdminSQLiteOffline dbHandler;
        String respuesta = "";
        dbHandler = new AdminSQLiteOffline(getActivity(), null, null, 1);
        SQLiteDatabase db = dbHandler.getWritableDatabase();
        Cursor cImg;
        cImg = dbHandler.getMmapaResinto();

        if(cImg.getCount() > 0){
            // cargamos el mapa
            this.mapaResinto.setImageBitmap(getImage(cImg.getString(0)));

            // hacemos zoomeable el mapa

            PhotoViewAttacher visorFoto = new PhotoViewAttacher(this.mapaResinto);
            float scala = (float)1;
            visorFoto.setScale(scala,true);
            visorFoto.setScaleType(ImageView.ScaleType.FIT_CENTER);
            visorFoto.update();

        }else{
            Toast.makeText(getActivity(), "No hay mapa de resinto", Toast.LENGTH_SHORT).show();
        }
    }

    public static Bitmap getImage(String imageS) {
        byte[] b = Base64.decode(imageS , Base64.DEFAULT);
        Bitmap bmp = BitmapFactory.decodeByteArray(b, 0, b.length);
        return  bmp;
    }

}
