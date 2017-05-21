package com.cmp2017.devworms.cmp2017;

import android.app.Activity;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

public class MiPerfilFragment  extends Fragment

{
    EditText edtNombre, edtApellido, edtClave;
    String userId, apiKey,resp,nombre,apellido,clave;
    String paramNombre,paramApellido,paramClave;
    Button btnGuarCamb, btnCambioContra;
    LinearLayout miperfil;
    ImageTools tools;
    private ProgressDialog pDialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_mi_perfil, container, false);
        tools = new ImageTools(getActivity());
        miperfil = (LinearLayout) view.findViewById(R.id.miperfil);
        tools.loadBackground(R.drawable.fondo,miperfil);
        SharedPreferences sp = getActivity().getSharedPreferences("prefe", Activity.MODE_PRIVATE);
        apiKey = sp.getString("APIkey","");
        userId = sp.getString("IdUser","");

        edtNombre = (EditText)view.findViewById(R.id.editNombrePerfil);
        edtApellido = (EditText)view.findViewById(R.id.editApellidoPerfil);
        edtClave = (EditText)view.findViewById(R.id.editClavePerfil);

        btnGuarCamb = (Button)view.findViewById(R.id.btnGuarCamb);
        btnGuarCamb.setOnClickListener(new GuardarCambios());
        btnCambioContra = (Button)view.findViewById(R.id.btnRecuperarContra) ;
        btnCambioContra.setOnClickListener(new RecuperarContra());

        new getPerfil().execute();
        return view;


    }

    class GuardarCambios implements View.OnClickListener {
        public void onClick(View v) {
            if  (!edtApellido.getText().toString().equals("") || !edtNombre.getText().toString().equals("")){


                paramNombre = edtNombre.getText().toString();
                paramApellido = edtApellido.getText().toString();
                paramClave =edtClave.getText().toString() ;
                new getCambios().execute();
            }else{
                Toast.makeText(getActivity(), "Nombre y Apellido son obligatorios", Toast.LENGTH_SHORT).show();

            }
        }
    }
    class RecuperarContra implements View.OnClickListener {
        public void onClick(View v) {

                startActivity(new Intent(getActivity(),ChangePassword.class));


        }
    }

    class getPerfil extends AsyncTask<String, String, String> {

        /**
         * Before starting background thread Show Progress Dialog
         */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(getActivity());
            pDialog.setMessage("Cargando...");
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


            String body = "http://cmp.devworms.com/api/user/profile/"+userId+"/"+apiKey+"";
            JSONParser jsp = new JSONParser();



            String respuesta= jsp.makeHttpRequest(body,"GET",body,"");
            Log.d("LoginRes : ", "> " + respuesta);
            if (respuesta != "error") {
                try {
                    JSONObject json = new JSONObject(respuesta);


                    String jsonPefil = json.getString("user");
                    JSONObject jsonDatos = new JSONObject(jsonPefil);
                    nombre = jsonDatos.getString("name");
                    apellido = jsonDatos.getString("last_name");
                    clave = jsonDatos.getString("clave");
                    if (clave.equals("null")){
                        clave= "";
                    }








                } catch (JSONException e) {
                    e.printStackTrace();
                }
                resp = "ok";


            } else {

            }


            return null;
        }


        /**
         * After completing background task Dismiss the progress dialog
         **/
        protected void onPostExecute(String file_url) {
            // dismiss the dialog after getting all albums

            pDialog.dismiss();

            // updating UI from Background Thread
            getActivity().runOnUiThread(new Runnable() {
                public void run() {
                    if (resp.equals("ok")) {
                        edtNombre.setText(nombre);
                        edtApellido.setText(apellido);
                        edtClave.setText(clave);

                    }



                }
            });

        }
    }

    class getCambios extends AsyncTask<String, String, String> {

        /**
         * Before starting background thread Show Progress Dialog
         */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(getActivity());
            pDialog.setMessage("Guardando cambios...");
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


            String body = "{\n\t\"user_id\" : \""+userId+"\",\n\t\"api_key\" : \""+apiKey+"\",\n\t\"name\" : \""+paramNombre+"\",\n\t\"last_name\" : \""+paramApellido+"\",\n\t\"clave\" : \""+paramClave+"\"\n}";
            JSONParser jsp = new JSONParser();



            String respuesta= jsp.makeHttpRequest("http://cmp.devworms.com/api/user/edit","POST",body,"");
            Log.d("LoginRes : ", "> " + respuesta);
            String rest = respuesta.substring(0,5);
            if (!rest.equals("error")) {
                try {
                    JSONObject json = new JSONObject(respuesta);




                    nombre = json.getString("mensaje");


                    SharedPreferences sp = getActivity().getSharedPreferences("prefe", Activity.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sp.edit();

                    editor.putString("Nombre","Bienvenido "+paramNombre + paramApellido );


                    editor.commit();



                } catch (JSONException e) {
                    e.printStackTrace();
                }
                resp  = nombre;


            } else {
                resp  = "No es posible actualizar sus datos";
            }
            return null;
        }


        /**
         * After completing background task Dismiss the progress dialog
         **/
        protected void onPostExecute(String file_url) {
            // dismiss the dialog after getting all albums

            pDialog.dismiss();

            // updating UI from Background Thread
            getActivity().runOnUiThread(new Runnable() {
                public void run() {
                    Toast.makeText(getActivity(), resp, Toast.LENGTH_SHORT).show();
                    if(resp.equals("Tu perfil se actualizo correctamente")) {

                        ((MenuPrincipal) getActivity()).getSupportActionBar().setTitle("Bienvenido " + edtNombre.getText().toString() + edtApellido.getText().toString());
                    }

                }
            });

        }
    }
}
