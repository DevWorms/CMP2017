package com.cmp2017.devworms.cmp2017;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.database.Cursor;
import android.graphics.Typeface;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.ImageLoader;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ProgramFragment extends Fragment {

    Spinner spinTipoEven,spinDia;
    AdminSQLiteOffline dbHandlerOffline;
    ConstraintLayout fondoProg;
    View view;
    private  ImageTools imageTools;

    private final static String[] diaSelec = { "Seleciona un día", "Todos", "Lunes 5 de Junio",
            "Martes 6 de Junio", "Miercoles 7 de Junio","Jueves 8 de Junio","Viernes 9 de Junio","Sabado 10 de Junio" };
    ConnectionDetector cd;
    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_program, container, false);
        spinTipoEven = (Spinner)view.findViewById(R.id.spinTipoEven);
        spinDia = (Spinner)view.findViewById(R.id.spinDia);
        TextView txtTituloP = (TextView) view.findViewById(R.id.txtTituloP);
        String font_path = "font/mulibold.ttf";  //definimos un STRING con el valor PATH ( o ruta por                                                                                    //donde tiene que buscar ) de nuetra fuente

        Typeface TF = Typeface.createFromAsset(getActivity().getAssets(),font_path);
        txtTituloP.setTypeface(TF);
        cd = new ConnectionDetector(getActivity());

        dbHandlerOffline = new AdminSQLiteOffline(getActivity(), null, null, 1);
        Cursor rs = dbHandlerOffline.getJsonCategorias();
        String strCategorias = rs.getString(0);
        List<TiposEventos> eventos;
        try{
            JSONObject json = new JSONObject(strCategorias);

            JSONArray jCategoria = new JSONArray(json.getString("categorias"));
            eventos = new ArrayList<TiposEventos>();
            TiposEventos sinEvento = new TiposEventos("Seleciona un tipo de evento",0);
            eventos.add(sinEvento);
            eventos.add(new TiposEventos("Todos",0));
            for(int cont= 0; cont < jCategoria.length() ; cont++){
                JSONObject tempPrograma = jCategoria.getJSONObject(cont);
                eventos.add(new TiposEventos(tempPrograma.getString("nombre"),tempPrograma.getInt("id")));
            }


            ArrayAdapter adapterTiposEven = new ArrayAdapter(getActivity(), android.R.layout.simple_spinner_dropdown_item, eventos);
            spinTipoEven.setAdapter(adapterTiposEven);

        }catch(JSONException jex){
            Log.e("EVENTOS JEX " , jex.getMessage());
        }



        ArrayAdapter adapterDia = new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_spinner_dropdown_item, diaSelec);
        spinDia.setAdapter(adapterDia);



        imageTools = new ImageTools(this.getContext());

        fondoProg = (ConstraintLayout) view.findViewById(R.id.fondoProgFra);



        return view;



    }

    @Override
    public void onResume() {
        super.onResume();
        Button btnBusca = (Button)view.findViewById(R.id.btnSubmit);
        btnBusca.setOnClickListener(new Buscar());
        btnBusca.setBackgroundResource(R.drawable.btnbuscar);
        imageTools.loadBackground(R.drawable.fondo,fondoProg);
    }

    class Buscar implements View.OnClickListener {
        public void onClick(View v) {

        
            if(spinDia.getSelectedItem().toString().equals("Seleciona un día")){
                Toast.makeText(getActivity(), "Seleciona un dia", Toast.LENGTH_SHORT).show();
                return;
            }
            if(spinTipoEven.getSelectedItem().toString().equals("Seleciona un tipo de evento")){
                Toast.makeText(getActivity(), "Seleciona un tipo de evento a buscar", Toast.LENGTH_SHORT).show();
                return;
            }
            Fragment fragment = new ListadoFragment();

            Bundle parametro = new Bundle();

            parametro.putString("diaProgra",spinDia.getSelectedItem().toString());
            // en lugar de mandar el dato mostrado en el spiner mandamos el id
            TiposEventos sentEvento = (TiposEventos) spinTipoEven.getSelectedItem();
            parametro.putInt("tipoEvento",sentEvento.id);
            parametro.putString("seccion","programas");


            fragment.setArguments(parametro);

            final FragmentTransaction ft = getFragmentManager()
                    .beginTransaction();
            ft.replace(R.id.actividad, fragment, "tag");

            ft.addToBackStack("tag");

            ft.commit();




        }
    }

    // para poder mostrar un valor en el spiner y mandar otro (nombre,id)
    class TiposEventos{

        public String nombre;
        public Integer id;

        public TiposEventos(String nombre, Integer id){
            this.nombre = nombre;
            this.id = id;
        }

        @Override
        public  String toString(){
            return this.nombre;
        }
    }
}

