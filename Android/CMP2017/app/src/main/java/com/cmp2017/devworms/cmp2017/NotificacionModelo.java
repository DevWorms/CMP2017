package com.cmp2017.devworms.cmp2017;

/**
 * Created by Alienware on 25/04/2017.
 */

public class NotificacionModelo {
    // test
    public int id;
    public int user_id;
    public  String notificacion;
    public  String asunto;
    public int leido;
    public  String saved;
    public  String created;
    public NotificacionModelo(String notificacion, int leido)

    {
        this.notificacion = notificacion;
        this.leido = leido;

    }
    public int getid() {
        return user_id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public int getUser_id() {
        return user_id;
    }
    public void setUser_id(int user_id) {
        this.user_id =user_id ;
    }
    public String getNotificacion() {
        return notificacion;
    }
    public void setNotificacion(String notificacion) {
        this.notificacion =notificacion;
    }
    public int getLeido() {
        return leido;
    }
    public void setLeido(int leido) {
        this.leido=leido;
    }
    public String getSaved() {
        return saved;
    }
    public void setSaved(String saved) {
        this.saved = saved;
    }
    public String getCreated() {
        return created;
    }
    public void setCreated(String created) {
        this.created = created;
    }


}
