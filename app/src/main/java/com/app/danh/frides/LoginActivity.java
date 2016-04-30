package com.app.danh.frides;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.HashMap;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener, MyAsyncListener {
    EditText loginUsername;
    EditText loginPassword;
    Button loginBtn;
    Button forgotPasswordBtn;
    Button registerBtn;
    TextView tv;
    static String cookieHeader;
    HashMap<String, String> postDataParams;
    MyAsyncTask myAsyncTask = null;

    //-------------TESTING AREA----------------------------------
    Button testingBttn;
    //-----------------------------------------------------------

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        loginUsername = (EditText) findViewById(R.id.loginUsername);
        loginPassword = (EditText) findViewById(R.id.loginPassword);
        loginBtn = (Button) findViewById(R.id.loginBtn);
        forgotPasswordBtn = (Button) findViewById(R.id.forgotPasswordBtn);
        registerBtn = (Button) findViewById(R.id.registerBtn);

        loginBtn.setOnClickListener(this);
        forgotPasswordBtn.setOnClickListener(this);
        registerBtn.setOnClickListener(this);

        cookieHeader = null;
        postDataParams = new HashMap<>();

        tv = (TextView) findViewById(R.id.text);

        //-------------TESTING AREA----------------------------------
        testingBttn = (Button) findViewById(R.id.testingRiderBtt);
        testingBttn.setOnClickListener(this);
        //-----------------------------------------------------------
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == loginBtn.getId()) {
            postDataParams.clear();
            postDataParams.put("username", loginUsername.getText().toString());
            postDataParams.put("password", loginPassword.getText().toString());

            Data data = new Data("http://52.38.64.32/main/login", postDataParams);
            myAsyncTask = new MyAsyncTask(this);
            myAsyncTask.execute(data);
        }
        else if(v.getId() == forgotPasswordBtn.getId()) {
            Intent myIntent = new Intent(this, ChangePasswordActivity.class);
            this.startActivity(myIntent);
        }
        else if(v.getId() == registerBtn.getId()) {
            Intent myIntent = new Intent(this, RegisterActivity.class);
            this.startActivity(myIntent);
        }

        //-------------TESTING AREA----------------------------------
        else if (v.getId() == testingBttn.getId())
        {
            Intent intent = new Intent(this, RiderActivity.class);
            this.startActivity(intent);
        }
        //-----------------------------------------------------------
    }

    @Override
    public void onSuccessfulExecute(String response) {
        tv.setText(response);

        myAsyncTask.cancel(true);
        myAsyncTask = null;
    }
}
