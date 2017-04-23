package com.cmp2017.devworms.cmp2017;

/**
 * Created by AndrewAlan on 23/04/2017.
 */

public class AgendaModel {

    String tipoEvento;
    String idEvento;
    String urlImagen;
    String horarioEvento;
    String nombreEvento;
    String diaEvento;

    public AgendaModel(){}

    public AgendaModel(String tipoEvento, String idEvento, String urlImagen, String horarioEvento, String nombreEvento) {
        this.tipoEvento = tipoEvento;
        this.idEvento = idEvento;
        this.urlImagen = urlImagen;
        this.horarioEvento = horarioEvento;
        this.nombreEvento = nombreEvento;
    }

    public String getTipoEvento() {
        return tipoEvento;
    }

    public void setTipoEvento(String tipoEvento) {
        this.tipoEvento = tipoEvento;
    }

    public String getDiaEvento() {
        return diaEvento;
    }

    public void setDiaEvento(String diaEvento) {
        this.diaEvento = diaEvento;
    }

    public String getIdEvento() {
        return idEvento;
    }

    public void setIdEvento(String idEvento) {
        this.idEvento = idEvento;
    }

    public String getUrlImagen() {
        return urlImagen;
    }

    public void setUrlImagen(String urlImagen) {
        this.urlImagen = urlImagen;
    }

    public String getHorarioEvento() {
        return horarioEvento;
    }

    public void setHorarioEvento(String horarioEvento) {
        this.horarioEvento = horarioEvento;
    }

    public String getNombreEvento() {
        return nombreEvento;
    }

    public void setNombreEvento(String nombreEvento) {
        this.nombreEvento = nombreEvento;
    }
}
