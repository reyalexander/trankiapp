package com.example.guardian.models;

public class TicketModel {
    private int id;
    private String codDispositivo;
    private String numOperacion;
    private String fecha;
    private String hora;
    private String cliNombre;
    private String cliCelular;
    private String direccion;
    private double latitud;
    private double longitud;
    private String urlMap;

    public TicketModel(){}

    public TicketModel(int id, String codDispositivo, String numOperacion, String fecha, String hora, String cliNombre, String cliCelular, String direccion, double latitud, double longitud, String urlMap) {
        this.id = id;
        this.codDispositivo = codDispositivo;
        this.numOperacion = numOperacion;
        this.fecha = fecha;
        this.hora = hora;
        this.cliNombre = cliNombre;
        this.cliCelular = cliCelular;
        this.direccion = direccion;
        this.latitud = latitud;
        this.longitud = longitud;
        this.urlMap = urlMap;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCodDispositivo() {
        return codDispositivo;
    }

    public void setCodDispositivo(String codDispositivo) {
        this.codDispositivo = codDispositivo;
    }

    public String getNumOperacion() {
        return numOperacion;
    }

    public void setNumOperacion(String numOperacion) {
        this.numOperacion = numOperacion;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getHora() {
        return hora;
    }

    public void setHora(String hora) {
        this.hora = hora;
    }

    public String getCliNombre() {
        return cliNombre;
    }

    public void setCliNombre(String cliNombre) {
        this.cliNombre = cliNombre;
    }

    public String getCliCelular() {
        return cliCelular;
    }

    public void setCliCelular(String cliCelular) {
        this.cliCelular = cliCelular;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getUrlMap() {
        return urlMap;
    }

    public void setUrlMap(String urlMap) {
        this.urlMap = urlMap;
    }

    public double getLatitud() {
        return latitud;
    }

    public void setLatitud(double latitud) {
        this.latitud = latitud;
    }

    public double getLongitud() {
        return longitud;
    }

    public void setLongitud(double longitud) {
        this.longitud = longitud;
    }
}
