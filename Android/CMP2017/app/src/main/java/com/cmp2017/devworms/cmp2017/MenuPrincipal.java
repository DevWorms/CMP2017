package com.cmp2017.devworms.cmp2017;

import android.app.Activity;
import android.app.Application;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

public class MenuPrincipal extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    String nombre, inicioComo;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_principal);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);



        SharedPreferences sp = getSharedPreferences("prefe", Activity.MODE_PRIVATE);
        inicioComo = sp.getString("Nombre","");
        if(inicioComo.equals("invi")){
            nombre = "Bienvenido Usuario";

        }else{

            nombre = sp.getString("Nombre","");


        }

        Log.d("nombre : ", "> " + nombre);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);




        toggle.setDrawerIndicatorEnabled(false);
        toggle.setHomeAsUpIndicator(R.drawable.ic_menu_app);
        toggle.syncState();
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);

        navigationView.setNavigationItemSelectedListener(MenuPrincipal.this);
        toolbar.setTitleTextColor(Color.parseColor("#FFFFFF"));

        toggle.setToolbarNavigationClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
                if (drawer.isDrawerOpen(GravityCompat.START)) {
                    drawer.closeDrawer(GravityCompat.START);
                } else {
                    drawer.openDrawer(GravityCompat.START);
                }
            }
        });


        getSupportActionBar().setTitle(nombre);
        getFragmentManager().beginTransaction()
                .replace(R.id.actividad, new MenuFragment()).addToBackStack(null).commit();

    }



    @Override
    public void onBackPressed() {

        int count = getFragmentManager().getBackStackEntryCount();

        if (count == 0) {
            super.onBackPressed();
            getFragmentManager().popBackStack();
        } else if (count == 1){

        } else{

            getFragmentManager().popBackStack();//No se porqué puse lo mismo O.o
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_principal, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.btnHome) {

            getFragmentManager().beginTransaction()
                    .replace(R.id.actividad, new MenuFragment()).commit();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_mi_perfil) {
            if(inicioComo.equals("invi")){
                Toast.makeText(MenuPrincipal.this,"Registrate para activar esta sección",Toast.LENGTH_SHORT).show();

            }else{
                getFragmentManager().beginTransaction()
                        .replace(R.id.actividad, new MiPerfilFragment()).commit();
                DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
                drawer.closeDrawer(GravityCompat.START);


            }

            return true;
        } else if (id == R.id.nav_agenda) {
            if(inicioComo.equals("invi")){
                Toast.makeText(MenuPrincipal.this,"Registrate para activar esta sección",Toast.LENGTH_SHORT).show();

            }else{

                Toast.makeText(MenuPrincipal.this,"En Desarrollo",Toast.LENGTH_SHORT).show();


            }

        } else if (id == R.id.nav_expositores) {
            if(inicioComo.equals("invi")){
                Toast.makeText(MenuPrincipal.this,"Registrate para activar esta sección",Toast.LENGTH_SHORT).show();

            }else{

                Toast.makeText(MenuPrincipal.this,"En Desarrollo",Toast.LENGTH_SHORT).show();


            }
        } else if (id == R.id.nav_encuestas) {
            if(inicioComo.equals("invi")){
                Toast.makeText(MenuPrincipal.this,"Registrate para activar esta sección",Toast.LENGTH_SHORT).show();

            }else{

                Toast.makeText(MenuPrincipal.this,"En Desarrollo",Toast.LENGTH_SHORT).show();


            }
        } else if (id == R.id.nav_contactos) {
            if(inicioComo.equals("invi")){
                Toast.makeText(MenuPrincipal.this,"Registrate para activar esta sección",Toast.LENGTH_SHORT).show();

            }else{

                Toast.makeText(MenuPrincipal.this,"En Desarrollo",Toast.LENGTH_SHORT).show();


            }
        } else if (id == R.id.nav_notifiaciones) {
            if(inicioComo.equals("invi")){
                Toast.makeText(MenuPrincipal.this,"Registrate para activar esta sección",Toast.LENGTH_SHORT).show();

            }else{

                Toast.makeText(MenuPrincipal.this,"En Desarrollo",Toast.LENGTH_SHORT).show();


            }
        } else if (id == R.id.nav_informacion) {
            if(inicioComo.equals("invi")){
                Toast.makeText(MenuPrincipal.this,"Registrate para activar esta sección",Toast.LENGTH_SHORT).show();

            }else{

                Toast.makeText(MenuPrincipal.this,"En Desarrollo",Toast.LENGTH_SHORT).show();


            }
        } else if (id == R.id.nav_cerrar) {

            SharedPreferences sp = getSharedPreferences("prefe", Activity.MODE_PRIVATE);
            SharedPreferences.Editor editor = sp.edit();
            editor.putString("APIkey", "");
            editor.putString("Nombre", "");
            editor.putString("IdUser", "");

            editor.commit();

            Intent intent = new Intent(MenuPrincipal.this, LoginActivity.class);

            startActivity(intent);
            finish();

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }




}
