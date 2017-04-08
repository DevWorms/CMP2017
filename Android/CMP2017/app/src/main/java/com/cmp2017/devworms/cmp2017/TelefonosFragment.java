package com.cmp2017.devworms.cmp2017;

import android.app.Fragment;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

public class TelefonosFragment extends Fragment

{
    TextView txtAmbulancias, txtPlocias, txtBomberos,txtTaxisPuebla, txtTaxisSitio;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_telefonos, container, false);

        txtAmbulancias = (TextView) view.findViewById(R.id.txtAmbula);
        txtPlocias = (TextView) view.findViewById(R.id.txtPolicia);
        txtBomberos = (TextView) view.findViewById(R.id.txtBomberos);
        txtTaxisPuebla = (TextView) view.findViewById(R.id.txtTaxisPuebla);
        txtTaxisSitio = (TextView) view.findViewById(R.id.txtTaxisSitio);

        txtAmbulancias.setOnClickListener(new Ambulancia());
        txtPlocias.setOnClickListener(new Policia());
        txtBomberos.setOnClickListener(new Bomberos());
        txtTaxisPuebla.setOnClickListener(new TaxisPuebla());
        txtTaxisSitio.setOnClickListener(new TaxisSitio());


        return view;


    }
    class Ambulancia implements View.OnClickListener {
        public void onClick(View v) {
            Intent callIntent = new Intent(Intent.ACTION_DIAL);
            callIntent.setData(Uri.parse("tel:055"));

            startActivity(callIntent);

        }
    }
    class Policia implements View.OnClickListener {
        public void onClick(View v) {
            Intent callIntent = new Intent(Intent.ACTION_DIAL);
            callIntent.setData(Uri.parse("tel:060"));

            startActivity(callIntent);

        }
    }
    class Bomberos implements View.OnClickListener {
        public void onClick(View v) {
            Intent callIntent = new Intent(Intent.ACTION_DIAL);
            callIntent.setData(Uri.parse("tel:080"));

            startActivity(callIntent);

        }
    }
    class TaxisPuebla implements View.OnClickListener {
        public void onClick(View v) {
            Intent callIntent = new Intent(Intent.ACTION_DIAL);
            callIntent.setData(Uri.parse("tel:21542222"));

            startActivity(callIntent);

        }
    }
    class TaxisSitio implements View.OnClickListener {
        public void onClick(View v) {
            Intent callIntent = new Intent(Intent.ACTION_DIAL);
            callIntent.setData(Uri.parse("tel:21137431"));

            startActivity(callIntent);

        }
    }

}
