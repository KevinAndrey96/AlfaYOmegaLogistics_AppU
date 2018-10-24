package kevinherrera.alfayomegalogisticsu.Modelo;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import kevinherrera.alfayomegalogisticsu.Control.Config;
import kevinherrera.alfayomegalogisticsu.Control.JSONParser;
import kevinherrera.alfayomegalogisticsu.R;

/**
 * Created by Andrey on 24/10/2016.
 */
public class CaracteristicasServicio extends Activity {
    RadioButton sobres,cajas,especiales;
    TextView Tipo, Km, Valor;
    double Valorenvio;
    TextView espnota;

    Button btncalcularvalor,btnAceptar;
    String Usuario,CordOrigen,CordDestino,DirOrigen,DirDestino,Descripcion,Value;

    EditText Direori,DireDesti,espdescri,cajlargo,cajancho,cajalto,cajpeso;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_caracteristicasservicio);

        SharedPreferences User = getSharedPreferences("Usuario", Context.MODE_PRIVATE);
        Usuario=User.getString("Usuario","");

        especiales = (RadioButton) findViewById(R.id.rbespecial);
        cajas = (RadioButton) findViewById(R.id.rbcaja);
        sobres = (RadioButton) findViewById(R.id.rbsobre);

        Tipo = (TextView) findViewById(R.id.tvtipo);
        Km = (TextView) findViewById(R.id.tvkm);
        Valor = (TextView) findViewById(R.id.tvvalor);

        especiales.setOnClickListener(clickespeciales);
        cajas.setOnClickListener(clickcajas);
        sobres.setOnClickListener(clicksobres);

        espdescri = (EditText) findViewById(R.id.txtespdescripcion);
        espnota = (TextView) findViewById(R.id.txtespnota);

        cajlargo = (EditText) findViewById(R.id.txtlargo);
        cajancho = (EditText) findViewById(R.id.txtancho);
        cajalto = (EditText) findViewById(R.id.txtalto);
        cajpeso = (EditText) findViewById(R.id.tvpesop);

        cajlargo.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {
                CalculaValor();

            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                CalculaValor();
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                CalculaValor();
            }
        });


    cajancho.addTextChangedListener(new TextWatcher() {

        public void afterTextChanged(Editable s) {
            CalculaValor();

        }

        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            CalculaValor();
        }

        public void onTextChanged(CharSequence s, int start, int before, int count) {
            CalculaValor();
        }
    });

    cajalto.addTextChangedListener(new TextWatcher() {

        public void afterTextChanged(Editable s) {
            CalculaValor();

        }

        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            CalculaValor();
        }

        public void onTextChanged(CharSequence s, int start, int before, int count) {
            CalculaValor();
        }
    });

    Direori = (EditText) findViewById(R.id.txtdiro);
        DireDesti = (EditText) findViewById(R.id.txtdird);

        Direori.setText(getIntent().getExtras().getString("DO",""));
        DireDesti.setText(getIntent().getExtras().getString("DD",""));

        btncalcularvalor =(Button) findViewById(R.id.btncancelarcaracteristicas);
        btnAceptar = (Button) findViewById(R.id.btnaceptarcaracteristicas);

        btncalcularvalor.setOnClickListener(clickcancelarcar);
        btnAceptar.setOnClickListener(clickaceptarcar);

        btnAceptar.setVisibility(View.INVISIBLE);
        btnAceptar.setEnabled(false);

        visiesp(false);
        visicaj(false);
        String km=getIntent().getExtras().getString("Km","");
        CordOrigen=getIntent().getExtras().getString("CO","");
        CordDestino=getIntent().getExtras().getString("CD","");


        Km.setText(km);
        Kilos=Double.parseDouble(km);
        CalculaValor();

    }
    private View.OnClickListener clickcancelarcar = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            CalculaValor();
            btnAceptar.setVisibility(View.VISIBLE);
            btnAceptar.setEnabled(true);

        }
    };

    private View.OnClickListener clickaceptarcar = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            boolean err=false;
            if(who.equals("Caja"))
            {
                if(Integer.parseInt(cajlargo.getText().toString())>0 || Integer.parseInt(cajalto.getText().toString())>0 || Integer.parseInt(cajancho.getText().toString())>0) {
                    if (Integer.parseInt(cajlargo.getText().toString()) > 50 || Integer.parseInt(cajalto.getText().toString()) > 50 || Integer.parseInt(cajancho.getText().toString()) > 50 || Integer.parseInt(cajpeso.getText().toString()) > 30) {
                        Toast.makeText(CaracteristicasServicio.this, "La caja es demasiado grande o pesada, considere seleccionar tipo especiales", Toast.LENGTH_SHORT).show();
                        err = true;
                    }
                }
                else
                {
                    err=true;
                }
                CalculaValor();
            }
            if(who.equals("Sobre"))
            {
                    //espdescri.setText("Sobre");
            }
            if(Usuario.equals("") || who.equals("") || Kilos==0 || CordOrigen.equals("") || CordDestino.equals("") || Direori.getText().toString().equals("") || DireDesti.getText().toString().equals("") || Valorenvio==0 )
            {
                Toast.makeText(CaracteristicasServicio.this, "Se produjo un error con su servicio", Toast.LENGTH_SHORT).show();
                finish();
            }
            else
            {
                if(!err){
                    new Enviar().execute();
                }
            }
        }
    };

    double Kilos;
    private void CalculaValor()
    {
        Kilos = Math.ceil(Kilos);

        Valorenvio=9000;
        if(who.equals("Caja"))
        {

            if(!cajlargo.getText().toString().equals("") && !cajalto.getText().toString().equals("") && !cajancho.getText().toString().equals("") && !cajpeso.getText().toString().equals(""))
            {
                if (Integer.parseInt(cajlargo.getText().toString()) <= 25 && Integer.parseInt(cajalto.getText().toString()) <= 25 && Integer.parseInt(cajancho.getText().toString()) <= 25 && Integer.parseInt(cajpeso.getText().toString()) < 15) {
                    //Toast.makeText(CaracteristicasServicio.this, Integer.parseInt(cajlargo.getText().toString())+" "+Integer.parseInt(cajalto.getText().toString())+" "+Integer.parseInt(cajancho.getText().toString())+" "+Integer.parseInt(cajpeso.getText().toString()), Toast.LENGTH_SHORT).show();
                    //Valorenvio += 1000;
                    Valorenvio += 10000;
                   // Toast.makeText(CaracteristicasServicio.this, "Caja pequeña: "+Valorenvio, Toast.LENGTH_SHORT).show();
                    //Valorenvio+=(Kilos-9)*900;
                    //Toast.makeText(CaracteristicasServicio.this, "Kilometros: "+Valorenvio, Toast.LENGTH_SHORT).show();
                } else {
                    //Toast.makeText(CaracteristicasServicio.this, Integer.parseInt(cajlargo.getText().toString())+" "+Integer.parseInt(cajalto.getText().toString())+" "+Integer.parseInt(cajancho.getText().toString())+" "+Integer.parseInt(cajpeso.getText().toString()), Toast.LENGTH_SHORT).show();
                    //Valorenvio += 7000;

                    Valorenvio += 16000;
                    //Toast.makeText(CaracteristicasServicio.this, "Caja grande: "+Valorenvio, Toast.LENGTH_SHORT).show();
                    //Valorenvio+=(Kilos-9)*900;
                    //Toast.makeText(CaracteristicasServicio.this, "Kilometros: "+Valorenvio, Toast.LENGTH_SHORT).show();
                }
                Valor.setText("$" + Valorenvio);
                //espdescri.setText(espdescri.getText().toString()+" "+"Caja Tamaño " + cajlargo.getText().toString() + "cm x " + cajancho.getText().toString() + "cm x " + cajalto.getText().toString() + "cm - "+ cajpeso.getText().toString() + "Kg");
            }

        }
        if(Kilos>9)
        {
            Valorenvio+=(Kilos-9)*900;
        }
        if(who.equals("Especial"))
        {
            Valor.setText("No disponible");

        }
        else {
            Valor.setText("$" + Valorenvio);

        }
        Valor.setText("$" + Valorenvio);
    }
    private void visiesp(boolean flag)
    {
        if(flag) {
            espdescri.setVisibility(View.VISIBLE);
            espnota.setVisibility(View.VISIBLE);
            btncalcularvalor.setVisibility(View.INVISIBLE);
            btncalcularvalor.setEnabled(false);
            btnAceptar.setVisibility(View.VISIBLE);
            btnAceptar.setEnabled(true);
            espdescri.setText("");
            /*if(espdescri.getText().toString().equals("Sobre"))
            {
                espdescri.setText("");
            }*/
            if(!espdescri.getText().equals("")) {
                /*if (!espdescri.getText().toString().substring(0, 3).equals("Caj")) {
                    espdescri.setText("");
                }*/
            }
        }else
        {
            //espdescri.setVisibility(View.INVISIBLE);
            espnota.setVisibility(View.INVISIBLE);
            btncalcularvalor.setVisibility(View.VISIBLE);
            btncalcularvalor.setEnabled(true);
            btnAceptar.setVisibility(View.INVISIBLE);
            btnAceptar.setEnabled(false);
        }
    }
    private void visicaj(boolean flag)
    {
        if(flag) {
            cajancho.setVisibility(View.VISIBLE);
            cajalto.setVisibility(View.VISIBLE);
            cajlargo.setVisibility(View.VISIBLE);
            cajpeso.setVisibility(View.VISIBLE);
            btncalcularvalor.setVisibility(View.VISIBLE);
            btncalcularvalor.setEnabled(true);
            btnAceptar.setVisibility(View.INVISIBLE);
            btnAceptar.setEnabled(false);

            espdescri.setText("");
        }else
        {
            cajalto.setVisibility(View.INVISIBLE);
            cajlargo.setVisibility(View.INVISIBLE);
            cajancho.setVisibility(View.INVISIBLE);
            cajpeso.setVisibility(View.INVISIBLE);
            btncalcularvalor.setVisibility(View.INVISIBLE);
            btncalcularvalor.setEnabled(false);
            btnAceptar.setVisibility(View.VISIBLE);
            btnAceptar.setEnabled(true);
        }
    }
String who="";
    private View.OnClickListener clickespeciales = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            sobres.setChecked(false);
            espdescri.setText("");
            cajas.setChecked(false);
            Tipo.setText("Especial");
            who="Especial";
            visiesp(true);
            visicaj(false);
            CalculaValor();
        }
    };
    private View.OnClickListener clickcajas = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            sobres.setChecked(false);
            especiales.setChecked(false);
            espdescri.setText("");
            Tipo.setText("Caja");
            visiesp(false);
            visicaj(true);
            who="Caja";
            Valor.setText("$"+"10000");
            CalculaValor();
        }
    };
    private View.OnClickListener clicksobres = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            especiales.setChecked(false);
            cajas.setChecked(false);
            espdescri.setText("");
            Tipo.setText("Sobre");
            visiesp(false);
            who="Sobre";
            visicaj(false);
            CalculaValor();
        }
    };

    private ProgressDialog pDialog;
    JSONParser jParser = new JSONParser();
    static Config C=new Config();
    private static String ENVIO_URL = C.ServidorURL+"Envia_servicio.php";
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_MESSAGE = "message";

    class Enviar extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(CaracteristicasServicio.this);
            pDialog.setMessage("Solicitando servicio");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        @Override
        protected String doInBackground(String... args) {


            int success;



            String Datos[]= new String[9];
            Datos[0]=Usuario;
            Datos[1]=who;
            Datos[2]=Km.getText().toString();
            Datos[3]=CordOrigen;
            Datos[4]=CordDestino;
            Datos[5]=Direori.getText().toString();
            Datos[6]=DireDesti.getText().toString();
            Datos[7]=espdescri.getText().toString();
            Datos[8]=""+Valorenvio;

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
                Toast.makeText(CaracteristicasServicio.this, file_url, Toast.LENGTH_LONG).show();
                finish();
            }
        }
    }
}
