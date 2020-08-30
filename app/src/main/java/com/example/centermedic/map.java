package com.example.centermedic;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.centermedic.models.citas;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.Random;
import static android.Manifest.permission.ACCESS_FINE_LOCATION;

public class map extends FragmentActivity implements OnMapReadyCallback, LocationListener {
    private GoogleMap mMap;
    private FusedLocationProviderClient usuario;
    private ProgressDialog progress;
    private DatabaseReference mdatabase;
    private ArrayList<citas> marray = new ArrayList<>();
    public double currentLatitude;
    public double currentLongitude;


        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.fragment_map);
            // Obtain the SupportMapFragment and get notified when the map is ready to be used.
            SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
            mapFragment.getMapAsync(this);
            mdatabase= FirebaseDatabase.getInstance().getReference();
        }

        /**
         * Manipulates the map once available.
         * This callback is triggered when the map is ready to be used.
         * This is where we can add markers or lines, add listeners or move the camera.
         * In this case, we just add a marker near Sydney, Australia.
         * If Google Play services is not installed on the device, the user will be prompted to
         * install it inside the SupportMapFragment. This method will only be triggered once the
         * user has installed Google Play services and returned to the app.
         */
        @Override
        public void onMapReady(GoogleMap googleMap) {
            mMap = googleMap;
            mMap.getUiSettings().setZoomControlsEnabled(true);
            mMap.getUiSettings().setMapToolbarEnabled(true);
            progress = new ProgressDialog(map.this);
            progress.setTitle("");
            progress.setMessage("por favor un momento...");
            progress.setCancelable(false);
            progress.show();

            SolicitarPermiso();
            usuario = LocationServices.getFusedLocationProviderClient(map.this);
            if (ActivityCompat.checkSelfPermission(map.this, ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }

         /*   usuario.getLastLocation().addOnSuccessListener(map.this, new OnSuccessListener<Location>() {
                @Override
                public void onSuccess(Location location) {
                    if (location != null) {
                        LatLng  usuarioGSM = new LatLng(location.getLatitude(), location.getLongitude());
                        mMap.addMarker(new MarkerOptions().position(usuarioGSM).title("Mi ubicacion").title("Mi Ubicacion Actual").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE))).showInfoWindow();
                        //mover camara ala marca
                        mMap.moveCamera(CameraUpdateFactory.newLatLng(usuarioGSM));
                    }
                }
            });*/
            Query query = mdatabase.child("Hospitales").orderByChild("nombre");
            query.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    for (DataSnapshot ds :snapshot.getChildren()) {
                        String Hospital = ds.child("nombre").getValue().toString();
                        String lon = ds.child("longitud").getValue().toString();
                        String lat = ds.child("latitud").getValue().toString();
                        LatLng posicion = new LatLng(Double.parseDouble(lon),Double.parseDouble(lat));//Esto esta alreves, pero asi se guardo el lat y lon en la base, debe ser primero lat y luego lon
                        mMap.addMarker(new MarkerOptions().position(posicion).title(Hospital)).showInfoWindow();
                        mMap.addMarker(new MarkerOptions().position(posicion).title(Hospital).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE))).showInfoWindow();
                        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(posicion, 12));
                        //Log.d("num encontrados", Integer.toString(marray.size()));
                    }
                    progress.dismiss();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

            mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
                @Override
                public void onInfoWindowClick(Marker marker) {
                    Intent intent = new Intent(getBaseContext(), Registrocita.class);
                    String referenceHospital = marker.getTitle();
                    intent.putExtra("referenceMapTitle", referenceHospital);
                    // Starting the  Activity
                    startActivity(intent);
                    finish();
                }
            });
        }
    @Override
    public void onLocationChanged(Location location) {
        currentLatitude = location.getLatitude();               //getting current latitude
        currentLongitude = location.getLongitude();             //getting current longitude

    }


    public void SolicitarPermiso() {
        ActivityCompat.requestPermissions(this,new String[]{ACCESS_FINE_LOCATION},1);
    }
}