package com.app.danh.frides;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    Button riderBtn;
    Button driverBtn;
    TextView welcomeText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        riderBtn = (Button) findViewById(R.id.riderBtn);
        driverBtn = (Button) findViewById(R.id.driverBtn);
        welcomeText = (TextView) findViewById(R.id.welcomeText);

        riderBtn.setOnClickListener(this);
        driverBtn.setOnClickListener(this);

        Intent intent = getIntent();
        String username = intent.getStringExtra("username");
        welcomeText.setText("Hello " + username);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == riderBtn.getId()) {
            Intent intent = new Intent(this, RiderActivity.class);
            this.startActivity(intent);
        }
    }
}
