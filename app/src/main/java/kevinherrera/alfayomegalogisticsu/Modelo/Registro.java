package kevinherrera.alfayomegalogisticsu.Modelo;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.pm.ActivityInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import kevinherrera.alfayomegalogisticsu.Control.Config;
import kevinherrera.alfayomegalogisticsu.Control.JSONParser;
import kevinherrera.alfayomegalogisticsu.R;

/**
 * Created by Andrey on 01/09/2016.
 */

public class Registro extends Activity {

    EditText Nombres,Apellidos,Email,Contrasena1,Contrasena2,Ciudad,Telefono,Direccion;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        Nombres=(EditText) findViewById(R.id.txtregistronombres);
        Apellidos=(EditText) findViewById(R.id.txtregistroapellidos);
        Email=(EditText) findViewById(R.id.txtregistroemail);
        Ciudad=(EditText) findViewById(R.id.txtregistrociudad);
        Telefono=(EditText) findViewById(R.id.txtregistrotelefono);
        Direccion=(EditText) findViewById(R.id.txtregistrodireccion);
        Contrasena1=(EditText) findViewById(R.id.txtregistrocontrasena1);
        Contrasena2=(EditText) findViewById(R.id.txtregistrocontrasena2);

        Button btnregistrar= (Button) findViewById(R.id.btnregistroenviar);
        btnregistrar.setOnClickListener(enviarregistro);
    }
    private static final String PATTERN_EMAIL = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
            + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
    public static boolean ValidarEmail(String email) {

        Pattern pattern = Pattern.compile(PATTERN_EMAIL);

        Matcher matcher = pattern.matcher(email);
        return matcher.matches();

    }
    private View.OnClickListener enviarregistro = new View.OnClickListener(){
        @Override
        public void onClick(View v)
        {
            if(Nombres.getText().toString().equals("") || Apellidos.getText().toString().equals("") || Email.getText().toString().equals("") || Ciudad.getText().toString().equals("") || Direccion.getText().toString().equals("") || Contrasena1.getText().toString().equals(""))
            {
                Toast.makeText(Registro.this, "Por favor llene todos los campos", Toast.LENGTH_SHORT).show();
            }
            else {
                if (ValidarEmail(Email.getText().toString())) {
                    if (Contrasena1.getText().toString().equals(Contrasena2.getText().toString())) {
                        new Enviar().execute();
                    } else {
                        Toast.makeText(Registro.this, "Las contraseñas no coinciden", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(Registro.this, "Ingrese un E-mail valido", Toast.LENGTH_SHORT).show();
                }
            }
        }
    };
    private ProgressDialog pDialog;
    JSONParser jParser = new JSONParser();
    static Config C=new Config();
    private static String ENVIO_URL = C.ServidorURL+"Envia_registro.php";
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_MESSAGE = "message";

    class Enviar extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(Registro.this);
            pDialog.setMessage("Registrando");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        @Override
        protected String doInBackground(String... args) {


            int success;

            String Datos[]= new String[7];
            Datos[0]=Nombres.getText().toString();
            Datos[1]=Apellidos.getText().toString();
            Datos[2]=Email.getText().toString();
            Datos[3]=Ciudad.getText().toString();
            Datos[4]=Contrasena1.getText().toString();
            Datos[5]=Direccion.getText().toString();
            Datos[6]=Telefono.getText().toString();

            try {

                List params = new ArrayList();
                for (int i=0; i<Datos.length; i++)
                    params.add(new BasicNameValuePair("Dato"+(i+1), Datos[i]));

                JSONObject json = jParser.makeHttpRequest(ENVIO_URL, "POST", params);

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
                Toast.makeText(Registro.this, file_url, Toast.LENGTH_LONG).show();
            }
        }
    }
}
