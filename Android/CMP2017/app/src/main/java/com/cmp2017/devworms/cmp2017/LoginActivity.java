package com.cmp2017.devworms.cmp2017;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.app.LoaderManager.LoaderCallbacks;

import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;

import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static android.Manifest.permission.READ_CONTACTS;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity {

    EditText correo, pass;
    String resp,nombre;
    private ProgressDialog pDialog;
    ConnectionDetector cd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        // Set up the login form.

        correo = (EditText) findViewById(R.id.editCorreo);
        pass = (EditText) findViewById(R.id.editPass);
        Button iniSesion = (Button) findViewById(R.id.btnIniSe);
        Button btnRegistro = (Button) findViewById(R.id.btnRegis);
        btnRegistro.setOnClickListener(new RegistroCam());
        iniSesion.setOnClickListener(new IniSe());
    }

    class RegistroCam implements View.OnClickListener {
        public void onClick(View v) {


            Intent intent = new Intent(getApplicationContext(), Registro.class);
            startActivity(intent);


        }
    }

    class IniSe implements View.OnClickListener {
        public void onClick(View v) {

            cd = new ConnectionDetector(getApplicationContext());

            // Check if Internet present
            if (!cd.isConnectingToInternet()) {
                // Internet Connection is not present
                Toast.makeText(LoginActivity.this, "Se necesita internet", Toast.LENGTH_SHORT).show();
                // stop executing code by return
                return;
            }
            new getLogin().execute();


        }
    }

    class getLogin extends AsyncTask<String, String, String> {

        /**
         * Before starting background thread Show Progress Dialog
         */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(LoginActivity.this);
            pDialog.setMessage("Login...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        /**
         * getting Albums JSON
         */
        protected String doInBackground(String... args) {
            // Building Parameters
            //add your data


            String body = " {\n\t\"user\" : \"" + correo.getText() + "\",\n\t\"password\" : \"" + pass.getText() + "\"\n                    \n }\n\n";
            JSONParser jsp = new JSONParser();



            String respuesta= jsp.makeHttpRequest("http://cmp.devworms.com/api/user/login","POST",body,"");
            Log.d("LoginRes : ", "> " + respuesta);
            if (respuesta != "error") {
                try {
                    JSONObject json = new JSONObject(respuesta);


                    String apikey = json.getString("api_key");

                    nombre = json.getString("mensaje");
                    String iduser = json.getString("user_id");
                    // String idUser= json.getString("Token");
                    SharedPreferences sp = getSharedPreferences("prefe", Activity.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sp.edit();
                    editor.putString("APIkey", apikey);
                    editor.putString("Nombre", nombre);
                    editor.putString("IdUser", iduser);

                    editor.commit();



                } catch (JSONException e) {
                    e.printStackTrace();
                }
                resp = "Login completo";


            } else {
                resp = "Usuario o contraseña incorrecto ";
            }


            return null;
        }


        /**
         * After completing background task Dismiss the progress dialog
         **/
        protected void onPostExecute(String file_url) {
            // dismiss the dialog after getting all albums
            Log.d("Login : ", "> " + resp);
            pDialog.dismiss();
            if (resp.equals("Login completo")) {
                Intent i = new Intent(LoginActivity.this, MenuPrincipal.class);
                i.putExtra("parametro", "no");
                startActivity(i);
                finish();
            }
            // updating UI from Background Thread
            runOnUiThread(new Runnable() {
                public void run() {

                    Toast.makeText(LoginActivity.this, resp, Toast.LENGTH_SHORT).show();


                }
            });

        }
    }
}







