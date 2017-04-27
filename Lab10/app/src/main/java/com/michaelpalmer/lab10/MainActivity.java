package com.michaelpalmer.lab10;

import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import com.androidplot.util.PlotStatistics;
import com.androidplot.xy.BarFormatter;
import com.androidplot.xy.BoundaryMode;
import com.androidplot.xy.LineAndPointFormatter;
import com.androidplot.xy.SimpleXYSeries;
import com.androidplot.xy.XYPlot;

import java.util.Arrays;

public class MainActivity extends AppCompatActivity implements SensorEventListener {

    private XYPlot plotLevels, plotHistory;
    private SimpleXYSeries levelsSeries, historyAzimuthSeries, historyPitchSeries, historyRollSeries;
    private static final int HISTORY_SIZE = 50;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // APR Levels
        plotLevels = (XYPlot) findViewById(R.id.plot_levels);
        plotLevels.setTitle("Levels");
        levelsSeries = new SimpleXYSeries("APR Levels");
        levelsSeries.useImplicitXVals();
        plotLevels.addSeries(levelsSeries, new BarFormatter(Color.argb(100, 0, 200, 0), Color.rgb(0, 80, 0)));
        plotLevels.setDomainStepValue(5);
        plotLevels.setTicksPerRangeLabel(3);
        plotLevels.setRangeBoundaries(-180, 359, BoundaryMode.GROW);
        plotLevels.setDomainLabel("Axis");
        plotLevels.getDomainLabelWidget().pack();
        plotLevels.setRangeLabel("Angle (Degs)");
        plotLevels.getRangeLabelWidget().pack();
        plotLevels.setGridPadding(15, 0, 15, 0);


        // APR History
        plotHistory = (XYPlot) findViewById(R.id.plot_history);
        plotHistory.setTitle("History");
        historyAzimuthSeries = new SimpleXYSeries("Azimuth");
        historyPitchSeries = new SimpleXYSeries("Pitch");
        historyRollSeries = new SimpleXYSeries("Roll");
        historyAzimuthSeries.useImplicitXVals();
        historyPitchSeries.useImplicitXVals();
        historyRollSeries.useImplicitXVals();

        // Create formatters
        LineAndPointFormatter lineAndPointFormatter1 =
                new LineAndPointFormatter(Color.rgb(400, 0, 0), null, null, null);
        LineAndPointFormatter lineAndPointFormatter2 =
                new LineAndPointFormatter(Color.rgb(0, 400, 0), null, null, null);
        LineAndPointFormatter lineAndPointFormatter3 =
                new LineAndPointFormatter(Color.rgb(0, 0, 400), null, null, null);

        // Add series
        plotHistory.addSeries(historyAzimuthSeries, lineAndPointFormatter1);
        plotHistory.addSeries(historyPitchSeries, lineAndPointFormatter2);
        plotHistory.addSeries(historyRollSeries, lineAndPointFormatter3);

        // Configure
        plotHistory.setRangeBoundaries(-180, 359, BoundaryMode.GROW);
        plotHistory.setDomainBoundaries(0, 30, BoundaryMode.GROW);
        plotHistory.setDomainStepValue(5);
        plotHistory.setTicksPerRangeLabel(3);
        plotHistory.setDomainLabel("Sample Index");
        plotHistory.getDomainLabelWidget().pack();
        plotHistory.setRangeLabel("Angle (Degs)");
        plotHistory.getRangeLabelWidget().pack();

        // Create plot statistics
        final PlotStatistics levelStats = new PlotStatistics(1000, false);
        final PlotStatistics historyStats = new PlotStatistics(1000, false);
        plotLevels.addListener(levelStats);
        plotHistory.addListener(historyStats);

        // Hardware acceleration checkbox
        CheckBox hwAccel = (CheckBox) findViewById(R.id.checkbox_hardware_accelerated);
        hwAccel.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    plotLevels.setLayerType(View.LAYER_TYPE_NONE, null);
                    plotHistory.setLayerType(View.LAYER_TYPE_NONE, null);
                } else {
                    plotLevels.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
                    plotHistory.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
                }
            }
        });

        // Show FPS checkbox
        CheckBox showFps = (CheckBox) findViewById(R.id.checkbox_show_fps);
        showFps.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                levelStats.setAnnotatePlotEnabled(isChecked);
                historyStats.setAnnotatePlotEnabled(isChecked);
            }
        });

        // Setup sensor listener
        SensorManager sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        sensorManager.registerListener(
                this,
                sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
                SensorManager.SENSOR_DELAY_UI
        );
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        // Only respond to the accelerometer
        if (event.sensor.getType() != Sensor.TYPE_ACCELEROMETER) {
            return;
        }

        // Get data
        Number[] data = {event.values[0], event.values[1], event.values[2]};

        // Update the levels model
        levelsSeries.setModel(Arrays.asList(data), SimpleXYSeries.ArrayFormat.Y_VALS_ONLY);

        // Clear oldest sample from history
        if (historyRollSeries.size() > HISTORY_SIZE) {
            historyRollSeries.removeFirst();
            historyPitchSeries.removeFirst();
            historyAzimuthSeries.removeFirst();
        }

        // Add sample to history
        historyAzimuthSeries.addLast(null, data[0]);
        historyPitchSeries.addLast(null, data[1]);
        historyRollSeries.addLast(null, data[2]);

        // Redraw
        plotLevels.redraw();
        plotHistory.redraw();
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}
