package mc.usi.org.mobilecomputingproject.Views.RideDetail;

import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import mc.usi.org.mobilecomputingproject.Views.Base.SingleFragmentActivity;

public class RideDetailActivity extends SingleFragmentActivity {

    @Override
    protected Fragment createFragment() {
        return RideDetailFragment.newInstance();
    }
}
