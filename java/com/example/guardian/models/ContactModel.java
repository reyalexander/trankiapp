package com.example.guardian.models;

public class ContactModel {
    private int id_cont;
    private int id_cont_api;
    private String pos_lati_cont;
    private String pos_longi_cont;
    private String name_cont;
    private String apelli_cont;
    private String correo_cont;
    private String DNI_cont;
    private String Celular_cont;
    private String Addrescont;
    private String state_cont;

    public ContactModel(){}

    public ContactModel(int id_cont, String pos_lati_cont, String pos_longi_cont, String name_cont, String correo_cont, String DNI_cont, String celular_cont, String state_cont) {
        this.id_cont = id_cont;
        this.pos_lati_cont = pos_lati_cont;
        this.pos_longi_cont = pos_longi_cont;
        this.name_cont = name_cont;
        this.correo_cont = correo_cont;
        this.DNI_cont = DNI_cont;
        Celular_cont = celular_cont;
        this.state_cont = state_cont;
    }

    public int getId_cont_api() {
        return id_cont_api;
    }

    public void setId_cont_api(int id_cont_api) {
        this.id_cont_api = id_cont_api;
    }

    public int getId_cont() {
        return id_cont;
    }

    public void setId_cont(int id_cont) {
        this.id_cont = id_cont;
    }

    public String getPos_lati_cont() {
        return pos_lati_cont;
    }

    public void setPos_lati_cont(String pos_lati_cont) {
        this.pos_lati_cont = pos_lati_cont;
    }

    public String getPos_longi_cont() {
        return pos_longi_cont;
    }

    public void setPos_longi_cont(String pos_longi_cont) {
        this.pos_longi_cont = pos_longi_cont;
    }

    public String getName_cont() {
        return name_cont;
    }

    public void setName_cont(String name_cont) {
        this.name_cont = name_cont;
    }

    public String getApelli_cont() {
        return apelli_cont;
    }

    public void setApelli_cont(String apelli_cont) {
        this.apelli_cont = apelli_cont;
    }

    public String getAddrescont() {
        return Addrescont;
    }

    public void setAddrescont(String addrescont) {
        Addrescont = addrescont;
    }

    public String getCorreo_cont() {
        return correo_cont;
    }

    public void setCorreo_cont(String correo_cont) {
        this.correo_cont = correo_cont;
    }

    public String getDNI_cont() {
        return DNI_cont;
    }

    public void setDNI_cont(String DNI_cont) {
        this.DNI_cont = DNI_cont;
    }

    public String getCelular_cont() {
        return Celular_cont;
    }

    public void setCelular_cont(String celular_cont) {
        Celular_cont = celular_cont;
    }

    public String getState_cont() {
        return state_cont;
    }

    public void setState_cont(String state_cont) {
        this.state_cont = state_cont;
    }
}
