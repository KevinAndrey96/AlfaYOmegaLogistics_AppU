package kevinherrera.alfayomegalogisticsu.Modelo;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import kevinherrera.alfayomegalogisticsu.Control.Config;
import kevinherrera.alfayomegalogisticsu.Control.JSONParser;
import kevinherrera.alfayomegalogisticsu.R;

/**
 * Created by Andrey on 30/10/2016.
 */
public class DatosMensajero extends Activity {
    TextView Nombre,Documento,Telefono,Email;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_datosmensajero);

        Nombre = (TextView) findViewById(R.id.TVNombre);
        Documento = (TextView) findViewById(R.id.TVDocumento);
        Telefono = (TextView) findViewById(R.id.TVTelefono);
        Email = (TextView) findViewById(R.id.TVEmail);

        Palabra=getIntent().getExtras().getString("Mensajero");

        new Consulta().execute();

    }
    ProgressDialog pDialog;
    private String Palabra = "";

    static Config C=new Config();
    ArrayList<HashMap<String, String>> empresaList;
    private static String CONSULTA_URL = C.ServidorURL+"Lista_datosmensajero.php";

    private static final String TAG_DATO1 ="DATO1";
    private static final String TAG_DATO2 ="DATO2";
    private static final String TAG_DATO3 ="DATO3";
    private static final String TAG_DATO4 ="DATO4";

    private static final String TAG_SUCCESS = "success";
    private static final String TAG_RECEIVE = "receive";
    private static final String TAG_MESSAGE = "message";

    JSONParser jParser = new JSONParser();
    JSONArray products = null;
    ListView lista;

    String[] Datos=new String [4];

    class Consulta extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(DatosMensajero.this);
            pDialog.setMessage("Cargando...");
            pDialog.setIndeterminate(true);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        protected String doInBackground(String... args) {

            String P = Palabra;
            List params = new ArrayList();

            params.add(new BasicNameValuePair("Parametro", P));

            JSONObject json = jParser.makeHttpRequest(CONSULTA_URL, "POST", params);

            try {

                int success = json.getInt(TAG_SUCCESS);

                if (success == 1) {
                    products = json.getJSONArray(TAG_RECEIVE);

                    for (int i = 0; i < products.length(); i++) {
                        JSONObject c = products.getJSONObject(i);

                        Datos[0] = c.getString(TAG_DATO1);
                        Datos[1] = c.getString(TAG_DATO2);
                        Datos[2] = c.getString(TAG_DATO3);
                        Datos[3] = c.getString(TAG_DATO4);

                        HashMap map = new HashMap();

                        map.put(TAG_DATO1, ""+Datos[0]);
                        map.put(TAG_DATO2, ""+Datos[1]);
                        map.put(TAG_DATO3, ""+Datos[2]);
                        map.put(TAG_DATO4, ""+Datos[3]);


                    }
                    return json.getString(TAG_MESSAGE);
                }
                else
                {
                    return json.getString(TAG_MESSAGE);
                }
            } catch (JSONException e) {

            }
            return null;
        }

        protected void onPostExecute(String file_url) {
            //Toast.makeText(Teoria.this, file_url, Toast.LENGTH_SHORT).show();
            pDialog.dismiss();

            Nombre.setText("Nombre: "+ Datos[0]);
            Documento.setText("Documento: "+Datos[1]);
            Telefono.setText("Teléfono: "+Datos[2]);
            Email.setText("Email: "+Datos[3]);
        }
    }
}
