package data;

import android.media.Image;
import android.provider.BaseColumns;

import java.sql.Blob;

public class ActividadesFormulario {


    public static abstract class EntradaActividades implements BaseColumns {

        public static final String Tabla_Actividades = "actividades";
        public static final String ID = "id";
        public static final String alumnoID ="alumnoID";
        public static final String actividad = "actividad";
        public static final String fecha = "fecha";
        public static final String hora_inicio = "hora_inicio";
        public static final String hora_final="hora_final";
        public static final String imagen= "image_data";
        public static final String valoracion="valoracion";




    }
}
