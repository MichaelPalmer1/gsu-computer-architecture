package com.michaelpalmer.lab9txtdata;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String FILE_NAME = "KnuthQuotes";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button btn_read_data = (Button) findViewById(R.id.btn_read_data);
        Button btn_write_data = (Button) findViewById(R.id.btn_write_data);
        Button btn_delete_data = (Button) findViewById(R.id.btn_delete_file);
        btn_read_data.setOnClickListener(this);
        btn_write_data.setOnClickListener(this);
        btn_delete_data.setOnClickListener(this);

        File file = new File(getFilesDir(), FILE_NAME);

        if (!file.exists()) {
            Toast.makeText(this, "File does not exist. Creating...", Toast.LENGTH_SHORT).show();
            writeData();
        } else {
            Toast.makeText(this, "Found existing file. Reading...", Toast.LENGTH_SHORT).show();
            readData();
        }
    }

    void writeData() {
        try {
            BufferedWriter output = new BufferedWriter(
                    new OutputStreamWriter(openFileOutput(FILE_NAME, MODE_PRIVATE)));
            String[] data = getResources().getStringArray(R.array.knuth_quotes);
            for (String str : data) {
                output.write(str + "\n");
            }
            output.close();
            Toast.makeText(this, "File creation complete", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            Toast.makeText(this, "Error while writing to file: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    void readData() {
        try {
            BufferedReader inputStream = new BufferedReader(new InputStreamReader(openFileInput(FILE_NAME)));
            String line;
            while ((line = inputStream.readLine()) != null) {
                Toast.makeText(this, line, Toast.LENGTH_SHORT).show();
            }
            inputStream.close();
        } catch (FileNotFoundException e) {
            Toast.makeText(this, "File does not exist", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            Toast.makeText(this, "Error while reading from file: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_read_data:
                readData();
                break;

            case R.id.btn_write_data:
                writeData();
                break;

            case R.id.btn_delete_file:
                File f = new File(getFilesDir(), FILE_NAME);
                if (f.exists()) {
                    if (f.delete()) {
                        Toast.makeText(this, "Deletion successful", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(this, "Deletion failed", Toast.LENGTH_SHORT).show();
                    }
                }
                break;
        }
    }
}
