package com.cmp2017.devworms.cmp2017;

import android.app.Activity;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by mac on 22/03/17.
 */

public class ListAdapterCustom extends ArrayAdapter<String> {

    private final Activity context;
    private final String[] itemname, itemdescrip, itemmapa, itempag;
    private final Bitmap[] itemBipmap;

    public ListAdapterCustom(Activity context, String[] itemname,String[] itemdescrip,String[] itemmapa,String[] itempag, Bitmap[] itemBipmap) {
        super(context, R.layout.formato_lista_sitios_interes, itemname);
        // TODO Auto-generated constructor stub

        this.context=context;

        this.itemname=itemname;
        this.itemdescrip=itemdescrip;
        this.itemmapa=itemmapa;
        this.itempag=itempag;

        this.itemBipmap=itemBipmap;
    }

    public View getView(int posicion, View view, ViewGroup parent){

        LayoutInflater inflater=context.getLayoutInflater();
        View rowView=inflater.inflate(R.layout.formato_lista_sitios_interes,null,true);

        TextView txtNombreLugar = (TextView) rowView.findViewById(R.id.txtNombreLugar);
        ImageView imageView = (ImageView) rowView.findViewById(R.id.imgLugar);
        TextView txtDescrip = (TextView) rowView.findViewById(R.id.txtDescripLugar);
        TextView txtSitioweb = (TextView) rowView.findViewById(R.id.txtSitioweb);


        txtNombreLugar.setText(itemname[posicion]);
        imageView.setImageBitmap(itemBipmap[posicion]);
        txtDescrip.setText(itemdescrip[posicion]);
        txtSitioweb.setText(itempag[posicion]);

        return rowView;
    }


}

