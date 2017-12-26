package mc.usi.org.mobilecomputingproject.Views.PasswordReset;

import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import mc.usi.org.mobilecomputingproject.Views.Base.SingleFragmentActivity;

public class PasswordResetActivity extends SingleFragmentActivity {

    @Override
    protected Fragment createFragment() {
        return PasswordResetFragment.newInstance();
    }
}
