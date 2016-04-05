package com.example.user.webservice;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.util.Set;

public class BTActivity extends AppCompatActivity {

    public static String EXTRA_DEVICE_ADDRESS = "device_address";
    private static final int REQUEST_ENABLE_BT = 1;
    private ListView mlvConnected;
    private ListView mlvSearch;
    private ArrayAdapter<String> adapter;
    private ArrayAdapter<String> adatterSearch;
    private BluetoothAdapter bluetoothAdapter;
    private Button mButton;
    private IntentFilter filter;

    private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            // When discovery finds a device
            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                // Get the BluetoothDevice object from the Intent
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                // Add the name and address to an array adapter to show in a ListView
                adatterSearch.add(device.getName() + "\n" + device.getAddress());
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.btactivity_main);

        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if(bluetoothAdapter == null){
            //Device does not support Bluetooth
            return;
        }

        mlvConnected = (ListView) findViewById(R.id.ListActivity);
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1);

        mlvConnected.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String object = (String) mlvConnected.getItemAtPosition(position);
                String address = object.substring(object.length() - 17);
                // Create the result Intent and include the MAC address
                Intent intent = new Intent(getApplicationContext(),RemoteBluetooth.class);
                intent.putExtra(EXTRA_DEVICE_ADDRESS, address);
                // Set result and finish this Activity
                setResult(Activity.RESULT_OK, intent);
                startActivity(intent);
                finish();
            }
        });


        mlvSearch = (ListView) findViewById(R.id.listViewSearched);
        adatterSearch = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1);
        mlvSearch.setAdapter(adatterSearch);

        mlvSearch.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String object = (String) mlvSearch.getItemAtPosition(position);
                String address = object.substring(object.length() - 17);
                // Create the result Intent and include the MAC address
                Intent intent = new Intent(getApplicationContext(), RemoteBluetooth.class);
                intent.putExtra(EXTRA_DEVICE_ADDRESS, address);
                // Set result and finish this Activity
                setResult(Activity.RESULT_OK, intent);
                startActivity(intent);
                finish();
            }
        });

        if(!bluetoothAdapter.isEnabled()){
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
        }else{
            printList();
        }

        mButton = (Button) findViewById(R.id.button);
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(),"Buscando dispositivos", Toast.LENGTH_LONG).show();
                searchForDevices();
            }
        });

        // Register the BroadcastReceiver
        filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        registerReceiver(mReceiver, filter); // Don't forget to unregister during onDestroy
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
        unregisterReceiver(mReceiver);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == REQUEST_ENABLE_BT) {
            if(resultCode == Activity.RESULT_OK){
                Toast.makeText(getApplicationContext(),"Bluetooth Activado", Toast.LENGTH_LONG).show();
                printList();
            }

            if (resultCode == Activity.RESULT_CANCELED) {
                Toast.makeText(getApplicationContext(),"No hay nada que mostrar", Toast.LENGTH_LONG).show();
            }
        }
    }

    private void printList(){
        Set<BluetoothDevice> pairedDevices = bluetoothAdapter.getBondedDevices();
        // If there are paired devices
        if (pairedDevices.size() > 0) {
            // Loop through paired devices
            for (BluetoothDevice device : pairedDevices) {
                // Add the name and address to an array adapter to show in a ListView
                adapter.add(device.getName() + "\n" + device.getAddress());
            }
        }

        mlvConnected.setAdapter(adapter);
    }

    private void searchForDevices(){
        if (bluetoothAdapter.isDiscovering()) {
            bluetoothAdapter.cancelDiscovery();
        }
        bluetoothAdapter.startDiscovery();
    }

}
