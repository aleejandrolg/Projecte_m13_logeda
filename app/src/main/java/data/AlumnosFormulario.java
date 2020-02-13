package data;

import android.provider.BaseColumns;

public class AlumnosFormulario {


    public static abstract class EntradaAlumnos implements BaseColumns {

        public static final String TABLE_NAME = "alumno";

        public static final String ID = "id";
        public static final String Nombre = "Nombre";
        public static final String Apellidos = "Apellidos";
        public static final String Fecha_Nacimiento = "Fecha_de_Nacimiento";


    }
}
