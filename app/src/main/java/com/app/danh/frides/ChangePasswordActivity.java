package com.app.danh.frides;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashMap;

public class ChangePasswordActivity extends AppCompatActivity implements View.OnClickListener, OnMyAsyncListener {
    TextView passQuestion;
    EditText passAnswer;
    Button getAnswerBtn;
    EditText password1;
    EditText password2;

    MyAsyncTask myAsyncTask = null;
    HashMap<String, String> postDataParams;
    String username;
    int step;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.getusername);

        postDataParams = new HashMap<>();
    }

    public void setGetPasswordLayout(View view) {
        step = 1;
        username = ((EditText) findViewById(R.id.getUsername)).getText().toString();
        postDataParams.clear();
        postDataParams.put("username", username);
        Data data = new Data("POST", "http://52.38.64.32/main/get_secret_question", postDataParams);
        myAsyncTask = new MyAsyncTask(this);
        myAsyncTask.execute(data);

        setContentView(R.layout.getquestion);
        passQuestion = (TextView) findViewById(R.id.getPasswordQuestion);
        passAnswer = (EditText) findViewById(R.id.getPasswordAnswer);
        getAnswerBtn = (Button) findViewById(R.id.getAnswerBtn);
        getAnswerBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == getAnswerBtn.getId()) {
            postDataParams.clear();
            postDataParams.put("username", username);
            postDataParams.put("answer", passAnswer.getText().toString());
            Data data = new Data("POST", "http://52.38.64.32/main/submit_answer", postDataParams);
            myAsyncTask = new MyAsyncTask(this);
            myAsyncTask.execute(data);
        }
    }

    @Override
    public void onSuccessfulExecute(String response) {
        switch (step) {
            case 1:
                passQuestion.setText(response);
                step = 2;
                break;
            case 2:
                if (response.equalsIgnoreCase("Correct Answer!")) {
                    setContentView(R.layout.changepassword);
                    password1 = (EditText) findViewById(R.id.newPass1);
                    password2 = (EditText) findViewById(R.id.newPass2);
                    step = 3;
                }
                else {
                    passAnswer.setText(response);
                }
                break;
            case 3:
                if (response.contains("successfully")) {
                    setContentView(R.layout.done);
                    TextView tv = (TextView) findViewById(R.id.result);
                    tv.setText(response);
                }
                else {
                    TextView tv = (TextView) findViewById(R.id.error);
                    tv.setText(response);
                }

                break;
        }

        myAsyncTask.cancel(true);
        myAsyncTask = null;
    }

    public void changePassword(View view) {
        postDataParams.clear();
        postDataParams.put("username", username);
        postDataParams.put("password1", password1.getText().toString());
        postDataParams.put("password2", password2.getText().toString());
        Data data = new Data("POST", "http://52.38.64.32/main/change_password", postDataParams);
        myAsyncTask = new MyAsyncTask(this);
        myAsyncTask.execute(data);
    }
}
