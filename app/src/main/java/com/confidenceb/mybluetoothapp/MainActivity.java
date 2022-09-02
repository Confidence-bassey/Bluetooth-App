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
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    ListView listView;
    TextView tview;
    Button searchBtn;
    BluetoothAdapter bluetoothAdapter;

    public void searchbtnClicked(View view){
        tview.setText("Searching available devices...");
        searchBtn.setEnabled(false);
        bluetoothAdapter.startDiscovery();
        Toast.makeText(MainActivity.this, "Search started", Toast.LENGTH_SHORT).show();
    }

    private  final BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            Log.i("Action performed is: ", action);

            if(BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)){
                tview.setText("Device found");
                searchBtn.setEnabled(true);
            }else if(BluetoothDevice.ACTION_FOUND.equals(action)){
                BluetoothDevice foundDevice = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                String address = foundDevice.getAddress();
                String name = foundDevice.getName();
                String rssi = Integer.toString(intent.getShortExtra(BluetoothDevice.EXTRA_RSSI, Short.MIN_VALUE));
                Log.i("Found device"," Name: "+name+ " Address: "+address+" rssi: "+rssi);
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listView = findViewById(R.id.listview);
        tview = findViewById(R.id.deviceStatus);
        searchBtn = findViewById(R.id.searchbtn);

        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(BluetoothAdapter.ACTION_STATE_CHANGED);
        intentFilter.addAction(BluetoothDevice.ACTION_FOUND);
        intentFilter.addAction(BluetoothAdapter.ACTION_DISCOVERY_STARTED);
        intentFilter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
        registerReceiver(receiver,intentFilter);

    }
}