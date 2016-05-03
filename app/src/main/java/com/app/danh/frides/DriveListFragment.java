package com.app.danh.frides;

import android.support.annotation.Nullable;
import android.support.v4.app.ListFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Danh on 5/2/2016.
 */
public class DriveListFragment extends ListFragment {
    RideRequestAdapter rideRequestAdapter;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        rideRequestAdapter = new RideRequestAdapter(getContext(), ((DriverActivity) getActivity()).requestsList);
        setListAdapter(rideRequestAdapter);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.rider_ride_list, container, false);
        return view;
    }
}
