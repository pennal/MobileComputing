package mc.usi.org.mobilecomputingproject.Utils;

import java.text.DecimalFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * Created by Lucas on 20.12.17.
 */

public class TimeUtils {
    public static String getFormattedDuration(Date from, Date to) {
        DecimalFormat format = new DecimalFormat("00");

        long diff = to.getTime() - from.getTime();//as given


        long seconds = modifyValue(TimeUnit.MILLISECONDS.toSeconds(diff), 60);
        long minutes = modifyValue(TimeUnit.MILLISECONDS.toMinutes(diff), 60);
        long hours = modifyValue(TimeUnit.MILLISECONDS.toHours(diff), 24);


        return String.format("%s:%s:%s", format.format(hours), format.format(minutes), format.format(seconds));
    }

    private static long modifyValue(long value, int divisor) {
        return value - divisor * Math.abs(value/divisor);
    }
}
