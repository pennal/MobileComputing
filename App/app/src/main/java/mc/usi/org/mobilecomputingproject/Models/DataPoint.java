package mc.usi.org.mobilecomputingproject.Models;

import java.util.Date;
import java.util.UUID;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by Lucas on 19.12.17.
 */

public class DataPoint extends RealmObject {
    @PrimaryKey
    private String id = UUID.randomUUID().toString();

    private double latitude;
    private double longitude;

    double accX;
    double accY;
    double accZ;
    double gyroX;
    double gyroY;
    double gyroZ;

    private Date timeStamp;
    private String sessionId;


    public DataPoint(double latitude, double longitude, double accX, double accY, double accZ, double gyroX, double gyroY, double gyroZ, Date timeStamp, String sessionId) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.timeStamp = timeStamp;
        this.sessionId = sessionId;

        this.accX = accX;
        this.accY = accY;
        this.accZ = accZ;

        this.gyroX = gyroX;
        this.gyroY = gyroY;
        this.gyroZ = gyroZ;
    }

    public static DataPoint fromRealmQueryElement(DataPoint r) {
        return new DataPoint(r.getLatitude(), r.getLongitude(), r.getAccX(), r.getAccY(), r.getAccZ(), r.getGyroX(), r.getGyroY(), r.getGyroZ(), r.getTimeStamp(), r.getSessionId());
    }

    public DataPoint() {

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public Date getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(Date timeStamp) {
        this.timeStamp = timeStamp;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    @Override
    public String toString() {
        return "[" + getSessionId() + "] - @" + getTimeStamp().getTime() + " - (" + getLatitude() + ", " + getLongitude() + ") - ACC[" + accX + ", " + accY + ", " + accZ + "] - GYRO[" + gyroX + ", " + gyroY + ", " + gyroZ + "]";
    }

    public double getAccX() {
        return accX;
    }

    public void setAccX(double accX) {
        this.accX = accX;
    }

    public double getAccY() {
        return accY;
    }

    public void setAccY(double accY) {
        this.accY = accY;
    }

    public double getAccZ() {
        return accZ;
    }

    public void setAccZ(double accZ) {
        this.accZ = accZ;
    }

    public double getGyroX() {
        return gyroX;
    }

    public void setGyroX(double gyroX) {
        this.gyroX = gyroX;
    }

    public double getGyroY() {
        return gyroY;
    }

    public void setGyroY(double gyroY) {
        this.gyroY = gyroY;
    }

    public double getGyroZ() {
        return gyroZ;
    }

    public void setGyroZ(double gyroZ) {
        this.gyroZ = gyroZ;
    }
}
