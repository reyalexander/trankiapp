package com.example.guardian.models;

public class ContactCelModel {
    private int id_cont;
    private String name_cont;
    private String Celular_cont;

    public ContactCelModel(){}

    public ContactCelModel(int id_cont, String name_cont, String celular_cont) {
        this.id_cont = id_cont;
        this.name_cont = name_cont;
        Celular_cont = celular_cont;
    }

    public int getId_cont() {
        return id_cont;
    }

    public void setId_cont(int id_cont) {
        this.id_cont = id_cont;
    }

    public String getName_cont() {
        return name_cont;
    }

    public void setName_cont(String name_cont) {
        this.name_cont = name_cont;
    }

    public String getCelular_cont() {
        return Celular_cont;
    }

    public void setCelular_cont(String celular_cont) {
        Celular_cont = celular_cont;
    }
}
