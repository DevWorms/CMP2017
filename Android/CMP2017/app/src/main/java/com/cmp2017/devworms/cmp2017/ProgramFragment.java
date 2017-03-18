package com.cmp2017.devworms.cmp2017;

import android.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;

public class ProgramFragment extends Fragment {

    private final static String[] tipoEvento = { "Seleciona un tipo de evento", " Todos", "Sesiones Técnicas",
            "Comidas Conferencias","e-Poster", "Otros" };
    private final static String[] diaSelec = { "Seleciona un día ", "Todos", "Lunes 5 de Junio",
            "Martes 6 de Junio", "Miercoles 7 de Junio","Jueves 8 de Junio","Viernes 9 de Junio" };
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_program, container, false);
        Spinner spinTipoEven = (Spinner)view.findViewById(R.id.spinTipoEven);
        Spinner spinDia = (Spinner)view.findViewById(R.id.spinDia);

        ArrayAdapter adapterTiposEven = new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_spinner_dropdown_item, tipoEvento);
        spinTipoEven.setAdapter(adapterTiposEven);

        ArrayAdapter adapterDia = new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_spinner_dropdown_item, diaSelec);
        spinDia.setAdapter(adapterDia);



        return view;



    }

    class SecProgram implements View.OnClickListener {
        public void onClick(View v) {







        }
    }
}
