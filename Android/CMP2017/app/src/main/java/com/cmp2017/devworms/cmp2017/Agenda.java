package com.cmp2017.devworms.cmp2017;

import android.app.Fragment;
import android.content.Intent;
import android.graphics.Color;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTabHost;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.TabHost;
import android.widget.Toast;

public class Agenda extends Fragment implements View.OnClickListener{
    private Button btnLunes;
    private Button btnMartes;
    private Button btnMiercoles;
    private Button btnJueves;
    private Button btnViernes;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_agenda, container, false);

        // botones para los dias de la agenda
        this.btnLunes = (Button) view.findViewById(R.id.btnLunes);
        this.btnMartes = (Button) view.findViewById(R.id.btnMartes);
        this.btnMiercoles = (Button) view.findViewById(R.id.btnMiercoles);
        this.btnJueves = (Button) view.findViewById(R.id.btnJueves);
        this.btnViernes = (Button) view.findViewById(R.id.btnViernes);

        // les agregamos un listener para el click
        this.btnLunes.setOnClickListener(this);
        this.btnMartes.setOnClickListener(this);
        this.btnMiercoles.setOnClickListener(this);
        this.btnJueves.setOnClickListener(this);
        this.btnViernes.setOnClickListener(this);

        return view;

    }

    @Override
    public void onClick(View clicked) {

        int colorNormal = Color.WHITE;
        int colorResalto = Color.YELLOW;

        if(clicked == this.btnLunes){

            this.btnLunes.setTextColor(colorResalto);
            this.btnMartes.setTextColor(colorNormal);
            this.btnMiercoles.setTextColor(colorNormal);
            this.btnJueves.setTextColor(colorNormal);
            this.btnViernes.setTextColor(colorNormal);

        }else if(clicked == this.btnMartes){

            this.btnLunes.setTextColor(colorNormal);
            this.btnMartes.setTextColor(colorResalto);
            this.btnMiercoles.setTextColor(colorNormal);
            this.btnJueves.setTextColor(colorNormal);
            this.btnViernes.setTextColor(colorNormal);

        }else if(clicked == this.btnMiercoles){

            this.btnLunes.setTextColor(colorNormal);
            this.btnMartes.setTextColor(colorNormal);
            this.btnMiercoles.setTextColor(colorResalto);
            this.btnJueves.setTextColor(colorNormal);
            this.btnViernes.setTextColor(colorNormal);

        }else if(clicked == this.btnJueves){

            this.btnLunes.setTextColor(colorNormal);
            this.btnMartes.setTextColor(colorNormal);
            this.btnMiercoles.setTextColor(colorNormal);
            this.btnJueves.setTextColor(colorResalto);
            this.btnViernes.setTextColor(colorNormal);

        }else if(clicked == this.btnViernes){

            this.btnLunes.setTextColor(colorNormal);
            this.btnMartes.setTextColor(colorNormal);
            this.btnMiercoles.setTextColor(colorNormal);
            this.btnJueves.setTextColor(colorNormal);
            this.btnViernes.setTextColor(colorResalto);
        }
    }
}
