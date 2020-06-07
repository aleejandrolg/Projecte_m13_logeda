package alumne.vidalibarraquer.logopeda;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;


import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import data.Actividad;
import data.Alumno;
import data.ManejadordeDatos;

public class inicio extends AppCompatActivity implements View.OnClickListener, NavigationView.OnNavigationItemSelectedListener {

    /////Menu/////

    DrawerLayout drawerLayout;


    ///////Componentes layout//////////
    TextView fecha_hoy;
    TextView dia;
    Button bt_hoy;
    Button bt_ayer;
    Button bt_mañana;
    Button bt_otro;


    //////fechas/////

    Calendar c = Calendar.getInstance();


    Date fecha = c.getTime();


    ////pulsado/////

    Boolean ayer_pulsado = false;
    Boolean hoy_pulsado = false;
    Boolean mañana_pulsado = false;

    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/YYYY");

    ///////Listview de las actividades/////////////////////////////
    ListView lw_actividades;


    ///////Textview Actividades//////////////////////////////

    TextView twActividad;
    TextView tw_Alumno;

    ///////Imageview Actividades/////////////////////////////////

    ImageView imagen;


    boolean borrado = false;
    DrawerLayout drawerlayout;
    NavigationView navegador;


    //Variables para obtener la fecha
    final int mes = c.get(Calendar.MONTH);
    final int diap = c.get(Calendar.DAY_OF_MONTH);
    final int anio = c.get(Calendar.YEAR);
    String elegido;
    //Widgets


    private static final String CERO = "0";
    private static final String BARRA = "/";

    Image alternativa;
    SimpleAdapter adaptador;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.principal);

        navegador = (NavigationView) findViewById(R.id.navView);
        drawerlayout = (DrawerLayout) findViewById(R.id.drawer);
        navegador.setNavigationItemSelectedListener(this);

        dia = (TextView) findViewById(R.id.txt_dia);

        bt_hoy = (Button) findViewById(R.id.bt_hoy);
        bt_hoy.setOnClickListener(this);

        bt_ayer = (Button) findViewById(R.id.bt_ayer);
        bt_ayer.setOnClickListener(this);

        bt_mañana = (Button) findViewById(R.id.bt_mañana);
        bt_mañana.setOnClickListener(this);

        bt_otro = (Button) findViewById(R.id.bt_otro);
        bt_otro.setOnClickListener(this);


        fecha_hoy = (TextView) findViewById(R.id.tw_fecha_hoy);


        /////Incia con la fecha de Hoy//////////
        c = Calendar.getInstance();
        fecha = c.getTime();

        dia.setText(getApplicationContext().getString(R.string.hoy));
        fecha_hoy.setText(sdf.format(fecha));


        /////Listview  actividades////////////////////
        lw_actividades = (ListView) findViewById(R.id.lw_actividades);


        ///////Textview de actividades////////////////

        twActividad = (TextView) findViewById(R.id.twActividad);
        tw_Alumno = (TextView) findViewById(R.id.tw_Alumno);

        ///////Imageview de actividades//////////////////////

        imagen = (ImageView) findViewById(R.id.imgFoto);


        ///cargara sin pulsar ningun boton las actividades que coincidan con la fecha de hoy/////////////
        listar(sdf.format(fecha));
        fecha_hoy.setText(sdf.format(fecha));


        lw_actividades.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                final ManejadordeDatos actividadesDB = new ManejadordeDatos(getApplicationContext());

                final List<Actividad> actividades = actividadesDB.actividadPorfecha(sdf.format(fecha));
                final List<Alumno> alumnos = actividadesDB.llenar_listview();


                Alumno alumno = new Alumno();
                ManejadordeDatos manejadorDatos = new ManejadordeDatos(inicio.this);


                for (final Actividad actividad : actividades) {


                    int alumnoid = actividades.get(position).getAlumnoID();


                    alumno = manejadorDatos.obtenerAlumno(alumnoid);


                    String nombreAlu = alumno.getNombre();
                    String apellidoAlu = alumno.getApellidos();


                    Intent intent = new Intent(inicio.this, mostrarActividad.class);
                    intent.putExtra("id", String.valueOf(actividades.get(position).getID()));
                    intent.putExtra("actividad", String.valueOf(actividades.get(position).getActividad()));
                    intent.putExtra("alumnoID", String.valueOf(actividades.get(position).getAlumnoID()));
                    intent.putExtra("nombreAlu", nombreAlu);
                    intent.putExtra("apellidoAlu", apellidoAlu);
                    intent.putExtra("fecha", String.valueOf(actividades.get(position).getFecha()));
                    intent.putExtra("inicio", String.valueOf(actividades.get(position).getHora_inicio()));
                    intent.putExtra("final", String.valueOf(actividades.get(position).getHora_finaliza()));
                    intent.putExtra("image", (actividades.get(position).getImagen()));
                    startActivity(intent);


                }

            }
        });


    }


    //////menu lateral//////////////
    @Override
    public boolean onNavigationItemSelected(MenuItem menuItem) {


        drawerlayout.closeDrawers();


        switch (menuItem.getItemId()) {

            case R.id.nav_item_one:
                menuItem.setChecked(true);
                break;
            case R.id.nav_item_two:

                Intent hoy = new Intent(inicio.this, MainActivity.class);
                startActivity(hoy);
                break;
            case R.id.nav_item_three:
                Intent activitats = new Intent(inicio.this, actividades.class);
                startActivity(activitats);

                break;
            case R.id.nav_item_four:
                Intent mostrar = new Intent(inicio.this, mostrar.class);
                startActivity(mostrar);
                break;

        }

        return true;

    }

    ///////dependiendo el boton cambiara la fecha por la de ayer, hoy, mañana o otro dia//////////////

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.bt_ayer:
                lw_actividades.setAdapter(null);

                c = Calendar.getInstance();
                if (ayer_pulsado == false) {

                    c.add(Calendar.DATE, -1);
                    fecha = c.getTime();
                    ayer_pulsado = true;

                    hoy_pulsado = false;
                    mañana_pulsado = false;

                }

                fecha_hoy.setText(sdf.format(fecha));
                listar(sdf.format(fecha));
                dia.setText(getApplicationContext().getText(R.string.ayer));


                break;
            case R.id.bt_hoy:
                lw_actividades.setAdapter(null);

                c = Calendar.getInstance();

                if (hoy_pulsado == false) {


                    fecha = c.getTime();

                    hoy_pulsado = true;

                    ayer_pulsado = false;
                    mañana_pulsado = false;
                }
                listar(sdf.format(fecha));
                fecha_hoy.setText(sdf.format(fecha));

                dia.setText(getApplicationContext().getText(R.string.hoy));


                break;
            case R.id.bt_mañana:
                lw_actividades.setAdapter(null);

                c = Calendar.getInstance();

                if (mañana_pulsado == false) {

                    c.add(Calendar.DATE, +1);
                    fecha = c.getTime();

                    mañana_pulsado = true;

                    ayer_pulsado = false;
                    hoy_pulsado = false;
                }
                listar(sdf.format(fecha));
                fecha_hoy.setText(sdf.format(fecha));

                dia.setText(getApplicationContext().getText(R.string.ma_ana));


                break;

            case R.id.bt_otro:

                mañana_pulsado = false;
                ayer_pulsado = false;
                hoy_pulsado = false;
                lw_actividades.setAdapter(null);
                obtenerFecha();

                break;

        }
    }


    /////obtiene la fecha i la formatea////////////

    private void obtenerFecha() {

        final Date elegido;
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

                fecha_hoy.setText(diaFormateado + BARRA + mesFormateado + BARRA + year);
                Calendar calendar = Calendar.getInstance();
                calendar.set(year, month, dayOfMonth);


                fecha = calendar.getTime();


                listar(sdf.format(fecha));
                fecha_hoy.setText(sdf.format(fecha));

                dia.setText(getApplicationContext().getText(R.string.dia));

            }

        }, anio, mes, diap);
        //Muestro el widget
        recogerFecha.show();

    }


    /////lista las actividades por fecha//////////////////
    public void listar(final String fecha) {

        final ManejadordeDatos actividadesDB = new ManejadordeDatos(getApplicationContext());

        final List<Actividad> actividades = actividadesDB.actividadPorfecha(fecha);
        HashMap<String, Object> hashMap;
        final ArrayList<HashMap<String, Object>> listactividades;
        listactividades = new ArrayList<>();

        Alumno alumno = new Alumno();
        ManejadordeDatos manejadorDatos = new ManejadordeDatos(this);
        for (final Actividad actividad : actividades) {


            int id = actividad.getID();
            int alumnoid = actividad.getAlumnoID();
            String actividadst = actividad.getActividad();
            String fechast = actividad.getFecha();
            String h_incio = actividad.getHora_inicio();
            String h_final = actividad.getHora_finaliza();
            byte[] imagen = actividad.getImagen();


            alumno = manejadorDatos.obtenerAlumno(alumnoid);
            String nombreAlu = alumno.getNombre();
            String apellidoAlu = alumno.getApellidos();
            String nacimientoAlu = alumno.getFecha_Nacimiento();

            String nombreapellido = nombreAlu + " " + apellidoAlu;
            if (imagen == null) {

                hashMap = new HashMap<String, Object>();

                hashMap.put("actividad", actividadst);
                hashMap.put("nombreAlu", nombreapellido);
                listactividades.add(hashMap);

                adaptador = new SimpleAdapter(inicio.this, listactividades, R.layout.lw_personalizado, new String[]{"actividad", "nombreAlu"}, new int[]{R.id.twActividad, R.id.tw_Alumno});

            } else {
                Bitmap image = BitmapFactory.decodeByteArray(imagen, 0, imagen.length);

                hashMap = new HashMap<String, Object>();

                hashMap.put("actividad", actividadst);
                hashMap.put("nombreAlu", nombreapellido);
                hashMap.put("imagen", image);
                listactividades.add(hashMap);


                adaptador = new SimpleAdapter(inicio.this, listactividades, R.layout.lw_personalizado, new String[]{"actividad", "nombreAlu", "imagen"}, new int[]{R.id.twActividad, R.id.tw_Alumno, R.id.imgFoto});


                adaptador.setViewBinder(new SimpleAdapter.ViewBinder() {
                    @Override
                    public boolean setViewValue(View view, Object data, String textRepresentation) {
                        if ((view instanceof ImageView) & (data instanceof Bitmap)) {
                            ImageView iv = (ImageView) view;
                            Bitmap bm = (Bitmap) data;
                            iv.setImageBitmap(bm);
                            return true;
                        }
                        return false;
                    }
                });
            }


            lw_actividades.setAdapter(adaptador);

            ///////////////////Parte para eliminar la actividad///////////////////////////
            lw_actividades.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                @Override
                public boolean onItemLongClick(AdapterView<?> parent, View view, final int i, final long id) {
                    final int posicion = i;

                    final AlertDialog.Builder diaologo = new AlertDialog.Builder(inicio.this);
                    diaologo.setTitle(getApplicationContext().getString(R.string.importante));
                    diaologo.setMessage(getApplicationContext().getString(R.string.desea_Actividad));
                    diaologo.setCancelable(true);
                    diaologo.setPositiveButton(getApplicationContext().getString(R.string.confirmar), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {


                            actividadesDB.eliminarActividad(actividades.get(posicion));
                            Toast.makeText(inicio.this, getApplicationContext().getString(R.string.registro_eliminado), Toast.LENGTH_SHORT).show();

                            borrado = true;

                        }
                    });
                    diaologo.setNegativeButton(getApplicationContext().getString(R.string.cancelar), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            Toast.makeText(inicio.this, getApplicationContext().getString(R.string.registro_no_eliminado), Toast.LENGTH_SHORT).show();

                        }
                    });

                    diaologo.show();

                    return true;
                }
            });

        }


    }

}


