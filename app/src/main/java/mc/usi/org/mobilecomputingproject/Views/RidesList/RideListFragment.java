package mc.usi.org.mobilecomputingproject.Views.RidesList;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import mc.usi.org.mobilecomputingproject.Models.DataPoint;
import mc.usi.org.mobilecomputingproject.Models.Ride;
import mc.usi.org.mobilecomputingproject.R;
import mc.usi.org.mobilecomputingproject.REST.API;
import mc.usi.org.mobilecomputingproject.Utils.DataPointUtils;
import mc.usi.org.mobilecomputingproject.Utils.FailableCallback;
import mc.usi.org.mobilecomputingproject.Utils.NumberUtils;
import mc.usi.org.mobilecomputingproject.Utils.Singleton;
import mc.usi.org.mobilecomputingproject.Utils.TimeUtils;
import mc.usi.org.mobilecomputingproject.Views.RideDetail.RideDetailActivity;
import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by Lucas on 03.11.17.
 */

public class RideListFragment extends Fragment {

    public static final String TAG = "RideListFragment";
    public static final String ARG_CONTENT = "RideType";




    public enum RideType {
        PERSONAL,
        PUBLIC
    }

    public List<Ride> mRideList;
    public RideType mRideType;

    private RecyclerView mRecyclerView;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private TextView mEmptyContestListPlaceholder;


    public RideListFragment() {}

    public static RideListFragment newInstance(RideType rideType) {
        RideListFragment frag = new RideListFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_CONTENT, rideType);
        frag.setArguments(args);
        return frag;
    }



    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState == null) {
            Bundle args = getArguments();
            mRideType = (RideType) args.get(ARG_CONTENT);
            Log.d(TAG, "Fetching from args, current type: " + mRideType);
        } else {
            mRideType = RideType.PUBLIC;
            Log.d(TAG, "Setting to default");
        }

        updateRideList(false);
    }

    private void updateRideList(final boolean shouldStopRefreshAnimation) {
        API.getRides(mRideType, new FailableCallback<List<Ride>>(getActivity()) {
            @Override
            public void onResponse(Call<List<Ride>> call, Response<List<Ride>> response) {
                if (shouldStopRefreshAnimation && mSwipeRefreshLayout != null) {
                    mSwipeRefreshLayout.setRefreshing(false);
                }
                if (response.code() == 200) {
                    List<Ride> rides = response.body();
                    if (rides == null || rides.isEmpty()) {
                        mRideList = new ArrayList<>();
                        setupView(false);
                    } else {
                        mRideList = rides;
                        setupView(true);
                        setupAdapter();
                    }
                }
            }
        });
    }

    private void setupView(boolean hasList) {
        if (hasList) {
            mEmptyContestListPlaceholder.setVisibility(View.INVISIBLE);
            mRecyclerView.setVisibility(View.VISIBLE);
        } else {
            mEmptyContestListPlaceholder.setVisibility(View.VISIBLE);
            mRecyclerView.setVisibility(View.INVISIBLE);
            mEmptyContestListPlaceholder.setText("No rides found");
        }
    }
    private void setupAdapter() {
        if (isAdded()) {
            Log.i(TAG, "Setting up adapter");
            // Depending on the status, either show the c list or the grid view

            mRecyclerView.setAdapter(new RideListAdapter(mRideList));

        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {



        View view = inflater.inflate(R.layout.fragment_rides_list, container, false);

        this.mRecyclerView = view.findViewById(R.id.ride_list_recycler_view);

        setLayoutManager();

        mSwipeRefreshLayout = view.findViewById(R.id.swipe_refresh_layout);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                updateRideList(true);
            }
        });

        mEmptyContestListPlaceholder = view.findViewById(R.id.empty_contest_list_placeholder);


        Log.d(TAG, "Loaded up Ride List");


        return view;
    }

    private class RideListHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        // Properties of the cell
        private TextView mDateTextView;
        private TextView mTimeTextView;
        private TextView mSpeedTextView;
        private TextView mLeanAngleTextView;



        public RideListHolder(View itemView) {
            super(itemView);

            itemView.setOnClickListener(this);

            // Find all elements on the cell of the table (labels, etc)
            this.mDateTextView = itemView.findViewById(R.id.date_list_label);
            this.mTimeTextView= itemView.findViewById(R.id.time_list_label);
            this.mSpeedTextView = itemView.findViewById(R.id.speed_list_label);
            this.mLeanAngleTextView = itemView.findViewById(R.id.lean_list_label);
        }

        public void bindElements(Ride r) {
            this.mDateTextView.setText(mc.usi.org.mobilecomputingproject.Utils.DateUtils.getFormattedDate(r.getEndDate()));
            this.mTimeTextView.setText(TimeUtils.getFormattedDuration(r.getStartDate(), r.getEndDate()));



            this.mSpeedTextView.setText(NumberUtils.twoDecimalValues(DataPointUtils.getAverageSpeedInKmPerHour(r.getDataPoints())) + " Km/h");
            this.mLeanAngleTextView.setText(NumberUtils.oneDecimalValue(DataPointUtils.getMaximumLeanAngle(r.getDataPoints())) + "°");
        }

        @Override
        public void onClick(View view) {
            int position = getAdapterPosition();
            Singleton.getInstance().setRide(mRideList.get(position));

            Intent intent = new Intent();
            intent.setClass(getActivity(), RideDetailActivity.class);
            startActivity(intent);
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
            holder.bindElements(mRideList.get(position));
        }

        @Override
        public int getItemCount() {
            return mRideList.size();
        }
    }

    public void setLayoutManager() {
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
    }
}
