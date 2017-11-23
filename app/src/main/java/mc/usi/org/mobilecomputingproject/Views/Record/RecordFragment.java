package mc.usi.org.mobilecomputingproject.Views.Record;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.support.v4.app.Fragment;
import android.widget.TextView;

import mc.usi.org.mobilecomputingproject.R;

/**
 * Created by Lucas on 03.11.17.
 */

public class RecordFragment extends Fragment implements SensorEventListener {

    public static final String TAG = "RecordFragment";

    public static final int ACC_SENSIBILITY = SensorManager.SENSOR_DELAY_UI;

    private SensorManager senSensorManager;
    private Sensor senAccelerometer;
    private Sensor senGyro;


    private TextView accellerometerXTextView;
    private TextView accellerometerYTextView;
    private TextView accellerometerZTextView;

    private TextView gyroXTextView;
    private TextView gyroYTextView;
    private TextView gyroZTextView;

    public RecordFragment() {}

    public static RecordFragment newInstance() {
        RecordFragment recordFragment = new RecordFragment();
        return recordFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_record, container, false);

        senSensorManager = (SensorManager) getContext().getSystemService(Context.SENSOR_SERVICE);

        senAccelerometer = senSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        senGyro = senSensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);

        senSensorManager.registerListener(this, senAccelerometer , ACC_SENSIBILITY);
        senSensorManager.registerListener(this, senGyro, SensorManager.SENSOR_DELAY_UI);


        this.accellerometerXTextView = (TextView) view.findViewById(R.id.acc_x_label);
        this.accellerometerYTextView = (TextView) view.findViewById(R.id.acc_y_label);
        this.accellerometerZTextView = (TextView) view.findViewById(R.id.acc_z_label);

        this.gyroXTextView = view.findViewById(R.id.gyro_x_label);
        this.gyroYTextView = view.findViewById(R.id.gyro_y_label);
        this.gyroZTextView = view.findViewById(R.id.gyro_z_label);


        Log.d(TAG, "Loaded up record fragment");


        return view;
    }


    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        Sensor mySensor = sensorEvent.sensor;

        float x = sensorEvent.values[0];
        float y = sensorEvent.values[1];
        float z = sensorEvent.values[2];

        if (mySensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            this.accellerometerXTextView.setText(String.format("X: %s", x));
            this.accellerometerYTextView.setText(String.format("Y: %s", y));
            this.accellerometerZTextView.setText(String.format("Z: %s", z));
        }

        if (mySensor.getType() == Sensor.TYPE_GYROSCOPE) {
            this.gyroXTextView.setText(String.format("X: %s", x));
            this.gyroYTextView.setText(String.format("Y: %s", y));
            this.gyroZTextView.setText(String.format("Z: %s", z));
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }

    @Override
    public void onPause() {
        super.onPause();
        senSensorManager.unregisterListener(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        senSensorManager.registerListener(this, senAccelerometer, ACC_SENSIBILITY);
        senSensorManager.registerListener(this, senGyro, ACC_SENSIBILITY);
    }
}
