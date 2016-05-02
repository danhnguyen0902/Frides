package com.app.danh.frides;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import org.json.JSONException;
import org.json.JSONObject;

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
    OnFragmentListener fragmentListener;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.rider_info, container, false);
        username = (EditText) view.findViewById(R.id.username);
        username.setEnabled(false);
        password1 = (EditText) view.findViewById(R.id.password1);
        password2 = (EditText) view.findViewById(R.id.password2);
        fName = (EditText) view.findViewById(R.id.fName);
        lName = (EditText) view.findViewById(R.id.lName);
        email = (EditText) view.findViewById(R.id.email);
        secretQuestion = (EditText) view.findViewById(R.id.secretQuestion);
        secretAnswer = (EditText) view.findViewById(R.id.secretAnswer);
        updateBtn = (Button) view.findViewById(R.id.updateBtn);
        updateBtn.setOnClickListener(this);

        return view;
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == updateBtn.getId()) {
            JSONObject obj = new JSONObject();
            try {
                obj.put("password1", password1.getText().toString());
                obj.put("password2", password1.getText().toString());
                obj.put("fName", fName.getText().toString());
                obj.put("lName", lName.getText().toString());
                obj.put("email", email.getText().toString());
                obj.put("secretQuestion", secretQuestion.getText().toString());
                obj.put("secretAnswer", secretAnswer.getText().toString());
            } catch (JSONException e) {
                e.printStackTrace();
            }
            fragmentListener.onButtonClicked(obj);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentListener) {
            fragmentListener = (OnFragmentListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        fragmentListener = null;
    }

    public void updateUI(User user) {
        username.setText(user.getUsername());
        fName.setText(user.getFName());
        lName.setText(user.getLName());
        email.setText(user.getEmail());
        secretQuestion.setText(user.getSecretQuestion());
        secretAnswer.setText(user.getSecretAnswer());
    }

    /**
     * Created by Danh on 4/30/2016.
     */
    public interface OnFragmentListener {
        void onButtonClicked(JSONObject obj);
    }
}