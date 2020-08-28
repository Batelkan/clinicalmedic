package com.example.centermedic.models;

public class citas {

    private String Area;
    private String Medico;
    private String Hospital;
    private String Fecha;
    private String Horario;

    public String getLongitud() {
        return Longitud;
    }

    public void setLongitud(String longitud) {
        Longitud = longitud;
    }

    public String getLatitud() {
        return Latitud;
    }

    public void setLatitud(String latitud) {
        Latitud = latitud;
    }

    public String getTelefono() {
        return Telefono;
    }

    public void setTelefono(String telefono) {
        Telefono = telefono;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    private String Longitud;
    private String Latitud;
    private String Telefono;
    private String uuid;


    public citas()
    {

    }
    public  citas(String Area, String Medico, String Hospital,String Fecha, String Horario,String uuid)
    {
        this.Area= Area;
        this.Medico=Medico;
        this.Hospital = Hospital;
        this.Fecha= Fecha;
        this.Horario = Horario;
        this.uuid = uuid;

    }



    public String getArea() {
        return Area;
    }

    public void setArea(String area) {
        Area = Area;
    }

    public String getMedico() {
        return Medico;
    }

    public void setMedico(String medico) {
        Medico = Medico;
    }

    public String getHospital() {
        return Hospital;
    }

    public void setHospital(String Hospital) {
        Hospital = Hospital;
    }

    public String getHorario() {
        return Horario;
    }

    public void setHorario(String horario) {
        Horario = Horario;
    }

    public String getFecha() {
        return Fecha;
    }

    public void setFecha(String fecha) {
        Fecha = Fecha;
    }

}
