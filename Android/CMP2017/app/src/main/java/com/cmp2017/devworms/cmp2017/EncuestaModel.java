package com.cmp2017.devworms.cmp2017;

import android.graphics.Bitmap;

/**
 * Created by AndrewAlan on 11/04/2017.
 */

public class EncuestaModel {
    public int id;
    public Bitmap imagen;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Bitmap getImagen() {
        return imagen;
    }

    public void setImagen(Bitmap imagen) {
        this.imagen = imagen;
    }
}
