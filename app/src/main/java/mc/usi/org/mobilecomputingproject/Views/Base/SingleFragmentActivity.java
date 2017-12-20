package mc.usi.org.mobilecomputingproject.Views.Base;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;

import mc.usi.org.mobilecomputingproject.R;

/**
 * Created by Lucas on 17.12.17.
 */

public abstract class SingleFragmentActivity extends AppCompatActivity {
    /**
     * Create the fragment related to the activity extending this class
     * @return Fragment to be set in the activity
     */
    protected abstract Fragment createFragment();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment);

        FragmentManager fm = getSupportFragmentManager();
        Fragment fragment = fm.findFragmentById(R.id.fragment_container);

        if (fragment == null) {
            fragment = createFragment();
            fm.beginTransaction().add(R.id.fragment_container, fragment).commit();
        }
    }
}