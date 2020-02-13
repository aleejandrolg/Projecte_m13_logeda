package alumne.vidalibarraquer.logopeda;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListAdapter;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.HashMap;

public class mostrarAlumno extends AppCompatActivity {

    TextView tw_id;
    TextView tw_nombre;
    TextView tw_apellidos;
    TextView tw_fecha;

    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mostra_alumne);

        Intent intent = getIntent();

        String id = intent.getStringExtra("id");
        String nombre = intent.getStringExtra("nombre");
        String apellidos = intent.getStringExtra("apellidos");
        String fecha = intent.getStringExtra("fecha");


        tw_id = (TextView) findViewById(R.id.text_ID);
        tw_nombre = (TextView) findViewById(R.id.text_Nombre);
        tw_apellidos = (TextView) findViewById(R.id.text_Apellidos);
        tw_fecha = (TextView) findViewById(R.id.text_Fecha);


        tw_id.setText("ID: "+id);
        tw_nombre.setText("Nombre: "+nombre);
        tw_apellidos.setText("Apellidos: "+apellidos);
        tw_fecha.setText("Fecha: "+fecha);

    }



}
