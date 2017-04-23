package com.cmp2017.devworms.cmp2017;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Paint;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import org.w3c.dom.Text;

/**
 * Created by mac on 22/03/17.
 */

public class ListAdapterCustom extends ArrayAdapter<String> {

    private final Activity context;
    private final String[] itemname, itemdescrip, itemmapa, itempag;
    private final Bitmap[] itemBipmap;
    private String paginaWeb;
    private String googleMaps;

    public ListAdapterCustom(Activity context, String[] itemname, String[] itemdescrip, String[] itemmapa, String[] itempag, Bitmap[] itemBipmap) {
        super(context, R.layout.formato_lista_sitios_interes, itemname);
        // TODO Auto-generated constructor stub

        this.context = context;

        this.itemname = itemname;
        this.itemdescrip = itemdescrip;
        this.itemmapa = itemmapa;
        this.itempag = itempag;

        this.itemBipmap = itemBipmap;
    }

    public View getView(int posicion, View view, ViewGroup parent) {

        LayoutInflater inflater = context.getLayoutInflater();
        View rowView = inflater.inflate(R.layout.formato_lista_sitios_interes, null, true);

        TextView txtNombreLugar = (TextView) rowView.findViewById(R.id.txtNombreLugar);
        ImageView imageView = (ImageView) rowView.findViewById(R.id.imgLugar);
        TextView txtDescrip = (TextView) rowView.findViewById(R.id.txtDescripLugar);
        TextView txtSitioweb = (TextView) rowView.findViewById(R.id.txtSitioweb);
        TextView txtMap = (TextView) rowView.findViewById(R.id.txtUrlMapLugar);

        txtNombreLugar.setText(itemname[posicion]);
        imageView.setImageBitmap(itemBipmap[posicion]);
        txtDescrip.setText(itemdescrip[posicion]);
        txtSitioweb.setText(itempag[posicion]);
        //txtMap.setText(itemmapa[posicion]);
        paginaWeb = itempag[posicion];
        txtSitioweb.setPaintFlags(Paint.UNDERLINE_TEXT_FLAG);
        txtMap.setPaintFlags(Paint.UNDERLINE_TEXT_FLAG);
        txtSitioweb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = null;
                intent = new Intent(intent.ACTION_VIEW, Uri.parse(paginaWeb));
                context.startActivity(intent);
            }
        });
        googleMaps = itemmapa[posicion];
        txtMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = null;
                intent = new Intent(intent.ACTION_VIEW, Uri.parse(googleMaps));
                context.startActivity(intent);
            }
        });
        return rowView;
    }


}

