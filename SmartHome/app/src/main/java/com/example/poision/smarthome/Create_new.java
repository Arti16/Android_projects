package com.example.poision.smarthome;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

/**
 * Created by poision on 1/4/17.
 */

public class Create_new extends AppCompatActivity {

    DatabaseReference head = FirebaseDatabase.getInstance().getReference();
    DatabaseReference rooms=head.child("home_data");
    Spinner combo_room,combo_devices,etport,room_present;
    ArrayAdapter<String> spinnerAdapter,spinnerAdapter2,devicesNotPresent,ports;
    Button addRoom,addDevice;
    EditText setet;
    TextView tv1;
    Map<String,String> map_present,map_room_present;
    String roomArr[],deviceArr[], roomSelected,deviceSelected;

    // DataSnapshot d=new DataSnapshot(head);
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_new);
        spinnerAdapter = new ArrayAdapter<String>(this,R.layout.spinner_item);
        spinnerAdapter2 = new ArrayAdapter<String>(this, R.layout.spinner_item);
        devicesNotPresent = new ArrayAdapter<String>(this, R.layout.spinner_item);
        ports= new ArrayAdapter<String>(this, R.layout.spinner_item);
        combo_room=(Spinner) findViewById(R.id.room_spin);
        etport=(Spinner) findViewById(R.id.port);
        combo_devices=(Spinner) findViewById(R.id.device_select);
        room_present=(Spinner) findViewById(R.id.room_select);
        addRoom=(Button)findViewById(R.id.setroom);
        addDevice=(Button)findViewById(R.id.adddevice);
        roomArr=new String[7];
        roomArr[0]="bathroom";
        roomArr[1]="bedroom";
        roomArr[2]="drawingroom";
        roomArr[3]="lobby";
        roomArr[4]="guestroom";
        roomArr[5]="diningroom";
        roomArr[6]="kitchen";
        deviceArr=new String[3];
        deviceArr[0]="lights";
        deviceArr[1]="fan";
        deviceArr[2]="television";
        ports.add("4");ports.add("17");ports.add("18");ports.add("27");ports.add("22");ports.add("23");ports.add("24");
        etport.setAdapter(ports);
        etport.setVisibility(View.GONE);
        map_present=new HashMap<String,String>();
        map_room_present=new HashMap<String,String>();
        setDBChange();
        addRoom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                rooms.child(combo_room.getSelectedItem().toString()).child("dummy").setValue(-1);
            }
        });
        combo_room.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        room_present.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                roomSelected=room_present.getSelectedItem().toString();
                devicesNotPresent.clear();
                for(int ii=0;ii<deviceArr.length;ii++)
                {
                    if(map_present.get(roomSelected+"-"+deviceArr[ii])==null)
                    {
                        devicesNotPresent.add(deviceArr[ii]);
                    }
                }
                combo_devices.setAdapter(devicesNotPresent);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        combo_devices.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                deviceSelected=combo_devices.getSelectedItem().toString();
                etport.setVisibility(View.VISIBLE);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        etport.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        addDevice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(room_present.getSelectedItem()!=null && combo_devices.getSelectedItem()!=null && etport.getSelectedItem()!=null )
                {
                    String room=room_present.getSelectedItem().toString();
                    rooms.child(room).child("dummy").removeValue();
                    int port=Integer.parseInt(etport.getSelectedItem().toString());
                    rooms.child(room_present.getSelectedItem().toString()).child(combo_devices.getSelectedItem().toString()).setValue(port);
                    etport.setVisibility(View.GONE);
                }
            }
        });

    }
    void setDBChange()
    {
        rooms.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                map_present.clear();
                map_room_present.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    map_room_present.put(snapshot.getKey(),"1");
                    for(DataSnapshot child:snapshot.getChildren())
                    {
                        map_present.put(snapshot.getKey()+"-"+child.getKey(),child.getValue().toString());
                    }
                }
                spinnerAdapter.clear();
                spinnerAdapter2.clear();
                for(int i=0;i<roomArr.length;i++)
                {
                    if(map_room_present.get(roomArr[i])==null)
                    {
                        spinnerAdapter.add(roomArr[i]);
                    }
                    else if(map_room_present.get(roomArr[i]).equals("1"))
                    {
                        spinnerAdapter2.add(roomArr[i]);
                    }
                }
                combo_room.setAdapter(spinnerAdapter);
                room_present.setAdapter(spinnerAdapter2);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
