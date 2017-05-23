package com.cmp2017.devworms.cmp2017;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;

import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import com.loopj.android.http.Base64;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

import uk.co.senab.photoview.PhotoViewAttacher;

/**
 * Created by AndrewAlan on 16/05/2017.
 */

public class ImageTools {

    /*ATRIBUTOS*/
    private Context contexto;
    private View targetLaoyut;
    public ImageLoader imageLoader;
    /** CONSTRUCTORES **/
    public ImageTools(Context contexto){

        this.contexto = contexto;
        if(ImageLoader.getInstance().isInited()){
            Log.e("IMAGECARGA","venia iniciado c");
            ImageLoader.getInstance().destroy();
        }

    }


    /* Metodo para manejar drawables fisico en image view nesecita el id del recurso del drawable
    *   y el image view target donde se cargara
    * */
    public void drawableToImageView(int drawableResource, ImageView target){

        // este metodo carga un drawable fisico por medio de la liberia ImageLoader

        if(ImageLoader.getInstance().isInited()){
            Log.e("IMAGECARGA","venia iniciado DI");
            ImageLoader.getInstance().destroy();
        }

        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(this.contexto).build();
        ImageLoader.getInstance().init(config);

        this.imageLoader = ImageLoader.getInstance();

        String imageUri = "drawable://" + drawableResource;

        this.imageLoader.displayImage(imageUri,target);




    }

    /* Metodo que recibe el string base 64 de una imagen lo transforma en byte y los cargar en un
    * image view
    */
    public void  loadByBytesToImageView(String strB64,ImageView target){

        // validamos que se haya guarado la imagen es decir que haya bytes que convertir
        if(strB64 != null && !strB64.equals("")){
            byte[] loadBytes = Base64.decode(strB64 , Base64.DEFAULT);

            Glide.with(this.contexto).load(loadBytes).into(target);
        }

    }




    /* Metodo para cargar imagenes de fondo nesecita el id del drawable y el layout target
    * */

    public void loadBackground (int rDrawable, View target) {
        System.gc();
        this.targetLaoyut = target;

        String strUri = "drawable://" + rDrawable;

        if(ImageLoader.getInstance().isInited()){
            ImageLoader.getInstance().destroy();
        }

        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(this.contexto).build();
        ImageLoader.getInstance().init(config);

        this.imageLoader = ImageLoader.getInstance();

        this.imageLoader.loadImage(strUri, new SimpleImageLoadingListener() {
            @Override
            public void onLoadingComplete(String s, View view, Bitmap bitmap) {
                Log.e("MI FONDO", "SEVE");
                targetLaoyut.setBackgroundDrawable(new BitmapDrawable(bitmap));
            }
        });


    }
    /*Metodo que hace zoomeable una imagen*/
    public void setZooming(ImageView target){

        PhotoViewAttacher visorFoto = new PhotoViewAttacher(target);
        //escala inicial
        float scala = (float)1;
        visorFoto.setScale(scala,true);
        visorFoto.setScaleType(ImageView.ScaleType.FIT_CENTER);
        visorFoto.update();
    }





}
