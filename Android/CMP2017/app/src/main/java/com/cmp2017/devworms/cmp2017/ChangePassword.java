package com.cmp2017.devworms.cmp2017;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

public class ChangePassword extends FragmentActivity implements View.OnClickListener {
    private ProgressDialog pDialog;
    private EditText nuevaContasena;
    private EditText repiteContrasena;
    private Button btnCambiar;
    private Activity yo;
    private String apiKey;
    private String nuevaContrasena;
    private String repetirContra;
    private String userId;
    private String status;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_cahnge_password);

        SharedPreferences sp = this.getSharedPreferences("prefe", Activity.MODE_PRIVATE);
        this.apiKey = sp.getString("APIkey", "");
        this.userId = sp.getString("IdUser", "");
        // ESTOS PARAMETROS SON PARA QUE SE PUEDA VER LA VENTANA COMO POP UP
        DisplayMetrics metrics = new DisplayMetrics();

        getWindowManager().getDefaultDisplay().getMetrics(metrics);

        int width = (int) (metrics.widthPixels * 0.9);
        int height = (int) (metrics.heightPixels * 0.55);

        getWindow().setLayout(width, height);

        this.nuevaContasena = (EditText) findViewById(R.id.nuevaContra);
        this.repiteContrasena = (EditText) findViewById(R.id.repetirContra);
        this.btnCambiar = (Button) findViewById(R.id.btnCambiar);
        this.yo = this;
        this.btnCambiar.setOnClickListener(this);
        this.status = "Lo sentimos no se pudo guardar \n revisa tu conexión a internet y \n que la contraseña sea de mas de 5 caracteres";


    }

    @Override
    public void onClick(View v) {

        nuevaContrasena = this.nuevaContasena.getText().toString();
        repetirContra = this.repiteContrasena.getText().toString();

        if (nuevaContrasena.equals(repetirContra)) {
            new CambiarPassword().execute();

        } else {
            Toast.makeText(this, "Las contraseñas no coinciden ", Toast.LENGTH_LONG).show();
        }


    }

    class CambiarPassword extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //ABRIMOS EL DIALOG DE ESPERA
            pDialog = new ProgressDialog(yo);
            pDialog.setMessage("Guardando cambios");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
            //ENVIO POR POST
            String saveUrl = "http://cmp.devworms.com/api/user/resetpassword";
            // creo un json object para setear parametros
            JSONObject parametros = new JSONObject();
            JSONParser parse = new JSONParser();
            try {
                parametros.put("user_id", userId);
                parametros.put("api_key", apiKey);
                parametros.put("password", repetirContra);

            } catch (JSONException jex) {
                Log.e("ENVIO_RESETPASS", jex.getMessage());
            }
            String strParametros = parametros.toString();
            String response = parse.makeHttpRequest(saveUrl, "POST", strParametros, "");
            Log.e("RESPONSEPASS", response);
            try {
                JSONObject jResponse = new JSONObject(response);

                Log.e("RESPONSEPASS", jResponse.toString());
                status = jResponse.getString("mensaje");
                Log.e("RESPONSEPASS", status);

            } catch (JSONException jex) {
                Log.e("NosavepASS", jex.getMessage());
            }

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(yo, status, Toast.LENGTH_LONG).show();
                }
            });
            return null;
        }

        @Override
        protected void onPostExecute(String str) {
            //CERRAMOS EL DIALOG E ESPERA
            pDialog.dismiss();

        }
    }
}
