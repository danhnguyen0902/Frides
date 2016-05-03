package com.app.danh.frides;

import android.app.ListFragment;
import android.graphics.Color;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class DriverActivity extends FragmentActivity implements View.OnClickListener, OnMyAsyncListener, AccountFragment.OnFragmentListener {
    FragmentPagerAdapter mAppSectionsPagerAdapter;
    ViewPager mViewPager;
    List<Fragment> fragmentList;

    LinearLayout tabRideList;
    LinearLayout tabMyRide;
    LinearLayout tabAccount;

    ImageButton imgButtonRideList;
    ImageButton imgButtonMyRide;
    ImageButton imgButtonAccount;

    TextView txtViewRideList;
    TextView txtViewMyRide;
    TextView txtViewAccount;

    TextView topBarText;

    Fragment rideListFragment;
    Fragment myRideFragment;
    Fragment accountFragment;

    MyAsyncTask myAsyncTask = null;
    HashMap<String, String> postDataParams;
    User user = null;

    String tab;
    String accountFrag;
    String rideListFrag;

    ArrayList<JSONObject> requestsList;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver);

        initView();
        initListener();

        postDataParams = new HashMap<>();
        tab = "myRideFrag";
    }


    private void initView() {
        mViewPager = (ViewPager) findViewById(R.id.viewPager);

        //---------------------This for Bottom Bar Control---------------------------
        tabMyRide = (LinearLayout) findViewById(R.id.tab_my_list);
        tabRideList = (LinearLayout) findViewById(R.id.tab_list);
        tabAccount = (LinearLayout) findViewById(R.id.tab_account);

        imgButtonAccount = (ImageButton) findViewById(R.id.tab_account_img);
        imgButtonMyRide = (ImageButton) findViewById(R.id.tab_my_list_img);
        imgButtonRideList = (ImageButton) findViewById(R.id.tab_list_img);

        txtViewAccount = (TextView) findViewById(R.id.tab_account_text);
        txtViewMyRide = (TextView) findViewById(R.id.tab_my_list_text);
        txtViewRideList = (TextView) findViewById(R.id.tab_list_text);
        //---------------------------------------------------------------------------
        topBarText = (TextView) findViewById(R.id.topTitle);

        rideListFragment = new DriveListFragment();
        requestsList = new ArrayList<>();
        accountFragment = new AccountFragment();
        myRideFragment = new MyRideFragment();

        fragmentList = new ArrayList<Fragment>();
        fragmentList.add(myRideFragment);
        fragmentList.add(rideListFragment);
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
                        tab = "myRideFrag";

                        mViewPager.setCurrentItem(0);
                        imgButtonMyRide.setImageResource(R.mipmap.ic_contacts_press);
                        txtViewMyRide.setTextColor(Color.parseColor("#777777"));
                        topBarText.setText("My List");
                        break;
                    case 1:
                        tab = "rideListFrag";

                        mViewPager.setCurrentItem(1);
                        imgButtonRideList.setImageResource(R.mipmap.ic_friends_press);
                        txtViewRideList.setTextColor(Color.parseColor("#777777"));
                        topBarText.setText("Ride List");

                        // Get all ride requests
                        postDataParams.clear();
                        data = new Data("GET", "http://52.38.64.32/main/get_all_requests", postDataParams);
                        myAsyncTask = new MyAsyncTask(DriverActivity.this);
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
                        myAsyncTask = new MyAsyncTask(DriverActivity.this);
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
        tabRideList.setOnClickListener(this);
        tabAccount.setOnClickListener(this);
    }

    private void resetControl() {
        imgButtonAccount.setImageResource(R.mipmap.ic_person);
        imgButtonMyRide.setImageResource(R.mipmap.ic_contact);
        imgButtonRideList.setImageResource(R.mipmap.ic_friends);
        txtViewAccount.setTextColor(Color.WHITE);
        txtViewRideList.setTextColor(Color.WHITE);
        txtViewMyRide.setTextColor(Color.WHITE);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.tab_account:
                mViewPager.setCurrentItem(2);
                break;
            case R.id.tab_list:
                mViewPager.setCurrentItem(1);
                break;
            case R.id.tab_my_list:
                mViewPager.setCurrentItem(0);
                break;
            default:
                break;
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
        else if (tab.equalsIgnoreCase("rideListFrag")) {
            // URL: get_all_requests
            JSONArray jsonArray = null;
            try {
                jsonArray = new JSONArray(response);
                int size = jsonArray.length();
                requestsList.clear();
                for (int i = 0; i < size; i++) {
                    requestsList.add(jsonArray.getJSONObject(i));
                }
                ((ArrayAdapter) ((android.support.v4.app.ListFragment) rideListFragment).getListAdapter()).notifyDataSetChanged();
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
