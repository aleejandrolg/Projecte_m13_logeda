package alumne.vidalibarraquer.logopeda;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import data.Alumno;
import data.ManejadordeDatos;

public class actividades extends AppCompatActivity implements View.OnClickListener,NavigationView.OnNavigationItemSelectedListener {
    EditText edt_Fecha;
    EditText edt_h_inicio;
    EditText edt_h_final;
    EditText edt_actividades;
    Button bt_guardar;
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



    ////Spinner donde se mostraran los alumnos///////
    Spinner spinner_alumnos;



    ManejadordeDatos con;

    Alumno alumno;

    int alumnoID=0;
    String ide;
    ////imagen a insertar en la actividad/////////////////////

    ImageView iw_imagen;
    byte[] imageInByte;

    DrawerLayout drawerlayout;
    NavigationView navegador;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.actividades);

        navegador = (NavigationView) findViewById(R.id.navView);
        drawerlayout = (DrawerLayout) findViewById(R.id.drawer);
        navegador.setNavigationItemSelectedListener(this);
        spinner_alumnos = (Spinner) findViewById(R.id.spinner_alumnos);

        ////Seleccionar alumno de la actividad

        con = new ManejadordeDatos(getApplicationContext());

        final List<Alumno> alumnos = con.llenar_listview();


        HashMap<String, String> hashMap;
        final ArrayList<HashMap<String, String>> listaAlumnos;
        listaAlumnos = new ArrayList<HashMap<String, String>>();


        for (Alumno alumno : alumnos) {

            String ida = String.valueOf(alumno.getId());
            String nom = alumno.getNombre();
            String apellido = alumno.getApellidos();
            String fecha = alumno.getFecha_Nacimiento();

            hashMap = new HashMap<String, String>();


            hashMap.put("id", ida);
            hashMap.put("nom", nom);
            hashMap.put("apellido", apellido);
            hashMap.put("fecha", fecha);


            listaAlumnos.add(hashMap);

            /////adaptador del spinner que mostrara el id el nombre y el apellido del alumno///////

            SpinnerAdapter adaptador = new SimpleAdapter(this, listaAlumnos, R.layout.adaptador_combo, new String[]{"id", "nom", "apellido"}, new int[]{R.id.tw_id, R.id.tw_nom, R.id.tw_apellidos});
            spinner_alumnos.setAdapter(adaptador);


        }

        //////Pedir fecha y hora  de la actividad

        edt_Fecha = findViewById(R.id.edt_fecha);
        edt_h_inicio = findViewById(R.id.edt_h_inicio);
        edt_h_final = findViewById(R.id.edt_h_fin);
        edt_actividades = findViewById(R.id.edt_actividades);

        bt_guardar = findViewById(R.id.bt_guardar);


        edt_Fecha.setOnClickListener(this);
        edt_h_inicio.setOnClickListener(this);
        edt_h_final.setOnClickListener(this);

        bt_guardar.setOnClickListener(this);

        ////imagen/////

        iw_imagen = (ImageView) findViewById(R.id.iw_imagen);
        iw_imagen.setOnClickListener(this);


        ///////Spinner////////////////
        spinner_alumnos.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {



                HashMap<String, String> map = (HashMap<String, String>) spinner_alumnos.getItemAtPosition(position);

                ide = map.get("id");

                alumnoID = Integer.parseInt(ide);


            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });



    }
    ///////menu lateral//////////////////

    @Override
    public boolean onNavigationItemSelected(MenuItem menuItem) {


        drawerlayout.closeDrawers();


        switch (menuItem.getItemId()) {

            case R.id.nav_item_one:
                Intent hoy = new Intent(actividades.this, inicio.class);
                startActivity(hoy);
                break;
            case R.id.nav_item_two:
                Intent activitats = new Intent(actividades.this, MainActivity.class);
                startActivity(activitats);

                break;
            case R.id.nav_item_three:

                menuItem.setChecked(true);


                break;
            case R.id.nav_item_four:
                Intent mostrar = new Intent(actividades.this, mostrar.class);
                startActivity(mostrar);
                break;

        }

        return true;

    }
    @Override
    public void onClick(View v) {
        final ManejadordeDatos alumnosDb = new ManejadordeDatos(getApplicationContext());
        switch (v.getId()) {

            case R.id.edt_fecha:
                obtenerFecha();

                break;

            case R.id.edt_h_inicio:
                boolean identificador = true;

                obtenerHora(identificador);
                break;
            case R.id.edt_h_fin:
                identificador = false;

                obtenerHora(identificador);


                break;

            case R.id.iw_imagen:

                cargarImagen();


                break;

                //////Antes de guardar se asegura que todos los campos esten llenos,menos la imagen que puede estar vacia,si estan llenas comprovara ademasa que la fecha de la actividad sea igual o superior al dia de hoy
            case R.id.bt_guardar:

                if(spinner_alumnos!=null && spinner_alumnos.getSelectedItem()!=null) {

                    if (!edt_actividades.getText().toString().trim().equalsIgnoreCase("") && !edt_h_inicio.getText().toString().trim().equalsIgnoreCase("") && !edt_h_final.getText().toString().trim().equalsIgnoreCase("")&&!edt_Fecha.getText().toString().trim().equalsIgnoreCase("")) {

                        SimpleDateFormat sdf=new SimpleDateFormat("dd/MM/yyyy");

                        try {
                            Calendar calendar=Calendar.getInstance();

                            Date srDate=sdf.parse(edt_Fecha.getText().toString());
                            long hoy=System.currentTimeMillis();
                            Date fechahoy=new Date(hoy);

                            ///////pongo la hora y los miutos a 0 ya que lo me interesa obtener la fecha de hoy para que no me impida poner actividades con fecha de hoy/////////
                            calendar.setTime(fechahoy);
                            calendar.set(Calendar.HOUR_OF_DAY,0);
                            calendar.set(Calendar.MINUTE,0);
                            calendar.set(Calendar.SECOND,0);
                            calendar.set(Calendar.MILLISECOND,0);

                            fechahoy=calendar.getTime();

                            //////Una vez puesta a 0 ya puedo comparar si la fecha de hoy es superior o igual a la que se ha puesto en el edittext/////////////
                            if (fechahoy.getTime()<=srDate.getTime()){


                                alumnosDb.agregarActividad(alumnoID, edt_actividades.getText().toString(), edt_Fecha.getText().toString(), edt_h_inicio.getText().toString(), edt_h_final.getText().toString(), imageInByte);
                                Toast.makeText(getApplicationContext(), getApplicationContext().getText(R.string.actividad_bien), Toast.LENGTH_SHORT).show();

                                edt_actividades.setText("");
                                edt_Fecha.setText("");
                                edt_h_inicio.setText("");
                                edt_h_final.setText("");
                                iw_imagen.setImageResource(0);
                            }

                            else {
                                Toast.makeText(getApplicationContext(), getApplicationContext().getText(R.string.fecha_mal), Toast.LENGTH_SHORT).show();

                            }

                        } catch (ParseException e) {
                            e.printStackTrace();
                        }


                    } else {

                        Toast.makeText(getApplicationContext(),getApplicationContext().getText(R.string.faltan), Toast.LENGTH_SHORT).show();
                    }

                }
                else {
                    Toast.makeText(getApplicationContext(),getApplicationContext().getText(R.string.faltan), Toast.LENGTH_SHORT).show();

                }

                break;

        }

    }
///////////////////obtiene la imagen////////
    private void cargarImagen() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/*");

        startActivityForResult(intent.createChooser(intent,getApplicationContext().getString(R.string.seleccione)), 10);
    }


    ////////////////obtiene la imagen y la comprime para que después se pueda almacenar en la base de datos//////////////////
    @Override
    protected void onActivityResult(int reqCode, int resultCode, Intent data) {
        super.onActivityResult(reqCode, resultCode, data);


        if (resultCode == RESULT_OK) {
            try {
                final Uri imageUri = data.getData();
                final InputStream imageStream = getContentResolver().openInputStream(imageUri);
                final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
                iw_imagen.setImageBitmap(selectedImage);
                Bitmap bitmap = ((BitmapDrawable) iw_imagen.getDrawable()).getBitmap();

                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                 bitmap.compress(Bitmap.CompressFormat.JPEG, 10, baos);
                 imageInByte = baos.toByteArray();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                Toast.makeText(actividades.this,getApplicationContext().getString(R.string.salio_mal), Toast.LENGTH_LONG).show();
            }

        }else {
            Toast.makeText(actividades.this, getApplicationContext().getString(R.string.no_imagen),Toast.LENGTH_LONG).show();
        }
    }


        ///////obtiene la fecha y la formatea/////////////////
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

                edt_Fecha.setText(diaFormateado + BARRA + mesFormateado + BARRA + year);


            }

        }, anio, mes, dia);
        //Muestro el widget
        recogerFecha.show();

    }



    ///////obitene la hora y la formatea///////////////////
    private void obtenerHora(final boolean identificador) {
        TimePickerDialog recogerHora = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                //Formateo el hora obtenido: antepone el 0 si son menores de 10
                String horaFormateada = (hourOfDay < 10) ? String.valueOf(CERO + hourOfDay) : String.valueOf(hourOfDay);
                //Formateo el minuto obtenido: antepone el 0 si son menores de 10
                String minutoFormateado = (minute < 10) ? String.valueOf(CERO + minute) : String.valueOf(minute);
                //Obtengo el valor a.m. o p.m., dependiendo de la selección del usuario

                //Muestro la hora con el formato deseado

                if (identificador == true) {
                    edt_h_inicio.setText(horaFormateada + ":" + minutoFormateado);

                } else

                    edt_h_final.setText(horaFormateada + ":" + minutoFormateado);


            }
            //Estos valores deben ir en ese orden
            //Al colocar en false se muestra en formato 12 horas y true en formato 24 horas
            //Pero el sistema devuelve la hora en formato 24 horas
        }, hora, minuto, true);

        recogerHora.show();
    }

}
