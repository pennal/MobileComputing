package mc.usi.org.mobilecomputingproject.Views.RidesList;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import mc.usi.org.mobilecomputingproject.Models.Ride;
import mc.usi.org.mobilecomputingproject.R;

/**
 * Created by Lucas on 03.11.17.
 */

public class RideListFragment extends Fragment {

    public static final String TAG = "RideListFragment";

    private RecyclerView mRecyclerView;
    private List<Ride> mRides;


    public RideListFragment() {}

    public static Fragment newInstance() {
        RideListFragment rideListFragment = new RideListFragment();
        return rideListFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        this.mRides = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            this.mRides.add(new Ride());
        }

        View view = inflater.inflate(R.layout.fragment_rides_list, container, false);

        this.mRecyclerView = view.findViewById(R.id.ride_list_recycler_view);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.setAdapter(new RideListAdapter(mRides));

        Log.d(TAG, "Loaded up Ride List");


        return view;
    }

    private class RideListHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        // Properties of the cell



        public RideListHolder(View itemView) {
            super(itemView);

            itemView.setOnClickListener(this);

            // Find all elements on the cell of the table (labels, etc)
        }

        @Override
        public void onClick(View view) {
            Toast.makeText(getActivity(), "Not yet implemented!", Toast.LENGTH_SHORT).show();
        }
    }

    private class RideListAdapter extends RecyclerView.Adapter<RideListHolder> {
        private List<Ride> mRideList;

        public RideListAdapter(List<Ride> rides) {
            mRideList = rides;
        }

        @Override
        public RideListHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            View view = layoutInflater.inflate(R.layout.list_item_ride, parent, false);
            return new RideListHolder(view);
        }

        @Override
        public void onBindViewHolder(RideListHolder holder, int position) {

        }

        @Override
        public int getItemCount() {
            return mRideList.size();
        }
    }
}
