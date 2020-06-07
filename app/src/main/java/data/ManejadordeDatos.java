package data;

import android.content.ActivityNotFoundException;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.media.Image;
import android.os.Bundle;

import java.sql.Blob;
import java.util.ArrayList;
import java.util.List;

import alumne.vidalibarraquer.logopeda.mostrar;


public class ManejadordeDatos extends SQLiteOpenHelper {


    public static final int DATABASE_VERSION = 12;
    public static final String DATABASE_NAME = "Alumnos.db";

    private static final String KEY_ID = "id";

    public ManejadordeDatos(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL("CREATE TABLE " + AlumnosFormulario.EntradaAlumnos.Tabla_Alumnos + " ("

                + AlumnosFormulario.EntradaAlumnos.ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + AlumnosFormulario.EntradaAlumnos.Nombre + " TEXT NOT NULL,"
                + AlumnosFormulario.EntradaAlumnos.Apellidos + " TEXT NOT NULL,"
                + AlumnosFormulario.EntradaAlumnos.Fecha_Nacimiento + " TEXT NOT NULL )"
        );


        db.execSQL("CREATE TABLE " + ActividadesFormulario.EntradaActividades.Tabla_Actividades + " ("
                + ActividadesFormulario.EntradaActividades.ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + ActividadesFormulario.EntradaActividades.actividad + " TEXT NOT NULL,"
                + ActividadesFormulario.EntradaActividades.fecha + " TEXT NOT NULL,"
                + ActividadesFormulario.EntradaActividades.hora_inicio + " TEXT NOT NULL, "
                + ActividadesFormulario.EntradaActividades.hora_final + " TEXT NOT NULL,"
                + ActividadesFormulario.EntradaActividades.valoracion + " TEXT,"
                + ActividadesFormulario.EntradaActividades.imagen + " BLOB,"
                + ActividadesFormulario.EntradaActividades.alumnoID + " INTEGER, "
                + " FOREIGN KEY (" + ActividadesFormulario.EntradaActividades.alumnoID + ") REFERENCES " + AlumnosFormulario.EntradaAlumnos.Tabla_Alumnos + "(" + AlumnosFormulario.EntradaAlumnos.ID + "))");


    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + AlumnosFormulario.EntradaAlumnos.Tabla_Alumnos);
        db.execSQL("DROP TABLE IF EXISTS " + ActividadesFormulario.EntradaActividades.Tabla_Actividades);
        onCreate(db);
    }

    public void agregarAlumne(String nombre, String apellidos, String f_nacimientos) {

        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        if (sqLiteDatabase != null) {

            ContentValues values = new ContentValues();


            values.put(AlumnosFormulario.EntradaAlumnos.Nombre, nombre);
            values.put(AlumnosFormulario.EntradaAlumnos.Apellidos, apellidos);
            values.put(AlumnosFormulario.EntradaAlumnos.Fecha_Nacimiento, f_nacimientos);
            sqLiteDatabase.insert(AlumnosFormulario.EntradaAlumnos.Tabla_Alumnos, null, values);

            System.out.println(AlumnosFormulario.EntradaAlumnos.ID + nombre + apellidos + f_nacimientos);

            sqLiteDatabase.close();

        }
    }


    public void eliminarAlumne(Alumno alumno) {


        SQLiteDatabase db = this.getWritableDatabase();


        db.delete(AlumnosFormulario.EntradaAlumnos.Tabla_Alumnos, AlumnosFormulario.EntradaAlumnos.ID + "=?", new String[]{String.valueOf(alumno.getId())});

        db.close();


    }


    public ArrayList llenar_listview() {

        ArrayList<Alumno> lista = new ArrayList<>();

        SQLiteDatabase database = this.getWritableDatabase();

        String consulta = "SELECT * FROM alumno";

        Cursor registros = database.rawQuery(consulta, null);

        if (registros.moveToFirst()) {

            do {

                Alumno alumno = new Alumno();
                alumno = new Alumno(registros.getInt(0), registros.getString(1), registros.getString(2), registros.getString(3));

                lista.add(alumno);

            } while (registros.moveToNext());

        }


        return lista;


    }


    public Alumno obtenerAlumno(int id) {


        SQLiteDatabase db = this.getReadableDatabase();

        //////Creamos un cursor para desplazarse por los registros para localizar el alumno con el id////


        Cursor cursor = db.query(AlumnosFormulario.EntradaAlumnos.Tabla_Alumnos, new String[]{AlumnosFormulario.EntradaAlumnos.ID, AlumnosFormulario.EntradaAlumnos.Nombre, AlumnosFormulario.EntradaAlumnos.Apellidos, AlumnosFormulario.EntradaAlumnos.Fecha_Nacimiento},
                AlumnosFormulario.EntradaAlumnos.ID + "= ?", new String[]{String.valueOf(id)}, null, null, null, null);


        boolean trobat = false;

        if (cursor != null) {

            trobat = cursor.moveToFirst();

            cursor.moveToFirst();

        } else {

            trobat = false;
        }


        if (trobat == true) {

            Alumno alumno = new Alumno((cursor.getInt(0)), (cursor.getString(1)), cursor.getString(2), cursor.getString(3));


            return alumno;

        } else {

            return null;
        }
    }



    public int updateAlumno(Alumno alumno) {

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues valores = new ContentValues();

        valores.put(AlumnosFormulario.EntradaAlumnos.ID, alumno.getId());
        valores.put(AlumnosFormulario.EntradaAlumnos.Nombre, alumno.getNombre());
        valores.put(AlumnosFormulario.EntradaAlumnos.Apellidos, alumno.getApellidos());
        valores.put(AlumnosFormulario.EntradaAlumnos.Fecha_Nacimiento, alumno.getFecha_Nacimiento());


        //Actualitzamos la base de dadtos


        return db.update(AlumnosFormulario.EntradaAlumnos.Tabla_Alumnos, valores, AlumnosFormulario.EntradaAlumnos.ID + "=?",
                new String[]{String.valueOf(alumno.getId())});


    }



    ///////////////////////////////////////////////////////////////////////////////Parte de las actividades//////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////Agregar actividades a la tabla//////////////////////////////////////////////////////
    public void agregarActividad(int alumnoID, String actividad, String fecha, String hora_inicio, String hora_final, byte[] imagen) {

        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        if (sqLiteDatabase != null) {

            ContentValues values = new ContentValues();

            values.put(ActividadesFormulario.EntradaActividades.alumnoID, alumnoID);
            values.put(ActividadesFormulario.EntradaActividades.actividad, actividad);
            values.put(ActividadesFormulario.EntradaActividades.fecha, fecha);
            values.put(ActividadesFormulario.EntradaActividades.hora_inicio, hora_inicio);
            values.put(ActividadesFormulario.EntradaActividades.hora_final, hora_final);
            values.put(ActividadesFormulario.EntradaActividades.imagen, imagen);
            sqLiteDatabase.insert(ActividadesFormulario.EntradaActividades.Tabla_Actividades, null, values);

            sqLiteDatabase.close();

        }
    }


    public List<Actividad> actividadPorfecha(String fecha) {

        List<Actividad> actividades = new ArrayList<>();

        SQLiteDatabase db = this.getWritableDatabase();

        ////Creamos un cursor para desplazarnos por los registros y localizar aquellas actividades que coincidan con la fecha/////


        Cursor cursor = db.query(ActividadesFormulario.EntradaActividades.Tabla_Actividades, new String[]{ActividadesFormulario.EntradaActividades.ID, ActividadesFormulario.EntradaActividades.alumnoID, ActividadesFormulario.EntradaActividades.actividad, ActividadesFormulario.EntradaActividades.fecha, ActividadesFormulario.EntradaActividades.hora_inicio, ActividadesFormulario.EntradaActividades.hora_final, ActividadesFormulario.EntradaActividades.imagen},
                ActividadesFormulario.EntradaActividades.fecha + "= ?", new String[]{String.valueOf(fecha)}, null, null, null, null
        );


        if (cursor.moveToFirst()) {
            do {

                Actividad actividad = new Actividad();

                actividad.setID(cursor.getInt(0));
                actividad.setAlumnoID(cursor.getInt(1));
                actividad.setActividad(cursor.getString(2));
                actividad.setFecha(cursor.getString(3));
                actividad.setHora_inicio(cursor.getString(4));
                actividad.setHora_finaliza(cursor.getString(5));
                actividad.setImagen(cursor.getBlob(6));


                actividades.add(actividad);


            } while (cursor.moveToNext());


        }

        return actividades;

    }

    public void eliminarActivadesPorAlumno(String alumnoid) {


        SQLiteDatabase db = this.getWritableDatabase();


        db.delete(ActividadesFormulario.EntradaActividades.Tabla_Actividades, ActividadesFormulario.EntradaActividades.alumnoID + "=?", new String[]{alumnoid});

        db.close();


    }


    public List<Actividad> actividadesPorAlumnis(String alumnoid) {

        List<Actividad> actividades = new ArrayList<>();

        SQLiteDatabase db = this.getWritableDatabase();

        ////Creamos un cursor para desplazarnos por los registros y localizar aquellas actividades que coincidan con la fecha/////


        Cursor cursor = db.query(ActividadesFormulario.EntradaActividades.Tabla_Actividades, new String[]{ActividadesFormulario.EntradaActividades.ID, ActividadesFormulario.EntradaActividades.alumnoID, ActividadesFormulario.EntradaActividades.actividad, ActividadesFormulario.EntradaActividades.fecha, ActividadesFormulario.EntradaActividades.hora_inicio, ActividadesFormulario.EntradaActividades.hora_final, ActividadesFormulario.EntradaActividades.imagen, ActividadesFormulario.EntradaActividades.valoracion},
                ActividadesFormulario.EntradaActividades.alumnoID + "= ?", new String[]{String.valueOf(alumnoid)}, null, null, null, null
        );


        if (cursor.moveToFirst()) {
            do {

                Actividad actividad = new Actividad();

                actividad.setID(cursor.getInt(0));
                actividad.setAlumnoID(cursor.getInt(1));
                actividad.setActividad(cursor.getString(2));
                actividad.setFecha(cursor.getString(3));
                actividad.setHora_inicio(cursor.getString(4));
                actividad.setHora_finaliza(cursor.getString(5));
                actividad.setImagen(cursor.getBlob(6));
                actividad.setValoracion(cursor.getString(7));


                actividades.add(actividad);


            } while (cursor.moveToNext());


        }

        return actividades;

    }


    public void eliminarActividad(Actividad actividad) {


        SQLiteDatabase db = this.getWritableDatabase();


        db.delete(ActividadesFormulario.EntradaActividades.Tabla_Actividades, KEY_ID + "=?", new String[]{String.valueOf(actividad.getID())});

        db.close();


    }


    public int anadirValoracion(Actividad actividad) {

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues valores = new ContentValues();

        valores.put(ActividadesFormulario.EntradaActividades.ID, actividad.getID());
        valores.put(ActividadesFormulario.EntradaActividades.alumnoID, actividad.getAlumnoID());
        valores.put(ActividadesFormulario.EntradaActividades.actividad, actividad.getActividad());
        valores.put(ActividadesFormulario.EntradaActividades.fecha, actividad.getFecha());
        valores.put(ActividadesFormulario.EntradaActividades.hora_inicio, actividad.getHora_inicio());
        valores.put(ActividadesFormulario.EntradaActividades.hora_final, actividad.getHora_finaliza());
        valores.put(ActividadesFormulario.EntradaActividades.imagen, actividad.getImagen());
        valores.put(ActividadesFormulario.EntradaActividades.valoracion, actividad.getValoracion());


        return db.update(ActividadesFormulario.EntradaActividades.Tabla_Actividades, valores, ActividadesFormulario.EntradaActividades.ID + "=?", new String[]{String.valueOf(actividad.getID())});


    }


}
