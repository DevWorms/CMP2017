package com.cmp2017.devworms.cmp2017;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.app.ListFragment;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.graphics.drawable.AnimationDrawable;
import android.media.Image;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import static android.R.attr.fragment;

public class MenuFragment extends Fragment {


    String resp, userId, apiKey, urlImage;
    ProgressDialog pDialog;
    ImageView imgFoto;
    URL imageUrl;
    Bitmap[] imagen;
    HttpURLConnection conn;
    String[] ArrayBanners;
    ImageView imageAnim;
    View viewBanner;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_menu, container, false);
        SharedPreferences sp = getActivity().getSharedPreferences("prefe", Activity.MODE_PRIVATE);
        String inicioComo = sp.getString("Nombre", "");
        apiKey = sp.getString("APIkey", "");
        userId = sp.getString("IdUser", "");

        imageAnim = (ImageView) view.findViewById(R.id.imgBanner);

        if(imagen == null){
            new getBanner().execute();
        }


        ImageView imgbtnProgramas = (ImageView) view.findViewById(R.id.imagbtnPrograma);
        imgbtnProgramas.setOnClickListener(new SecProgram());

        ImageView imagbtnExpos= (ImageView) view.findViewById(R.id.imagbtnExpos);
        imagbtnExpos.setOnClickListener(new SecExpo());

        ImageView imagbtnEvenAcom= (ImageView) view.findViewById(R.id.imagbtnAcom);
        imagbtnEvenAcom.setOnClickListener(new SecAcom());

        ImageView imagbtnSocialDep= (ImageView) view.findViewById(R.id.imagbtnSociaDepo);
        imagbtnSocialDep.setOnClickListener(new SecSociaDep());

        ImageView imagbtnPatro= (ImageView) view.findViewById(R.id.imagbtnPatro);
        imagbtnPatro.setOnClickListener(new SecPatro());

        ImageView imagbtnMapa= (ImageView) view.findViewById(R.id.imagbtnMapas);
        imagbtnMapa.setOnClickListener(new SecMapa());

        ImageView imagbtnTrans= (ImageView) view.findViewById(R.id.imagbtnTransp);
        imagbtnTrans.setOnClickListener(new SecTrans());

        ImageView imagbtnPuebla= (ImageView) view.findViewById(R.id.imagbtnPuebla);
        imagbtnPuebla.setOnClickListener(new SecPuebla());

        if(inicioComo.equals("invi")){

            imgbtnProgramas.setImageDrawable(getResources().getDrawable(R.drawable.btnprogramagris));
            imgbtnProgramas.setEnabled(false);

            imagbtnEvenAcom.setImageDrawable(getResources().getDrawable(R.drawable.btnacompgris));
            imagbtnEvenAcom.setEnabled(false);

            imagbtnSocialDep.setImageDrawable(getResources().getDrawable(R.drawable.btnsociadeportivosgris));
            imagbtnSocialDep.setEnabled(false);

            imagbtnMapa.setImageDrawable(getResources().getDrawable(R.drawable.btnmapasgris));
            imagbtnMapa.setEnabled(false);

            imagbtnTrans.setImageDrawable(getResources().getDrawable(R.drawable.btntransportaciongris));
            imagbtnTrans.setEnabled(false);

            imagbtnPuebla.setImageDrawable(getResources().getDrawable(R.drawable.btnpueblagris));
            imagbtnPuebla.setEnabled(false);

        }

        return view;



    }
public void cambioBanner(){
    final Handler handler = new Handler();
    Runnable runnable = new Runnable() {
        int i = 0;

        public void run() {
            imageAnim.setImageBitmap(imagen[i]);
            i++;
            if (i > imagen.length - 1) {
                i = 0;
            }
            handler.postDelayed(this, 2000);
        }
    };
    handler.postDelayed(runnable, 2000);
}
    class SecProgram implements View.OnClickListener {
        public void onClick(View v) {


            getFragmentManager().beginTransaction()
                    .replace(R.id.actividad, new ProgramFragment()).addToBackStack(null).commit();


        }
    }


    class SecExpo implements View.OnClickListener {
        public void onClick(View v) {


            getFragmentManager().beginTransaction()
                    .replace(R.id.actividad, new ExpositoresFrgament()).addToBackStack(null).commit();


        }
    }

    class SecAcom implements View.OnClickListener {
        public void onClick(View v) {

            Fragment fragment = new ListadoFragment();

            Bundle parametro = new Bundle();

            parametro.putString("nombre","Eventos de \n Acompañantes");
            parametro.putString("seccion","acomp");

            fragment.setArguments(parametro);

            final FragmentTransaction ft = getFragmentManager()
                    .beginTransaction();
            ft.replace(R.id.actividad, fragment, "tag");

            ft.addToBackStack("tag");

            ft.commit();
            /*ListadoFragment fragment = new ListadoFragment();


            Bundle args = new Bundle();
            args.putString("nombre","Eventos de Acompañantes");
            fragment.setArguments(args);
            getFragmentManager().beginTransaction()
                    .replace(R.id.actividad, new ListadoFragment()).commit();*/


        }
    }

    class SecSociaDep implements View.OnClickListener {
        public void onClick(View v) {

            Fragment fragment = new ListadoFragment();

            Bundle parametro = new Bundle();

            parametro.putString("nombre","Eventos de Sociales \n y Deportivos");
            parametro.putString("seccion","social");

            fragment.setArguments(parametro);

            final FragmentTransaction ft = getFragmentManager()
                    .beginTransaction();
            ft.replace(R.id.actividad, fragment, "tag");

            ft.addToBackStack("tag");

            ft.commit();
            /*ListadoFragment fragment = new ListadoFragment();


            Bundle args = new Bundle();
            args.putString("nombre","Eventos de Acompañantes");
            fragment.setArguments(args);
            getFragmentManager().beginTransaction()
                    .replace(R.id.actividad, new ListadoFragment()).commit();*/


        }
    }

    class SecPatro implements View.OnClickListener {
        public void onClick(View v) {

            Fragment fragment = new ExpositoresFrgament();

            Bundle parametro = new Bundle();

            parametro.putString("nombre","Patrocinadores");


            fragment.setArguments(parametro);

            final FragmentTransaction ft = getFragmentManager()
                    .beginTransaction();
            ft.replace(R.id.actividad, fragment, "tag");

            ft.addToBackStack("tag");

            ft.commit();



        }
    }

    class SecMapa implements View.OnClickListener {
        public void onClick(View v) {

            Toast.makeText(getActivity(),"En Desarrollo",Toast.LENGTH_SHORT).show();
            /*getFragmentManager().beginTransaction()
                    .replace(R.id.actividad, new MapaFragment()).addToBackStack(null).commit();*/


        }
    }

    class SecTrans implements View.OnClickListener {
        public void onClick(View v) {
            Toast.makeText(getActivity(),"En Desarrollo",Toast.LENGTH_SHORT).show();


            /*getFragmentManager().beginTransaction()
                    .replace(R.id.actividad, new TransportacionFragment()).addToBackStack(null).commit();*/


        }
    }

    class SecPuebla implements View.OnClickListener {
        public void onClick(View v) {


            getFragmentManager().beginTransaction()
                    .replace(R.id.actividad, new PueblaFragment()).addToBackStack(null).commit();


        }
    }
    class getBanner extends AsyncTask<String, String, String> {

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

            String body= "";



            body = "http://cmp.devworms.com/api/banners/all/"+userId+"/"+apiKey+"";


            JSONParser jsp = new JSONParser();
            SharedPreferences sp = getActivity().getSharedPreferences("prefe", Activity.MODE_PRIVATE);
            String jsonBannerOffline = sp.getString("respuestaBanner", "");
            SharedPreferences.Editor editor = sp.edit();

            String respuesta= "";
            if(jsonBannerOffline.equals("")) {
                respuesta = jsp.makeHttpRequest(body, "GET", body, "");
            }else{
                respuesta = jsonBannerOffline;
            }



            Log.d("LoginRes : ", "> " + respuesta);
            if (respuesta != "error") {
                try {
                    editor.putString("respuestaBanner", respuesta);
                    editor.commit();
                    JSONObject json = new JSONObject(respuesta);
                    String banners = "";

                    banners = json.getString("banners");

                    JSONArray jsonBanner = new JSONArray(banners);

                    ArrayBanners = new String[jsonBanner.length()];

                    // looping through All albums
                    for (int i = 0; i <= jsonBanner.length(); i++) {
                        JSONObject c = jsonBanner.getJSONObject(i);


                        // creating new HashMap

                        ArrayBanners[i] = c.getString("url");

                        try {
                            imagen = new Bitmap[ArrayBanners.length];
                            for (int cou = 0; cou < imagen.length; cou++) {
                                imageUrl = new URL(ArrayBanners[cou]);
                                conn = (HttpURLConnection) imageUrl.openConnection();
                                conn.connect();

                                BitmapFactory.Options options = new BitmapFactory.Options();
                                options.inSampleSize = 2; // el factor de escala a minimizar la imagen, siempre es potencia de 2

                                imagen[cou] = BitmapFactory.decodeStream(conn.getInputStream(), new Rect(0, 0, 0, 0), options);


                            }


                        } catch (MalformedURLException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                    }

                    resp = "ok";


                } catch (JSONException e) {
                    e.printStackTrace();

                }
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
            cambioBanner();
            // updating UI from Background Thread


        }
    }


}
