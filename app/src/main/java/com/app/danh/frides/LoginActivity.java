package com.app.danh.frides;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.HashMap;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener, OnMyAsyncListener {
    EditText loginUsername;
    EditText loginPassword;
    Button loginBtn;
    Button forgotPasswordBtn;
    Button registerBtn;
    static String cookieHeader;
    HashMap<String, String> postDataParams;
    MyAsyncTask myAsyncTask = null;
    TextView errorTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        loginUsername = (EditText) findViewById(R.id.loginUsername);
        loginPassword = (EditText) findViewById(R.id.loginPassword);
        loginBtn = (Button) findViewById(R.id.loginBtn);
        forgotPasswordBtn = (Button) findViewById(R.id.forgotPasswordBtn);
        registerBtn = (Button) findViewById(R.id.registerBtn);
        errorTextView = (TextView) findViewById(R.id.errorTextView);

        errorTextView.setText("");

        loginBtn.setOnClickListener(this);
        forgotPasswordBtn.setOnClickListener(this);
        registerBtn.setOnClickListener(this);

        cookieHeader = null;
        postDataParams = new HashMap<>();
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == loginBtn.getId()) {
            postDataParams.clear();
            postDataParams.put("username", loginUsername.getText().toString().trim());
            postDataParams.put("password", loginPassword.getText().toString().trim());

            Data data = new Data("POST", "http://52.38.64.32/main/login", postDataParams);
            myAsyncTask = new MyAsyncTask(this);
            myAsyncTask.execute(data);
        } else if (v.getId() == forgotPasswordBtn.getId()) {
            Intent myIntent = new Intent(this, ChangePasswordActivity.class);
            this.startActivity(myIntent);
        } else if (v.getId() == registerBtn.getId()) {
            Intent myIntent = new Intent(this, RegisterActivity.class);
            this.startActivity(myIntent);
        }
    }

    @Override
    public void onSuccessfulExecute(String response) {
        myAsyncTask.cancel(true);
        myAsyncTask = null;

        if (response.contains("is logged in")) {
            String[] parts = response.split(" ");
            Intent myIntent = new Intent(this, MainActivity.class);
            errorTextView.setText("");
            loginPassword.setText("");
            loginUsername.setText("");
            myIntent.putExtra("username", parts[0]);
            this.startActivity(myIntent);
        } else
        {
            errorTextView.setText("Incorrect username or password!");
        }
    }
}
