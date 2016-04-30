package com.app.danh.frides;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.app.danh.frides.R;

import org.w3c.dom.Text;

import java.util.HashMap;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener, MyAsyncListener {
    EditText registerUsername;
    EditText registerPassword1;
    EditText registerPassword2;
    EditText registerFName;
    EditText registerLName;
    EditText registerEmail;
    EditText registerSecretQuestion;
    EditText registerSecretAnswer;
    Button registerBtn;

    MyAsyncTask myAsyncTask = null;
    HashMap<String, String> postDataParams;
    TextView tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        registerUsername = (EditText) findViewById(R.id.registerUsername);
        registerPassword1 = (EditText) findViewById(R.id.registerPassword1);
        registerPassword2 = (EditText) findViewById(R.id.registerPassword2);
        registerFName = (EditText) findViewById(R.id.registerFName);
        registerLName = (EditText) findViewById(R.id.registerLName);
        registerEmail = (EditText) findViewById(R.id.registerEmail);
        registerSecretQuestion = (EditText) findViewById(R.id.registerSecretQuestion);
        registerSecretAnswer = (EditText) findViewById(R.id.registerSecretAnswer);
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
    }

    @Override
    public void onSuccessfulExecute(String response) {
        tv.setText(response);
        if (response.compareToIgnoreCase("Account is registered") == 0)
        {
            Intent myIntent = new Intent(this, MainActivity.class);
            this.startActivity(myIntent);
        }
        myAsyncTask.cancel(true);
        myAsyncTask = null;
    }
}
