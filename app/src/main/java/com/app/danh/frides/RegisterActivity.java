package com.app.danh.frides;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashMap;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener, OnMyAsyncListener {
    EditText registerUsername;
    EditText registerPassword1;
    EditText registerPassword2;
    EditText registerFName;
    EditText registerLName;
    EditText registerEmail;
    EditText registerSecretQuestion;
    EditText registerSecretAnswer;
    Button registerBtn;

    Boolean checked;

    MyAsyncTask myAsyncTask = null;
    HashMap<String, String> postDataParams;
    TextView tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        checked = false;

        registerUsername = (EditText) findViewById(R.id.registerUsername);
        registerUsername.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus)
                {
                    //Log.d("text", registerUsername.getText().toString());
                    if (registerUsername.getText().toString().equals("")) {
                        registerUsername.setError("Username cannot be empty!");
                        Toast.makeText(getApplicationContext(), "You did not enter a username", Toast.LENGTH_SHORT).show();
                        checked = false;
                    }
                    else if(registerUsername.getText().toString().length() < 8 || registerUsername.getText().toString().length() > 12 ){
                        registerUsername.setError("Username should be between 8 and 12 characters!");
                        Toast.makeText(getApplicationContext(), "Username should be between 8 and 12 characters", Toast.LENGTH_SHORT).show();
                        checked = false;
                    }
                    else {
                        checked = true;
                    }
                }
            }
        });
        registerPassword1 = (EditText) findViewById(R.id.registerPassword1);
        registerPassword1.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus)
                {
                    //Log.d("text", registerUsername.getText().toString());
                    if (registerPassword1.getText().toString().equals("")) {
                        registerPassword1.setError("Password cannot be empty!");
                        Toast.makeText(getApplicationContext(), "You did not enter a password", Toast.LENGTH_SHORT).show();
                        checked = false;
                    }
                    else if(registerPassword1.getText().toString().length() < 8 || registerPassword1.getText().toString().length() > 12 ){
                        registerPassword1.setError("Password should be between 8 and 12 characters");
                        Toast.makeText(getApplicationContext(), "Password should be between 8 and 12 characters", Toast.LENGTH_SHORT).show();
                        checked = false;
                    }
                    else {
                        checked = true;
                    }
                }
            }
        });
        registerPassword2 = (EditText) findViewById(R.id.registerPassword2);
        registerPassword2.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus)
                {
                    //Log.d("text", registerUsername.getText().toString());
                    if (registerPassword2.getText().toString().equals("")) {
                        registerPassword2.setError("Password cannot be empty!");
                        Toast.makeText(getApplicationContext(), "You did not enter a password", Toast.LENGTH_SHORT).show();
                        checked = false;
                    }
                    else if(registerPassword2.getText().toString().length() < 8 || registerPassword2.getText().toString().length() > 12 ){
                        registerPassword2.setError("Password should be between 8 and 12 characters");
                        Toast.makeText(getApplicationContext(), "Password should be between 8 and 12 characters", Toast.LENGTH_SHORT).show();
                        checked = false;
                    }
                    else if(!registerPassword2.getText().toString().equals(registerPassword1.getText().toString())){
                        registerPassword2.setError("Two passwords are not the same!");
                        Toast.makeText(getApplicationContext(), "Two passwords are not the same", Toast.LENGTH_SHORT).show();
                        checked = false;
                    }
                    else {
                        checked = true;
                    }
                }
            }
        });
        registerFName = (EditText) findViewById(R.id.registerFName);
        registerFName.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus)
                {
                    //Log.d("text", registerUsername.getText().toString());
                    if (registerFName.getText().toString().equals("")) {
                        registerFName.setError("First name cannot be empty!");
                        Toast.makeText(getApplicationContext(), "First name cannot be empty", Toast.LENGTH_SHORT).show();
                        checked = false;
                    }
                    else if( registerFName.getText().toString().length() > 24 ){
                        registerFName.setError("First name can't exceed 24 characters!");
                        Toast.makeText(getApplicationContext(), "First name can't exceed 24 characters", Toast.LENGTH_SHORT).show();
                        checked = false;
                    }
                    else {
                        checked = true;
                    }
                }
            }
        });
        registerLName = (EditText) findViewById(R.id.registerLName);
        registerLName.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus)
                {
                    //Log.d("text", registerUsername.getText().toString());
                    if (registerLName.getText().toString().equals("")) {
                        registerLName.setError("Last name cannot be empty!");
                        Toast.makeText(getApplicationContext(), "First name cannot be empty", Toast.LENGTH_SHORT).show();
                        checked = false;
                    }
                    else if( registerLName.getText().toString().length() > 24 ){
                        registerLName.setError("Last name can't exceed 24 characters!");
                        Toast.makeText(getApplicationContext(), "First name can't exceed 24 characters", Toast.LENGTH_SHORT).show();
                        checked = false;
                    }
                    else {
                        checked = true;
                    }
                }
            }
        });
        registerEmail = (EditText) findViewById(R.id.registerEmail);
        registerEmail.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus)
                {
                    //Log.d("text", registerUsername.getText().toString());
                    if (registerEmail.getText().toString().equals("")) {
                        registerEmail.setError("Last name cannot be empty!");
                        Toast.makeText(getApplicationContext(), "First name cannot be empty", Toast.LENGTH_SHORT).show();
                        checked = false;
                    }
                    else if( !Patterns.EMAIL_ADDRESS.matcher(registerEmail.getText().toString()).matches()){
                        registerEmail.setError("Email address is in wrong format!");
                        Toast.makeText(getApplicationContext(), "Please input a valid email address", Toast.LENGTH_SHORT).show();
                        checked = false;
                    }
                    else {
                        checked = true;
                    }
                }
            }
        });
        registerSecretQuestion = (EditText) findViewById(R.id.registerSecretQuestion);
        registerSecretQuestion.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus)
                {
                    //Log.d("text", registerUsername.getText().toString());
                    if (registerSecretQuestion.getText().toString().equals("")) {
                        registerSecretQuestion.setError("Secret question cannot be empty!");
                        Toast.makeText(getApplicationContext(), "Secret question cannot be empty!", Toast.LENGTH_SHORT).show();
                        checked = false;
                    }
                    else if( registerSecretQuestion.getText().toString().length() > 64 ){
                        registerSecretQuestion.setError("Secret question can't exceed 64 characters!");
                        Toast.makeText(getApplicationContext(), "Secret question can't exceed 64 characters", Toast.LENGTH_SHORT).show();
                        checked = false;
                    }
                    else {
                        checked = true;
                    }
                }
            }
        });
        registerSecretAnswer = (EditText) findViewById(R.id.registerSecretAnswer);
        registerSecretAnswer.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus)
                {
                    //Log.d("text", registerUsername.getText().toString());
                    if (registerSecretAnswer.getText().toString().equals("")) {
                        registerSecretAnswer.setError("Secret answer cannot be empty!");
                        Toast.makeText(getApplicationContext(), "Secret answer cannot be empty!", Toast.LENGTH_SHORT).show();
                        checked = false;
                    }
                    else if( registerSecretAnswer.getText().toString().length() > 64 ){
                        registerSecretAnswer.setError("Secret answer can't exceed 64 characters!");
                        Toast.makeText(getApplicationContext(), "Secret answer can't exceed 64 characters", Toast.LENGTH_SHORT).show();
                        checked = false;
                    }
                    else {
                        checked = true;
                    }
                }
            }
        });
        registerBtn = (Button) findViewById(R.id.registerBtn);

        registerBtn.setOnClickListener(this);

        postDataParams = new HashMap<>();

        tv = (TextView) findViewById(R.id.text);
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == registerBtn.getId()) {
            // TODO: make sure all fields are filled (except email?!?!)
            // ...

            if(checked){

                postDataParams.clear();
                postDataParams.put("username", registerUsername.getText().toString());
                postDataParams.put("password1", registerPassword1.getText().toString());
                postDataParams.put("password2", registerPassword1.getText().toString());
                postDataParams.put("first_name", registerFName.getText().toString());
                postDataParams.put("last_name", registerLName.getText().toString());
                postDataParams.put("email", registerEmail.getText().toString());
                postDataParams.put("secret_question", registerSecretQuestion.getText().toString());
                postDataParams.put("secret_answer", registerSecretAnswer.getText().toString());

                Data data = new Data("POST", "http://52.38.64.32/main/register", postDataParams);
                myAsyncTask = new MyAsyncTask(this);
                myAsyncTask.execute(data);
            }
            else {
                Toast.makeText(getApplicationContext(), "Please make sure all the fields are correctly filled!", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onSuccessfulExecute(String response) {
        tv.setText(response);
        System.out.println("Response: " + response);
        if (response.compareToIgnoreCase("Account is registered") == 0)
        {
            Intent myIntent = new Intent(this, MainActivity.class);
            this.startActivity(myIntent);
        }
        myAsyncTask.cancel(true);
        myAsyncTask = null;
    }
}
