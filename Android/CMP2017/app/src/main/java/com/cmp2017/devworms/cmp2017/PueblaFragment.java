package com.cmp2017.devworms.cmp2017;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
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

    //comentario commit
            getFragmentManager().beginTransaction()
                    .replace(R.id.actividad, new SitiosInteresFragment()).addToBackStack(null).commit();


        }
    }

    class Clima implements View.OnClickListener {
        public void onClick(View v) {


            Fragment fragment = new ClimaFragment();

            Bundle parametro = new Bundle();


            parametro.putString("url","a");
            parametro.putString("tipo","1");


            fragment.setArguments(parametro);

            final FragmentTransaction ft = getActivity().getFragmentManager()
                    .beginTransaction();
            ft.replace(R.id.actividad, fragment, "tag");

            ft.addToBackStack("tag");

            ft.commit();

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


            getFragmentManager().beginTransaction()
                    .replace(R.id.actividad, new MapaPueblaFragment()).addToBackStack(null).commit();


        }
    }
}
