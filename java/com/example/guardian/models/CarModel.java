package com.example.guardian.models;

public class CarModel {
    private int id;
    private String pos_lati;
    private String pos_longi;
    private String type;
    private String placa;
    private String modelo;
    private String motor;
    private String color;
    private String state;

    public CarModel(){};

    public CarModel(int id, String pos_lati, String pos_longi, String type, String placa, String modelo, String motor, String color, String state) {
        this.id = id;
        this.pos_lati = pos_lati;
        this.pos_longi = pos_longi;
        this.type = type;
        this.placa = placa;
        this.modelo = modelo;
        this.motor = motor;
        this.color = color;
        this.state = state;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getPlaca() {
        return placa;
    }

    public void setPlaca(String placa) {
        this.placa = placa;
    }

    public String getModelo() {
        return modelo;
    }

    public void setModelo(String modelo) {
        this.modelo = modelo;
    }

    public String getMotor() {
        return motor;
    }

    public void setMotor(String motor) {
        this.motor = motor;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }
}
