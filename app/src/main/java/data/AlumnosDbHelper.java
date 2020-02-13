package data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;

import java.util.ArrayList;

import alumne.vidalibarraquer.logopeda.mostrar;


public class AlumnosDbHelper extends SQLiteOpenHelper {


    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "Alumnos.db";

    private static final String KEY_ID = "id";

    public AlumnosDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        sqLiteDatabase.execSQL("CREATE TABLE " + AlumnosFormulario.EntradaAlumnos.TABLE_NAME + " ("

                + AlumnosFormulario.EntradaAlumnos.ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + AlumnosFormulario.EntradaAlumnos.Nombre + " TEXT NOT NULL,"
                + AlumnosFormulario.EntradaAlumnos.Apellidos + " TEXT NOT NULL,"
                + AlumnosFormulario.EntradaAlumnos.Fecha_Nacimiento + " TEXT NOT NULL )"
        );


    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + AlumnosFormulario.EntradaAlumnos.TABLE_NAME);
        onCreate(db);
    }

    public void agregarAlumne(String nombre, String apellidos, String f_nacimientos) {

        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        if (sqLiteDatabase != null) {

            ContentValues values = new ContentValues();


            values.put(AlumnosFormulario.EntradaAlumnos.Nombre, nombre);
            values.put(AlumnosFormulario.EntradaAlumnos.Apellidos, apellidos);
            values.put(AlumnosFormulario.EntradaAlumnos.Fecha_Nacimiento, f_nacimientos);
            sqLiteDatabase.insert(AlumnosFormulario.EntradaAlumnos.TABLE_NAME, null, values);

            System.out.println(AlumnosFormulario.EntradaAlumnos.ID + nombre + apellidos + f_nacimientos);

            sqLiteDatabase.close();

        }
    }


    public void eliminarAlumne(Alumno alumno) {


        SQLiteDatabase db = this.getWritableDatabase();


        db.delete(AlumnosFormulario.EntradaAlumnos.TABLE_NAME, KEY_ID + "=?", new String[]{String.valueOf(alumno.getId())});

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


}
