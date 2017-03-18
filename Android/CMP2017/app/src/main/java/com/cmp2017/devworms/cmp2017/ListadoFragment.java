package com.cmp2017.devworms.cmp2017;

import android.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

public class ListadoFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_listado, container, false);

      String nombre = getArguments() != null ? getArguments().getString("nombre"):"Resultados";
        TextView txtTitulo= (TextView) view.findViewById(R.id.txtTituloListado);
        txtTitulo.setText(nombre);
        return view;



    }
}
