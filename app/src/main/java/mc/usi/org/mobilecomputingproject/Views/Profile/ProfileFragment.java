package mc.usi.org.mobilecomputingproject.Views.Profile;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import mc.usi.org.mobilecomputingproject.R;

/**
 * A placeholder fragment containing a simple view.
 */
public class ProfileFragment extends Fragment {

    public final static String TAG = "ProfileFragment";

    public ProfileFragment() {
    }

    public static ProfileFragment newInstance() {
        ProfileFragment profileFragment = new ProfileFragment();
        return profileFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_profile, container, false);


        Log.d(TAG, "Loaded up profile");


        return view;
    }
}
