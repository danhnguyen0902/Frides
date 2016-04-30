package com.app.danh.frides;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    Button driverButton;
    Button riderButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        driverButton = (Button) findViewById(R.id.driverButton);
        riderButton = (Button) findViewById(R.id.riderButton);

        driverButton.setOnClickListener(this);
        riderButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Intent intent;
        if(v.getId() == driverButton.getId()) {
            intent = new Intent(this, RiderActivity.class);
            this.startActivity(intent);
        }
        else if(v.getId() == riderButton.getId()) {
            intent = new Intent(this, DriverActivity.class);
            this.startActivity(intent);
        }
    }
}
