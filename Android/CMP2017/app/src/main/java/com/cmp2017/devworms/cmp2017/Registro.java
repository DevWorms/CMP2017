package com.cmp2017.devworms.cmp2017;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

public class Registro extends AppCompatActivity {

    Spinner spiTipoUsu, spiAso;

    private final static String[] tipoUsuarios = { "Visitante", "Trabajador de Pemex", "Estudiante",
            "Staff", "Otro" };
    private final static String[] asociaciones = { "Asociacion 1", "Asociacion 2", "Asociacion 3",
            "Asociacion 4", "Ninguna" };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);
        spiTipoUsu = (Spinner)findViewById(R.id.spiTipoUse);
        spiAso = (Spinner)findViewById(R.id.spiAso);
        spiTipoUsu.setPrompt("Tipo de Usuarios");
        spiAso.setPrompt("Asociaciones");

        ArrayAdapter adapterTiposUsu = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_dropdown_item, tipoUsuarios);
        spiTipoUsu.setAdapter(adapterTiposUsu);

        ArrayAdapter adapterAsocia = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_dropdown_item, asociaciones);
        spiAso.setAdapter(adapterAsocia);
    }

}
