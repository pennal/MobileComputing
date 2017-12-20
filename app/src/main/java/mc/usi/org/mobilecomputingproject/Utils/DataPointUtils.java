package mc.usi.org.mobilecomputingproject.Utils;

import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.SphericalUtil;

import java.util.ArrayList;
import java.util.List;

import mc.usi.org.mobilecomputingproject.Models.DataPoint;

/**
 * Created by Lucas on 20.12.17.
 */

public class DataPointUtils {

    public static double getDistanceInKm(List<DataPoint> dataPoints) {
        return DataPointUtils.getDistanceInMeters(dataPoints) / 1000.0;
    }

    public static double getDistanceInMeters(List<DataPoint> dataPoints) {
        double distance = 0;

        for (int i = 0; i < dataPoints.size() - 1; i++) {
            DataPoint current = dataPoints.get(i);
            DataPoint next = dataPoints.get(i + 1);

            LatLng currentPoint = new LatLng(current.getLatitude(), current.getLongitude());
            LatLng nextPoint = new LatLng(next.getLatitude(), next.getLongitude());

            distance += SphericalUtil.computeDistanceBetween(currentPoint, nextPoint);
        }

        return distance;
    }

    public static double getAverageSpeedInMetersPerSecond(List<DataPoint> dataPointList) {
        List<Double> speeds = new ArrayList<>();

        for (int i = 0; i < dataPointList.size() - 1; i++) {
            DataPoint current = dataPointList.get(i);
            DataPoint next = dataPointList.get(i + 1);

            LatLng currentPoint = new LatLng(current.getLatitude(), current.getLongitude());
            LatLng nextPoint = new LatLng(next.getLatitude(), next.getLongitude());

            double distance = SphericalUtil.computeDistanceBetween(currentPoint, nextPoint);
            double time = next.getTimeStamp().getTime() - current.getTimeStamp().getTime();

            speeds.add(distance/time);
        }

        double totalSpeed = 0;

        for (Double s: speeds) {
            totalSpeed += s;
        }

        return totalSpeed/speeds.size();
    }

    public static double getAverageSpeedInKmPerHour(List<DataPoint> dataPoints) {
        double ms = DataPointUtils.getAverageSpeedInMetersPerSecond(dataPoints);
        return ms * 18.0 / 5.0;
    }

    public static double getMaximumLeanAngle(List<DataPoint> dataPoints) {
        double max = 0;

        for (DataPoint d: dataPoints) {
            if (d.getGyroZ() > max) {
                max = d.getGyroZ();
            }
        }

        return max;
    }

    public static double getAverageLeanAngle(List<DataPoint> dataPoints) {
        List<Double> angles = new ArrayList<>();

        for (DataPoint d: dataPoints) {

            double val = Math.abs(d.getGyroZ());

            angles.add(val);
        }

        double totalAngle = 0;

        for (Double a: angles) {
            totalAngle += a;
        }

        return totalAngle/angles.size();
    }

    public static double getMaximumSpeedInMetersPerSecond(List<DataPoint> dataPointList) {
        double maxSpeed = 0;

        for (int i = 0; i < dataPointList.size() - 1; i++) {
            DataPoint current = dataPointList.get(i);
            DataPoint next = dataPointList.get(i + 1);

            LatLng currentPoint = new LatLng(current.getLatitude(), current.getLongitude());
            LatLng nextPoint = new LatLng(next.getLatitude(), next.getLongitude());

            double distance = SphericalUtil.computeDistanceBetween(currentPoint, nextPoint);
            double time = next.getTimeStamp().getTime() - current.getTimeStamp().getTime();
            double speed = distance/time;
            if (speed >= maxSpeed) {
                maxSpeed = speed;
            }
        }

        return maxSpeed;
    }

    public static double getMaximumSpeedInKmPerHour(List<DataPoint> dataPointList) {
        double ms = getMaximumSpeedInMetersPerSecond(dataPointList);
        return ms * 18.0 / 5.0;
    }
}
