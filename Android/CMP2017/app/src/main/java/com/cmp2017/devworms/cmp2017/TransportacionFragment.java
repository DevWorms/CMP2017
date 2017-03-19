package com.cmp2017.devworms.cmp2017;

import android.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

public class TransportacionFragment extends Fragment

{

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_transportacion, container, false);


        Button btnRevisaRutas = (Button)view.findViewById(R.id.btnRevisaRutas);
        btnRevisaRutas.setOnClickListener(new RevisaRuta());

        return view;


    }

    class RevisaRuta implements View.OnClickListener {
        public void onClick(View v) {


            getFragmentManager().beginTransaction()
                    .replace(R.id.actividad, new RevisaRutaFragment()).commit();


        }
    }
}
