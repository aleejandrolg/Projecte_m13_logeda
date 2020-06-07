package alumne.vidalibarraquer.logopeda;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import data.Actividad;
import data.ManejadordeDatos;

public class mostrarActividad extends AppCompatActivity implements View.OnClickListener {

    TextView tw_actividad_Id;
    TextView tw_inicio;
    TextView tw_final;
    TextView tw_actividad;
    TextView tw_alumno;
    TextView tw_valoracion;
    ImageView imW_actividad;
    Button bt_nota;


    String id;
    String actividad;
    String alumnoID;
    String nombreAlu;
    String apellidoAlu;
    String fecha;
    String h_inicio;
    String h_final;
    byte[] image;
    boolean buit;
    Bitmap imagen;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_muestra_actividad);

        tw_actividad_Id = (TextView) findViewById(R.id.tw_actividad_Id);
        tw_inicio = (TextView) findViewById(R.id.tw_inicio);
        tw_final = (TextView) findViewById(R.id.tw_final);
        tw_actividad = (TextView) findViewById(R.id.tw_nom);
        tw_alumno = (TextView) findViewById(R.id.tw_alumno);
        tw_valoracion = (TextView) findViewById(R.id.tw_valoracion);
        imW_actividad = (ImageView) findViewById(R.id.imW_actividad);
        bt_nota = (Button) findViewById(R.id.bt_nota);
        bt_nota.setOnClickListener(this);


        Intent intent = getIntent();

        id = intent.getStringExtra("id");
        actividad = intent.getStringExtra("actividad");
        alumnoID = intent.getStringExtra("alumnoID");
        nombreAlu = intent.getStringExtra("nombreAlu");
        apellidoAlu = intent.getStringExtra("apellidoAlu");
        fecha = intent.getStringExtra("fecha");
        h_inicio = intent.getStringExtra("inicio");
        h_final = intent.getStringExtra("final");
        image = intent.getByteArrayExtra("image");


        try {
            imagen = BitmapFactory.decodeByteArray(image, 0, image.length);
        } catch (NullPointerException e) {
            buit = true;
        }
        ////// si no hay imagen lo mostrara sin imagen//////
        if (buit == true) {

            ////limpia el imageview antes de mostrar la imagen////////
            imW_actividad.setImageResource(0);
            String nombreapellidoAlu = nombreAlu + " " + apellidoAlu;

            tw_actividad_Id.setText(id);
            tw_inicio.setText(h_inicio);
            tw_final.setText(h_final);
            tw_actividad.setText(actividad);
            tw_alumno.setText(nombreapellidoAlu);

            ////// si hay imagen la mostrara///////////////////

        } else {
            ////limpia el imageview antes de mostrar la imagen////////
            imW_actividad.setImageResource(0);
            imagen = BitmapFactory.decodeByteArray(image, 0, image.length);

            String nombreapellidoAlu = nombreAlu + " " + apellidoAlu;

            tw_actividad_Id.setText(id);
            tw_inicio.setText(h_inicio);
            tw_final.setText(h_final);
            tw_actividad.setText(actividad);
            tw_alumno.setText(nombreapellidoAlu);
            imW_actividad.setImageBitmap(imagen);
        }
    }

    @Override

    //////Añade una valoracion a la actividad esta valoracion se podra ver en cada alumno//////
    public void onClick(View v) {
        String valoracion = String.valueOf(tw_valoracion.getText());

        int ide = Integer.parseInt(id);


        int alumnoIDe = Integer.parseInt(alumnoID);

        Actividad upactividad = new Actividad(ide, alumnoIDe, actividad, fecha, h_inicio, h_final, image, valoracion);


        ManejadordeDatos manejadordeDatos = new ManejadordeDatos(mostrarActividad.this);

        manejadordeDatos.anadirValoracion(upactividad);

        Toast.makeText(getApplicationContext(), getApplicationContext().getText(R.string.nota_add), Toast.LENGTH_SHORT).show();

        tw_valoracion.setText("");

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        this.finish();

        Intent mostrar = new Intent(mostrarActividad.this, inicio.class);
        startActivity(mostrar);


    }
}
