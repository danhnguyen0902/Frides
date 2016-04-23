package com.app.danh.frides;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.CookieHandler;
import java.net.CookieManager;
import java.net.CookiePolicy;
import java.net.CookieStore;
import java.net.HttpCookie;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    Button loginBtn;
    TextView tv;
    HashMap<String, String> postDataParams;
    CookieManager cookieManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        loginBtn = (Button) findViewById(R.id.loginBtn);
        loginBtn.setOnClickListener(this);
        tv = (TextView) findViewById(R.id.text);

        postDataParams = new HashMap<>();

        //cookieManager = new CookieManager();
        //CookieHandler.setDefault(cookieManager);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == loginBtn.getId()) {
            postDataParams.clear();
            postDataParams.put("username", "danh0902");
            postDataParams.put("password", "danh1234");

            new MyAsyncTask().execute("http://52.38.64.32/main/login");
        }
    }

    private class MyAsyncTask extends AsyncTask<String, Integer, String> {
        private String getCsrfToken(String requestURL) {
            String csrf_token = null;
            try {
                URL url = new URL(requestURL);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");
                conn.setReadTimeout(15000);
                conn.setConnectTimeout(15000);

                int responseCode = conn.getResponseCode();
                if (responseCode == HttpsURLConnection.HTTP_OK) {
                    // Get cookies
                    List<String> cookies = conn.getHeaderFields().get("Set-Cookie");
                    for (String cookie : cookies) {
                        if (cookie.contains("csrf")) {
                            //String[] part = cookie.split(";");
                            //String[] csrf = part[0].split("=");
                            //csrf_token = csrf[1];
                            csrf_token = cookie.split(";")[0];

                            break;
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            return csrf_token;
        }

        private String sendPostRequest(String requestURL, String csrf_token) {
            String response = "";
            try {
                URL url = new URL(requestURL);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestProperty("Cookie", csrf_token);
                conn.setRequestProperty("X-CSRFToken", csrf_token.split("=")[1]);
                conn.setRequestMethod("POST");
                conn.setReadTimeout(15000);
                conn.setConnectTimeout(15000);
                conn.setDoOutput(true);
//                conn.setDoInput(true); // true by default, so no needs to do it

                OutputStream os = conn.getOutputStream();
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
                String requestBody = getPostDataString(postDataParams);
                writer.write(requestBody);
                writer.flush();
                writer.close();
                os.close();

//                conn.disconnect(); // this costs a lot of resources

                int responseCode = conn.getResponseCode();
                if (responseCode == HttpsURLConnection.HTTP_OK) {
                    // Get cookies
                    List<String> cookies = conn.getHeaderFields().get("Set-Cookie");

                    // Get response string
                    String line;
                    BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                    while ((line = br.readLine()) != null) {
                        response += line;
                    }
                } else {
                    response = "";

                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            return response;
        }

        @Override
        protected String doInBackground(String... params) {
            String requestURL = params[0];

            String csrf_token = getCsrfToken(requestURL);
            String response = sendPostRequest(requestURL, csrf_token);

            return response;
        }

        private String getPostDataString(HashMap<String, String> params) throws UnsupportedEncodingException {
            StringBuilder result = new StringBuilder();
            boolean first = true;
            for (Map.Entry<String, String> entry : params.entrySet()) {
                if (first)
                    first = false;
                else
                    result.append("&");

                result.append(URLEncoder.encode(entry.getKey(), "UTF-8"));
                result.append("=");
                result.append(URLEncoder.encode(entry.getValue(), "UTF-8"));
            }

            return result.toString();
        }

        protected void onPostExecute(String response) {
            tv.setText(response);
        }

        protected void onProgressUpdate(Integer... progress) {
        }
    }
}
