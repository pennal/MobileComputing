package mc.usi.org.mobilecomputingproject.Views.Login;

import android.content.ContextWrapper;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.pixplicity.easyprefs.library.Prefs;

import mc.usi.org.mobilecomputingproject.R;
import mc.usi.org.mobilecomputingproject.Views.Base.SingleFragmentActivity;

public class LoginActivity extends SingleFragmentActivity {

    @Override
    protected Fragment createFragment() {
        return LoginFragment.newInstance();
    }
}
