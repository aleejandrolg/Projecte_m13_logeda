package alumne.vidalibarraquer.logopeda;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import data.Actividad;
import data.Alumno;
import data.ManejadordeDatos;

public class mostrarAlumno extends AppCompatActivity implements View.OnClickListener {

    TextView tw_id;
    TextView tw_nombre;
    TextView tw_apellidos;
    TextView tw_fecha;

    Button actualizar;

    ListView Lw_notas;

    String id;
    String nombre;
    String apellidos;
    String fecha;

    EditText editText;

    AlertDialog dialog;

    Calendar c = Calendar.getInstance();
    //Variables para obtener la fecha
    final int mes = c.get(Calendar.MONTH);
    final int dia = c.get(Calendar.DAY_OF_MONTH);
    final int anio = c.get(Calendar.YEAR);
    //Widgets


    private static final String CERO = "0";
    private static final String BARRA = "/";


    final int hora = c.get(Calendar.HOUR_OF_DAY);
    final int minuto = c.get(Calendar.MINUTE);

    Calendar calendar = Calendar.getInstance();
    long hoy = System.currentTimeMillis();
    Date fechahoy = new Date(hoy);


    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_muestra_alumno);

        Intent intent = getIntent();

        dialog = new AlertDialog.Builder(this).create();

        id = intent.getStringExtra("id");
        nombre = intent.getStringExtra("nombre");
        apellidos = intent.getStringExtra("apellidos");
        fecha = intent.getStringExtra("fecha");


        tw_id = (TextView) findViewById(R.id.text_ID);
        tw_nombre = (TextView) findViewById(R.id.text_Nombre);
        tw_apellidos = (TextView) findViewById(R.id.text_Apellidos);
        tw_fecha = (TextView) findViewById(R.id.text_Fecha);


        tw_id.setText(id);
        tw_nombre.setText(nombre);
        tw_apellidos.setText(apellidos);
        tw_fecha.setText(fecha);

        editText = new EditText(this);

        dialog.setView(editText);

        tw_nombre.setOnClickListener(this);
        tw_apellidos.setOnClickListener(this);
        tw_fecha.setOnClickListener(this);

        actualizar = (Button) findViewById(R.id.bt_Actualizar);
        actualizar.setOnClickListener(this);

        Lw_notas = (ListView) findViewById(R.id.lw_notas);

        calendar.setTime(fechahoy);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        listar();

    }

    private void listar() {


        final ManejadordeDatos actividadesDB = new ManejadordeDatos(getApplicationContext());

        final List<Actividad> actividades = actividadesDB.actividadesPorAlumnis(id);
        HashMap<String, Object> hashMap;
        final ArrayList<HashMap<String, Object>> listactividades;
        listactividades = new ArrayList<>();


        for (final Actividad actividad : actividades) {


            String actividadst = actividad.getActividad();
            String nota = actividad.getValoracion();


            hashMap = new HashMap<String, Object>();

            hashMap.put("actividad", actividadst);
            hashMap.put("nota", nota);

            listactividades.add(hashMap);

            final ListAdapter adaptador = new SimpleAdapter(this, listactividades, R.layout.actividad, new String[]{"actividad", "nota"}, new int[]{R.id.tw_nom_Actividad, R.id.tw_nota});


            Lw_notas.setAdapter(adaptador);

        }

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        this.finish();

        Intent mostrar = new Intent(mostrarAlumno.this, mostrar.class);
        startActivity(mostrar);


    }


    ///////si queremos cambiar algun valor del alumno le daras click a alguno de los textview y se mostrara un dialogo para poder cambiarlo//////
    @Override
    public void onClick(View v) {

        switch (v.getId()) {


            case R.id.text_Nombre:


                dialog.setButton(DialogInterface.BUTTON_POSITIVE, getApplicationContext().getString(R.string.guardar_Registro), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        tw_nombre.setText(editText.getText());


                    }
                });

                editText.setText(tw_nombre.getText());
                dialog.show();
                break;


            case R.id.text_Apellidos:
                dialog.setButton(DialogInterface.BUTTON_POSITIVE, getApplicationContext().getString(R.string.guardar_Registro), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        tw_apellidos.setText(editText.getText());


                    }
                });

                editText.setText(tw_apellidos.getText());
                dialog.show();


                break;

            case R.id.text_Fecha:

                obtenerFecha();


                break;


            case R.id.bt_Actualizar:
                int id = Integer.parseInt(tw_id.getText().toString());
                String nom = tw_nombre.getText().toString();
                String apellido = tw_apellidos.getText().toString();
                String macimiento = tw_fecha.getText().toString();


                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

                    //////comprueba que todos los campos esten llenos/////
                if (!nom.trim().equalsIgnoreCase("")&&!apellido.trim().equalsIgnoreCase("")&&!macimiento.trim().equalsIgnoreCase("")) {

                    try {


                        Date srDate = sdf.parse(tw_fecha.getText().toString());


                        fechahoy = calendar.getTime();

                        ///////comprueba que la fecha de nacimiento sea inferior a la fecha de hoy/////////////

                        if (fechahoy.getTime() >= srDate.getTime()) {


                            Toast.makeText(getApplicationContext(), getApplicationContext().getText(R.string.registro_actualizado), Toast.LENGTH_SHORT).show();

                            Alumno alumno = new Alumno(id, nom, apellido, macimiento);

                            ManejadordeDatos manejadordeDatos = new ManejadordeDatos(mostrarAlumno.this);
                            manejadordeDatos.updateAlumno(alumno);

                        } else {
                            Toast.makeText(getApplicationContext(), getApplicationContext().getText(R.string.fecha_mal), Toast.LENGTH_SHORT).show();

                        }

                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }else{

                    Toast.makeText(getApplicationContext(), getApplicationContext().getText(R.string.faltan), Toast.LENGTH_SHORT).show();
                }


                break;
        }
    }



    ///////obitene la fecha y la formatea//////////

    private void obtenerFecha() {
        DatePickerDialog recogerFecha = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                //Esta variable lo que realiza es aumentar en uno el mes ya que comienza desde 0 = enero
                final int mesActual = month + 1;
                //Formateo el día obtenido: antepone el 0 si son menores de 10
                String diaFormateado = (dayOfMonth < 10) ? CERO + String.valueOf(dayOfMonth) : String.valueOf(dayOfMonth);
                //Formateo el mes obtenido: antepone el 0 si son menores de 10
                String mesFormateado = (mesActual < 10) ? CERO + String.valueOf(mesActual) : String.valueOf(mesActual);
                //Muestro la fecha con el formato deseado

                System.out.println(diaFormateado + "  " + mesFormateado + "   " + year);

                tw_fecha.setText(diaFormateado + BARRA + mesFormateado + BARRA + year);


            }

        }, anio, mes, dia);
        //Muestro el widget
        recogerFecha.show();

    }

}
