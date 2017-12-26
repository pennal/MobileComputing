package mc.usi.org.mobilecomputingproject.Utils;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;

/**
 * Created by Lucas on 18.12.17.
 */

public class DialogUtils {
    public static ProgressDialog getIndefiniteProgressDialog(Context context, String title, String message) {
        final ProgressDialog progress = new ProgressDialog(context);
        progress.setTitle(title);
        progress.setMessage(message);
        progress.setCancelable(false); // disable dismiss by tapping outside of the dialog

        return progress;
    }


    public static AlertDialog getDialogWithOkButton(Context context, String title, String message) {
        return DialogUtils.getDialogWithOkButton(context, title, message, null);
    }

    public static AlertDialog getDialogWithOkButton(Context context, String title, String message, DialogInterface.OnClickListener listener) {
        return new AlertDialog.Builder(context).setTitle(title).setMessage(message).setPositiveButton("OK", listener).create();
    }

    public static AlertDialog showDialogWithConfirmation(Context context, String title, String message, String confirmButtonText, DialogInterface.OnClickListener positiveClickListener, String cancelButtonText, DialogInterface.OnClickListener cancelClickListener) {
        return new AlertDialog.Builder(context)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton(confirmButtonText,  positiveClickListener)
                .setNegativeButton(cancelButtonText, cancelClickListener)
                .create();
    }
}
