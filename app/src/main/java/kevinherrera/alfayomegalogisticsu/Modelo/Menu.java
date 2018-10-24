package kevinherrera.alfayomegalogisticsu.Modelo;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import kevinherrera.alfayomegalogisticsu.R;


/**
 * Created by Andrey on 01/09/2016.
 */
public class Menu extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);


        Button CerrarSesion =(Button) findViewById(R.id.btnmenucerrarsesion);
        CerrarSesion.setOnClickListener(clickcerrarsesion);
        
        Button Cambiacontra =(Button) findViewById(R.id.btncambiarcontra);
        Cambiacontra.setOnClickListener(clickcambiarcontra);

        Button Solcitudservicio =(Button) findViewById(R.id.btnsolicitudsercvicio);
        Solcitudservicio.setOnClickListener(clicksolicitudservicio);

        Button SerSol =(Button) findViewById(R.id.btnserviciossolicitados);
        SerSol.setOnClickListener(serviciossolicitados);

        Button HiSol =(Button) findViewById(R.id.btnhistorialservicios);
        HiSol.setOnClickListener(clickhistorialservicios);

        Button servespe =(Button) findViewById(R.id.btnserviciosespeciales);
        servespe.setOnClickListener(clickserviciosespeciales);

    }
    private View.OnClickListener clickserviciosespeciales = new View.OnClickListener(){
        public void onClick(View v)
        {
            Intent i=new Intent(Menu.this, ServiciosEspeciales.class);
            startActivity(i);
        }
    };
    private View.OnClickListener clickhistorialservicios = new View.OnClickListener(){
        public void onClick(View v)
        {
            Intent i=new Intent(Menu.this, HistorialServicios.class);
            startActivity(i);
        }
    };
    private View.OnClickListener clicksolicitudservicio = new View.OnClickListener(){
        public void onClick(View v)
        {
            Intent i=new Intent(Menu.this, MapsActivity.class);
            startActivity(i);
        }
    };
    private View.OnClickListener serviciossolicitados = new View.OnClickListener(){
        public void onClick(View v)
        {
            Intent i=new Intent(Menu.this, ServiciosSolicitados.class);
            startActivity(i);
        }
    };
    private View.OnClickListener clickcerrarsesion = new View.OnClickListener(){
        public void onClick(View v)
        {
            FragmentManager fragmentManager = getFragmentManager();
            DialogoSeleccion dialogo = new DialogoSeleccion();
            dialogo.show(fragmentManager, "tagAlerta");
        }
    };
    private View.OnClickListener clickcambiarcontra = new View.OnClickListener(){
        public void onClick(View v)
        {
            Intent i=new Intent(Menu.this,CambiarContrasena.class);
            startActivity(i);
        }
    };
    public class DialogoSeleccion extends DialogFragment {
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {

            final String items[]=new String [2];
            items[0]="Si";
            items[1]="No";

            AlertDialog.Builder builder =
                    new AlertDialog.Builder(getActivity());

            builder.setTitle("¿Está Seguro?")
                    .setItems(items, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int item) {
                            switch (items[item]) {
                                case "Si": {
                                    SharedPreferences preferencias = getSharedPreferences("Usuario", Context.MODE_PRIVATE);
                                    SharedPreferences.Editor editor = preferencias.edit();
                                    editor.putString("Usuario", "");
                                    editor.commit();

                                    SharedPreferences preferencias2 = getSharedPreferences("Contrasena", Context.MODE_PRIVATE);
                                    SharedPreferences.Editor editor2 = preferencias2.edit();
                                    editor2.putString("Contrasena", "");
                                    editor2.commit();

                                    Intent i= new Intent(Menu.this,Login.class);
                                    startActivity(i);
                                    finish();
                                }
                                break;
                                case "No": {
                                }
                                break;
                            }
                        }
                    });

            return builder.create();
        }
    }
}
