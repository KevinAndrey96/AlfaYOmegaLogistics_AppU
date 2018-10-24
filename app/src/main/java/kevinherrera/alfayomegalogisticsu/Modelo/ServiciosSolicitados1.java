package kevinherrera.alfayomegalogisticsu.Modelo;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

public class ServiciosSolicitados1 extends Activity {
String Pin;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        empresaList = new ArrayList<HashMap<String, String>>();
        lista=(ListView) findViewById(R.id.lstglobal);

        SharedPreferences User = getSharedPreferences("Usuario", Context.MODE_PRIVATE);
        String Usuario=User.getString("Usuario","");

        Palabra=Usuario;
        new Consulta().execute();
    }
    ProgressDialog pDialog;
    private String Palabra = "";

    static Config C=new Config();
    ArrayList<HashMap<String, String>> empresaList;
    private static String CONSULTA_URL = C.ServidorURL+"Lista_serviciossolicitados.php";

    private static final String TAG_DATO1 ="DATO1";
    private static final String TAG_DATO2 ="DATO2";
    private static final String TAG_DATO3 ="DATO3";
    private static final String TAG_DATO4 ="DATO4";
    private static final String TAG_DATO5 ="DATO5";
    private static final String TAG_DATO6 ="DATO6";
    private static final String TAG_DATO7 ="DATO7";
    private static final String TAG_DATO8 ="DATO8";
    private static final String TAG_DATO9 ="DATO9";
    private static final String TAG_DATO10 ="DATO10";
    private static final String TAG_DATO11 ="DATO11";

    private static final String TAG_SUCCESS = "success";
    private static final String TAG_RECEIVE = "receive";
    private static final String TAG_MESSAGE = "message";

    JSONParser jParser = new JSONParser();
    JSONArray products = null;
    ListView lista;

    class Consulta extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(ServiciosSolicitados1.this);
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
                    String[] Datos=new String [11];
                    for (int i = 0; i < products.length(); i++) {
                        JSONObject c = products.getJSONObject(i);

                        Datos[0] = c.getString(TAG_DATO1);
                        Datos[1] = c.getString(TAG_DATO2);
                        Datos[2] = c.getString(TAG_DATO3);
                        Datos[3] = c.getString(TAG_DATO4);
                        Datos[4] = c.getString(TAG_DATO5);
                        Datos[5] = c.getString(TAG_DATO6);
                        Datos[6] = c.getString(TAG_DATO7);
                        Datos[7] = c.getString(TAG_DATO8);
                        Datos[8] = c.getString(TAG_DATO9);
                        Datos[9] = c.getString(TAG_DATO10);
                        Datos[10] = c.getString(TAG_DATO11);

                        HashMap map = new HashMap();

                        map.put(TAG_DATO1, "# Servicio: "+Datos[0]);
                        map.put(TAG_DATO2, ""+Datos[1]);
                        map.put(TAG_DATO3, ""+Datos[2]);
                        map.put(TAG_DATO4, ""+Datos[3]);
                        map.put(TAG_DATO5, ""+Datos[4]);
                        map.put(TAG_DATO6, ""+Datos[5]);
                        map.put(TAG_DATO7, ""+Datos[6]);
                        map.put(TAG_DATO8, ""+Datos[7]);
                        map.put(TAG_DATO9, ""+Datos[8]);
                        map.put(TAG_DATO10, ""+Datos[9]);
                        map.put(TAG_DATO11, ""+Datos[10]);

                        empresaList.add(map);
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
            runOnUiThread(new Runnable() {
                public void run() {
                    ListAdapter adapter = new SimpleAdapter(
                            ServiciosSolicitados1.this,
                            empresaList,
                            R.layout.activity_spservicios1,
                            new String[]{
                                    TAG_DATO1,
                                    //TAG_DATO2,
                                    TAG_DATO3,
    //                                TAG_DATO4,
      //                              TAG_DATO5,
        //                            TAG_DATO6,
                                    TAG_DATO7,
          //                          TAG_DATO8,
                                    TAG_DATO9,
            //                        TAG_DATO10,
              //                      TAG_DATO11,
                            },
                            new int[]{
                                    R.id.sp1pin,
                                   // R.id.sptipodeservicio,
                                    R.id.sp1estado,
                                   // R.id.spdirecorigen,
                                    //R.id.spdirecdestino,
                      //              R.id.spdistancia,
                                    R.id.sp1fechasolicitado,
                      //              R.id.spfechaentrega,
                                    R.id.sp1descripcion,
                      //              R.id.spvalorservicio,
                     //               R.id.spmensajero,
                            }){
                    @Override
                    public View getView(int position, View convertView, ViewGroup parent) {
                        View v = convertView;
                        for(int i=0;i<empresaList.size();i++){
                            if(v == null){
                                LayoutInflater vi = (LayoutInflater)ServiciosSolicitados1.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                                v=vi.inflate(R.layout.activity_spservicios1, null);
                            }

                            TextView ElDato1 = (TextView) v.findViewById(R.id.sp1pin);
                            TextView ElDato2 = (TextView) v.findViewById(R.id.sp1estado);
                            TextView ElDato3 = (TextView) v.findViewById(R.id.sp1fechasolicitado);
                            TextView ElDato4 = (TextView) v.findViewById(R.id.sp1descripcion);

                            ElDato1.setText(""+empresaList.get(position).get(TAG_DATO1));
                            ElDato2.setText(empresaList.get(position).get(TAG_DATO3));
                            ElDato3.setText(empresaList.get(position).get(TAG_DATO7));
                            ElDato4.setText(empresaList.get(position).get(TAG_DATO9));


                            if(empresaList.get(position).get(TAG_DATO3).equals("Espera")){
                                ElDato2.setText(empresaList.get(position).get(TAG_DATO3));
                                //ElDato2.setTextColor(ServiciosSolicitados1.this.getResources().getColor(R.color.rojo));

                                ElDato2.setBackgroundResource(R.drawable.roundedformazul);
                                //ElDato2.setBackgroundColor(getResources().getColor(R.color.azul));

                            }else
                            if(empresaList.get(position).get(TAG_DATO3).equals("Recogido")){
                                ElDato2.setText(empresaList.get(position).get(TAG_DATO3));
                                ElDato2.setBackgroundColor(getResources().getColor(R.color.amarillo));
                                ElDato2.setBackgroundResource(R.drawable.roundedformamarillo);
                            }
                            else
                            if(empresaList.get(position).get(TAG_DATO3).equals("Asignado")){
                                ElDato2.setText(empresaList.get(position).get(TAG_DATO3));
                                ElDato2.setBackgroundColor(getResources().getColor(R.color.amarillo));
                                ElDato2.setBackgroundResource(R.drawable.roundedformamarillo);
                            }
                            else
                            if(empresaList.get(position).get(TAG_DATO3).equals("Aceptado")){
                                ElDato2.setText(empresaList.get(position).get(TAG_DATO3));
                                ElDato2.setBackgroundColor(getResources().getColor(R.color.amarillo));
                                ElDato2.setBackgroundResource(R.drawable.roundedformrojo);
                            }
                            else
                            if(empresaList.get(position).get(TAG_DATO3).equals("Solicitud")){
                                ElDato2.setText(empresaList.get(position).get(TAG_DATO3));
                                ElDato2.setBackgroundColor(getResources().getColor(R.color.amarillo));
                                ElDato2.setBackgroundResource(R.drawable.roundedformrojo);
                            }
                            else
                            {
                                ElDato2.setText(empresaList.get(position).get(TAG_DATO3));
                                ElDato2.setBackgroundColor(getResources().getColor(R.color.amarillo));
                                ElDato2.setBackgroundResource(R.drawable.roundedformverde);
                            }
                        }
                        return v;
                    }
                    };
                    lista.setAdapter(adapter);
                    lista.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            Map<String, Object> map = (Map<String, Object>) lista.getItemAtPosition(position);
                            Pin = (String) map.get("DATO1");
                            Pin=Pin.substring(12);
                            String type, description, datesol, address1,address2, distance, value,co1,co2, mens;

                            type=(String) map.get("DATO2");
                            description=(String) map.get("DATO9");
                            datesol="Fecha: ";
                            datesol+=(String) map.get("DATO7");
                            address1=(String) map.get("DATO4");
                            address2=(String) map.get("DATO5");
                            distance=(String) map.get("DATO6");
                            value=(String) map.get("DATO10");
                            mens =(String) map.get("DATO11");
                           /* co1=(String) map.get("DATO12");
                            co2=(String) map.get("DATO13");
*/
                            Intent j= new Intent(ServiciosSolicitados1.this,ServiciosEspecificos.class);

                            j.putExtra("Pin",Pin);
                            j.putExtra("Tip",type);
                            j.putExtra("Des",description);
                            j.putExtra("Fec",datesol);
                            j.putExtra("Di1",address1);
                            j.putExtra("Di2",address2);
                            j.putExtra("Dis",distance);
                            j.putExtra("Val",value);
                            j.putExtra("Men",mens);
                           /* j.putExtra("Co1",co1);
                            j.putExtra("Co2",co2);*/
                            j.putExtra("Back","ServiciosSolicitados");
                            finish();
                            startActivity(j);
                        }
                    });
                }
            });
        }
    }
}