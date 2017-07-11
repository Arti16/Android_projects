package com.secure.google.tracker;

import android.*;
import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.StreetViewPanoramaCamera;
import com.google.firebase.FirebaseException;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.net.Socket;
import java.text.DateFormat;
import java.text.FieldPosition;
import java.text.ParsePosition;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    TextView tv;
    Button btn;
    private GoogleMap mMap;
    String name;
    FirebaseDatabase db = FirebaseDatabase.getInstance();
    DatabaseReference db_location = db.getReference().child("Location");
    DatabaseReference db_users = db.getReference().child("Users");
    DatabaseReference db_request = db.getReference().child("Request");
    Map<String,String> map;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        tv=(TextView)findViewById(R.id.textView);
        btn=(Button)findViewById(R.id.button2);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                db_request.child(name).setValue(Math.random());
            }
        });
        map=new HashMap<>();
        db_users.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                map.clear();
                for(DataSnapshot ds:dataSnapshot.getChildren())
                    map.put(ds.getKey(),"1");
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        Context context = MapsActivity.this;
        LayoutInflater li = LayoutInflater.from(context);
        View promptsView = li.inflate(R.layout.imagealert, null);

        AlertDialog.Builder adb = new AlertDialog.Builder(
                context);
        adb.setTitle("Enter whom to track");
        // set prompts.xml to alertdialog builder
        adb.setView(promptsView);

        final EditText userInput = (EditText) promptsView
                .findViewById(R.id.ipinput);
        adb.setCancelable(false);
        adb.setPositiveButton("ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                final String ip = userInput.getText().toString().trim();
                name = ip;
                if (name == null || name.isEmpty())
                    finish();
                if(map.get(name)==null)
                {
                    Toast.makeText(getApplicationContext(), "user not found", Toast.LENGTH_SHORT).show();
                    return;
                }
                db_location = db_location.child(name);
                db_request.child(name).setValue(Math.random());
                SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                        .findFragmentById(R.id.map);
                mapFragment.getMapAsync(MapsActivity.this);
            }
        });
        AlertDialog aa = adb.create();
        aa.show();
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.

    }

    @Override
    protected void onStart() {
        final ProgressDialog pd=new ProgressDialog(this);
        pd.setMessage("please wait");
        super.onStart();
        DatabaseReference connectedRef = FirebaseDatabase.getInstance().getReference(".info/connected");
        connectedRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                if(snapshot==null)
                    return;
                boolean connected = snapshot.getValue(Boolean.class);
                if (connected) {
                    {
                        pd.cancel();
                    }
                } else {
                    pd.show();
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                System.err.println("Listener was cancelled");
            }
        });
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        mMap.setBuildingsEnabled(true);
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)== PackageManager.PERMISSION_GRANTED)
            mMap.setMyLocationEnabled(true);
        try {
            db_location.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    try {
                        String lat = dataSnapshot.child("lat").getValue().toString();
                        String longi = dataSnapshot.child("long").getValue().toString();
                        String time = dataSnapshot.child("time").getValue().toString();
                        String accuracy=dataSnapshot.child("accuracy").getValue().toString();
                        tv.setText("Last Update: "+time+"\nAccuracy:"+accuracy);
                        mMap.clear();
                        LatLng newLoc = new LatLng(Double.parseDouble(lat), Double.parseDouble(longi));
                        mMap.addMarker(new MarkerOptions().position(newLoc).title(name).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_VIOLET)));
                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(newLoc, 18));
                    } catch (Exception e) {

                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }catch (Exception e)
        {
            Toast.makeText(this, "User not found", Toast.LENGTH_SHORT).show();
        }
    }
}
