package mc.usi.org.mobilecomputingproject.Views.RideDetail;


import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.maps.android.SphericalUtil;

import org.w3c.dom.Text;

import java.text.DecimalFormat;

import mc.usi.org.mobilecomputingproject.Models.DataPoint;
import mc.usi.org.mobilecomputingproject.Models.Ride;
import mc.usi.org.mobilecomputingproject.R;
import mc.usi.org.mobilecomputingproject.REST.API;
import mc.usi.org.mobilecomputingproject.Utils.DataPointUtils;
import mc.usi.org.mobilecomputingproject.Utils.DateUtils;
import mc.usi.org.mobilecomputingproject.Utils.DialogUtils;
import mc.usi.org.mobilecomputingproject.Utils.FailableCallback;
import mc.usi.org.mobilecomputingproject.Utils.NumberUtils;
import mc.usi.org.mobilecomputingproject.Utils.Singleton;
import mc.usi.org.mobilecomputingproject.Utils.TimeUtils;
import retrofit2.Call;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class RideDetailFragment extends Fragment {

    private static final String MAPVIEW_BUNDLE_KEY = "MapViewBundleKey";
    private static final String TAG = "RideDetail";

    private MapView mMapView;

    private TextView mDateTextView;
    private TextView mDurationTextView;
    private TextView mMaxSpeedTextView;
    private TextView mAvgSpeedTextView;
    private TextView mMaxLeanAngleTextView;
    private TextView mAvgLeanAngleTextView;
    private TextView mGpsPointsTextView;
    private TextView mDistanceTextView;

    private Ride mRide;

    private Button mMakePublicButton;

    public RideDetailFragment() {
        // Required empty public constructor
    }

    public static Fragment newInstance() {
        return new RideDetailFragment();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_ride_detail, container, false);

        Bundle mapViewBundle = null;
        if (savedInstanceState != null) {
            mapViewBundle = savedInstanceState.getBundle(MAPVIEW_BUNDLE_KEY);
        }

        this.mMapView = v.findViewById(R.id.map_view);

        this.mDistanceTextView = v.findViewById(R.id.distance_detail_label);
        this.mDateTextView = v.findViewById(R.id.date_detail_label);
        this.mDurationTextView = v.findViewById(R.id.duration_detail_label);
        this.mMaxSpeedTextView = v.findViewById(R.id.max_speed_detail_label);
        this.mAvgSpeedTextView = v.findViewById(R.id.avg_speed_detail_label);
        this.mMaxLeanAngleTextView = v.findViewById(R.id.max_lean_angle_detail_label);
        this.mAvgLeanAngleTextView = v.findViewById(R.id.avg_lean_angle_label);
        this.mGpsPointsTextView = v.findViewById(R.id.gps_points_detail_label);


        mMapView.onCreate(mapViewBundle);

        mRide = Singleton.getInstance().getRide();
        Singleton.getInstance().setRide(null);

        this.mMapView.getMapAsync(googleMap -> {

            googleMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);


            int rideLength = mRide.getDataPoints().size();


            PolylineOptions polylineOptions = new PolylineOptions().color(Color.RED).width(10);
            LatLngBounds.Builder boundsBuilder = new LatLngBounds.Builder();

            double maxAngle = 0;

            boolean hasIncludedPoints = false;

            for (int i = 0; i < mRide.getDataPoints().size(); i++) {
                DataPoint p = mRide.getDataPoints().get(i);

                LatLng point = new LatLng(p.getLatitude(), p.getLongitude());

                if (i == (rideLength - 1)) {
                    googleMap.addMarker(new MarkerOptions()
                            .position(point)
                            .title("End"));
                }

                if (i == 0) {
                    googleMap.addMarker(new MarkerOptions()
                            .position(point)
                            .title("Start"));
                }


                if (p.getGyroZ() >= maxAngle) {
                    maxAngle = p.getGyroZ();
                }

                polylineOptions.add(point);
                boundsBuilder.include(point);
                hasIncludedPoints = true;

            }

            googleMap.addPolyline(polylineOptions);




            this.mDistanceTextView.setText(NumberUtils.twoDecimalValues(DataPointUtils.getDistanceInKm(mRide.getDataPoints())) + " Km");
            this.mDateTextView.setText(DateUtils.getFormattedDate(mRide.getEndDate()));
            this.mDurationTextView.setText(TimeUtils.getFormattedDuration(mRide.getStartDate(), mRide.getEndDate()));
            this.mMaxSpeedTextView.setText(NumberUtils.twoDecimalValues(DataPointUtils.getMaximumSpeedInKmPerHour(mRide.getDataPoints())) + " Km/h");
            this.mAvgSpeedTextView.setText(NumberUtils.twoDecimalValues(DataPointUtils.getAverageSpeedInKmPerHour(mRide.getDataPoints())) + " Km/h");
            this.mMaxLeanAngleTextView.setText(NumberUtils.twoDecimalValues(Math.toDegrees(DataPointUtils.getMaximumLeanAngle(mRide.getDataPoints()))) + "°");
            this.mAvgLeanAngleTextView.setText(NumberUtils.twoDecimalValues(Math.toDegrees(DataPointUtils.getAverageLeanAngle(mRide.getDataPoints()))) + "°");
            this.mGpsPointsTextView.setText(mRide.getDataPoints().size() + "");


            boolean finalHasIncludedPoints = hasIncludedPoints;
            googleMap.setOnMapLoadedCallback(() -> {
                //Your code where exception occurs goes here...
                int routePadding = 100;

                if (finalHasIncludedPoints) {
                    LatLngBounds latLngBounds = boundsBuilder.build();

                    googleMap.animateCamera(CameraUpdateFactory.newLatLngBounds(latLngBounds, routePadding));
                }
            });
        });


        this.mMakePublicButton = v.findViewById(R.id.make_route_public_button);
        if (mRide.isPublic()) {
            this.mMakePublicButton.setVisibility(View.INVISIBLE);
        } else {
            this.mMakePublicButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    API.makeRoutePublic(mRide.getId(), new FailableCallback<Void>(getActivity()) {
                        @Override
                        public void onResponse(Call<Void> call, Response<Void> response) {
                            if (response.code() == 200) {
                                mMakePublicButton.setVisibility(View.INVISIBLE);
                            } else {
                                DialogUtils.getDialogWithOkButton(getActivity(), "Failed", "Could not make ride public").show();
                            }
                        }
                    });
                }
            });
        }



        return v;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        Bundle mapViewBundle = outState.getBundle(MAPVIEW_BUNDLE_KEY);
        if (mapViewBundle == null) {
            mapViewBundle = new Bundle();
            outState.putBundle(MAPVIEW_BUNDLE_KEY, mapViewBundle);
        }

        mMapView.onSaveInstanceState(mapViewBundle);
    }

    @Override
    public void onResume() {
        super.onResume();
        mMapView.onResume();
    }

    @Override
    public void onStart() {
        super.onStart();
        mMapView.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
        mMapView.onStop();
    }

    @Override
    public void onPause() {
        mMapView.onPause();
        super.onPause();
    }

    @Override
    public void onDestroy() {
        mMapView.onDestroy();
        super.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mMapView.onLowMemory();
    }

    public double calculateDistanceBetweenPoints(LatLng StartP, LatLng EndP) {
        int Radius = 6371;// radius of earth in Km
        double lat1 = StartP.latitude;
        double lat2 = EndP.latitude;
        double lon1 = StartP.longitude;
        double lon2 = EndP.longitude;
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
                + Math.cos(Math.toRadians(lat1))
                * Math.cos(Math.toRadians(lat2)) * Math.sin(dLon / 2)
                * Math.sin(dLon / 2);
        double c = 2 * Math.asin(Math.sqrt(a));
        double valueResult = Radius * c;

        return valueResult;
    }

}
