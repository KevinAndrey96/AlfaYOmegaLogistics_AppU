package kevinherrera.alfayomegalogisticsu.Modelo;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewOutlineProvider;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import kevinherrera.alfayomegalogisticsu.Control.Config;
import kevinherrera.alfayomegalogisticsu.Control.JSONParser;
import kevinherrera.alfayomegalogisticsu.R;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {
    TextView messageTextView;
    //double lati,longi;
    String Ubica="";
    private GoogleMap mMap;
    private Marker mDest,mOri;
    private int contador=0;
    ObtenerWebService hiloconexion;
    Button Estimar,Continuar;

    String CordOr,CordDe,DirO="",DirD="";

    String Kilometros;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        Toast.makeText(MapsActivity.this, "Espere un momento...", Toast.LENGTH_LONG).show();

        LocationManager mlocManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        MyLocationListener mlocListener = new MyLocationListener();
        mlocListener.setMainActivity(this);
        mlocManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, (LocationListener) mlocListener);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        Estimar = (Button) findViewById(R.id.btnEstimar);
        Estimar.setOnClickListener(clickestimar);

        Continuar = (Button) findViewById(R.id.btnContinuar);
        Continuar.setOnClickListener(clickcontinuar);

        Estimar.setVisibility(View.INVISIBLE);
        Continuar.setVisibility(View.INVISIBLE);
        Estimar.setEnabled(false);
        Continuar.setEnabled(false);

    }
    private View.OnClickListener clickcontinuar = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if(Kilometros.equals("Cargando..."))
            {
                Toast.makeText(MapsActivity.this, "Espere un momento, mientras se carga el destino...", Toast.LENGTH_SHORT).show();
                Continuar.setEnabled(false);
                Continuar.setVisibility(View.INVISIBLE);
            }
            else {
                Intent i = new Intent(MapsActivity.this, CaracteristicasServicio.class);
                String[] h = new String[5];
                h=Kilometros.split(" ");
                i.putExtra("Km",""+h[0]);
                i.putExtra("CO",CordOr);
                i.putExtra("CD",CordDe);
                i.putExtra("DO",DirO);
                i.putExtra("DD",DirD);
                startActivity(i);
                finish();
            }
        }
    };
    private View.OnClickListener clickestimar = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            estimar();
        }
    };
    private void estimar()
    {
        Continuar.setEnabled(true);
        Continuar.setVisibility(View.VISIBLE);

        mMap.setOnMarkerDragListener(new GoogleMap.OnMarkerDragListener() {
            @Override
            public void onMarkerDragStart(Marker arg0) {
                // TODO Auto-generated method stub
                //Log.d("System out", "onMarkerDragStart..." + arg0.getPosition().latitude + "..." + arg0.getPosition().longitude);
            }

            @SuppressWarnings("unchecked")
            @Override
            public void onMarkerDragEnd(Marker arg0) {
                // TODO Auto-generated method stub
                Log.d("System out", "onMarkerDragEnd..." + arg0.getPosition().latitude + "..." + arg0.getPosition().longitude);
                mMap.animateCamera(CameraUpdateFactory.newLatLng(arg0.getPosition()));
            }

            @Override
            public void onMarkerDrag(Marker arg0) {
                // TODO Auto-generated method stub
                Log.i("System out", "onMarkerDrag...");
            }//
        });

        //Actualizar();

        double lato=mOri.getPosition().latitude;//lati;
        double longo=mOri.getPosition().longitude;//longi;
        double latd=mDest.getPosition().latitude;
        double longd=mDest.getPosition().longitude;


/*
        Location locationxs = new Location("Test");
        locationxs.setLatitude(latd);
        locationxs.setLongitude(longd);
        locationxs.setTime(new Date().getTime());
*/
        //setLocation2(locationxs);


        CordOr=lato+","+longo;
        CordDe=latd+","+longd;
        //Toast.makeText(MapsActivity.this, "Co: "+CordOr, Toast.LENGTH_SHORT).show();
        hiloconexion = new ObtenerWebService();
        hiloconexion.execute("" + lato, "" + longo, "" + latd, "" + longd);
    }
    /*
    private void Actualizar()
    {
        mOri.remove();
        LatLng Myu = new LatLng(lati, longi);
        CordOr = lati+","+longi;
        mOri=mMap.addMarker(new MarkerOptions().position(Myu).draggable(true).title("Origen"));
    }
    */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        LatLng ini = new LatLng(4.6650232,-74.0979017);
        mMap.moveCamera(CameraUpdateFactory.newLatLng(ini));
        UiSettings UISET= mMap.getUiSettings();
        UISET.setZoomControlsEnabled(true);
        mMap.animateCamera(CameraUpdateFactory.zoomTo(11.0f));
    }
    public void setLocation(Location loc) {

        if (loc.getLatitude() != 0.0 && loc.getLongitude() != 0.0) {
            try {
                Geocoder geocoder = new Geocoder(this, Locale.getDefault());
                List<Address> list = geocoder.getFromLocation(loc.getLatitude(), loc.getLongitude(), 1);
                if (!list.isEmpty()) {
                    Address address = list.get(0);
                    //messageTextView2.setText("Mi direcci—n es: \n" + address.getAddressLine(0));
                    //Toast.makeText(MapsActivity.this, "Mi direcci—n es: \n" + address.getAddressLine(0), Toast.LENGTH_SHORT).show();
                    /*if(!address.getAddressLine(0).equals("")) {
                        DirO = address.getAddressLine(0);
                    }*/
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    public void setLocation2(Location loc) {

        if (loc.getLatitude() != 0.0 && loc.getLongitude() != 0.0) {
            try {
                Geocoder geocoder = new Geocoder(this, Locale.getDefault());
                List<Address> list = geocoder.getFromLocation(loc.getLatitude(), loc.getLongitude(), 1);
                if (!list.isEmpty()) {
                    Address address = list.get(0);
                    if(!address.getAddressLine(0).equals("")) {
                        DirD = address.getAddressLine(0);
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    public class MyLocationListener implements LocationListener {
        MapsActivity mainActivity;

        public MapsActivity getMainActivity() {
            return mainActivity;
        }

        public void setMainActivity(MapsActivity mainActivity) {
            this.mainActivity = mainActivity;
        }

        @Override
        public void onLocationChanged(Location loc) {
           // loc.getLatitude();
          //  loc.getLongitude();
            /*String Text = "Mi ubicaci—n actual es: " + "\n Lat = "
                    + loc.getLatitude() + "\n Long = " + loc.getLongitude();
*/
            //lati=loc.getLatitude();
            //longi=loc.getLongitude();

            if(contador==0)
            {
                Toast.makeText(MapsActivity.this, "Rojo: Origen\nAzul: Destino", Toast.LENGTH_SHORT).show();
                Estimar.setVisibility(View.VISIBLE);
                Estimar.setEnabled(true);
                LatLng Myu = new LatLng(loc.getLatitude(), loc.getLongitude());
                mMap.moveCamera(CameraUpdateFactory.newLatLng(Myu));
                mOri=mMap.addMarker(new MarkerOptions().position(Myu).draggable(true).title("Origen"));
                mMap.animateCamera(CameraUpdateFactory.zoomTo(14.0f));

                contador++;

                double ll=loc.getLatitude()+0.001;
                double ln=loc.getLongitude()+0.001;
                Ubica = ll+"-"+ln;
                LatLng DESTINO = new LatLng(loc.getLatitude()+0.001,loc.getLongitude()+0.001);
                mDest = mMap.addMarker(new MarkerOptions()
                        .position(DESTINO)
                        .draggable(true)
                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));
                estimar();
            }
            //this.mainActivity.setLocation(loc);

        }

        @Override
        public void onProviderDisabled(String provider) {
            Toast.makeText(MapsActivity.this, "GPS Desactivado", Toast.LENGTH_SHORT).show();
            finish();
        }

        @Override
        public void onProviderEnabled(String provider) {
            Toast.makeText(MapsActivity.this, "GPS Activado", Toast.LENGTH_SHORT).show();
        }


        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {

        }
    }
    public class ObtenerWebService extends AsyncTask<String, Integer, String> {

        @Override
        protected String doInBackground(String... params) {

            String cadena = "https://maps.googleapis.com/maps/api/directions/json?origin="+params[0]+","+params[1]+"&destination="+params[2]+","+params[3]+"&sensor=false";
            //http://maps.googleapis.com/maps/api/geocode/json?latlng=38.404593,-0.529534&sensor=false
            String devuelve = "";
            if(Ubica.equals(params[2]+"-"+params[3]))
            {
                devuelve+="Cargando...";

            }
            URL url = null; // Url de donde queremos obtener información
            try {
                url = new URL(cadena);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection(); //Abrir la conexión
                connection.setRequestProperty("User-Agent", "Mozilla/5.0" +
                        " (Linux; Android 1.5; es-ES) Ejemplo HTTP");
                //connection.setHeader("content-type", "application/json");

                int respuesta = connection.getResponseCode();
                StringBuilder result = new StringBuilder();

                if (respuesta == HttpURLConnection.HTTP_OK){


                    InputStream in = new BufferedInputStream(connection.getInputStream());

                    BufferedReader reader = new BufferedReader(new InputStreamReader(in));

                    String line;
                    while ((line = reader.readLine()) != null) {
                        result.append(line);
                    }

                    JSONObject jsonObject = new JSONObject(result.toString());
                    JSONArray routesArray = jsonObject.getJSONArray("routes");
                    JSONObject route = routesArray.getJSONObject(0);
                    JSONArray legs = route.getJSONArray("legs");
                    JSONObject leg = legs.getJSONObject(0);


                    JSONObject distanceObject = leg.getJSONObject("distance");
                    String distance = distanceObject.getString("value");


                        DirD = leg.getString("end_address");

                        DirO = leg.getString("start_address");

                    double Km=Double.parseDouble(distance)/1000;
                    if(!devuelve.equals("Cargando...")) {
                        devuelve = "" + Km + " Km";
                    }

                }


            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            Kilometros=devuelve;
            return devuelve;
        }

        @Override
        protected void onCancelled(String aVoid) {
            super.onCancelled(aVoid);
        }

        @Override
        protected void onPostExecute(String aVoid) {
            //resultado.setText(aVoid);
            Toast.makeText(MapsActivity.this, aVoid, Toast.LENGTH_LONG).show();
            if(aVoid.equals("Cargando...")) {
                Continuar.setVisibility(View.INVISIBLE);
                Continuar.setEnabled(false);
            }
            super.onPostExecute(aVoid);
        }

        @Override
        protected void onPreExecute() {
            //resultado.setText("");
            super.onPreExecute();
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
        }
    }
}
