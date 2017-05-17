package com.cmp2017.devworms.cmp2017;

/**
 * Created by Alienware on 25/04/2017.
 */

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.support.constraint.solver.ArrayLinkedVariables;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Alienware on 24/04/2017.
 */

public class ListViewAdapter extends BaseAdapter {
    private ArrayList<NotificacionModelo> arrayListNoti;
    private Context context;
    private LayoutInflater layoutInflater;
    private String userId,apikey,idNoti,response;
    ImageTools tools;
    ProgressDialog pDialog;


    public ListViewAdapter(Activity contexto, ArrayList<NotificacionModelo> modelo,String userId,String apikey, String idNoti) {
        //super(contexto, R.layout.layout_notificacion, modelo);
        this.arrayListNoti = modelo;
        this.context = contexto;
        this.userId = userId;
        this.apikey = apikey;
        this.idNoti = idNoti;
        tools = new ImageTools(contexto);

    }

    @Override
    public int getCount() {
        return arrayListNoti.size();
    }

    @Override
    public Object getItem(int position) {
        return arrayListNoti.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, final View convertView, ViewGroup parent) {
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View vista = layoutInflater.inflate(R.layout.layout_notificacion,parent,false);
        final ImageView ivImagen= (ImageView) vista.findViewById(R.id.ivImagen);
        TextView txtNoti = (TextView) vista.findViewById(R.id.txtNoti);
        // ivImagen.setImageResource(arrayListNoti.get(position).getLeido());
        txtNoti.setText(arrayListNoti.get(position).getNotificacion());
        if(arrayListNoti.get(position).leido == 1)
        {
            tools.drawableToImageView(R.mipmap.mensaje_recibido,ivImagen);
            //ivImagen.setImageResource(R.mipmap.mensaje_recibido);
        }else {
            tools.drawableToImageView(R.mipmap.mensaje,ivImagen);
        }

        vista.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(arrayListNoti.get(position).leido == 1)
                {
                    //vImagen.setImageResource(R.mipmap.mensaje_recibido);
                    tools.drawableToImageView(R.mipmap.mensaje_recibido,ivImagen);
                }
                else
                {
                    tools.drawableToImageView(R.mipmap.mensaje,ivImagen);
                    //ivImagen.setImageResource(R.mipmap.mensaje_recibido);
                    arrayListNoti.get(position).setLeido(1);
                }
                new MandarLeido().execute();
                AlertDialog.Builder a_builder;
                a_builder = new AlertDialog.Builder(vista.getContext());
                a_builder.setMessage(arrayListNoti.get(position).notificacion)
                        .setCancelable(false)
                        .setPositiveButton("aceptar",new DialogInterface.OnClickListener(){
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }

                        });
                AlertDialog alert = a_builder.create();
                alert.setTitle("Mensaje");
                alert.show();

            }
        });
        return vista;
    }
    class MandarLeido extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected String doInBackground(String... params) {

            String fromUrl = "http://cmp.devworms.com/api/notificacion/markasread/"+userId+"/"+apikey+"/"+idNoti+"";
            JSONParser jsonParser = new JSONParser();
            String respuesta = jsonParser.makeHttpRequest(fromUrl, "GET", fromUrl, "");

            if (respuesta != "error") {
                try {


                    response = "OK";



                }catch (Exception ex){
                    response = "NO";
                    ex.printStackTrace();
                }

            }else{
                response = "NO";
            }

            return response;
        }

        protected void onPostExecute(String file_url) {


        }
    }
}