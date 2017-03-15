package com.cmp2017.devworms.cmp2017;

import android.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

public class ExpositoresFrgament extends Fragment {


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_expo, container, false);


        String nombre = getArguments() != null ? getArguments().getString("nombre"):"Expositores";
        TextView txtTitulo= (TextView) view.findViewById(R.id.txtDescrip);
        txtTitulo.setText(nombre);

        return view;



    }
}
