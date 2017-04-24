package com.cmp2017.devworms.cmp2017;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTabHost;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class AgendaFragment extends Fragment implements View.OnClickListener{
    private Button btnLunes;
    private Button btnMartes;
    private Button btnMiercoles;
    private Button btnJueves;
    private Button btnViernes;
    private Button btnSabado;
    private ListView listaAgenda;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_agenda, container, false);

        // botones para los dias de la agenda
        this.btnLunes = (Button) view.findViewById(R.id.btnLunes);
        this.btnMartes = (Button) view.findViewById(R.id.btnMartes);
        this.btnMiercoles = (Button) view.findViewById(R.id.btnMiercoles);
        this.btnJueves = (Button) view.findViewById(R.id.btnJueves);
        this.btnViernes = (Button) view.findViewById(R.id.btnViernes);
        this.btnSabado = (Button) view.findViewById(R.id.btnSabado);
        // les agregamos un listener para el click
        this.btnLunes.setOnClickListener(this);
        this.btnMartes.setOnClickListener(this);
        this.btnMiercoles.setOnClickListener(this);
        this.btnJueves.setOnClickListener(this);
        this.btnViernes.setOnClickListener(this);
        this.btnSabado.setOnClickListener(this);
        this.listaAgenda = (ListView) view.findViewById(R.id.lvAgenda);
        //por default cargamos el lunes
        this.loadEventoPorDia("2017-06-05");
        return view;

    }

    @Override
    public void onClick(View clicked) {

        int colorNormal = Color.WHITE;
        int colorResalto = Color.YELLOW;
        String dias[] = {"2017-06-05",
                "2017-06-06",
                "2017-06-07",
                "2017-06-08",
                "2017-06-09",
                "2017-06-10"};

        if(clicked == this.btnLunes){

            this.btnLunes.setTextColor(colorResalto);
            this.btnMartes.setTextColor(colorNormal);
            this.btnMiercoles.setTextColor(colorNormal);
            this.btnJueves.setTextColor(colorNormal);
            this.btnViernes.setTextColor(colorNormal);
            this.btnSabado.setTextColor(colorNormal);
            this.loadEventoPorDia(dias[0]);
        }else if(clicked == this.btnMartes){

            this.btnLunes.setTextColor(colorNormal);
            this.btnMartes.setTextColor(colorResalto);
            this.btnMiercoles.setTextColor(colorNormal);
            this.btnJueves.setTextColor(colorNormal);
            this.btnViernes.setTextColor(colorNormal);
            this.btnSabado.setTextColor(colorNormal);
            this.loadEventoPorDia(dias[1]);
        }else if(clicked == this.btnMiercoles){

            this.btnLunes.setTextColor(colorNormal);
            this.btnMartes.setTextColor(colorNormal);
            this.btnMiercoles.setTextColor(colorResalto);
            this.btnJueves.setTextColor(colorNormal);
            this.btnViernes.setTextColor(colorNormal);
            this.btnSabado.setTextColor(colorNormal);
            this.loadEventoPorDia(dias[2]);

        }else if(clicked == this.btnJueves){

            this.btnLunes.setTextColor(colorNormal);
            this.btnMartes.setTextColor(colorNormal);
            this.btnMiercoles.setTextColor(colorNormal);
            this.btnJueves.setTextColor(colorResalto);
            this.btnViernes.setTextColor(colorNormal);
            this.btnSabado.setTextColor(colorNormal);
            this.loadEventoPorDia(dias[3]);

        }else if(clicked == this.btnViernes){

            this.btnLunes.setTextColor(colorNormal);
            this.btnMartes.setTextColor(colorNormal);
            this.btnMiercoles.setTextColor(colorNormal);
            this.btnJueves.setTextColor(colorNormal);
            this.btnViernes.setTextColor(colorResalto);
            this.btnSabado.setTextColor(colorNormal);
            this.loadEventoPorDia(dias[4]);

        }else if(clicked == this.btnSabado){

            this.btnLunes.setTextColor(colorNormal);
            this.btnMartes.setTextColor(colorNormal);
            this.btnMiercoles.setTextColor(colorNormal);
            this.btnJueves.setTextColor(colorNormal);
            this.btnViernes.setTextColor(colorNormal);
            this.btnSabado.setTextColor(colorResalto);
            this.loadEventoPorDia(dias[5]);
        }
    }

    public void loadEventoPorDia(String dia){
        //instanceo objeto de la BD

        AdminSQLiteAgenda dbHandlerAgenda;
        dbHandlerAgenda = new AdminSQLiteAgenda(getActivity(), null, null, 1);
        SQLiteDatabase db = dbHandlerAgenda.getWritableDatabase();

        Cursor resultados = dbHandlerAgenda.listaPorDia(dia);



        List<AgendaModel> modelo = new ArrayList<AgendaModel>();
        AgendaModel elemento;
        String tempHorario;
        int cuantos = resultados.getCount();
        for (int c = 0; c < cuantos ; c++){

                elemento = new AgendaModel();
                elemento.setIdEvento(resultados.getString(1));
                elemento.setNombreEvento(resultados.getString(2));
                elemento.setDiaEvento(resultados.getString(3));
                tempHorario = new String(resultados.getString(4) + "\n" + resultados.getString(5));
                elemento.setHorarioEvento(tempHorario);
                elemento.setTipoEvento(resultados.getString(6));
                elemento.setUrlImagen(resultados.getString(7));
                modelo.add(elemento);

                resultados.moveToNext();

        }
        Context c = getActivity();

        ListAdapterAgenda adapterAgenda = new ListAdapterAgenda(c,modelo,getActivity());
        this.listaAgenda.setAdapter(adapterAgenda);
    }
}
