package mc.usi.org.mobilecomputingproject.Views.Record;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.support.v4.app.Fragment;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.google.maps.android.SphericalUtil;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.logging.SimpleFormatter;

import io.realm.Realm;
import io.realm.RealmResults;
import mc.usi.org.mobilecomputingproject.GPS.FallbackLocationTracker;
import mc.usi.org.mobilecomputingproject.GPS.LocationTracker;
import mc.usi.org.mobilecomputingproject.Models.DataPoint;
import mc.usi.org.mobilecomputingproject.Models.Ride;
import mc.usi.org.mobilecomputingproject.R;
import mc.usi.org.mobilecomputingproject.REST.API;
import mc.usi.org.mobilecomputingproject.REST.responses.UserResponse;
import mc.usi.org.mobilecomputingproject.Utils.DialogUtils;
import mc.usi.org.mobilecomputingproject.Utils.FailableCallback;
import mc.usi.org.mobilecomputingproject.Utils.Singleton;
import mc.usi.org.mobilecomputingproject.Utils.TimeUtils;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Lucas on 03.11.17.
 */

public class RecordFragment extends Fragment implements SensorEventListener {

    public static final String TAG = "RecordFragment";

    public static final int ACC_SENSIBILITY = SensorManager.SENSOR_DELAY_UI;

    private TextView mPositionLabel;
    private TextView mAngleLabel;
    private TextView mSpeedLabel;
    private TextView mTimeLabel;

    private Button mRecordButton;

    public RecordFragment() {}

    public static RecordFragment newInstance() {
        RecordFragment recordFragment = new RecordFragment();
        return recordFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_record, container, false);

        this.mPositionLabel = view.findViewById(R.id.position_label);
        this.mAngleLabel = view.findViewById(R.id.angle_label);
        this.mSpeedLabel = view.findViewById(R.id.speed_label);
        this.mTimeLabel = view.findViewById(R.id.time_label);

        this.mPositionLabel.setText("-,-");
        this.mAngleLabel.setText("-°");
        this.mSpeedLabel.setText("- m/s");
        this.mTimeLabel.setText("-:-:-");

        SensorManager sensorManager = (SensorManager) getContext().getSystemService(Context.SENSOR_SERVICE);
        Sensor senAccelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        Sensor senGyroscope = sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);


        Singleton.getInstance().setSensorManager(sensorManager);
        Singleton.getInstance().setAccellerometer(senAccelerometer);
        Singleton.getInstance().setAccellerometer(senGyroscope);

        sensorManager.registerListener(this, senAccelerometer, ACC_SENSIBILITY);
        sensorManager.registerListener(this, senGyroscope, ACC_SENSIBILITY);

        this.mRecordButton = view.findViewById(R.id.record_button);

        if (Singleton.getInstance().isRecording()) {
            mRecordButton.setText("Stop Recording");
            mRecordButton.setBackgroundColor(Color.RED);
        } else {
            mRecordButton.setText("Start Recording");
            mRecordButton.setBackgroundColor(Color.GREEN);
        }

        API.getUserInfo(new FailableCallback<UserResponse>(getActivity()) {
            @Override
            public void onResponse(Call<UserResponse> call, Response<UserResponse> response) {
                if (response.code() == 200) {
                    Singleton.getInstance().setProfile(response.body());
                } else {
                    DialogUtils.getDialogWithOkButton(getActivity(), "Something went wrong", "Could not retrieve the profile").show();
                }
            }
        });


        this.mRecordButton.setOnClickListener(view1 -> {
            boolean isRecording = Singleton.getInstance().isRecording();


            if (isRecording) {
                Singleton.getInstance().getTracker().stop();
                Singleton.getInstance().setTracker(null);
                Singleton.getInstance().setRecording(false);


                mRecordButton.setText("Start Recording");

                // Fetch all the elements, and push them to the current ride
                String sessionId = Singleton.getInstance().getSessionId();
                Ride currentRide = Singleton.getInstance().getCurrentRide();

                RealmResults<DataPoint> dataPointRealmQuery = Realm.getDefaultInstance().where(DataPoint.class).equalTo("sessionId", sessionId).findAll();



                // DO NOT TOUCH THIS. Retrofit and realm do not play nice together...
                // All elements need to be recreated and copied
                List<DataPoint> points = new ArrayList<>();
                for (DataPoint dataPoint : dataPointRealmQuery) {
                    DataPoint a = DataPoint.fromRealmQueryElement(dataPoint);
                    points.add(a);
                }
                dataPointRealmQuery = null;


                currentRide.setDataPoints(points);
                currentRide.setEndDate(new Date());

                ProgressDialog p = DialogUtils.getIndefiniteProgressDialog(getActivity(), "Uploading data", "This might take a while...");
                p.show();

                API.postRide(currentRide, new FailableCallback<Void>(getActivity()) {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {
                        if (response.code() == 200) {
                            Log.d(TAG, "Data sent correctly!");
                            Singleton.getInstance().setSessionId(null);
                            Singleton.getInstance().setCurrentRide(null);
                        } else {
                            Log.e(TAG, "DATA SEND FAILED");
                            DialogUtils.getDialogWithOkButton(getActivity(), "Could not save route", "Looks like something went wrong...check your internet connection").show();
                        }
                        p.dismiss();
                }});


            } else {
                // Depending on the action, choose the needed permissions
                List<String> permissions = new ArrayList<>();
                permissions.add(Manifest.permission.ACCESS_FINE_LOCATION);

                String[] finalPermissions = permissions.toArray(new String[0]);

                Dexter.withActivity(getActivity())
                        .withPermissions(finalPermissions)
                        .withListener(new MultiplePermissionsListener() {
                            @Override public void onPermissionsChecked(MultiplePermissionsReport report) {
                                if (report.areAllPermissionsGranted()) {
                                    if (report.areAllPermissionsGranted()) {
                                        // Starting recording
                                        startTracking();
                                    } else {
                                        Log.e(TAG, "NOT ALL PERMISSIONS WERE GRANTED");
                                    }
                                }
                            }
                            @Override public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                                token.continuePermissionRequest();
                            }
                        }).onSameThread().check();
            }
        });


        Log.d(TAG, "Loaded up record fragment");


        return view;
    }

    public void startTracking() {

        getActivity().getWindow().addFlags(WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD);
        FallbackLocationTracker tracker = new FallbackLocationTracker(getActivity());


        Ride currentRide = new Ride();
        currentRide.setStartDate(new Date());

        Singleton.getInstance().setRecording(true);
        Singleton.getInstance().setSessionId(UUID.randomUUID().toString());
        Singleton.getInstance().setCurrentRide(currentRide);
        Singleton.getInstance().setTracker(tracker);

        mRecordButton.setText("Stop Recording");
        mRecordButton.setBackgroundColor(Color.RED);



        tracker.start(new LocationTracker.LocationUpdateListener() {
            @Override
            public void onUpdate(Location oldLoc, long oldTime, Location newLoc, long newTime) {

                String sessionId = Singleton.getInstance().getSessionId();

                double[] accData = Singleton.getInstance().getAccData();
                double[] gyroData = Singleton.getInstance().getGyroData();

                final DataPoint dataPoint = new DataPoint(newLoc.getLatitude(), newLoc.getLongitude(), accData[0], accData[1], accData[2], gyroData[0], gyroData[1], gyroData[2], new Date(), sessionId);

                DecimalFormat positionFormat = new DecimalFormat("##.00");

                mPositionLabel.setText(positionFormat.format(newLoc.getLatitude()) + ", " + positionFormat.format(newLoc.getLongitude()));
                mAngleLabel.setText("-°");

                if (oldLoc != null) {
                    double distance = oldLoc.distanceTo(newLoc);
                    double time = newTime - oldTime;

                    DecimalFormat distanceFormat = new DecimalFormat("0.00");

                    mSpeedLabel.setText(distanceFormat.format(distance / time) + " m/s");

                }

                mTimeLabel.setText(TimeUtils.getFormattedDuration(new Date(newTime), Singleton.getInstance().getCurrentRide().getStartDate()));


                Realm.getDefaultInstance().executeTransaction(new Realm.Transaction() {
                    @Override
                    public void execute(Realm realm) {
                        Log.d(TAG, "Pushing " + dataPoint);
                        realm.copyToRealmOrUpdate(dataPoint);
                    }
                });
            }
        });
    }


    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        Sensor mySensor = sensorEvent.sensor;

        float x = sensorEvent.values[0];
        float y = sensorEvent.values[1];
        float z = sensorEvent.values[2];

        if (mySensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            Singleton.getInstance().setAccData(x, y, z);
        }

        if (mySensor.getType() == Sensor.TYPE_GYROSCOPE) {
            Singleton.getInstance().setGyroData(x, y, z);
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }
}
