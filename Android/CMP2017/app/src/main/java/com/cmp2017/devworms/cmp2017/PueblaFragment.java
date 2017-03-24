package com.cmp2017.devworms.cmp2017;

import android.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

public class PueblaFragment extends Fragment

{

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_puebla, container, false);

        Button btnSitiosInt = (Button) view.findViewById(R.id.btnSitiosInteres);
        btnSitiosInt.setOnClickListener(new SitiosInteres());

        Button btnTelefonos = (Button) view.findViewById(R.id.btnTelefonos);
        btnTelefonos.setOnClickListener(new Telefonos());

        Button btnClima = (Button) view.findViewById(R.id.btnClima);
        btnClima.setOnClickListener(new Clima());

        Button btnMapa = (Button) view.findViewById(R.id.btnMapa);
        btnMapa.setOnClickListener(new Mapa());
        return view;


    }

    class SitiosInteres implements View.OnClickListener {
        public void onClick(View v) {


            getFragmentManager().beginTransaction()
                    .replace(R.id.actividad, new SitiosInteresFragment()).addToBackStack(null).commit();


        }
    }

    class Clima implements View.OnClickListener {
        public void onClick(View v) {


            getFragmentManager().beginTransaction()
                    .replace(R.id.actividad, new ClimaFragment()).addToBackStack(null).commit();


        }
    }

    class Telefonos implements View.OnClickListener {
        public void onClick(View v) {


            getFragmentManager().beginTransaction()
                    .replace(R.id.actividad, new TelefonosFragment()).addToBackStack(null).commit();


        }
    }
    class Mapa implements View.OnClickListener {
        public void onClick(View v) {


            Toast.makeText(getActivity(),"En Desarrollo",Toast.LENGTH_SHORT).show();



        }
    }
}
