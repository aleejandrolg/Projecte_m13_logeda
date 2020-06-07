package alumne.vidalibarraquer.logopeda;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.MenuItemCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import data.Alumno;
import data.ManejadordeDatos;

public class mostrar extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    Boolean largo = false;
    Boolean corto;

    ListView LW_alumnos;
    ArrayList<String> lista;

    ArrayAdapter adaptador;

    TextView tw_ID;
    TextView tw_Nom;

    SwipeRefreshLayout swipeRefreshLayout;

    DrawerLayout drawerlayout;
    NavigationView navegador;

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.listar);

        navegador = (NavigationView) findViewById(R.id.navView);
        drawerlayout = (DrawerLayout) findViewById(R.id.drawer);
        navegador.setNavigationItemSelectedListener(this);

        LW_alumnos = (ListView) findViewById(R.id.LW_alumnos);

        tw_ID = (TextView) findViewById(R.id.tw_id);
        tw_Nom = (TextView) findViewById(R.id.tw_nom);


        swipeRefreshLayout = findViewById(R.id.swiperefresh);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {


            @Override
            public void onRefresh() {

                actualizar();

            }

        });


        if (largo == false) {
            LW_alumnos.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    final ManejadordeDatos alumnosDB = new ManejadordeDatos(getApplicationContext());

                    final List<Alumno> alumnos = alumnosDB.llenar_listview();

                    Intent intent = new Intent(mostrar.this, mostrarAlumno.class);
                    intent.putExtra("id", String.valueOf(alumnos.get(position).getId()));
                    intent.putExtra("nombre", String.valueOf(alumnos.get(position).getNombre()));
                    intent.putExtra("apellidos", String.valueOf(alumnos.get(position).getApellidos()));
                    intent.putExtra("fecha", String.valueOf(alumnos.get(position).getFecha_Nacimiento()));
                    startActivity(intent);


                }
            });

        }


        listar();

    }


    /////////busca los alumnos por nombre y apellidos//////
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflate = getMenuInflater();
        inflate.inflate(R.menu.search, menu);

        MenuItem searchItem = menu.findItem(R.id.app_bar_search);

        SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                final ManejadordeDatos alumnosDB = new ManejadordeDatos(getApplicationContext());

                final List<Alumno> alumnos = alumnosDB.llenar_listview();


                HashMap<String, String> hashMap;
                final ArrayList<HashMap<String, String>> llistanoms;
                llistanoms = new ArrayList<HashMap<String, String>>();


                for (Alumno alumno : alumnos) {

                    String id = String.valueOf(alumno.getId());
                    String nombre = alumno.getNombre();
                    String apellidos = alumno.getApellidos();

                    String nombreApellido = nombre + " " + apellidos;


                    hashMap = new HashMap<String, String>();
                    hashMap.put("id", id);
                    hashMap.put("nom", nombreApellido);


                    if (nombreApellido.toLowerCase().contains(newText.toLowerCase())) {
                        llistanoms.add(hashMap);
                    }

                    final ListAdapter adaptador = new SimpleAdapter(mostrar.this, llistanoms, R.layout.textllistar, new String[]{"id", "nom"}, new int[]{R.id.tw_id, R.id.tw_nom});


                    LW_alumnos.setAdapter(adaptador);


                }
                return false;
            }
        });

        return true;

    }


    /////lista los alumnos//////////

    public void listar() {
        //

        final ManejadordeDatos alumnosDB = new ManejadordeDatos(getApplicationContext());

        final List<Alumno> alumnos = alumnosDB.llenar_listview();


        HashMap<String, String> hashMap;
        final ArrayList<HashMap<String, String>> llistanoms;
        llistanoms = new ArrayList<HashMap<String, String>>();


        for (final Alumno alumno : alumnos) {

            String id = String.valueOf(alumno.getId());
            String nombre = alumno.getNombre();
            String apellidos = alumno.getApellidos();

            String nombreApellido = nombre + " " + apellidos;


            hashMap = new HashMap<String, String>();
            hashMap.put("id", id);
            hashMap.put("nom", nombreApellido);


            llistanoms.add(hashMap);


            final ListAdapter adaptador = new SimpleAdapter(this, llistanoms, R.layout.textllistar, new String[]{"id", "nom"}, new int[]{R.id.tw_id, R.id.tw_nom});


            LW_alumnos.setAdapter(adaptador);


            /////////////////////parte nueva para eliminar///////////////////////


            LW_alumnos.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                @Override
                public boolean onItemLongClick(AdapterView<?> parent, View view, final int i, final long id) {
                    final int posicion = i;

                    final AlertDialog.Builder diaologo = new AlertDialog.Builder(mostrar.this);
                    diaologo.setTitle(getApplicationContext().getString(R.string.importante));
                    diaologo.setMessage(getApplicationContext().getString(R.string.desea_Eliminar));
                    diaologo.setCancelable(true);
                    diaologo.setPositiveButton(getApplicationContext().getString(R.string.confirmar), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            alumnosDB.eliminarActivadesPorAlumno(String.valueOf(alumno.getId()));
                            alumnosDB.eliminarAlumne(alumnos.get(posicion));
                            Toast.makeText(mostrar.this,getApplicationContext().getString(R.string.registro_eliminado), Toast.LENGTH_SHORT).show();


                        }
                    });
                    diaologo.setNegativeButton(getApplicationContext().getString(R.string.cancelar), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            Toast.makeText(mostrar.this, getApplicationContext().getString(R.string.registro_no_eliminado), Toast.LENGTH_SHORT).show();

                        }
                    });

                    diaologo.show();

                    return largo = true;


                }
            });

        }


    }

    ////////////////swipe para actualizar el listview///////////////
    public void actualizar() {
        swipeRefreshLayout.setRefreshing(true);

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                final ManejadordeDatos alumnosDB = new ManejadordeDatos(getApplicationContext());

                final List<Alumno> alumnos = alumnosDB.llenar_listview();


                HashMap<String, String> hashMap;
                final ArrayList<HashMap<String, String>> llistanoms;
                llistanoms = new ArrayList<HashMap<String, String>>();


                for (Alumno alumno : alumnos) {

                    int id = alumno.getId();
                    String nom = alumno.getNombre();
                    String apellido = alumno.getApellidos();

                    String nombreApellido = nom + " " + apellido;

                    String ids = String.valueOf(id);
                    System.out.println(nom);
                    if (id > 0) {

                        hashMap = new HashMap<String, String>();
                        hashMap.put("id", ids);
                        hashMap.put("nom", nombreApellido);


                        llistanoms.add(hashMap);

                        final ListAdapter adaptador = new SimpleAdapter(getApplicationContext(), llistanoms, R.layout.textllistar, new String[]{"id", "nom"}, new int[]{R.id.tw_id, R.id.tw_nom});


                        LW_alumnos.setAdapter(adaptador);
                    } else {

                        LW_alumnos.setAdapter(null);
                    }


                }


                swipeRefreshLayout.setRefreshing(false);
            }
        }, 1200);
    }


    ///////menu lateral///////////
    @Override
    public boolean onNavigationItemSelected(MenuItem menuItem) {

        drawerlayout.closeDrawers();


        switch (menuItem.getItemId()) {

            case R.id.nav_item_one:
                Intent hoy = new Intent(mostrar.this, inicio.class);
                startActivity(hoy);
                break;
            case R.id.nav_item_two:
                Intent mostrar = new Intent(mostrar.this, MainActivity.class);
                startActivity(mostrar);

                break;
            case R.id.nav_item_three:
                Intent activitats = new Intent(mostrar.this, actividades.class);
                startActivity(activitats);

                break;
            case R.id.nav_item_four:
                menuItem.setChecked(true);
                break;

        }

        return true;
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        this.finish();

        Intent mostrar = new Intent(mostrar.this, MainActivity.class);
        startActivity(mostrar);


    }


}


