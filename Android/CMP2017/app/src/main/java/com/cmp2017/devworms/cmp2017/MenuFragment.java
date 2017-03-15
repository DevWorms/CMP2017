package com.cmp2017.devworms.cmp2017;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.app.ListFragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import static android.R.attr.fragment;

public class MenuFragment extends Fragment {


    @Override


    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_menu, container, false);

        ImageView imgbtnProgramas = (ImageView) view.findViewById(R.id.imagbtnPrograma);
        imgbtnProgramas.setOnClickListener(new SecProgram());

        ImageView imagbtnExpos= (ImageView) view.findViewById(R.id.imagbtnExpos);
        imagbtnExpos.setOnClickListener(new SecExpo());

        ImageView imagbtnEvenAcom= (ImageView) view.findViewById(R.id.imagbtnAcom);
        imagbtnEvenAcom.setOnClickListener(new SecAcom());

        ImageView imagbtnSocialDep= (ImageView) view.findViewById(R.id.imagbtnSociaDepo);
        imagbtnSocialDep.setOnClickListener(new SecSociaDep());

        ImageView imagbtnPatro= (ImageView) view.findViewById(R.id.imagbtnPatro);
        imagbtnPatro.setOnClickListener(new SecPatro());

        ImageView imagbtnMapa= (ImageView) view.findViewById(R.id.imagbtnMapas);
        imagbtnMapa.setOnClickListener(new SecMapa());

        ImageView imagbtnTrans= (ImageView) view.findViewById(R.id.imagbtnTransp);
        imagbtnTrans.setOnClickListener(new SecTrans());
        return view;



    }

    class SecProgram implements View.OnClickListener {
        public void onClick(View v) {


            getFragmentManager().beginTransaction()
                    .replace(R.id.actividad, new ProgramFragment()).commit();


        }
    }


    class SecExpo implements View.OnClickListener {
        public void onClick(View v) {


            getFragmentManager().beginTransaction()
                    .replace(R.id.actividad, new ExpositoresFrgament()).commit();


        }
    }

    class SecAcom implements View.OnClickListener {
        public void onClick(View v) {

            Fragment fragment = new ListadoFragment();

            Bundle parametro = new Bundle();

            parametro.putString("nombre","Eventos de \n Acompañantes");


            fragment.setArguments(parametro);

            final FragmentTransaction ft = getFragmentManager()
                    .beginTransaction();
            ft.replace(R.id.actividad, fragment, "tag");

            ft.addToBackStack("tag");

            ft.commit();
            /*ListadoFragment fragment = new ListadoFragment();


            Bundle args = new Bundle();
            args.putString("nombre","Eventos de Acompañantes");
            fragment.setArguments(args);
            getFragmentManager().beginTransaction()
                    .replace(R.id.actividad, new ListadoFragment()).commit();*/


        }
    }

    class SecSociaDep implements View.OnClickListener {
        public void onClick(View v) {

            Fragment fragment = new ListadoFragment();

            Bundle parametro = new Bundle();

            parametro.putString("nombre","Eventos de Sociales \n y Deportivos");


            fragment.setArguments(parametro);

            final FragmentTransaction ft = getFragmentManager()
                    .beginTransaction();
            ft.replace(R.id.actividad, fragment, "tag");

            ft.addToBackStack("tag");

            ft.commit();
            /*ListadoFragment fragment = new ListadoFragment();


            Bundle args = new Bundle();
            args.putString("nombre","Eventos de Acompañantes");
            fragment.setArguments(args);
            getFragmentManager().beginTransaction()
                    .replace(R.id.actividad, new ListadoFragment()).commit();*/


        }
    }

    class SecPatro implements View.OnClickListener {
        public void onClick(View v) {

            Fragment fragment = new ExpositoresFrgament();

            Bundle parametro = new Bundle();

            parametro.putString("nombre","Patrocinadores");


            fragment.setArguments(parametro);

            final FragmentTransaction ft = getFragmentManager()
                    .beginTransaction();
            ft.replace(R.id.actividad, fragment, "tag");

            ft.addToBackStack("tag");

            ft.commit();



        }
    }

    class SecMapa implements View.OnClickListener {
        public void onClick(View v) {


            getFragmentManager().beginTransaction()
                    .replace(R.id.actividad, new MapaFragment()).commit();


        }
    }

    class SecTrans implements View.OnClickListener {
        public void onClick(View v) {


            getFragmentManager().beginTransaction()
                    .replace(R.id.actividad, new TransportacionFragment()).commit();


        }
    }
}
