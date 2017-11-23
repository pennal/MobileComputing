package mc.usi.org.mobilecomputingproject.Views.TabBar;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import mc.usi.org.mobilecomputingproject.R;
import mc.usi.org.mobilecomputingproject.Views.Profile.ProfileFragment;
import mc.usi.org.mobilecomputingproject.Views.Record.RecordFragment;
import mc.usi.org.mobilecomputingproject.Views.RidesList.RideListFragment;

public class TabBarActivity extends AppCompatActivity {

    private static final String TAG = "TabBarActivity";

    private BottomNavigationView mBottomNavigationView;
    private int mSelectedItem;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_tab_bar);

        mBottomNavigationView = (BottomNavigationView) findViewById(R.id.navigation);

        mBottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                selectFragment(item);
                return true;
            }
        });

        // Currently, rotation is not handled
        // We set the item to be the first one
        MenuItem selectedItem = mBottomNavigationView.getMenu().getItem(1);
        selectFragment(selectedItem);
    }


    private void selectFragment(MenuItem item) {
        Fragment fragment = null;

        int selectedTab = item.getItemId();
        MenuItem selectedItem = item;

        switch (selectedTab) {
            case R.id.navigation_profile:
                fragment = ProfileFragment.newInstance();
                break;
            case R.id.navigation_record:
                fragment = RecordFragment.newInstance();
                break;
            case R.id.navigation_routes:
                fragment = RideListFragment.newInstance();
                break;
        }

        int oldSelected = mSelectedItem;
        mSelectedItem = selectedItem.getItemId();

        if (oldSelected == mSelectedItem) {
            Log.d(TAG, "Not switching tabs as the currently selected has been reselected");
            return;

        }

        for (int i = 0; i < mBottomNavigationView.getMenu().size(); i++) {
            MenuItem menuItem = mBottomNavigationView.getMenu().getItem(i);

            Log.i(TAG, "menuItem: " + menuItem.getItemId() + "; item: " + selectedItem.getItemId());

            if (menuItem.getItemId() == selectedItem.getItemId()) {
                Log.d(TAG, "Setting selected view to index " + i);
                mSelectedItem = menuItem.getItemId();
                menuItem.setChecked(true);
                break;
            } else {
                menuItem.setChecked(false);

            }


        }

        if (fragment != null) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.container, fragment, fragment.getTag());
            ft.commit();
        }


    }


    @Override
    public void onBackPressed() {
        Log.e(TAG, "YOU SHALL NOT PASS!");
    }
}

