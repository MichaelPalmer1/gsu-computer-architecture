package com.michaelpalmer.lab9sqldata;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TextView textView = (TextView) findViewById(R.id.text);

        TuringAwardDatabase database = new TuringAwardDatabase(this);
        database.populateData();

        String aliveRecipients = "";

        for (TuringAward award : database.getAliveRecipients()) {
            aliveRecipients += award.getName() + "\n";
        }

        textView.setText(aliveRecipients);

    }
}
