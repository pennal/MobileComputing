package mc.usi.org.mobilecomputingproject.Utils;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Lucas on 20.12.17.
 */

public class DateUtils {
    public static String getFormattedDate(Date d) {
        if (d == null) {
            return "";
        }

        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
        return format.format(d);
    }
}
