package com.cmp2017.devworms.cmp2017;

import android.app.Fragment;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

public class MapaPueblaFragment extends Fragment

{

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_mapa_puebla, container, false);
        ImageView imgVPuebla = (ImageView) view.findViewById(R.id.imgPuebla);



        Bitmap mapa = BitmapFactory.decodeResource(getActivity().getResources(),
                R.drawable.mapapuebla);


        imgVPuebla.setImageBitmap(mapa);

        return view;


    }
}
