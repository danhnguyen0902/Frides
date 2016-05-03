
package com.app.danh.frides;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Phuong on 4/30/2016.
 */
public class RideListFragment extends ListFragment implements AdapterView.OnItemClickListener {
    RideRequestAdapter rideRequestAdapter;
    OnFragmentListener fragmentListener;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        rideRequestAdapter = new RideRequestAdapter(getContext(), ((RiderActivity) getActivity()).riderRequestsList);
        setListAdapter(rideRequestAdapter);
        getListView().setOnItemClickListener(this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.rider_ride_list, container, false);
        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentListener) {
            fragmentListener = (OnFragmentListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        fragmentListener = null;
    }

    @Override
    public void onItemClick(AdapterView<?> adapter, View view, int position, long id) {
//        Toast.makeText(getActivity(), "Item: " + position, Toast.LENGTH_SHORT)
//                .show();

        fragmentListener.onItemClicked(rideRequestAdapter.getItem(position));
    }

    public interface OnFragmentListener {
        void onItemClicked(JSONObject obj);
    }
}