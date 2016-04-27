package com.app.danh.frides;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.HashMap;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    EditText loginUsername;
    EditText loginPassword;
    Button loginBtn;
    Button forgotPasswordBtn;
    Button registerBtn;
    TextView tv;
    HashMap<String, String> postDataParams;
    String cookieHeader;
    MyAsyncTask asyncTask;

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
        asyncTask = new MyAsyncTask(this);

        tv = (TextView) findViewById(R.id.text);
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == loginBtn.getId()) {
            postDataParams.clear();
            postDataParams.put("username", loginUsername.getText().toString());
            postDataParams.put("password", loginPassword.getText().toString());

            asyncTask.execute("http://52.38.64.32/main/login");
        }
        else if(v.getId() == forgotPasswordBtn.getId()) {

        }
        else if(v.getId() == registerBtn.getId()) {

        }
    }

    public String getCookieHeader() {
        return cookieHeader;
    }

    public void setCookieHeader(String cookieHeader) {
        this.cookieHeader = cookieHeader;
    }

    public HashMap<String, String> getPostDataParams() {
        return postDataParams;
    }

    public void setText(String text) {
        tv.setText(text);
    }
}
