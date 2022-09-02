package com.confidenceb.mybluetoothapp;

import androidx.appcompat.app.AppCompatActivity;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    ListView listView;
    TextView tview;
    Button searchBtn;
    BluetoothAdapter bluetoothAdapter;

    ArrayList<String> availableDevices = new ArrayList<>();
    ArrayAdapter arrayAdapter;

    private  final BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            Log.i("Action performed is: ", action);

            if(BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)){
                tview.setText("Finished");
                searchBtn.setEnabled(true);
            }else if(BluetoothDevice.ACTION_FOUND.equals(action)){
                BluetoothDevice foundDevice = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                String address = foundDevice.getAddress();
                String name = foundDevice.getName();
                String rssi = Integer.toString(intent.getShortExtra(BluetoothDevice.EXTRA_RSSI, Short.MIN_VALUE));
                //Log.i("Found device"," Name: "+name+ " Address: "+address+" rssi: "+rssi);
                if(name==null||name.equals("")){
                    availableDevices.add(address+ " "+"- RSSI"+ rssi+" dBm");
                }else{
                    availableDevices.add(name+ " "+"- RSSI"+ rssi+" dBm");
                }
                arrayAdapter.notifyDataSetChanged();
            }
        }
    };


    public void searchbtnClicked(View view){
        tview.setText("Searching available devices...");
        searchBtn.setEnabled(false);
        bluetoothAdapter.startDiscovery();
        Toast.makeText(MainActivity.this, "Search started", Toast.LENGTH_SHORT).show();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listView = findViewById(R.id.listview);
        tview = findViewById(R.id.deviceStatus);
        searchBtn = findViewById(R.id.searchbtn);

        arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, availableDevices);

        listView.setAdapter(arrayAdapter);

        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(BluetoothAdapter.ACTION_STATE_CHANGED);
        intentFilter.addAction(BluetoothDevice.ACTION_FOUND);
        intentFilter.addAction(BluetoothAdapter.ACTION_DISCOVERY_STARTED);
        intentFilter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
        registerReceiver(receiver,intentFilter);

    }
}