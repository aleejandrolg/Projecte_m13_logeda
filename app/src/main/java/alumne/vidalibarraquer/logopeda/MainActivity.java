package alumne.vidalibarraquer.logopeda;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import java.util.Calendar;

import data.AlumnosDbHelper;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    Button CONFIRMAR;
    Button CANCELAR;
    Button LISTAR;
    Button ELIMINAR;
    EditText Nombre;
    EditText Apellidos;
    EditText txt_Fecha_Nacimiento;
    EditText ID;
    public final Calendar c = Calendar.getInstance();

    //Variables para obtener la fecha
    final int mes = c.get(Calendar.MONTH);
    final int dia = c.get(Calendar.DAY_OF_MONTH);
    final int anio = c.get(Calendar.YEAR);
    //Widgets
    EditText etFecha;

    private static final String CERO = "0";
    private static final String BARRA = "/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        setTheme(R.style.AppTheme);

        super.onCreate(savedInstanceState);

        setContentView(R.layout.registro);


        Nombre = (EditText) findViewById(R.id.txt_Nombre);
        Apellidos = (EditText) findViewById(R.id.txt_Apellidos);

        txt_Fecha_Nacimiento = (EditText) findViewById(R.id.txt_Fecha_Nacimiento);
        txt_Fecha_Nacimiento.setOnClickListener(this);

        ID = (EditText) findViewById(R.id.txt_ID);


        CONFIRMAR = (Button) findViewById(R.id.bt_confirmar);
        CONFIRMAR.setOnClickListener(this);

        //CANCELAR=(Button)findViewById(R.id.bt_cancelar);
        //CANCELAR.setOnClickListener(this);

        LISTAR = (Button) findViewById(R.id.bt_listar);
        LISTAR.setOnClickListener(this);

        ELIMINAR = (Button) findViewById(R.id.bt_eliminar);
        ELIMINAR.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {

        final AlumnosDbHelper alumnosDb = new AlumnosDbHelper(getApplicationContext());
        switch (v.getId()) {
            case R.id.txt_Fecha_Nacimiento:
                obtenerFecha();
                break;


            case R.id.bt_confirmar:

                alumnosDb.agregarAlumne(Nombre.getText().toString(), Apellidos.getText().toString(), txt_Fecha_Nacimiento.getText().toString());
                Toast.makeText(getApplicationContext(), "Usuario agregado correctamente", Toast.LENGTH_SHORT).show();

                break;

            case R.id.bt_listar:
                Intent mostrar = new Intent(this, mostrar.class);
                startActivity(mostrar);

                break;

            case R.id.bt_eliminar:

                // alumnosDb.eliminarAlumne(Nombre.getText().toString(),Apellidos.getText().toString(),txt_Fecha_Nacimiento.getText().toString());


                //alumnosDb.eliminarAlumne(Integer.parseInt(ID.getText().toString()));
                Toast.makeText(getApplicationContext(), "Usuario eliminado correctamente", Toast.LENGTH_SHORT).show();

                break;

        }
    }

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

                txt_Fecha_Nacimiento.setText(diaFormateado + BARRA + mesFormateado + BARRA + year);


            }
            //Estos valores deben ir en ese orden, de lo contrario no mostrara la fecha actual
            /**
             *También puede cargar los valores que usted desee
             */
        }, anio, mes, dia);
        //Muestro el widget
        recogerFecha.show();

    }


}
