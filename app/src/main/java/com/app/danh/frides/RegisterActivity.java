package com.app.danh.frides;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.app.danh.frides.R;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {
    EditText registerUsername;
    EditText registerPassword;
    EditText registerName;
    EditText registerEmail;
    EditText registerSecretQuestion;
    EditText registerSecretAnswer;
    Button register;
    Button backToLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        registerUsername = (EditText) findViewById(R.id.registerUsername);
        registerPassword = (EditText) findViewById(R.id.registerPassword);
        registerName = (EditText) findViewById(R.id.registerName);
        registerEmail = (EditText) findViewById(R.id.registerEmail);
        registerSecretQuestion = (EditText) findViewById(R.id.registerSecretQuestion);
        registerSecretAnswer = (EditText) findViewById(R.id.registerSecretAnswer);
        register = (Button) findViewById(R.id.registerButton);
        backToLogin = (Button) findViewById(R.id.backToLogin);

        register.setOnClickListener(this);
        backToLogin.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == register.getId()) {

        }
        else if(v.getId() == backToLogin.getId()) {

        }
    }
}
