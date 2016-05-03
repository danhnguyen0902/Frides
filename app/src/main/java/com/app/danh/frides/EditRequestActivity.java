package com.app.danh.frides;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
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

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.HashMap;

/**
 * Created by Danh on 5/3/2016.
 */
public class EditRequestActivity extends AppCompatActivity implements View.OnClickListener,
        DatePickerDialog.OnDateSetListener, View.OnFocusChangeListener,
        TimePickerDialog.OnTimeSetListener, OnMyAsyncListener {
    int PLACE_PICKER_REQUEST = 1;

    EditText title;
    EditText date;
    EditText time;
    EditText contactInfo;
    Button locationBtn;
    Button updateBtn;
    CheckBox onlyEmail;
    String locationLatLong;

    private DatePickerDialog datePickerDialog;
    private TimePickerDialog timePickerDialog;
    private Calendar calendar;
    JSONObject jsonObj;
    MyAsyncTask myAsyncTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.rider_edit_request);

        Intent intent = getIntent();
        String jsonString = intent.getStringExtra("item");
        jsonObj = null;
        try {
            jsonObj = new JSONObject(jsonString);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        title = (EditText) findViewById(R.id.title);
        date = (EditText) findViewById(R.id.date);
        time = (EditText) findViewById(R.id.time);
        locationBtn = (Button) findViewById(R.id.locationBtn);
        updateBtn = (Button) findViewById(R.id.updateBtn);
        onlyEmail = (CheckBox) findViewById(R.id.onlyEmail);
        contactInfo = (EditText) findViewById(R.id.contactInfo);
        locationLatLong = "";
        onlyEmail.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (onlyEmail.isChecked()) {
                    contactInfo.setEnabled(false);
                } else {
                    contactInfo.setEnabled(true);
                }
            }
        });

        date.setOnFocusChangeListener(this);
        time.setOnFocusChangeListener(this);
        date.setOnClickListener(this);
        time.setOnClickListener(this);
        locationBtn.setOnClickListener(this);
        updateBtn.setOnClickListener(this);

        calendar = Calendar.getInstance();

        try {
            JSONObject data = jsonObj.getJSONObject("fields");
            title.setText(data.getString("title").toString());
            date.setText(data.getString("date").toString());
            time.setText(data.getString("time").toString());
            contactInfo.setText(data.getString("contact_info").toString());
            locationLatLong = data.getDouble("latitude") + "," + data.getDouble("longitude");
            onlyEmail.setChecked(false);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    PlacePicker.IntentBuilder intentBuilder;
    Intent ggMapIntent;

    @Override
    public void onClick(View v) {
        if (v.getId() == date.getId()) {
            calendar = Calendar.getInstance();
            datePickerDialog = new DatePickerDialog(getApplicationContext(), this, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
            datePickerDialog.show();
        } else if (v.getId() == time.getId()) {
            calendar = Calendar.getInstance();
            timePickerDialog = new TimePickerDialog(getApplicationContext(), this, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), false);
            timePickerDialog.show();
        } else if (v.getId() == locationBtn.getId()) {
            try {
                intentBuilder = new PlacePicker.IntentBuilder();
                ggMapIntent = intentBuilder.build(this);
                // Start the Intent by requesting a result, identified by a request code.
                startActivityForResult(ggMapIntent, PLACE_PICKER_REQUEST);
            } catch (GooglePlayServicesRepairableException e) {
                e.printStackTrace();
            } catch (GooglePlayServicesNotAvailableException e) {
                e.printStackTrace();
            }
        } else if (v.getId() == updateBtn.getId()) {
            if (title.getText().toString().isEmpty()) {
                popToast("Empty Title");
                return;
            }
            if (contactInfo.getText().toString().isEmpty() && onlyEmail.isChecked() == false) {
                popToast("Empty Contact Info");
                return;
            }

            // POST the request to the database
            HashMap<String, String> postData = new HashMap<>();
            postData.put("title", title.getText().toString());
            postData.put("date", date.getText().toString());
            postData.put("time", time.getText().toString());
            if (onlyEmail.isChecked() == false) {
                postData.put("contact info", contactInfo.getText().toString());
                postData.put("only email", "false");
            } else {
                postData.put("only email", "true");
                postData.put("contact info", "");
            }

            try {
                postData.put("pk", String.valueOf(jsonObj.getInt("pk")));
            } catch (JSONException e) {
                e.printStackTrace();
            }

            //----------------------LACK OF GG MAPS API, HARD CODE A LOCATION----------------------------------------------------
            postData.put("location", "37.2541066,-80.4138788");
            Data data = new Data("POST", "http://52.38.64.32/main/update_request", postData);
            myAsyncTask = new MyAsyncTask(this);
            myAsyncTask.execute(data);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        System.out.println("In On activity Result");
        if (requestCode == PLACE_PICKER_REQUEST) {
            if (resultCode == RESULT_OK) {
                System.out.println("IIn Result OK");
                final Place place = PlacePicker.getPlace(data, this);
                locationLatLong = place.getLatLng().latitude + "," + place.getLatLng().longitude;
            }
        }
    }

    @Override
    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
        date.setText(year + "-" + (monthOfYear + 1) + "-" + dayOfMonth);
    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        if (minute < 10) {
            time.setText(hourOfDay + ":0" + minute);
        } else {
            time.setText(hourOfDay + ":" + minute);
        }
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        if (v.getId() == date.getId() && hasFocus) {
            calendar = Calendar.getInstance();
            datePickerDialog = new DatePickerDialog(getApplicationContext(), this, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
            datePickerDialog.show();
        } else if (v.getId() == time.getId() && hasFocus) {
            calendar = Calendar.getInstance();
            timePickerDialog = new TimePickerDialog(getApplicationContext(), this, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), false);
            timePickerDialog.show();
        }
    }

    @Override
    public void onSuccessfulExecute(String response) {
        if (response.compareToIgnoreCase("Ride request is successfully updated!") == 0) {
            popToast("Successfully Updated!");
            resetForm();
        }
        myAsyncTask.cancel(true);
        myAsyncTask = null;

    }

    private void resetForm() {
        contactInfo.setText("");
        title.setText("");
        onlyEmail.setChecked(false);
        date.setText("");
        time.setText("");
    }

    private void popToast(String str) {
        Context context = getApplicationContext();
        int duration = Toast.LENGTH_SHORT;
        Toast toast = Toast.makeText(context, str, duration);
        toast.show();
    }
}
