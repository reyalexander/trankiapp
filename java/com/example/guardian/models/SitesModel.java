package com.example.guardian.models;

public class SitesModel {
    private int id;
    private String name;
    private String pos_lati;
    private String pos_longi;
    private String direccion;
    private String correo;
    private String RUC;
    private String horario;
    private String state;

    public SitesModel(){};

    public SitesModel(int id, String name, String pos_lati, String pos_longi, String direccion, String correo, String RUC, String horario, String state) {
        this.id = id;
        this.name = name;
        this.pos_lati = pos_lati;
        this.pos_longi = pos_longi;
        this.direccion = direccion;
        this.correo = correo;
        this.RUC = RUC;
        this.horario = horario;
        this.state = state;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPos_lati() {
        return pos_lati;
    }

    public void setPos_lati(String pos_lati) {
        this.pos_lati = pos_lati;
    }

    public String getPos_longi() {
        return pos_longi;
    }

    public void setPos_longi(String pos_longi) {
        this.pos_longi = pos_longi;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getRUC() {
        return RUC;
    }

    public void setRUC(String RUC) {
        this.RUC = RUC;
    }

    public String getHorario() {
        return horario;
    }

    public void setHorario(String horario) {
        this.horario = horario;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }
}
