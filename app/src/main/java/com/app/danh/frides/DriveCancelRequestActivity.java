package com.app.danh.frides;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

/**
 * Created by Danh on 5/3/2016.
 */
public class DriveCancelRequestActivity extends AppCompatActivity implements View.OnClickListener,
        OnMyAsyncListener {
    EditText title;
    EditText date;
    EditText time;
    EditText contactInfo;
    Button cancelBtn;
    String locationLatLong;
    TextView locationTxtView;

    JSONObject jsonObj;
    MyAsyncTask myAsyncTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.driver_cancel_request);

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
        cancelBtn = (Button) findViewById(R.id.cancelBtn);
        contactInfo = (EditText) findViewById(R.id.contactInfo);
        locationTxtView = (TextView) findViewById(R.id.locationTextView);
        locationLatLong = "";

        cancelBtn.setOnClickListener(this);

        try {
            JSONObject data = jsonObj.getJSONObject("fields");
            title.setText(data.getString("title").toString());
            date.setText(data.getString("date").toString());
            time.setText(data.getString("time").toString());
            contactInfo.setText(data.getString("contact_info").toString());
            locationLatLong = data.getDouble("latitude") + "," + data.getDouble("longitude");
            locationTxtView.setText("Location: (" + locationLatLong + ")");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == cancelBtn.getId()) {
            HashMap<String, String> postData = new HashMap<>();
            try {
                postData.put("pk", String.valueOf(jsonObj.getInt("pk")));
            } catch (JSONException e) {
                e.printStackTrace();
            }

            Data data = new Data("POST", "http://52.38.64.32/main/cancel_request", postData);
            myAsyncTask = new MyAsyncTask(this);
            myAsyncTask.execute(data);
        }
    }

    @Override
    public void onSuccessfulExecute(String response) {
        if (response.compareToIgnoreCase("Ride request is successfully cancelled!") == 0) {
            popToast("Successfully Cancelled the Request!");
        }
        myAsyncTask.cancel(true);
        myAsyncTask = null;

    }

    private void popToast(String str) {
        Context context = getApplicationContext();
        int duration = Toast.LENGTH_SHORT;
        Toast toast = Toast.makeText(context, str, duration);
        toast.show();
    }
}
