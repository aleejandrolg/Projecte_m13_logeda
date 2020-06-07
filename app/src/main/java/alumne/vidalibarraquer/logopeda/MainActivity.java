package alumne.vidalibarraquer.logopeda;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;


import com.google.android.material.navigation.NavigationView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import data.Alumno;
import data.ManejadordeDatos;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, NavigationView.OnNavigationItemSelectedListener {


    Button CONFIRMAR;


    EditText Nombre;
    EditText Apellidos;
    EditText txt_Fecha_Nacimiento;
    EditText ID;
    NavigationView navegador;

    ActionBarDrawerToggle actionBarDrawerToggle;
    DrawerLayout drawerlayout;
    public final Calendar c = Calendar.getInstance();

    //Variables para obtener la fecha
    final int mes = c.get(Calendar.MONTH);
    final int dia = c.get(Calendar.DAY_OF_MONTH);
    final int anio = c.get(Calendar.YEAR);
    //Widgets
    EditText etFecha;

    private static final String CERO = "0";
    private static final String BARRA = "/";


    Calendar calendar = Calendar.getInstance();
    long hoy = System.currentTimeMillis();
    Date fechahoy = new Date(hoy);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme);

        super.onCreate(savedInstanceState);

        setContentView(R.layout.registro);

        navegador = (NavigationView) findViewById(R.id.navView);
        drawerlayout = (DrawerLayout) findViewById(R.id.drawer);
        navegador.setNavigationItemSelectedListener(this);


        Nombre = (EditText) findViewById(R.id.txt_Nombre);
        Apellidos = (EditText) findViewById(R.id.txt_Apellidos);

        txt_Fecha_Nacimiento = (EditText) findViewById(R.id.txt_Fecha_Nacimiento);
        txt_Fecha_Nacimiento.setOnClickListener(this);


        CONFIRMAR = (Button) findViewById(R.id.bt_confirmar);
        CONFIRMAR.setOnClickListener(this);


        //////Pongo la hora los minutos, los segundos y los milisegundos a 0 del dia de de hoy para poder comparar con la fecha del textview de nacimiento////////////
        calendar.setTime(fechahoy);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

    }


    /////////menu lateral///////////////////

    @Override
    public boolean onNavigationItemSelected(MenuItem menuItem) {


        drawerlayout.closeDrawers();


        switch (menuItem.getItemId()) {

            case R.id.nav_item_one:
                Intent hoy = new Intent(MainActivity.this, inicio.class);
                startActivity(hoy);
                break;
            case R.id.nav_item_two:

                menuItem.setChecked(true);
                break;
            case R.id.nav_item_three:
                Intent activitats = new Intent(MainActivity.this, actividades.class);
                startActivity(activitats);

                break;
            case R.id.nav_item_four:
                Intent mostrar = new Intent(MainActivity.this, mostrar.class);
                startActivity(mostrar);
                break;

        }

        return true;

    }

    @Override
    public void onClick(View v) {

        final ManejadordeDatos alumnosDb = new ManejadordeDatos(getApplicationContext());
        switch (v.getId()) {
            case R.id.txt_Fecha_Nacimiento:
                obtenerFecha();
                break;


                /////antes de añadir el alumno se asegura que todos los campos estan llenos y se asegura que la fecha de nacimiento es inferior al dia de hoy///////////////
            case R.id.bt_confirmar:

                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

                if (!Nombre.getText().toString().trim().equalsIgnoreCase("")&& !Apellidos.getText().toString().trim().equalsIgnoreCase("")&&!txt_Fecha_Nacimiento.getText().toString().trim().equalsIgnoreCase("")) {


                    try {


                        Date srDate = sdf.parse(txt_Fecha_Nacimiento.getText().toString());


                        fechahoy = calendar.getTime();

                        if (fechahoy.getTime() >= srDate.getTime()) {


                            alumnosDb.agregarAlumne(Nombre.getText().toString(), Apellidos.getText().toString(), txt_Fecha_Nacimiento.getText().toString());
                            Toast.makeText(getApplicationContext(), getApplicationContext().getString(R.string.usuario_agregado), Toast.LENGTH_SHORT).show();

                            Nombre.setText("");
                            Apellidos.setText("");
                            txt_Fecha_Nacimiento.setText("");

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


    //////obtiene la fecha y la formatea////////////
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

        }, anio, mes, dia);
        //Muestro el widget
        recogerFecha.show();

    }


}
