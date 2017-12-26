package mc.usi.org.mobilecomputingproject.Views.Splash;

import android.content.ContextWrapper;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.pixplicity.easyprefs.library.Prefs;

import io.realm.Realm;
import mc.usi.org.mobilecomputingproject.Utils.Singleton;
import mc.usi.org.mobilecomputingproject.Views.Login.LoginActivity;
import mc.usi.org.mobilecomputingproject.Views.TabBar.TabBarActivity;

/**
 * Created by Lucas on 20.12.17.
 */

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        new Prefs.Builder()
                .setContext(this)
                .setMode(ContextWrapper.MODE_PRIVATE)
                .setPrefsName(this.getPackageName())
                .setUseDefaultSharedPreference(true)
                .build();

        Realm.init(this);
        Realm.deleteRealm(Realm.getDefaultConfiguration());

        Intent intent = new Intent();

        String token = Prefs.getString("token", "_");
        if (token.equals("_")) {
            intent.setClass(this, LoginActivity.class);
        } else {
            intent.setClass(this, TabBarActivity.class);
        }

        startActivity(intent);
        finish();
    }
}