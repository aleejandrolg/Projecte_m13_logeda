package data;

/////Clase Alumno, contiene todos los parametros para rellenar el formulario.

public class Alumno {

    private int id;
    private String Nombre;
    private String Apellidos;
    private String Fecha_Nacimiento;


    public Alumno(int id, String Nombre, String Apellidos, String Fecha_Nacimiento) {

        this.id = id;
        this.Nombre = Nombre;
        this.Apellidos = Apellidos;
        this.Fecha_Nacimiento = Fecha_Nacimiento;

    }


    public Alumno() {
    }

    public Alumno(String nombre, String apellidos, String fecha_nacimiento) {
        this.Nombre = Nombre;
        this.Apellidos = Apellidos;
        this.Fecha_Nacimiento = Fecha_Nacimiento;
    }

    ////////////////////si no funciona quitar el id recordar//////////////////////////////////
    public int getId() {

        return id;
    }

    public String getNombre() {


        return Nombre;
    }

    public String getApellidos() {

        return Apellidos;
    }


    public String getFecha_Nacimiento() {

        return Fecha_Nacimiento;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setNombre(String nombre) {
        Nombre = nombre;
    }

    public void setApellidos(String apellidos) {
        Apellidos = apellidos;
    }

    public void setFecha_Nacimiento(String fecha_Nacimiento) {
        Fecha_Nacimiento = fecha_Nacimiento;
    }
}
