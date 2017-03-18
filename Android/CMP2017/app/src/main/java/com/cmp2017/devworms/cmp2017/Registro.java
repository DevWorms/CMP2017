package com.cmp2017.devworms.cmp2017;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

public class Registro extends AppCompatActivity {

    Spinner spiTipoUsu, spiAso;
    String resp,nom,ape,correo,contra,claveIns,tipoUsu,asoci;
    EditText edtNombre,edtApellido,edtCorreo,edtContraseña,edtClaveIns;
    Button btnRegis;

    private ProgressDialog pDialog;
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

        edtNombre= (EditText) findViewById(R.id.editNombre);
        edtApellido= (EditText) findViewById(R.id.editApellido);
        edtCorreo= (EditText) findViewById(R.id.editCorreoReg);
        edtContraseña= (EditText) findViewById(R.id.editContra);
        edtClaveIns= (EditText) findViewById(R.id.editClaveIns);

        btnRegis= (Button)findViewById(R.id.btnRegistrar);

        btnRegis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                nom= edtNombre.getText().toString();
                ape= edtApellido.getText().toString();
                correo= edtCorreo.getText().toString();
                contra= edtContraseña.getText().toString();
                claveIns= edtClaveIns.getText().toString();
                tipoUsu = spiTipoUsu.getSelectedItem().toString();
                asoci= spiAso.getSelectedItem().toString();



                if( nom.equals("") || nom == null ||ape.equals("") || ape == null ||  correo.equals("") || correo == null || contra.equals("") || contra == null ||  claveIns.equals("") || claveIns == null) {
                    Toast.makeText(Registro.this,"Falta llenar campos.",Toast.LENGTH_SHORT).show();
                }else{


                        new getRegstroAT().execute();



                }



            }

        });



    }






class getRegstroAT extends AsyncTask<String, String, String> {

        /**
         * Before starting background thread Show Progress Dialog
         * */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
             pDialog = new ProgressDialog(Registro.this);
            pDialog.setMessage("Registrando...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        /**
         * getting Albums JSON
         * */
        protected String doInBackground(String... args) {
            // Building Parameters
            //add your data

            Log.d("Nombre : ", "> " + nom);
            Log.d("Apellido : ", "> " + ape);
            Log.d("Correo : ", "> " + correo);
            Log.d("Pass : ", "> " + contra);
            Log.d("Clave : ", "> " + claveIns);
            Log.d("Tipo : ", "> " + tipoUsu);
            Log.d("Asoci : ", "> " + asoci);


            String body= "{\n\t\"name\": \""+nom+"\",\n\t\"last_name\" : \""+ape+"\",\n\t\"email\" : \""+correo+"\",\n\t\"password\" : \""+contra+"\",\n\t\"password_confirmation\" : \""+claveIns+"\",\n\t\"type\" : \""+tipoUsu+"\",\n\t\"association\" : \""+asoci+"\"\n\t\n}";
            JSONParser jsp= new JSONParser();



            String respuesta= jsp.makeHttpRequest("http://cmp.devworms.com/api/user/signup","POST",body,"");
            Log.d("Registro : ", "> " + respuesta);
            if(respuesta!="error"){
                try{ JSONObject json = new JSONObject(respuesta);

                    String apikey = json.getString("api_key");
                    Log.d("Registroapikey : ", "> " + apikey);
                    String nombrecomple= nom + " "+ ape;
                    SharedPreferences  sp = getSharedPreferences("prefe", Activity.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sp.edit();
                    editor.putString("APIkey", apikey);
                    editor.putString("Nombre", nombrecomple);
                    editor.commit();
                }
                catch (JSONException e) {
                    e.printStackTrace();
                }
                resp="Perfil creado correctamente";


            }else {
                resp="Usuario ya registrado";
            }



            return null;
        }


        /**
         * After completing background task Dismiss the progress dialog
         * **/
        protected void onPostExecute(String file_url) {
            // dismiss the dialog after getting all albums
            Log.d("RegistroResp : ", "> " + resp);
            pDialog.dismiss();
            if(resp!="Usuario ya registrado"){
                Intent i = new Intent(Registro.this, MenuPrincipal.class);
                startActivity(i);
                finish();
            }
            // updating UI from Background Thread
            runOnUiThread(new Runnable() {
                public void run() {

                    Toast.makeText(Registro.this,resp,Toast.LENGTH_SHORT).show();




                }
            });

        }
    }

}
