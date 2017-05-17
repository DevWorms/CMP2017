package com.cmp2017.devworms.cmp2017;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Fragment;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.ImageView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import uk.co.senab.photoview.PhotoViewAttacher;

public class MapaPueblaFragment extends Fragment{

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_mapa_puebla, container, false);
        // Create global configuration and initialize ImageLoader with this config
        final ImageView mapaPuebla = (ImageView)view.findViewById(R.id.imgPuebla);

        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(getActivity()).build();
        ImageLoader.getInstance().init(config);

        ImageLoader imageLoader = ImageLoader.getInstance();
        String imageUri = "drawable://" + R.drawable.mapapuebla;
        imageLoader.displayImage(imageUri,mapaPuebla);

        PhotoViewAttacher visorFoto = new PhotoViewAttacher(mapaPuebla);
        float scala = (float)1;
        visorFoto.setScale(scala,true);
        visorFoto.setScaleType(ImageView.ScaleType.FIT_CENTER);
        visorFoto.update();

        return view;
    }


}
