package com.app.danh.frides;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
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

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.text.SimpleDateFormat;

/**
 * Created by Phuong on 4/30/2016.
 */
public class NewRequestFragment extends Fragment implements View.OnClickListener, DatePickerDialog.OnDateSetListener, View.OnFocusChangeListener, TimePickerDialog.OnTimeSetListener, OnMyAsyncListener {
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

    MyAsyncTask myAsyncTask;

    @Nullable
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
        newRequestSelectDateText.setText(calendar.get(Calendar.MONTH) + "/" + calendar.get(Calendar.DAY_OF_MONTH) + "/" + calendar.get(Calendar.YEAR));
        newRequestSelectTimeText.setText(calendar.get(Calendar.HOUR) + ":" + calendar.get(Calendar.MINUTE) + ":00");
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
            try {
                intentBuilder = new PlacePicker.IntentBuilder();
                ggMapIntent = intentBuilder.build(getActivity());
                // Start the Intent by requesting a result, identified by a request code.
                startActivityForResult(ggMapIntent, ((RiderActivity) getActivity()).PLACE_PICKER_REQUEST);
//                startActivityForResult(intentBuilder.build(getActivity()), ((RiderActivity) getActivity()).PLACE_PICKER_REQUEST);
            } catch (GooglePlayServicesRepairableException e) {
                e.printStackTrace();
            } catch (GooglePlayServicesNotAvailableException e) {
                e.printStackTrace();
            }
        } else if (v.getId() == postNewRequest.getId()) {
            if (newRequestTitle.getText().toString().isEmpty()){
                popToast("Empty Title");
                return;
            }
            if (newRequestContactInfo.getText().toString().isEmpty() && onlyEmail.isChecked() == false)
            {
                popToast("Empty Contact Info");
                return;
            }
//            if (locationLatLong.isEmpty())
//            {
//                popToast("Empty location");
//                return;
//            }


            //TODO: POST the request to the database
            HashMap<String, String> postData = new HashMap<String, String>();

            postData.put("title", newRequestTitle.getText().toString());
//            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
//            Date date = null;
//            try {
//                date = format.parse(newRequestSelectDateText.getText().toString());
//            } catch (ParseException e) {
//                e.printStackTrace();
//            }
//            postData.put("date", date.toString());

            String date = newRequestSelectDateText.getText().toString();
            String[] parts = date.split("/");
            date = parts[2] + "-" + parts[0] + "-" + parts[1];
            postData.put("date", date);
            postData.put("time", newRequestSelectTimeText.getText().toString());
            if (onlyEmail.isChecked() == false) {
                postData.put("contact info", newRequestContactInfo.getText().toString());
                postData.put("only email", "false");
            } else {
                postData.put("only email", "true");
                postData.put("contact info", "");
            }

            for(String str:postData.values())
            {
                System.out.println(str);
            }

            //----------------------LACK OF GG MAPS API, HARD CODE A LOCATION----------------------------------------------------
            postData.put("location", "37.2541066,-80.4138788");
            Data data = new Data("POST", "http://52.38.64.32/main/submit_request", postData);
            myAsyncTask = new MyAsyncTask(this);
            myAsyncTask.execute(data);

        }
    }
     @Override
     public void onActivityResult(int requestCode, int resultCode, Intent data) {
         System.out.println("In On activity Result");
        if (requestCode == ((RiderActivity) getActivity()).PLACE_PICKER_REQUEST) {
            if (resultCode == ((RiderActivity) getActivity()).RESULT_OK) {
                System.out.println("IIn Result OK");
                final Place place = PlacePicker.getPlace(data, getActivity());
                locationLatLong = place.getLatLng().latitude + "," +place.getLatLng().longitude;
            }
        }
    }
    @Override
    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
        newRequestSelectDateText.setText(monthOfYear + "/" + dayOfMonth + "/" + year);
    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        newRequestSelectTimeText.setText(hourOfDay + ":"  + minute + ":" + "00");
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
    public void onSuccessfulExecute(String response) {
        if (response.compareToIgnoreCase("Account is registered") == 0)
        {
            popToast("Successfully Posted!");
            resetForm();
        }
        myAsyncTask.cancel(true);
        myAsyncTask = null;

    }

    private void  resetForm(){
        newRequestContactInfo.setText("");
        newRequestTitle.setText("");
        onlyEmail.setChecked(false);
    }
    private void popToast(String str) {
        Context context = this.getContext();//getApplicationContext();
        int duration = Toast.LENGTH_SHORT;
        Toast toast = Toast.makeText(context, str, duration);
        toast.show();
    }
}