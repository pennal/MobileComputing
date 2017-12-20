package mc.usi.org.mobilecomputingproject.Utils;

import java.text.DecimalFormat;

/**
 * Created by Lucas on 20.12.17.
 */

public class NumberUtils {
    public static String twoDecimalValues(Double number) {
        DecimalFormat distanceFormat = new DecimalFormat("0.00");

        return distanceFormat.format(number);
    }

    public static String oneDecimalValue(Double number) {
        DecimalFormat distanceFormat = new DecimalFormat("0.0");

        return distanceFormat.format(number);
    }

    public static String zeroDecimalValues(Double number) {
        return Math.floor(number) + "";
    }


}
