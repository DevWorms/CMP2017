package com.cmp2017.devworms.cmp2017;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
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
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

public class Registro extends AppCompatActivity {

    Spinner spiTipoUsu, spiAso;
    String resp,nom,ape,correo,contra,claveIns,tipoUsu,asoci;
    EditText edtNombre,edtApellido,edtCorreo,edtContraseña,edtClaveIns;
    Button btnRegis;
    ConnectionDetector cd;
    private ProgressDialog pDialog;
    private final static String[] tipoUsuarios = { "Congresista", "Acompañante", "Expositor", "Estudiante","Otro"};
    private final static String[] asociaciones = { "Ninguna","AIPM", "CIPM", "AMGE", "AMGP", "SPE / México" };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);
        spiTipoUsu = (Spinner)findViewById(R.id.spiTipoUse);
        spiAso = (Spinner)findViewById(R.id.spiAso);
        spiTipoUsu.setPrompt("Tipo de Usuarios");
        spiAso.setPrompt("Asociaciones");
        String font_path = "font/mulibold.ttf";  //definimos un STRING con el valor PATH ( o ruta por                                                                                    //donde tiene que buscar ) de nuetra fuente

        Typeface TF = Typeface.createFromAsset(getAssets(),font_path);
        TextView txtNombreR = (TextView) findViewById(R.id.textViewNombreR);
        TextView txtApellidoR = (TextView) findViewById(R.id.textViewApellidoR);
        TextView txtCorreoR = (TextView) findViewById(R.id.textViewCorreoR);
        TextView txtContraR = (TextView) findViewById(R.id.textViewContraseñaR);
        TextView txtClaveR = (TextView) findViewById(R.id.textViewClaveR);
        TextView txtTipoR = (TextView) findViewById(R.id.textViewTipoR);
        TextView txtAsociaR = (TextView) findViewById(R.id.textViewAsociaciónR);

        txtNombreR.setTypeface(TF);
        txtApellidoR.setTypeface(TF);
        txtCorreoR.setTypeface(TF);
        txtContraR.setTypeface(TF);
        txtClaveR.setTypeface(TF);
        txtTipoR.setTypeface(TF);
        txtAsociaR.setTypeface(TF);


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



                if( nom.equals("") || nom == null ) {
                    Toast.makeText(Registro.this,"Debes ingresar tu Nombre",Toast.LENGTH_SHORT).show();
                }else if(ape.equals("") || ape == null){
                    Toast.makeText(Registro.this,"Debes ingresar tu Apellido",Toast.LENGTH_SHORT).show();
                }else if(correo.equals("") || correo == null ){
                    Toast.makeText(Registro.this,"Debes ingresar tu correo",Toast.LENGTH_SHORT).show();
                }
                else if(contra.equals("") || contra == null || contra.length()<=5 ){
                    Toast.makeText(Registro.this,"La contraseña debe de ser de al menos 6 dígitos",Toast.LENGTH_SHORT).show();
                }
                else{
                    cd = new ConnectionDetector(getApplicationContext());

                    // Check if Internet present
                    if (!cd.isConnectingToInternet()) {
                        // Internet Connection is not present
                        Toast.makeText(Registro.this, "Se necesita internet", Toast.LENGTH_SHORT).show();
                        // stop executing code by return
                        return;
                    }

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


            String body= "{\n\t\"name\": \""+nom+"\",\n\t\"last_name\" : \""+ape+"\",\n\t\"email\" : \""+correo+"\",\n\t\"password\" : \""+contra+"\",\n\t\"clave\" : \""+claveIns+"\",\n\t\"type\" : \""+tipoUsu+"\",\n\t\"association\" : \""+asoci+"\"\n\t\n}";
            JSONParser jsp= new JSONParser();



            String respuesta= jsp.makeHttpRequest("http://cmp.devworms.com/api/user/signup","POST",body,"");
            Log.d("Registro : ", "> " + respuesta);
            String rest = respuesta.substring(0,5);
            if( !rest.equals("error")){
                try{
                    JSONObject json = new JSONObject(respuesta);

                    String apikey = json.getString("api_key");
                    String iduser = json.getString("user_id");
                    Log.d("Registroapikey : ", "> " + apikey);
                    String nombrecomple= nom + " "+ ape;
                    SharedPreferences  sp = getSharedPreferences("prefe", Activity.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sp.edit();
                    editor.putString("APIkey", apikey);
                    editor.putString("Nombre", "Bienvenido "+nombrecomple);
                    editor.putString("IdUser", iduser);
                    editor.commit();

                }
                catch (JSONException e) {
                    e.printStackTrace();
                }
                resp="Perfil creado correctamente";


            }else {

                resp="Usuario ya registrado";
                try{
                    String errorRest = respuesta.substring(6,respuesta.length());
                    JSONObject json = new JSONObject(errorRest);

                    String message = json.getString("mensaje");
                    if(message.equals("The email must be a valid email address."))
                    {
                        resp="Debes ingresar un correo electrónico válido";
                    }

                }
                catch (JSONException e) {
                    e.printStackTrace();
                }
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
            if(resp.equals("Perfil creado correctamente")){
                Intent i = new Intent(Registro.this, MenuPrincipal.class);
                i.putExtra("parametro", "no");
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
