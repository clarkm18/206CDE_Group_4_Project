package com.example.mapprototype;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Camera;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import java.util.UUID;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import android.content.Intent;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private FusedLocationProviderClient mFusedLocationClient;
    protected Location mLastLocation;
    private String mLatitudeLabel;
    private String mLongtitudeLabel;
    private static final String TAG = "MapsActivity";
    public String snipp = "", title = "", type = "", mapType = "Default";
    Marker currentMarker;
    LocationManager locationManager ;
    boolean GPSOn;
    private static final int REQUEST_PERMISSIONS_REQUEST_CODE = 34;
    Button typeButton, mapButton;
    //DataSnapshot dataSnapshot;

    List<Marker> markers = new ArrayList<>();
    int showMarkerInt = 0;

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference reference = database.getReference();
   // int uniqueID = 0;
    Random r = new Random();

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        typeButton = (Button) findViewById(R.id.typeButton);
        mapButton = (Button) findViewById(R.id.mapButton);

        //Sets up a listener to listen for when new data is added to the database;
        reference.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                loadMarkers(dataSnapshot);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void onStart()
    {
        super.onStart();
        locationManager = (LocationManager)getApplicationContext().getSystemService(Context.LOCATION_SERVICE);
        GPSOn = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        if (!checkPermissions())
        {
            requestPermissions();
        }
        else if (GPSOn == true)
        {
            getDeviceLocation();
        }
    }

    private boolean checkPermissions()
    {
        int permissionState = ActivityCompat.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION);
        return permissionState == PackageManager.PERMISSION_GRANTED;
    }

    private void requestPermissions()
    {
            Log.i(TAG, "Requesting permission");
            startLocationPermissionRequest();
    }

    private void startLocationPermissionRequest()
    {

        ActivityCompat.requestPermissions(MapsActivity.this, new String []
                {Manifest.permission.ACCESS_FINE_LOCATION},REQUEST_PERMISSIONS_REQUEST_CODE);
    }

    private void getDeviceLocation(){
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        try {
            Task location = mFusedLocationClient.getLastLocation();
            location.addOnCompleteListener(new OnCompleteListener() {
                @Override
                public void onComplete(@NonNull Task task) {
                    Location currentLocation = (Location) task.getResult();
                    LatLng pos = new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude());
                    float zoom = 15.0f;
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(pos, zoom));
                }
            });
        } catch (SecurityException e) {
            Log.e("Error", "getDeviceLocation: SecurityException: " + e.getMessage());
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        mMap.setInfoWindowAdapter(new InfoWindow(MapsActivity.this));

        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng point) {

            }
        });

        mMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
            @Override
            public void onMapLongClick(LatLng point) {
                Marker marker = mMap.addMarker(new MarkerOptions().position(point)
                        .title("Click here to submit the report")
                        .snippet(" ")
                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.defaultmarker)));
                markers.add(marker);
            }
        });

        mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(Marker marker) {
                String tempTitle = marker.getTitle();
                if(tempTitle.equals("Click here to submit the report")) {
                    Intent intent = new Intent(MapsActivity.this, MarkerInfo.class);
                    currentMarker = marker;
                    startActivityForResult(intent, 1);
                }
            }
        });

        typeButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent typeIntent = new Intent(MapsActivity.this, ChooseType.class);
                startActivityForResult(typeIntent, 2);
            }
        });

        mapButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MapsActivity.this, MapType.class);
                Log.i("mapTypeButton", "Success!");
                startActivityForResult(intent, 3);
            }
        });

    }

    //Loads the markers from the database and draws them on the map
    private void loadMarkers(DataSnapshot dataSnapshot)
    {
        if(dataSnapshot.exists()) {
            //Iterates through the children of the snapshot (the id's)
            for (DataSnapshot ds : dataSnapshot.getChildren()) {
                //Getting the id of each entry, as well as the data of each id
                //String uID = ds.getKey();
                Report report = ds.getValue(Report.class);

                // Log.i("ID", uID);
                Log.i("Title", "Title: " + report.getTitle());
                Log.i("Description", "Desc: " + report.getDescription());
                Log.i("Lat", "Lat: " + report.getLatitude());
                Log.i("Long", "Long: " + report.getLongitude());

                LatLng pos = new LatLng(report.getLatitude(), report.getLongitude());
                Log.i("Pos", "Pos: " + pos);

                String title = report.getTitle();

                //Creating the marker
                Marker newMarker = null;
                if(title != null) {
                    newMarker = mMap.addMarker(new MarkerOptions()
                            .position(pos)
                            .title(report.getTitle())
                            .snippet(report.getDescription()));
                }

                    if (title != null && newMarker != null) {
                        switch (title) {
                            case "Nearby Event":
                                newMarker.setIcon((BitmapDescriptorFactory.fromResource(R.drawable.nearbyevents)));
                                break;
                            case "Place Of Interest":
                                newMarker.setIcon((BitmapDescriptorFactory.fromResource(R.drawable.placeofinterest)));
                                break;
                            case "Broken Lights":
                                newMarker.setIcon((BitmapDescriptorFactory.fromResource(R.drawable.lightbulb)));
                                break;
                            case "Littering":
                                newMarker.setIcon((BitmapDescriptorFactory.fromResource(R.drawable.littering_1)));
                                break;
                            case "Potholes":
                                newMarker.setIcon((BitmapDescriptorFactory.fromResource(R.drawable.pothole)));
                                break;
                            case "Damaged Pavement":
                                newMarker.setIcon((BitmapDescriptorFactory.fromResource(R.drawable.pavement)));
                                break;
                            default:
                                newMarker.setIcon((BitmapDescriptorFactory.fromResource(R.drawable.defaultmarker)));
                                break;
                        }
                    }

                if(newMarker != null)
                    markers.add(newMarker); //Adding the new marker to the marker list
            }
        }
    }

    void ChangeMapMarkerIcons()
    {
        String newTitle = "";
        for(int i = 0; i < markers.size(); i++) {
            newTitle = markers.get(i).getTitle();
            if(mapType.equals("Hybrid") || mapType.equals("Satellite")) {
                if (newTitle != null) {
                    switch (newTitle) {
                        case "Nearby Event":
                            markers.get(i).setIcon((BitmapDescriptorFactory.fromResource(R.drawable.whiteevents)));
                            break;
                        case "Place Of Interest":
                            markers.get(i).setIcon((BitmapDescriptorFactory.fromResource(R.drawable.whiteplace)));
                            break;
                        case "Broken Lights":
                            markers.get(i).setIcon((BitmapDescriptorFactory.fromResource(R.drawable.whitelight)));
                            break;
                        case "Littering":
                            markers.get(i).setIcon((BitmapDescriptorFactory.fromResource(R.drawable.whitelitter)));
                            break;
                        case "Potholes":
                            markers.get(i).setIcon((BitmapDescriptorFactory.fromResource(R.drawable.whitepothole)));
                            break;
                        case "Damaged Pavement":
                            markers.get(i).setIcon((BitmapDescriptorFactory.fromResource(R.drawable.whitepavement)));
                            break;
                        default:
                            markers.get(i).setIcon((BitmapDescriptorFactory.fromResource(R.drawable.whitemarker)));
                            break;
                    }
                }
            } else if(mapType.equals("Default")) {
                if (newTitle != null) {
                    switch (newTitle) {
                        case "Nearby Event":
                            markers.get(i).setIcon((BitmapDescriptorFactory.fromResource(R.drawable.nearbyevents)));
                            break;
                        case "Place Of Interest":
                            markers.get(i).setIcon((BitmapDescriptorFactory.fromResource(R.drawable.placeofinterest)));
                            break;
                        case "Broken Lights":
                            markers.get(i).setIcon((BitmapDescriptorFactory.fromResource(R.drawable.lightbulb)));
                            break;
                        case "Littering":
                            markers.get(i).setIcon((BitmapDescriptorFactory.fromResource(R.drawable.littering_1)));
                            break;
                        case "Potholes":
                            markers.get(i).setIcon((BitmapDescriptorFactory.fromResource(R.drawable.pothole)));
                            break;
                        case "Damaged Pavement":
                            markers.get(i).setIcon((BitmapDescriptorFactory.fromResource(R.drawable.pavement)));
                            break;
                        default:
                            markers.get(i).setIcon((BitmapDescriptorFactory.fromResource(R.drawable.defaultmarker)));
                            break;
                    }
                }
            }
        }

    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        MapsActivity.super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1) {
            if(data != null) {

                snipp = Objects.requireNonNull(data.getExtras()).getString("String");
                title = data.getExtras().getString("Title");
                currentMarker.setSnippet(snipp);
                currentMarker.setTitle(title);
                currentMarker.showInfoWindow();

                String uniqueID = UUID.randomUUID().toString();
                DatabaseReference titleRef = database.getReference(String.valueOf(uniqueID) + "/Title");
                DatabaseReference descRef = database.getReference(String.valueOf(uniqueID) + "/Description");
                DatabaseReference latRef = database.getReference(String.valueOf(uniqueID) + "/Latitude");
                DatabaseReference longRef = database.getReference(String.valueOf(uniqueID) + "/Longitude");

                if(!snipp.equals(""))
                    descRef.setValue(snipp);
                latRef.setValue(currentMarker.getPosition().latitude);
                longRef.setValue(currentMarker.getPosition().longitude);
                titleRef.setValue(title);
            }
        }
        else if(requestCode == 2)
        {
            if(data != null)
            {
                type = data.getExtras().getString("Type");
               // if(type.equals("Nearby Event"))
                if(!type.equals("Show All"))
                {
                    for(int i = 0; i < markers.size(); i++) {
                        String temp = markers.get(i).getTitle();

                        if(!temp.equals(type))
                            markers.get(i).setVisible(false);
                        else
                            markers.get(i).setVisible(true);
                    }
                }
                else if(type.equals("Show All"))
                    for(int i = 0; i < markers.size(); i++)
                        markers.get(i).setVisible(true);
            }
        }
        else if(requestCode == 3)
        {
            if(data != null)
            {
                mapType = data.getExtras().getString("MapType");
               // Log.i("mapType", mapType);
                if(mapType.equals("Hybrid"))
                    mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
                else if(mapType.equals("Satellite"))
                    mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
                else if(mapType.equals(("Default")))
                    mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

                ChangeMapMarkerIcons();
            }
        }
    }
}



