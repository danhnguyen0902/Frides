package com.app.danh.frides;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

/**
 * Created by Phuong on 4/30/2016.
 */
public class AccountFragment extends Fragment implements View.OnClickListener {
    EditText username;
    EditText password1;
    EditText password2;
    EditText fName;
    EditText lName;
    EditText email;
    EditText secretQuestion;
    EditText secretAnswer;
    Button updateBtn;

    TextView tv;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.rider_info, container, false);
        username = (EditText) view.findViewById(R.id.username);
        password1 = (EditText) view.findViewById(R.id.password1);
        password2 = (EditText) view.findViewById(R.id.password2);
        fName = (EditText) view.findViewById(R.id.fName);
        lName = (EditText) view.findViewById(R.id.lName);
        email = (EditText) view.findViewById(R.id.email);
        secretQuestion = (EditText) view.findViewById(R.id.secretQuestion);
        secretAnswer = (EditText) view.findViewById(R.id.secretAnswer);
        updateBtn = (Button) view.findViewById(R.id.updateBtn);
        updateBtn.setOnClickListener(this);

        tv = (TextView) view.findViewById(R.id.text);

        return view;
    }

    @Override
    public void onClick(View v) {

    }

    public void setText(String text) {
        tv.setText(text);
    }
}