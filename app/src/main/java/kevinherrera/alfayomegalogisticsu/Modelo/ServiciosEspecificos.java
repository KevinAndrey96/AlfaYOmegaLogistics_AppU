package kevinherrera.alfayomegalogisticsu.Modelo;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import kevinherrera.alfayomegalogisticsu.Control.Config;
import kevinherrera.alfayomegalogisticsu.Control.JSONParser;
import kevinherrera.alfayomegalogisticsu.R;


/**
 * Created by Andrey on 01/11/2016.
 */
public class ServiciosEspecificos extends Activity {
    TextView Pin,type, description, datesol, address1,address2, distance, value;
    String Corori,Cordes, Usuario, Pin1, BackP, Ste, Mensa;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spserviciosespecificos);

        SharedPreferences User = getSharedPreferences("Usuario", Context.MODE_PRIVATE);
        Usuario=User.getString("Usuario", "");

        Pin=(TextView) findViewById(R.id.esnumservice);
        type=(TextView) findViewById(R.id.estipo);
        description=(TextView) findViewById(R.id.esdescri);
        datesol=(TextView) findViewById(R.id.esfechasoli);
        address1=(TextView) findViewById(R.id.esdirecori);
        address2=(TextView) findViewById(R.id.esdirecdes);
        distance=(TextView) findViewById(R.id.esdistan);
        value=(TextView) findViewById(R.id.esvalor);


        Pin1=getIntent().getExtras().getString("Pin");
        Pin.setText("# Servicio: "+Pin1);

        type.setText(getIntent().getExtras().getString("Tip"));
        description.setText(getIntent().getExtras().getString("Des"));
        datesol.setText(getIntent().getExtras().getString("Fec"));
        address1.setText("Desde: "+getIntent().getExtras().getString("Di1"));
        address2.setText("Hasta: "+getIntent().getExtras().getString("Di2"));
        distance.setText(getIntent().getExtras().getString("Dis")+" Km");
        value.setText("$"+getIntent().getExtras().getString("Val"));
        Mensa=getIntent().getExtras().getString("Men");

        /*Corori=getIntent().getExtras().getString("Co1");
        Cordes=getIntent().getExtras().getString("Co2");
*/

        Button boton  = (Button) findViewById(R.id.btnmensajero);

        boton.setOnClickListener(clickboton);

        Button factura = (Button) findViewById(R.id.btnfactura);
        factura.setOnClickListener(clickfactura);
        factura.setVisibility(View.INVISIBLE);
        factura.setEnabled(false);


        BackP = getIntent().getExtras().getString("Back");
        Ste = getIntent().getExtras().getString("Stat");
        if(BackP.equals("HistorialServicios"))
        {
            factura.setVisibility(View.VISIBLE);
            factura.setEnabled(true);
        }
        if(BackP.equals("ServiciosEspeciales"))
        {
            boton.setText("Continuar");
        }
        else
        {
            if(BackP.equals("ServiciosSolicitados") || BackP.equals("HistorialServicios"))
            {
                boton.setText("Ver Mensajero");
            }
        }
    }
    private View.OnClickListener clickboton =new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            if(BackP.equals("ServiciosSolicitados") ||  BackP.equals("HistorialServicios")) {
                if (Mensa.equals("Espera")) {
                    Toast.makeText(ServiciosEspecificos.this, "Aún no tiene mensajero asignado", Toast.LENGTH_SHORT).show();

                } else {
                    Intent l = new Intent(ServiciosEspecificos.this, DatosMensajero.class);
                    l.putExtra("Mensajero", Mensa);
                    startActivity(l);
                }
            }
            else
            {
                if(BackP.equals("ServiciosEspeciales")) {
                    FragmentManager fragmentManager = getFragmentManager();
                    DialogoSeleccion1 dialogo = new DialogoSeleccion1();
                    dialogo.show(fragmentManager, "tagAlerta");
                }
            }
        }
    };
    private View.OnClickListener clickfactura =new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse("http://alfayomegalogistics.com/factura.php?87ea5dfc8b8e384d848979496e706390b" +
                    "497e547="+Pin1+"&d94019fd760a71edf11844bb5c601a4de95aacaf="+Usuario));
            startActivity(intent);
        }
    };
    ProgressDialog pDialog;
    private String Palabra = "";

    static Config C=new Config();

    private static final String TAG_SUCCESS = "success";
    private static final String TAG_RECEIVE = "receive";
    private static final String TAG_MESSAGE = "message";

    JSONParser jParser = new JSONParser();
    JSONArray products = null;

    String Respuesta;
    public class DialogoSeleccion1 extends DialogFragment {
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {

            final String items[]=new String [2];
            items[0]="Si";
            items[1]="No";

            AlertDialog.Builder builder =
                    new AlertDialog.Builder(getActivity());

            builder.setTitle("¿Desea Aceptarlo?")
                    .setItems(items, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int item) {
                            switch (items[item]) {
                                case "Si": {
                                    Respuesta="Espera";
                                    new Enviar1().execute();
                                }
                                break;
                                case "No": {
                                    Respuesta="Cancelado";
                                    new Enviar1().execute();
                                }
                                break;
                            }
                        }
                    });

            return builder.create();
        }
    }

    private static String ENVIO_URL1 = C.ServidorURL+"Envia_servicioespecial.php";

    class Enviar1 extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(ServiciosEspecificos.this);
            pDialog.setMessage("Enviando respuesta");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        @Override
        protected String doInBackground(String... args) {


            int success;

            String Datos[]= new String[3];
            Datos[0]=Pin.getText().toString().substring(12);
            Datos[1]=Respuesta;

            try {

                List params = new ArrayList();
                for (int i=0; i<Datos.length; i++)
                    params.add(new BasicNameValuePair("Dato"+(i+1), Datos[i]));

                JSONObject json = jParser.makeHttpRequest(ENVIO_URL1, "POST", params);

                success = json.getInt(TAG_SUCCESS);
                if (success == 1) {
                    finish();
                    return json.getString(TAG_MESSAGE);
                } else {
                    return json.getString(TAG_MESSAGE);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return null;

        }

        protected void onPostExecute(String file_url) {

            pDialog.dismiss();
            if (file_url != null) {
                Toast.makeText(ServiciosEspecificos.this, file_url, Toast.LENGTH_LONG).show();
                finish();
            }
        }
    }
   /* private View.OnClickListener clickorigen =new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            ubico(Corori,address1.getText().toString());
        }
    };
    private View.OnClickListener clickdestino =new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            ubico(Cordes,address2.getText().toString());
        }
    };

    public void ubico(String O, String D) {

                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse("geo:" + O + "?q=" + O + "(" + D + ")"));
                startActivity(intent);
    }
*//*
    public class DialogoSeleccion extends DialogFragment {
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {

            final String items[]=new String[2];
            items[0]="Si";
            items[1]="No";

            AlertDialog.Builder builder =
                    new AlertDialog.Builder(getActivity());

            builder.setTitle("¿Está Seguro que desea realizar este servicio?")
                    .setItems(items, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int item) {
                            switch (items[item]) {
                                case "Si": {
                                    new Enviar1().execute();
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
*//*
    ProgressDialog pDialog;
    private String Palabra = "";

    static Config C=new Config();

    private static final String TAG_SUCCESS = "success";
    private static final String TAG_RECEIVE = "receive";
    private static final String TAG_MESSAGE = "message";

    JSONParser jParser = new JSONParser();
    JSONArray products = null;

    private static String ENVIO_URL1 = C.ServidorURL+"Envia_asignacion.php";
    class Enviar1 extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(ServiciosEspecificos.this);
            pDialog.setMessage("Solicitando servicio");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        @Override
        protected String doInBackground(String... args) {


            int success;

            String Datos[]= new String[3];
            Datos[0]=Pin1;
            Datos[1]=Usuario;

            try {

                List params = new ArrayList();
                for (int i=0; i<Datos.length; i++)
                    params.add(new BasicNameValuePair("Dato"+(i+1), Datos[i]));

                JSONObject json = jParser.makeHttpRequest(ENVIO_URL1, "POST", params);

                success = json.getInt(TAG_SUCCESS);
                if (success == 1) {
                    finish();
                    return json.getString(TAG_MESSAGE);
                } else {
                    return json.getString(TAG_MESSAGE);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return null;

        }

        protected void onPostExecute(String file_url) {

            pDialog.dismiss();
            if (file_url != null) {
                Toast.makeText(ServiciosEspecificos.this, file_url, Toast.LENGTH_LONG).show();
                finish();
            }
        }
    }

    public class DialogoSeleccion2 extends DialogFragment {
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {

            final String items[]=new String[2];
            items[0]="Si";
            items[1]="No";

            String Texto="";
            AlertDialog.Builder builder =
                    new AlertDialog.Builder(getActivity());

            if(Ste.equals("Asignado"))
            {
                Texto="¿Está Seguro que desea recoger este servicio?";
            }
            else
            {
                if(Ste.equals("Recogido"))
                {
                    Texto="¿Está Seguro que desea completar este servicio?";
                }
            }
            builder.setTitle(Texto)
                    .setItems(items, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int item) {
                            switch (items[item]) {
                                case "Si": {
                                    new Enviar2().execute();
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
    private static String ENVIO_URL2 = C.ServidorURL+"Envia_realizacion.php";
    class Enviar2 extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(ServiciosEspecificos.this);
            pDialog.setMessage("Completando servicio");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        @Override
        protected String doInBackground(String... args) {


            int success;

            String Datos[]= new String[3];
            Datos[0]=Pin1;
            Datos[1]=Usuario;
            Datos[2]=Ste;

            try {

                List params = new ArrayList();
                for (int i=0; i<Datos.length; i++)
                    params.add(new BasicNameValuePair("Dato"+(i+1), Datos[i]));

                JSONObject json = jParser.makeHttpRequest(ENVIO_URL2, "POST", params);

                success = json.getInt(TAG_SUCCESS);
                if (success == 1) {
                    finish();
                    return json.getString(TAG_MESSAGE);
                } else {
                    return json.getString(TAG_MESSAGE);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return null;

        }

        protected void onPostExecute(String file_url) {

            pDialog.dismiss();
            if (file_url != null) {
                Toast.makeText(ServiciosEspecificos.this, file_url, Toast.LENGTH_LONG).show();

            }
            if(Ste.equals("Asignado"))
            {
                finish();
            }
            else
            {
                if(Ste.equals("Recogido"))
                {
                    /*Intent i=new Intent(ServiciosEspecificos.this,FotoFirma.class);
                    i.putExtra("PIN",Pin1);
                    startActivity(i);
                    finish();*//*
                }
            }
        }
    }*/
}

