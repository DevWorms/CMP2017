package com.cmp2017.devworms.cmp2017;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.database.Cursor;
import android.graphics.Typeface;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ProgramFragment extends Fragment {

    Spinner spinTipoEven,spinDia;
    AdminSQLiteOffline dbHandlerOffline;



    private final static String[] diaSelec = { "Seleciona un día", "Todos", "Lunes 5 de Junio",
            "Martes 6 de Junio", "Miercoles 7 de Junio","Jueves 8 de Junio","Viernes 9 de Junio","Sabado 10 de Junio" };
    ConnectionDetector cd;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_program, container, false);
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
        String tipoEvento[];
        try{
            JSONObject json = new JSONObject(strCategorias);

            JSONArray jCategoria = new JSONArray(json.getString("categorias"));
            tipoEvento = new String[jCategoria.length() + 2];
            tipoEvento [0] = "Seleciona un tipo de evento";
            int longitudFinal = 0;
            for(int cont= 0; cont < jCategoria.length() ; cont++){
                JSONObject tempPrograma = jCategoria.getJSONObject(cont);
                tipoEvento[cont + 1] = tempPrograma.getInt("id") + "-" +  tempPrograma.getString("nombre");
                longitudFinal +=1;
                Log.e("TIPO EVENTO",tipoEvento[cont]);
            }
            tipoEvento[longitudFinal + 1] = "0-TODOS";
            ArrayAdapter adapterTiposEven = new ArrayAdapter<String>(getActivity(),
                    android.R.layout.simple_spinner_dropdown_item, tipoEvento);
            spinTipoEven.setAdapter(adapterTiposEven);

        }catch(JSONException jex){
            Log.e("EVENTOS JEX " , jex.getMessage());
        }



        ArrayAdapter adapterDia = new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_spinner_dropdown_item, diaSelec);
        spinDia.setAdapter(adapterDia);

        Button btnBusca = (Button)view.findViewById(R.id.btnSubmit);
        btnBusca.setOnClickListener(new Buscar());

        return view;



    }

    class Buscar implements View.OnClickListener {
        public void onClick(View v) {

            if (!cd.isConnectingToInternet()) {
                // Internet Connection is not present
                Toast.makeText(getActivity(), "Se necesita internet", Toast.LENGTH_SHORT).show();
                // stop executing code by return
                return;
            }
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
            parametro.putString("tipoProgra",spinTipoEven.getSelectedItem().toString());
            parametro.putString("seccion","programas");


            fragment.setArguments(parametro);

            final FragmentTransaction ft = getFragmentManager()
                    .beginTransaction();
            ft.replace(R.id.actividad, fragment, "tag");

            ft.addToBackStack("tag");

            ft.commit();




        }
    }
}
