package com.app.danh.frides;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.location.places.ui.PlacePicker;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class RiderActivity extends FragmentActivity implements View.OnClickListener, OnMyAsyncListener, AccountFragment.OnFragmentListener, GoogleApiClient.OnConnectionFailedListener {
    private GoogleApiClient googleApiClient;
    int PLACE_PICKER_REQUEST = 1;

    FragmentPagerAdapter mAppSectionsPagerAdapter;
    ViewPager mViewPager;
    List<Fragment> fragmentList;

    LinearLayout tabRequestRide;
    LinearLayout tabMyRide;
    LinearLayout tabAccount;

    ImageButton imgButtonRequestRide;
    ImageButton imgButtonMyRide;
    ImageButton imgButtonAccount;

    TextView txtViewRequestRide;
    TextView txtViewMyRide;
    TextView txtViewAccount;

    TextView topBarText;

    Fragment newRequestFragment;
    Fragment myRideFragment;
    Fragment accountFragment;

    MyAsyncTask myAsyncTask = null;
    HashMap<String, String> postDataParams;
    User user = null;

    String accountFrag;

    String tab;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rider);

        initView();
        initListener();

        postDataParams = new HashMap<>();
        googleApiClient = new GoogleApiClient.Builder(this)
                .addApi(Places.GEO_DATA_API)
                .addApi(Places.PLACE_DETECTION_API)
                .enableAutoManage(this, this)
                .build();

        tab = "newRequestFrag";
    }


    private void initView() {
        mViewPager = (ViewPager) findViewById(R.id.viewPager);


        //---------------------This for Bottom Bar Control---------------------------
        tabMyRide = (LinearLayout) findViewById(R.id.tab_my_list);
        tabRequestRide = (LinearLayout) findViewById(R.id.tab_new_request);
        tabAccount = (LinearLayout) findViewById(R.id.tab_account);

        imgButtonAccount = (ImageButton) findViewById(R.id.tab_account_img);
        imgButtonMyRide = (ImageButton) findViewById(R.id.tab_my_list_img);
        imgButtonRequestRide = (ImageButton) findViewById(R.id.tab_new_request_img);

        txtViewAccount = (TextView) findViewById(R.id.tab_account_text);
        txtViewMyRide = (TextView) findViewById(R.id.tab_my_list_text);
        txtViewRequestRide = (TextView) findViewById(R.id.tab_new_request_text);
        //---------------------------------------------------------------------------
        topBarText = (TextView) findViewById(R.id.topTitle);

        accountFragment = new AccountFragment();
        myRideFragment = new MyRideFragment();
        newRequestFragment = new NewRequestFragment();

        fragmentList = new ArrayList<Fragment>();
        fragmentList.add(newRequestFragment);
        fragmentList.add(myRideFragment);
        fragmentList.add(accountFragment);

        mAppSectionsPagerAdapter = new FragmentPagerAdapter(getSupportFragmentManager()) {
            /**
             * Return the number of views available.
             */
            @Override
            public int getCount() {
                return fragmentList.size();
            }

            /**
             * Return the Fragment associated with a specified position.
             *
             * @param position
             */
            @Override
            public Fragment getItem(int position) {
                return fragmentList.get(position);
            }
        };


        mViewPager.setAdapter(mAppSectionsPagerAdapter);


        mViewPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {

            @Override
            public void onPageSelected(int position) {
                Data data;
                resetControl();
                switch (position) {
                    case 0:
                        tab = "newRequestFrag";

                        mViewPager.setCurrentItem(0);
                        imgButtonRequestRide.setImageResource(R.mipmap.ic_chat_press);
                        txtViewRequestRide.setTextColor(Color.parseColor("#777777"));
                        topBarText.setText("New Request");
                        break;
                    case 1:
                        tab = "myRideFrag";

                        mViewPager.setCurrentItem(1);
                        imgButtonMyRide.setImageResource(R.mipmap.ic_contacts_press);
                        txtViewMyRide.setTextColor(Color.parseColor("#777777"));
                        topBarText.setText("My Request");

                        // Get user's ride requests
                        postDataParams.clear();
                        data = new Data("GET", "http://52.38.64.32/main/get_user_requests", postDataParams);
                        myAsyncTask = new MyAsyncTask(RiderActivity.this);
                        myAsyncTask.execute(data);
                        break;
                    case 2:
                        tab = "accountFrag";

                        mViewPager.setCurrentItem(2);
                        imgButtonAccount.setImageResource(R.mipmap.ic_person_press);
                        txtViewAccount.setTextColor(Color.parseColor("#777777"));
                        topBarText.setText("Account Information");

                        // Get user's personal information
                        accountFrag = "get info";
                        postDataParams.clear();
                        data = new Data("GET", "http://52.38.64.32/main/personal", postDataParams);
                        myAsyncTask = new MyAsyncTask(RiderActivity.this);
                        myAsyncTask.execute(data);
                        break;
                    default:
                        break;
                }
            }
        });
    }

    private void initListener() {
        tabMyRide.setOnClickListener(this);
        tabRequestRide.setOnClickListener(this);
        tabAccount.setOnClickListener(this);
    }

    private void resetControl() {
        imgButtonAccount.setImageResource(R.mipmap.ic_person);
        imgButtonRequestRide.setImageResource(R.mipmap.ic_chat);
        imgButtonMyRide.setImageResource(R.mipmap.ic_contact);
        txtViewAccount.setTextColor(Color.WHITE);
        txtViewMyRide.setTextColor(Color.WHITE);
        txtViewRequestRide.setTextColor(Color.WHITE);

    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.tab_account:
                mViewPager.setCurrentItem(2);
                break;
            case R.id.tab_my_list:
                mViewPager.setCurrentItem(1);
                break;
            case R.id.tab_new_request:
                mViewPager.setCurrentItem(0);
                break;
            default:
                break;
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PLACE_PICKER_REQUEST) {
            if (resultCode == RESULT_OK) {
                Place place = PlacePicker.getPlace(data, this);
                String toastMsg = String.format("Place: %s", place.getName());
                Toast.makeText(this, toastMsg, Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    public void onSuccessfulExecute(String response) {
        if (tab.equalsIgnoreCase("accountFrag")) {
            if (accountFrag.equalsIgnoreCase("get info")) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONObject obj = jsonObject.getJSONObject("fields");
                    String username = obj.getString("username");
                    String fName = obj.getString("first_name");
                    String lName = obj.getString("last_name");
                    String email = obj.getString("email");
                    if (email.equalsIgnoreCase("null")) {
                        email = "";
                    }
                    String secretQuestion = obj.getString("secret_question");
                    String secretAnswer = obj.getString("secret_answer");

                    user = new User(username, fName, lName, email, secretQuestion, secretAnswer);
                    ((AccountFragment) accountFragment).updateUI(user);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else if (accountFrag.equalsIgnoreCase("update info")) {
                // Do nothing
            }
        }
        else if (tab.equalsIgnoreCase("myRideFrag")) {
            JSONArray jsonArray = null;
            try {
                jsonArray = new JSONArray(response);
                int size = jsonArray.length();
                ArrayList<JSONObject> requests = new ArrayList<>();
                for (int i = 0; i < size; i++) {
                    requests.add(jsonArray.getJSONObject(i));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    public void onConnectionFailed(ConnectionResult connectionResult) {

    }

    @Override
    public void onButtonClicked(JSONObject obj) {
        accountFrag = "update info";
        postDataParams.clear();
        try {
            postDataParams.put("password1", obj.get("password1").toString());
            postDataParams.put("password2", obj.get("password2").toString());
            postDataParams.put("first_name", obj.get("fName").toString());
            postDataParams.put("last_name", obj.get("lName").toString());
            postDataParams.put("email", obj.get("email").toString());
            postDataParams.put("secret_question", obj.get("secretQuestion").toString());
            postDataParams.put("secret_answer", obj.get("secretAnswer").toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Data data = new Data("POST", "http://52.38.64.32/main/personal", postDataParams);
        myAsyncTask = new MyAsyncTask(this);
        myAsyncTask.execute(data);
    }
}
