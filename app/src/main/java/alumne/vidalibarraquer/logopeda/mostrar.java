package alumne.vidalibarraquer.logopeda;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import data.Alumno;
import data.AlumnosDbHelper;

public class mostrar extends AppCompatActivity {

    ListView LW_alumnos;
    ArrayList<String> lista;

    ArrayAdapter adaptador;

    TextView tw_ID;
    TextView tw_Nom;

    SwipeRefreshLayout swipeRefreshLayout;

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.listar);


        LW_alumnos = (ListView) findViewById(R.id.LW_alumnos);

        tw_ID = (TextView) findViewById(R.id.tw_ID);
        tw_Nom = (TextView) findViewById(R.id.tw_Nom);

        swipeRefreshLayout = findViewById(R.id.swiperefresh);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {


            @Override
            public void onRefresh() {

                actualizar();

            }

        });

        LW_alumnos.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                final AlumnosDbHelper alumnosDB = new AlumnosDbHelper(getApplicationContext());

                final List<Alumno> alumnos = alumnosDB.llenar_listview();


                HashMap<String, String> hashMap;
                final ArrayList<HashMap<String, String>> llistanoms;
                llistanoms = new ArrayList<HashMap<String, String>>();


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


                    llistanoms.add(hashMap);




                    //System.out.println("holllllllllaaaaaaaaaaaaaaa"+alumnos.get(position).getId());

                    Intent intent = new Intent(mostrar.this, mostrarAlumno.class);
                   intent.putExtra("id", String.valueOf(alumnos.get(position).getId()));
                   intent.putExtra("nombre",String.valueOf(alumnos.get(position).getNombre()));
                   intent.putExtra("apellidos",String.valueOf(alumnos.get(position).getApellidos()));
                   intent.putExtra("fecha",String.valueOf(alumnos.get(position).getFecha_Nacimiento()));
                   startActivity(intent);
                }


            }
        });
        listar();

    }


    public void listar() {
        //

        final AlumnosDbHelper alumnosDB = new AlumnosDbHelper(getApplicationContext());

        final List<Alumno> alumnos = alumnosDB.llenar_listview();


        HashMap<String, String> hashMap;
        final ArrayList<HashMap<String, String>> llistanoms;
        llistanoms = new ArrayList<HashMap<String, String>>();


        for (Alumno alumno : alumnos) {

            String id = String.valueOf(alumno.getId());
            String nom = alumno.getNombre();


            hashMap = new HashMap<String, String>();
            hashMap.put("id", id);
            hashMap.put("nom", nom);


            llistanoms.add(hashMap);


            final ListAdapter adaptador = new SimpleAdapter(this, llistanoms, R.layout.textllistar, new String[]{"id", "nom"}, new int[]{R.id.tw_ID, R.id.tw_Nom});


            LW_alumnos.setAdapter(adaptador);


            /////////////////////parte nueva para eliminar///////////////////////


            LW_alumnos.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                @Override
                public boolean onItemLongClick(AdapterView<?> parent, View view, final int i, final long id) {
                    final int posicion = i;

                    final AlertDialog.Builder diaologo = new AlertDialog.Builder(mostrar.this);
                    diaologo.setTitle("Imortante");
                    diaologo.setMessage("¿Desea eliminar este alumno?");
                    diaologo.setCancelable(true);
                    diaologo.setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {


                            alumnosDB.eliminarAlumne(alumnos.get(posicion));
                            Toast.makeText(mostrar.this, "Registre eliminado", Toast.LENGTH_SHORT).show();


                        }
                    });
                    diaologo.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            Toast.makeText(mostrar.this, "Registre no eliminado", Toast.LENGTH_SHORT).show();

                        }
                    });

                    diaologo.show();

                    return false;


                }
            });

        }


    }


    public void actualizar() {
        swipeRefreshLayout.setRefreshing(true);

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                final AlumnosDbHelper alumnosDB = new AlumnosDbHelper(getApplicationContext());

                final List<Alumno> alumnos = alumnosDB.llenar_listview();


                HashMap<String, String> hashMap;
                final ArrayList<HashMap<String, String>> llistanoms;
                llistanoms = new ArrayList<HashMap<String, String>>();


                for (Alumno alumno : alumnos) {

                    int id = alumno.getId();
                    System.out.println("Este es eld" + id);
                    String nom = alumno.getNombre();

                    String ids = String.valueOf(id);
                    System.out.println(nom);
                    if (id > 0) {

                        hashMap = new HashMap<String, String>();
                        hashMap.put("id", ids);
                        hashMap.put("nom", nom);


                        llistanoms.add(hashMap);

                        final ListAdapter adaptador = new SimpleAdapter(getApplicationContext(), llistanoms, R.layout.textllistar, new String[]{"id", "nom"}, new int[]{R.id.tw_ID, R.id.tw_Nom});


                        LW_alumnos.setAdapter(adaptador);
                    } else {

                        LW_alumnos.setAdapter(null);
                    }


                }


                swipeRefreshLayout.setRefreshing(false);
            }
        }, 1200);
    }


}