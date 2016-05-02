package com.app.danh.frides;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

/**
 * Created by sjstulga on 5/1/16.
 */
public class RideRequestAdapter extends ArrayAdapter<JSONObject> {
    private final Context context;
    private final List<JSONObject> data;

    public RideRequestAdapter(Context context, List<JSONObject> data) {
        super(context, -1, data);
        this.context = context;
        this.data = data;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.ride_request_list_entry, parent, false);
        TextView rideListTitle = (TextView) rowView.findViewById(R.id.rideListTitle);
        TextView rideListRequester = (TextView) rowView.findViewById(R.id.rideListRequester);
        TextView rideListContactInfo = (TextView) rowView.findViewById(R.id.rideListContactInfo);
        TextView rideListDate = (TextView) rowView.findViewById(R.id.rideListDate);
        TextView rideListTime = (TextView) rowView.findViewById(R.id.rideListTime);
        TextView rideListLat = (TextView) rowView.findViewById(R.id.rideListLat);
        TextView rideListLong = (TextView) rowView.findViewById(R.id.rideListLong);
        TextView rideListAccepted = (TextView) rowView.findViewById(R.id.rideListAccepted);


        try {
            JSONObject rowData = data.get(position).getJSONObject("fields");
            rideListTitle.setText(rowData.getString("title"));
            rideListRequester.setText(rowData.getString("posted_by"));
            rideListContactInfo.setText(rowData.getString("contact_info"));
            rideListDate.setText(rowData.getString("date"));
            rideListTime.setText(rowData.getString("time"));
            rideListLat.setText("" + rowData.getDouble("latitude"));
            rideListLong.setText("" + rowData.getDouble("longitude"));
            if(rowData.getString("accepted_by").equalsIgnoreCase("null")) {
                rideListAccepted.setText("none");
            }
            else {
                rideListAccepted.setText("" + rowData.getString("accepted_by"));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return rowView;
    }
}
