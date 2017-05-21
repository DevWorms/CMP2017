package com.cmp2017.devworms.cmp2017;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.support.constraint.ConstraintLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import com.bumptech.glide.Glide;
import com.loopj.android.http.Base64;
import com.nostra13.universalimageloader.core.ImageLoader;

public class MenuFragment extends Fragment {


    String resp, userId, apiKey;
    ProgressDialog pDialog;
    String misBytes[];
    ImageView imageAnim;
    ConstraintLayout fondoMenufragment;
    private ImageTools imageTools;
    private Handler myHandler;
    private Runnable runnable;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_menu, container, false);

        SharedPreferences sp = getActivity().getSharedPreferences("prefe", Activity.MODE_PRIVATE);
        String inicioComo = sp.getString("Nombre", "");
        apiKey = sp.getString("APIkey", "");
        userId = sp.getString("IdUser", "");
        if(inicioComo.equals("invi")){
            apiKey = "0";
            userId = "1";
        }


        imageAnim = (ImageView) view.findViewById(R.id.imgBanner);

        //objeto para manejar imagenes
        imageTools = new ImageTools(getActivity());

        fondoMenufragment = (ConstraintLayout) view.findViewById(R.id.fondoMenufragment);

        // cargamos el fondo por manejador
        imageTools.loadBackground(R.drawable.fondo,fondoMenufragment);

        // si no hay datos para los banner
        if(misBytes == null){
            Log.e("misBytes", "entro al if");
            llenarArregloImagenes();
            cambioBanner();
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

        /* if(inicioComo.equals("invi")){

            // ahora cargamos todas las imagenes por manejador
            imageTools.drawableToImageView(R.drawable.btnprogramagris,imgbtnProgramas);
            imgbtnProgramas.setEnabled(false);

            imageTools.drawableToImageView(R.drawable.btnacompgris,imagbtnEvenAcom);
            imagbtnEvenAcom.setEnabled(false);

            imageTools.drawableToImageView(R.drawable.btnsociadeportivosgris,imagbtnSocialDep);
            imagbtnSocialDep.setEnabled(false);

            imageTools.drawableToImageView(R.drawable.btnmapasgris,imagbtnMapa);
            imagbtnMapa.setEnabled(false);

            imageTools.drawableToImageView(R.drawable.btntransportaciongris,imagbtnTrans);
            imagbtnTrans.setEnabled(false);

            imageTools.drawableToImageView(R.drawable.btnpueblagris,imagbtnPuebla);
            imagbtnPuebla.setEnabled(false);

        }*/

        return view;



    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        myHandler.removeCallbacks(runnable);
        ImageLoader.getInstance().destroy();
        Glide.clear(imageAnim);



    }

    public void llenarArregloImagenes(){

        AdminSQLiteOffline dbHandler;
        dbHandler = new AdminSQLiteOffline(getActivity(), null, null, 1);
        SQLiteDatabase db = dbHandler.getWritableDatabase();
        Cursor cursor = dbHandler.listarTodosBanner();
        misBytes= new String[cursor.getCount()];
        int i=0;

        for(cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()){
            misBytes[i]= cursor.getString(0);
            i++;
        }

    }

    public static Bitmap getImage(String imageS) {

        byte[] b = Base64.decode(imageS , Base64.DEFAULT);
        //-----------------
        Bitmap bmp = BitmapFactory.decodeByteArray(b, 0, b.length);

        return  bmp;

    }




    public void cambioBanner(){
        try{

            myHandler = new Handler();

             runnable = new Runnable() {
                int i = 0;

                public void run() {
                    imageTools.loadByBytesToImageView(misBytes[i],imageAnim);
                    i++;
                    if (i > misBytes.length - 1) {
                        i = 0;
                    }
                    myHandler.postDelayed(this, 5000);
                }
            };

            myHandler.postDelayed(runnable, 5000);

        }catch(OutOfMemoryError error){
            Log.e("ERROR MEMORIA", error.getMessage());
        }catch (IllegalArgumentException ix){
            Log.e("IMAGEN CARGADO", "SE DETUVO");
        }

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

            parametro.putString("nombre","Eventos para \n Acompañantes");
            parametro.putString("seccion","acomp");

            fragment.setArguments(parametro);

            final FragmentTransaction ft = getFragmentManager()
                    .beginTransaction();
            ft.replace(R.id.actividad, fragment, "tag");

            ft.addToBackStack("tag");

            ft.commit();

        }
    }

    class SecSociaDep implements View.OnClickListener {
        public void onClick(View v) {

            Fragment fragment = new ListadoFragment();

            Bundle parametro = new Bundle();

            parametro.putString("nombre","Eventos Sociales \n y Deportivos");
            parametro.putString("seccion","social");

            fragment.setArguments(parametro);

            final FragmentTransaction ft = getFragmentManager()
                    .beginTransaction();
            ft.replace(R.id.actividad, fragment, "tag");

            ft.addToBackStack("tag");

            ft.commit();
        }
    }

    class SecPatro implements View.OnClickListener {
        public void onClick(View v) {

            Fragment fragment = new ExpositoresFrgament();

            Bundle parametro = new Bundle();

            parametro.putString("nombre","Patrocinadores");
            parametro.putString("MiExpo","No");

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

            getFragmentManager().beginTransaction()
                    .replace(R.id.actividad, new MapaFragment()).addToBackStack(null).commit();

        }
    }

    class SecTrans implements View.OnClickListener {
        public void onClick(View v) {

            getFragmentManager().beginTransaction()
                    .replace(R.id.actividad, new TransportacionFragment()).addToBackStack(null).commit();

        }
    }

    class SecPuebla implements View.OnClickListener {
        public void onClick(View v) {

            getFragmentManager().beginTransaction()
                    .replace(R.id.actividad, new PueblaFragment()).addToBackStack(null).commit();

        }
    }



}
