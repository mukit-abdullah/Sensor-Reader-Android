package com.example.sensorreader;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;


public class MainActivity extends AppCompatActivity implements SensorEventListener {

    private SensorManager sensorManager;
    private Sensor accelerometer;

    private TextView tvX, tvY, tvZ;

    private Spinner spinnerSensors;
    private TextView tvData;

    private Sensor currentSensor;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tvData = findViewById(R.id.tvData);
        spinnerSensors = findViewById(R.id.spinnerSensors);

        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);

        String[] sensors = {"Accelerometer", "Light Sensor", "Proximity Sensor"};

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, sensors);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerSensors.setAdapter(adapter);

        spinnerSensors.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                // Unregistering the previous sensor after selecting new sensor
                if (currentSensor != null) {
                    sensorManager.unregisterListener(MainActivity.this);
                }

                switch (position) {
                    case 0:
                        currentSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
                        break;
                    case 1:
                        currentSensor = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);
                        break;
                    case 2:
                        currentSensor = sensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY);
                        break;
                }

                if (currentSensor != null) {
                    sensorManager.registerListener(MainActivity.this, currentSensor, SensorManager.SENSOR_DELAY_NORMAL);

                }
                else {
                    tvData.setText("Selected sensor not available on this device");
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }


    @Override
    public void onSensorChanged(SensorEvent event) {

        switch (event.sensor.getType()) {

            case Sensor.TYPE_ACCELEROMETER:
                tvData.setText(String.format("Accelerometer\nX: %.2f\nY: %.2f\nZ: %.2f",event.values[0], event.values[1], event.values[2]));
                break;

            case Sensor.TYPE_LIGHT:
                tvData.setText(String.format("Light Sensor\nLux: %.2f", event.values[0]));
                break;

            case Sensor.TYPE_PROXIMITY:
                tvData.setText(String.format("Proximity Sensor\nDistance: %.2f", event.values[0]));
                break;
        }
    }


    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }
    @Override
    protected void onResume() {
        super.onResume();
        if (currentSensor != null) {
            sensorManager.registerListener(this, currentSensor, SensorManager.SENSOR_DELAY_NORMAL);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        sensorManager.unregisterListener(this);
    }

}