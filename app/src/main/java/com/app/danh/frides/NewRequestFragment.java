package com.app.danh.frides;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.location.places.ui.PlacePicker;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;

/**
 * Created by Phuong on 4/30/2016.
 */
public class NewRequestFragment extends Fragment implements View.OnClickListener, DatePickerDialog.OnDateSetListener, View.OnFocusChangeListener, TimePickerDialog.OnTimeSetListener {
    EditText newRequestTitle;
    EditText newRequestSelectDateText;
    EditText newRequestSelectTimeText;
    EditText newRequestContactInfo;
    Button newRequestSelectLocationButton;
    Button postNewRequest;
    CheckBox onlyEmail;


    String locationLatLong;

    private DatePickerDialog datePickerDialog;
    private TimePickerDialog timePickerDialog;

    private Calendar calendar;
    AccountFragment.OnFragmentListener fragmentListener;

    MyAsyncTask myAsyncTask;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.rider_new_request, container, false);
        newRequestSelectDateText = (EditText) view.findViewById(R.id.newRequestSelectDateText);
        newRequestSelectTimeText = (EditText) view.findViewById(R.id.newRequestSelectTimeText);
        newRequestSelectLocationButton = (Button) view.findViewById(R.id.newRequestOpenMapButton);
        postNewRequest = (Button) view.findViewById(R.id.postNewRequest);
        newRequestTitle = (EditText) view.findViewById(R.id.titleEditTxt);
        onlyEmail = (CheckBox) view.findViewById(R.id.onlyEmail);
        newRequestContactInfo = (EditText) view.findViewById(R.id.newRequestContactInfo);
        locationLatLong = "";

        onlyEmail.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (onlyEmail.isChecked()) {
                    newRequestContactInfo.setEnabled(false);
                } else {
                    newRequestContactInfo.setEnabled(true);
                }
            }
        });

        newRequestSelectDateText.setOnFocusChangeListener(this);
        newRequestSelectTimeText.setOnFocusChangeListener(this);
        newRequestSelectDateText.setOnClickListener(this);
        newRequestSelectTimeText.setOnClickListener(this);
        newRequestSelectLocationButton.setOnClickListener(this);
        postNewRequest.setOnClickListener(this);

        calendar = Calendar.getInstance();
        resetForm();

        return view;
    }

    PlacePicker.IntentBuilder intentBuilder;
    Intent ggMapIntent;

    @Override
    public void onClick(View v) {
        if (v.getId() == newRequestSelectDateText.getId()) {
            calendar = Calendar.getInstance();
            datePickerDialog = new DatePickerDialog(getContext(), this, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
            datePickerDialog.show();
        } else if (v.getId() == newRequestSelectTimeText.getId()) {
            calendar = Calendar.getInstance();
            timePickerDialog = new TimePickerDialog(getContext(), this, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), false);
            timePickerDialog.show();
        } else if (v.getId() == newRequestSelectLocationButton.getId()) {
            fragmentListener.onButtonClicked(null);
        } else if (v.getId() == postNewRequest.getId()) {
            if (newRequestTitle.getText().toString().isEmpty()) {
                popToast("Please enter the title!");
                return;
            }
            if (newRequestSelectDateText.getText().toString().isEmpty()) {
                popToast("Please enter the date!");
                return;
            }
            if (newRequestSelectTimeText.getText().toString().isEmpty()) {
                popToast("Please enter the time!");
                return;
            }
            if (newRequestContactInfo.getText().toString().isEmpty() && onlyEmail.isChecked() == false) {
                popToast("Please enter Contact Info!");
                return;
            }
            if (locationLatLong.isEmpty()) {
                popToast("Please enter location");
                return;
            }
            JSONObject postData = new JSONObject();
            try {
                postData.put("title", newRequestTitle.getText().toString());
                postData.put("date", newRequestSelectDateText.getText().toString());
                postData.put("time", newRequestSelectTimeText.getText().toString());
                if (onlyEmail.isChecked() == false) {
                    postData.put("contact info", newRequestContactInfo.getText().toString());
                    postData.put("only email", "false");
                } else {
                    postData.put("only email", "true");
                    postData.put("contact info", "");
                }
                postData.put("location", locationLatLong);

            } catch (JSONException e) {
                e.printStackTrace();
            }
            fragmentListener.onButtonClicked(postData);
        }
    }

    @Override
    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
        newRequestSelectDateText.setText(year + "-" + (monthOfYear + 1) + "-" + dayOfMonth);
    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

        if (minute < 10) {
            newRequestSelectTimeText.setText(hourOfDay + ":0" + minute);
        } else {
            newRequestSelectTimeText.setText(hourOfDay + ":" + minute);
        }
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        if (v.getId() == newRequestSelectDateText.getId() && hasFocus) {
            calendar = Calendar.getInstance();
            datePickerDialog = new DatePickerDialog(getContext(), this, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
            datePickerDialog.show();
        } else if (v.getId() == newRequestSelectTimeText.getId() && hasFocus) {
            calendar = Calendar.getInstance();
            timePickerDialog = new TimePickerDialog(getContext(), this, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), false);
            timePickerDialog.show();
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof AccountFragment.OnFragmentListener) {
            fragmentListener = (AccountFragment.OnFragmentListener) context;
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

    public void resetForm() {
        newRequestContactInfo.setText("");
        newRequestTitle.setText("");
        onlyEmail.setChecked(false);
        newRequestSelectDateText.setText("");
        newRequestSelectTimeText.setText("");
    }

    public void popToast(String str) {
        Context context = this.getContext();//getApplicationContext();
        int duration = Toast.LENGTH_SHORT;
        Toast toast = Toast.makeText(context, str, duration);
        toast.show();
    }

    public String getLocationLatLong() {
        return locationLatLong;
    }

    public void setLocationLatLong(String locationLatLong) {
        this.locationLatLong = locationLatLong;
    }

}