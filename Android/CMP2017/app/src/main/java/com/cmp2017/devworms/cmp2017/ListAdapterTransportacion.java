package com.cmp2017.devworms.cmp2017;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by mac on 29/03/17.
 */

public class ListAdapterTransportacion extends ArrayAdapter<String> {

    private final Activity context;
    private final String[] itemname, itemdescrip, itemurl;


    public ListAdapterTransportacion(Activity context, String[] itemname,String[] itemdescrip, String[] itemurl) {
        super(context, R.layout.formato_lista_sitios_interes, itemname);
        // TODO Auto-generated constructor stub

        this.context=context;

        this.itemname=itemname;
        this.itemdescrip=itemdescrip;
        this.itemurl=itemurl;


    }

    public View getView(final int posicion, View view, ViewGroup parent){

        LayoutInflater inflater=context.getLayoutInflater();
        View rowView=inflater.inflate(R.layout.formato_transportacion,null,true);

        TextView txtNombreLugar = (TextView) rowView.findViewById(R.id.txtNombre);

        TextView txtDescrip = (TextView) rowView.findViewById(R.id.txtDescrip);
        Button btnDetalleRuta = (Button) rowView.findViewById(R.id.btnDetalleRuta);

        btnDetalleRuta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Fragment fragment = new PDFFragment();

                Bundle parametro = new Bundle();

                parametro.putString("url",itemurl[posicion]);
                parametro.putInt("posicion",posicion);

                fragment.setArguments(parametro);

                final FragmentTransaction ft = context.getFragmentManager()
                        .beginTransaction();
                ft.replace(R.id.actividad, fragment, "tag");

                ft.addToBackStack("tag");

                ft.commit();


            }
        });

        txtNombreLugar.setText(itemname[posicion]);

        txtDescrip.setText(itemdescrip[posicion]);


        return rowView;
    }
}
