package mc.usi.org.mobilecomputingproject.Utils;

import android.hardware.Sensor;
import android.hardware.SensorManager;

import mc.usi.org.mobilecomputingproject.GPS.FallbackLocationTracker;
import mc.usi.org.mobilecomputingproject.Models.Ride;
import mc.usi.org.mobilecomputingproject.REST.responses.UserResponse;

/**
 * Created by Lucas on 17.12.17.
 */

public class Singleton {
    private static Singleton sSingleton;

    public static Singleton getInstance() {
        if (sSingleton == null) {
            sSingleton = new Singleton();
            sSingleton.isRecording = false;
            sSingleton.gyroData = new double[]{0,0,0};
            sSingleton.accData = new double[]{0,0,0};
        }
        return sSingleton;
    }

    private boolean isRecording;
    private String sessionId;
    private Ride currentRide;
    private FallbackLocationTracker tracker;

    private double[] gyroData;
    private double[] accData;

    private SensorManager mSensorManager;
    private Sensor mAccellerometer;
    private Sensor mGyro;

    private Ride mRide;

    private UserResponse mProfile;


    public FallbackLocationTracker getTracker() {
        return tracker;
    }

    public void setTracker(FallbackLocationTracker tracker) {
        this.tracker = tracker;
    }

    public boolean isRecording() {
        return isRecording;
    }

    public void setRecording(boolean recording) {
        isRecording = recording;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public Ride getCurrentRide() {
        return currentRide;
    }

    public void setCurrentRide(Ride currentRide) {
        this.currentRide = currentRide;
    }

    public double[] getGyroData() {
        return gyroData;
    }

    public void setGyroData(double gyroX, double gyroY, double gyroZ) {
        double[] temp = new double[]{gyroX, gyroY, gyroZ};
        this.gyroData = temp;
    }

    public double[] getAccData() {
        return accData;
    }

    public void setAccData(double accX, double accY, double accZ) {
        double[] temp = new double[]{accX, accY, accZ};
        this.accData = temp;
    }

    public SensorManager getSensorManager() {
        return mSensorManager;
    }

    public void setSensorManager(SensorManager sensorManager) {
        mSensorManager = sensorManager;
    }

    public Sensor getAccellerometer() {
        return mAccellerometer;
    }

    public void setAccellerometer(Sensor accellerometer) {
        mAccellerometer = accellerometer;
    }

    public Sensor getGyro() {
        return mGyro;
    }

    public void setGyro(Sensor gyro) {
        mGyro = gyro;
    }

    public Ride getRide() {
        return mRide;
    }

    public void setRide(Ride ride) {
        mRide = ride;
    }

    public UserResponse getProfile() {
        return mProfile;
    }

    public void setProfile(UserResponse profile) {
        mProfile = profile;
    }
}
