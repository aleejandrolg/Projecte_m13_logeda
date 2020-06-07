package data;

import android.media.Image;

import java.util.Date;

public class Actividad {

    private int ID;
    private int alumnoID;
    private String actividad;
    private String fecha;
    private String hora_inicio;
    private String hora_finaliza;
    private byte[] imagen;
    private String valoracion;

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public int getAlumnoID() {
        return alumnoID;
    }

    public void setAlumnoID(int alumnoID) {
        this.alumnoID = alumnoID;
    }

    public String getActividad() {
        return actividad;
    }

    public void setActividad(String actividad) {
        this.actividad = actividad;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getHora_inicio() {
        return hora_inicio;
    }

    public void setHora_inicio(String hora_inicio) {
        this.hora_inicio = hora_inicio;
    }

    public String getHora_finaliza() {
        return hora_finaliza;
    }

    public void setHora_finaliza(String hora_finaliza) {
        this.hora_finaliza = hora_finaliza;
    }

    public byte[] getImagen() {
        return imagen;
    }

    public void setImagen(byte[] imagen) {
        this.imagen = imagen;
    }

    public String getValoracion() {
        return valoracion;
    }

    public void setValoracion(String valoracion) {
        this.valoracion = valoracion;
    }

    public Actividad(int alumnoID, String actividad, String fecha, String hora_inicio, String hora_finaliza, byte[] imagen) {
        this.alumnoID = alumnoID;
        this.actividad = actividad;
        this.fecha = fecha;
        this.hora_inicio = hora_inicio;
        this.hora_finaliza = hora_finaliza;
        this.imagen = imagen;
    }

    public Actividad(int ID, int alumnoID, String actividad, String fecha, String hora_inicio, String hora_finaliza) {
        this.ID = ID;
        this.alumnoID = alumnoID;
        this.actividad = actividad;
        this.fecha = fecha;
        this.hora_inicio = hora_inicio;
        this.hora_finaliza = hora_finaliza;
    }

    public Actividad(int ID, int alumnoID, String actividad, String fecha, String hora_inicio, String hora_finaliza, byte[] imagen, String valoracion) {
        this.ID = ID;
        this.alumnoID = alumnoID;
        this.actividad = actividad;
        this.fecha = fecha;
        this.hora_inicio = hora_inicio;
        this.hora_finaliza = hora_finaliza;
        this.imagen = imagen;
        this.valoracion = valoracion;
    }

    public Actividad(int ID, int alumnoID, String actividad, String fecha, String hora_inicio, String hora_finaliza, String valoracion) {
        this.ID = ID;
        this.alumnoID = alumnoID;
        this.actividad = actividad;
        this.fecha = fecha;
        this.hora_inicio = hora_inicio;
        this.hora_finaliza = hora_finaliza;
        this.valoracion = valoracion;
    }

    public Actividad() {
    }
}


