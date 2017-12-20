package mc.usi.org.mobilecomputingproject.Utils;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

import retrofit2.Call;
import retrofit2.Callback;

/**
 * Created by Lucas on 18.12.17.
 */

public abstract class FailableCallback<T> implements Callback<T> {

    private Context ctx;

    public FailableCallback(Context ctx) {
        this.ctx = ctx;
    }

    @Override
    public void onFailure(Call<T> call, Throwable t) {
        new AlertDialog.Builder(this.ctx)
                .setMessage("Could not complete request. Do you have a network connection?")
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                })
                .create()
                .show();

        t.printStackTrace();
    }
}
