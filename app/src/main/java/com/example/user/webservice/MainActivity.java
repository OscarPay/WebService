package com.example.user.webservice;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private ListView listView;
    public static final String LOG_TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listView = (ListView) findViewById(R.id.list_view);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, new ArrayList<String>());
        listView.setAdapter(adapter);

        String filename = "dataFile.txt";
        String string = "Hello world!";
        FileOutputStream file = null;

        try {
            file = openFileOutput(filename, Context.MODE_PRIVATE);
        } catch (FileNotFoundException e) {
            Log.d(LOG_TAG, e.getMessage());
        }


        FetchDataTask fetchDataTask = new FetchDataTask(adapter, file);
        fetchDataTask.execute("Hola");


    }

    public void onClickBtn(View view){
        Intent intent = new Intent(getApplicationContext(),BTActivity.class);
        startActivity(intent);
    }

    /*
    private void writeFileToInternalStorage() {
        String filename = "dataFile.txt";
        String string = "Hello world!";
        FileOutputStream outputStream;

        try {
            outputStream = openFileOutput(filename, Context.MODE_PRIVATE);
            outputStream.write(string.getBytes());
            Log.d(LOG_TAG, "Se creo el archivo/data/data/com.example.user.webservice/files");
            outputStream.close();
        } catch (Exception e) {
            Log.d(LOG_TAG, e.getMessage());
        }
    }*/

}
