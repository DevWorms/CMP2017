package com.cmp2017.devworms.cmp2017;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

public class MapaFragment extends Fragment

{

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_mapa, container, false);

        Button btnMapaExpo= (Button) view.findViewById(R.id.btnMapExp);
        btnMapaExpo.setOnClickListener(new MapaExpo());

        return view;


    }
    class MapaExpo implements View.OnClickListener {
        public void onClick(View v) {

            Fragment fragment = new ClimaFragment();

            Bundle parametro = new Bundle();


            parametro.putString("url","http://congreso.digital/public-map.php");
            parametro.putString("tipo","3");


            fragment.setArguments(parametro);

            final FragmentTransaction ft = getActivity().getFragmentManager()
                    .beginTransaction();
            ft.replace(R.id.actividad, fragment, "tag");

            ft.addToBackStack("tag");

            ft.commit();


        }
    }
}
