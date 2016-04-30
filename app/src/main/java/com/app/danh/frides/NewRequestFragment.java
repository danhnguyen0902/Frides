package com.app.danh.frides;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.ui.PlacePicker;

import java.util.Calendar;
/**
 * Created by Phuong on 4/30/2016.
 */
public class NewRequestFragment extends Fragment implements View.OnClickListener, DatePickerDialog.OnDateSetListener, View.OnFocusChangeListener, TimePickerDialog.OnTimeSetListener {
    EditText newRequestSelectDateText;
    EditText newRequestSelectTimeText;
    Button newRequestSelectLocationButton;

    private DatePickerDialog datePickerDialog;
    private TimePickerDialog timePickerDialog;

    private Calendar calendar;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.rider_new_request, container, false);
        newRequestSelectDateText = (EditText) view.findViewById(R.id.newRequestSelectDateText);
        newRequestSelectTimeText = (EditText) view.findViewById(R.id.newRequestSelectTimeText);
        newRequestSelectLocationButton = (Button) view.findViewById(R.id.newRequestOpenMapButton);

        newRequestSelectDateText.setOnFocusChangeListener(this);
        newRequestSelectTimeText.setOnFocusChangeListener(this);
        newRequestSelectDateText.setOnClickListener(this);
        newRequestSelectTimeText.setOnClickListener(this);
        newRequestSelectLocationButton.setOnClickListener(this);

        calendar = Calendar.getInstance();
        newRequestSelectDateText.setText(calendar.get(Calendar.MONTH) + "-" + calendar.get(Calendar.DAY_OF_MONTH) + "-" + calendar.get(Calendar.YEAR));
        String am_pm = "am";
        if(calendar.get(Calendar.AM_PM) == 1) {
            am_pm = "pm";
        }
        newRequestSelectTimeText.setText(calendar.get(Calendar.HOUR) + ":" + calendar.get(Calendar.MINUTE) + " " + am_pm);
        return view;
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == newRequestSelectDateText.getId()) {
            calendar = Calendar.getInstance();
            datePickerDialog = new DatePickerDialog(getContext(), this, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
            datePickerDialog.show();
        }
        else if(v.getId() == newRequestSelectTimeText.getId()) {
            calendar = Calendar.getInstance();
            timePickerDialog = new TimePickerDialog(getContext(), this, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), false);
            timePickerDialog.show();
        }
        else if(v.getId() == newRequestSelectLocationButton.getId()) {
            try {
                PlacePicker.IntentBuilder intentBuilder = new PlacePicker.IntentBuilder();
                startActivityForResult(intentBuilder.build(getActivity()), ((RiderActivity) getActivity()).PLACE_PICKER_REQUEST);
            } catch (GooglePlayServicesRepairableException e) {
                e.printStackTrace();
            } catch (GooglePlayServicesNotAvailableException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
        newRequestSelectDateText.setText(monthOfYear + "-" + dayOfMonth + "-" + year);
    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        String am_pm = "am";
        if(hourOfDay > 12) {
            hourOfDay-=12;
            am_pm = "pm";
        }
        newRequestSelectTimeText.setText(hourOfDay + ":" + minute + " " + am_pm);
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        if(v.getId() == newRequestSelectDateText.getId() && hasFocus) {
            calendar = Calendar.getInstance();
            datePickerDialog = new DatePickerDialog(getContext(), this, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
            datePickerDialog.show();
        }
        else if(v.getId() == newRequestSelectTimeText.getId() && hasFocus) {
            calendar = Calendar.getInstance();
            timePickerDialog = new TimePickerDialog(getContext(), this, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), false);
            timePickerDialog.show();
        }
    }
}