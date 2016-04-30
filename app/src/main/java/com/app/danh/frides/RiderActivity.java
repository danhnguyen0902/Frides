package com.app.danh.frides;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class RiderActivity extends FragmentActivity implements View.OnClickListener, MyAsyncListener {

    FragmentPagerAdapter mAppSectionsPagerAdapter;
    ViewPager mViewPager;
    List<Fragment> fragmentList;

    LinearLayout tabRideList;
    LinearLayout tabRequestRide;
    LinearLayout tabMyRide;
    LinearLayout tabAccount;

    ImageButton imgButtonRideList;
    ImageButton imgButtonRequestRide;
    ImageButton imgButtonMyRide;
    ImageButton imgButtonAccount;

    TextView txtViewRideList;
    TextView txtViewRequestRide;
    TextView txtViewMyRide;
    TextView txtViewAccount;

    TextView topBarText;

    Fragment rideListFragment;
    Fragment newRequestFragment;
    Fragment myRideFragment;
    Fragment accountFragment;

    MyAsyncTask myAsyncTask = null;
    HashMap<String, String> postDataParams;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rider);

        initView();
        initListener();

        postDataParams = new HashMap<>();

    }


    private void initView() {
        mViewPager = (ViewPager) findViewById(R.id.viewPager);


        //---------------------This for Bottom Bar Control---------------------------
        tabMyRide = (LinearLayout) findViewById(R.id.tab_my_list);
        tabRequestRide = (LinearLayout) findViewById(R.id.tab_new_request);
        tabRideList = (LinearLayout) findViewById(R.id.tab_list);
        tabAccount = (LinearLayout) findViewById(R.id.tab_account);

        imgButtonAccount = (ImageButton) findViewById(R.id.tab_account_img);
        imgButtonMyRide = (ImageButton) findViewById(R.id.tab_my_list_img);
        imgButtonRideList = (ImageButton) findViewById(R.id.tab_list_img);
        imgButtonRequestRide = (ImageButton) findViewById(R.id.tab_new_request_img);

        txtViewAccount = (TextView) findViewById(R.id.tab_account_text);
        txtViewMyRide = (TextView) findViewById(R.id.tab_my_list_text);
        txtViewRequestRide = (TextView) findViewById(R.id.tab_new_request_text);
        txtViewRideList = (TextView) findViewById(R.id.tab_list_text);
        //---------------------------------------------------------------------------
        topBarText = (TextView) findViewById(R.id.topTitle);

        rideListFragment = new RideListFragment();
        accountFragment = new AccountFragment();
        myRideFragment = new MyRideFragment();
        newRequestFragment = new NewRequestFragment();

        fragmentList = new ArrayList<Fragment>();
        fragmentList.add(newRequestFragment);
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
                resetControl();
                switch (position) {
                    case 0:
                        mViewPager.setCurrentItem(0);
                        imgButtonRequestRide.setImageResource(R.mipmap.ic_chat_press);
                        txtViewRequestRide.setTextColor(Color.parseColor("#777777"));
                        topBarText.setText("New Request");
                        break;
                    case 1:
                        mViewPager.setCurrentItem(1);
                        imgButtonMyRide.setImageResource(R.mipmap.ic_contacts_press);
                        txtViewMyRide.setTextColor(Color.parseColor("#777777"));
                        topBarText.setText("My Request");
                        break;
                    case 2:
                        mViewPager.setCurrentItem(2);
                        imgButtonRideList.setImageResource(R.mipmap.ic_friends_press);
                        txtViewRideList.setTextColor(Color.parseColor("#777777"));
                        topBarText.setText("Ride List");
                        break;
                    case 3:
                        mViewPager.setCurrentItem(3);
                        imgButtonAccount.setImageResource(R.mipmap.ic_person_press);
                        txtViewAccount.setTextColor(Color.parseColor("#777777"));
                        topBarText.setText("Account Information");

                        // TODO: add http requests
                        postDataParams.clear();
                        Data data = new Data("GET", "http://52.38.64.32/main/personal", postDataParams);
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
        tabRideList.setOnClickListener(this);
        tabAccount.setOnClickListener(this);
    }

    private void resetControl() {
        imgButtonAccount.setImageResource(R.mipmap.ic_person);
        imgButtonRequestRide.setImageResource(R.mipmap.ic_chat);
        imgButtonMyRide.setImageResource(R.mipmap.ic_contact);
        imgButtonRideList.setImageResource(R.mipmap.ic_friends);
        txtViewAccount.setTextColor(Color.WHITE);
        txtViewRideList.setTextColor(Color.WHITE);
        txtViewMyRide.setTextColor(Color.WHITE);
        txtViewRequestRide.setTextColor(Color.WHITE);

    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.tab_account:
                mViewPager.setCurrentItem(3);

                break;
            case R.id.tab_list:
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


    @Override
    public void onSuccessfulExecute(String response) {
        try {
            JSONObject jsonObject = new JSONObject(response);

            // TODO: remove
            ((AccountFragment)accountFragment).setText(response);
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
}


