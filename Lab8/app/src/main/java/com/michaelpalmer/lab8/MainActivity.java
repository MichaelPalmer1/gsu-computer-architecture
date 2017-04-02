package com.michaelpalmer.lab8;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.SparseArray;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private SensorManager sensorManager;
    private ArrayList<SensorValueListener> sensorListeners = new ArrayList<>();

    private static SparseArray<String> sensorTypes = new SparseArray<>();

    static {
        sensorTypes.append(Sensor.TYPE_AMBIENT_TEMPERATURE, Sensor.STRING_TYPE_AMBIENT_TEMPERATURE);
        sensorTypes.append(Sensor.TYPE_RELATIVE_HUMIDITY, Sensor.STRING_TYPE_RELATIVE_HUMIDITY);
        sensorTypes.append(Sensor.TYPE_PRESSURE, Sensor.STRING_TYPE_PRESSURE);
        sensorTypes.append(Sensor.TYPE_LIGHT, Sensor.STRING_TYPE_LIGHT);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TableLayout table = (TableLayout) findViewById(R.id.sensorTable);

        // Get the sensor manager
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);

        for (int i = 0; i < sensorTypes.size(); i++) {
            // Initialize table
            TableRow row = new TableRow(this);
            TextView sensorName = new TextView(this), sensorValue = new TextView(this);
            TableRow.LayoutParams params = new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 0.5f);
            sensorName.setLayoutParams(params);
            sensorValue.setLayoutParams(params);

            // Get sensor
            Sensor sensor = sensorManager.getDefaultSensor(sensorTypes.keyAt(i));

            // Check if sensor was found
            if (sensor != null) {
                sensorName.setText(sensor.getName());
                sensorValue.setText(R.string.unknown);
            } else {
                sensorName.setText(sensorTypes.get(sensorTypes.keyAt(i)));
                sensorValue.setText(R.string.no_sensor);
            }

            // Add to table
            row.addView(sensorName);
            row.addView(sensorValue);
            table.addView(row);

            // Register listener
            if (sensor != null) {
                SensorValueListener listener = new SensorValueListener(sensor, sensorValue);
                sensorListeners.add(listener);
                registerSensorListener(listener, sensor);
            }
        }

    }

    /**
     * Register a sensor listener
     *
     * @param listener Sensor listener
     * @param sensor Sensor whose events will be monitored
     */
    private void registerSensorListener(SensorValueListener listener, Sensor sensor) {
        sensorManager.registerListener(listener, sensor, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    protected void onPause() {
        super.onPause();
        // Unregister listeners
        for (SensorValueListener listener : sensorListeners) {
            sensorManager.unregisterListener(listener);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Re-register listeners
        for (SensorValueListener listener : sensorListeners) {
            registerSensorListener(listener, listener.getSensor());
        }
    }

    /**
     * Listener for sensor value changes
     */
    private class SensorValueListener implements SensorEventListener {

        private TextView sensorValue;
        private Sensor sensor;

        SensorValueListener(Sensor sensor, TextView sensorValue) {
            this.sensor = sensor;
            this.sensorValue = sensorValue;
        }

        Sensor getSensor() {
            return sensor;
        }

        @Override
        public void onSensorChanged(SensorEvent event) {
            String value = "";
            for (float v : event.values) {
                if (!value.equals("")) {
                    value += ", ";
                }
                value += String.valueOf(v);
            }
            sensorValue.setText(value);
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {
            // Not implemented
        }
    }

}
