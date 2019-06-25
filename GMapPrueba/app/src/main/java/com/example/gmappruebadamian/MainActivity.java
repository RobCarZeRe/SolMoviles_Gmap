package com.example.gmappruebadamian;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback, GoogleMap.OnMapClickListener, Fragment_Formulario.OnFragmentInteractionListener {



    public String desde="";
   // public EditText etDesde;
    EditText etDesde;

    Fragment_Formulario fragment_formulario;

    @Override
    public void onFragmentInteraction(Uri uri) {

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //Controlar el map
        SupportMapFragment mapFragment = (SupportMapFragment)getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        FloatingActionButton fab = (FloatingActionButton)findViewById(R.id.fab);
        FloatingActionButton fab2 = (FloatingActionButton)findViewById(R.id.fab2);
        FloatingActionButton fab3 = (FloatingActionButton)findViewById(R.id.fab3);
       /*
        btn_trazar =(Button)findViewById(R.id.btn_trazarRutaUPT);*/


       //mi ubicacion
        fab.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("RestrictedApi")
            @Override
            public void onClick(View v) {
                FloatingActionButton fab_1 = (FloatingActionButton) findViewById(R.id.fab2);
                if (fab_1.getVisibility() != View.VISIBLE) {
                    fab_1.setVisibility(View.VISIBLE);
                }else{
                    fab_1.setVisibility(View.INVISIBLE);
                }
                FloatingActionButton fab_2 = (FloatingActionButton) findViewById(R.id.fab3);
               // fab_2.setImageResource(R.drawable.logo_upt);
                if (fab_2.getVisibility() != View.VISIBLE) {
                    fab_2.setVisibility(View.VISIBLE);
                }else{
                    fab_2.setVisibility(View.INVISIBLE);
                }
            }
        });

        //mi ubicacion hasta la UPT
        fab2.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("RestrictedApi")
            @Override
            public void onClick(View view) {
                FloatingActionButton fab2 = (FloatingActionButton) findViewById(R.id.fab2);
                FloatingActionButton fab3 = (FloatingActionButton) findViewById(R.id.fab3);
                if (fab2.getVisibility() == View.VISIBLE) {
                    fab2.setVisibility(View.INVISIBLE);
                    fab3.setVisibility(View.INVISIBLE);
                }
                if (mMap.getMyLocation() != null){
                    //Toast.makeText(getApplicationContext(),"hola2",Toast.LENGTH_LONG).show();
                    desde=miUbicacion.latitude+","+miUbicacion.longitude;
                    new RutaMapa(MainActivity.this,mMap,desde).execute();
                }

            }
        });

        //destino hasta upt
        fab3.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("RestrictedApi")
            @Override
            public void onClick(View view) {
                FloatingActionButton fab2 = (FloatingActionButton) findViewById(R.id.fab2);
                FloatingActionButton fab3 = (FloatingActionButton) findViewById(R.id.fab3);
                if (fab3.getVisibility() == View.VISIBLE) {
                    fab2.setVisibility(View.INVISIBLE);
                    fab3.setVisibility(View.INVISIBLE);
                }
                Toast.makeText(getApplicationContext(),"Abriendo formulario",Toast.LENGTH_LONG).show();
                //formulario dinamico
                fragment_formulario=new Fragment_Formulario();
                getSupportFragmentManager().beginTransaction().add(R.id.contenedorFragment,fragment_formulario).commit();


            }
        });


       /* btn_trazar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

              //  desde=etDesde.getText().toString();
                Toast.makeText(getApplicationContext(),"hola",Toast.LENGTH_LONG).show();
                //new RutaMapa(MainActivity.this,mMap,desde).execute();
            }
        });*/

    }
   public void onClick(View v){
       etDesde =(EditText)findViewById(R.id.etDesde);
        desde=etDesde.getText().toString();
       new RutaMapa(MainActivity.this,mMap,desde).execute();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {return true;}
        return super.onOptionsItemSelected(item);
    }

    private GoogleMap mMap;
    private Double miLatitud,miLongitud;
    private LatLng miUbicacion;
    Boolean actualPosition = true;


    @Override
    public void onMapReady(GoogleMap googleMap) {
      mMap = googleMap;
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_COARSE_LOCATION)) {
            } else {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                        1);
            }
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {


            } else {

                // No explanation needed, we can request the permission.

                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        1);
            }
            return;
        }
        mMap.setMyLocationEnabled(true);
        mMap.setOnMyLocationChangeListener(new GoogleMap.OnMyLocationChangeListener() {
            @Override
            public void onMyLocationChange(Location location) {
                if(actualPosition==true){
                    miLatitud=location.getLatitude();
                    miLongitud=location.getLongitude();
                    miUbicacion = new LatLng(miLatitud, miLongitud);
                    actualPosition=false;

                    mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);


                    mMap.addMarker(new MarkerOptions().position(miUbicacion).title("Aqui estoy"));

                   // mMap.setOnMapClickListener(this);

                    //posicion de camara
                    CameraPosition cameraPosition = new CameraPosition.Builder()
                            .target(new LatLng(miUbicacion.latitude,miUbicacion.longitude))
                            .zoom(15)//zoom
                            .bearing(30)//inclinacion
                            .build();//crear la posicion de la camara po el builder
                    Toast.makeText(MainActivity.this, "Mi ubicacion", Toast.LENGTH_SHORT).show();
                    mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
                }


            }
        });

    }

    @Override
    public void onMapClick(LatLng latLng) {}


}
