
package com.app.danh.frides;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Phuong on 4/30/2016.
 */
public class RideListFragment extends ListFragment {
    RideRequestAdapter rideRequestAdapter;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        rideRequestAdapter = new RideRequestAdapter(getContext(), ((RiderActivity) getActivity()).riderRequestsList);
        setListAdapter(rideRequestAdapter);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.rider_ride_list, container, false);
        return view;
    }
}