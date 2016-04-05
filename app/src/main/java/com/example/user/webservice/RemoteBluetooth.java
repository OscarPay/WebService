package com.example.user.webservice;


import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;

public class RemoteBluetooth extends Activity {

	private BToothConnectThread thread1;

	private static final int REQUEST_ENABLE_BT = 2;
	private static final int REQUEST_DEVICE_TO_CONNECT = 1;

	private BluetoothAdapter mBluetoothAdapter;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.btmain);

        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (mBluetoothAdapter == null) {
            Toast.makeText(this, "Bluetooth is not available", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        String address = getIntent().getExtras().getString(BTActivity.EXTRA_DEVICE_ADDRESS);

        Log.e("ADDRESS",address);

        conncectToServer(address);
    }

	@Override
	protected void onStart() {
		super.onStart();

		if (!mBluetoothAdapter.isEnabled()) {
            Toast.makeText(this, "Bluetooth is not running", Toast.LENGTH_SHORT).show();
			Intent enableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
			startActivityForResult(enableIntent, REQUEST_ENABLE_BT);
		}


	}

    public void sendClickListener(View view){
        try {
            File file = new File(getFilesDir(),"dataFile.txt");
            FileInputStream fis = new FileInputStream(file);

            thread1.sendData(file,fis);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            this.finish();
        }
    }

    private void conncectToServer(String address){
        // Get the BLuetoothDevice object
        BluetoothDevice device = mBluetoothAdapter.getRemoteDevice(address);
        TextView txt = (TextView)findViewById(R.id.dispositivo);
        txt.setText(device.getName());

        // Thread used to manage connection.
        thread1 = new BToothConnectThread(this,device);
        thread1.connectDevice();
    }
	    
}