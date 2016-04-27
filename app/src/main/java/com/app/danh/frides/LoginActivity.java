package com.app.danh.frides;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.app.danh.frides.R;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    EditText loginUsername;
    EditText loginPassword;
    Button login;
    Button forgotPassword;
    Button register;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        loginUsername = (EditText) findViewById(R.id.loginUsername);
        loginPassword = (EditText) findViewById(R.id.loginPassword);
        login = (Button) findViewById(R.id.login);
        forgotPassword = (Button) findViewById(R.id.forgotPassword);
        register = (Button) findViewById(R.id.register);

        login.setOnClickListener(this);
        forgotPassword.setOnClickListener(this);
        register.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == login.getId()) {

        }
        else if(v.getId() == forgotPassword.getId()) {

        }
        else if(v.getId() == register.getId()) {

        }
    }
}
