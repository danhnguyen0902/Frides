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
import java.net.CookieManager;
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
    String cookieHeader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        loginBtn = (Button) findViewById(R.id.loginBtn);
        loginBtn.setOnClickListener(this);
        tv = (TextView) findViewById(R.id.text);

        postDataParams = new HashMap<>();
        cookieHeader = null;
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
        private void getCookieHeader(String requestURL) {
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
                    StringBuilder sb = new StringBuilder();

                    for (String cookie : cookies) {
                        if (sb.length() > 0) {
                            sb.append(";");
                        }
                        // Only want the first part of the cookie header that has the value
                        String value = cookie.split(";")[0];
                        sb.append(value);
                    }

                    cookieHeader = sb.toString();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        private String sendPostRequest(String requestURL) {
            String response = "";
            try {
                URL url = new URL(requestURL);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                if (cookieHeader != null) {
                    conn.setRequestProperty("Cookie", cookieHeader);
                    String[] parts = cookieHeader.split(";");
                    for (String part : parts) {
                        if (part.contains("csrf")) {
                            conn.setRequestProperty("X-CSRFToken", part.split("=")[1]);
                            break;
                        }
                    }
                }
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
                    // Get cookies and update the cookieHeader after we logged in
                    List<String> cookies = conn.getHeaderFields().get("Set-Cookie");
                    StringBuilder sb = new StringBuilder();

                    for (String cookie : cookies) {
                        if (sb.length() > 0) {
                            sb.append(";");
                        }
                        // Only want the first part of the cookie header that has the value
                        String value = cookie.split(";")[0];
                        sb.append(value);
                    }

                    cookieHeader = sb.toString();

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

        String testPersonalPage(String requestURL) {
            String response = "";
            try {
                URL url = new URL(requestURL);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                if (cookieHeader != null) {
                    conn.setRequestProperty("Cookie", cookieHeader);
                    String[] parts = cookieHeader.split(";");
                    for (String part : parts) {
                        if (part.contains("csrf")) {
                            conn.setRequestProperty("X-CSRFToken", part.split("=")[1]);
                            break;
                        }
                    }
                }
                conn.setRequestMethod("POST");
                conn.setReadTimeout(15000);
                conn.setConnectTimeout(15000);
                conn.setDoOutput(true);

                OutputStream os = conn.getOutputStream();
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
                String requestBody = getPostDataString(postDataParams);
                writer.write(requestBody);
                writer.flush();
                writer.close();
                os.close();

                int responseCode = conn.getResponseCode();
                if (responseCode == HttpsURLConnection.HTTP_OK) {
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

            if (cookieHeader == null) {
                getCookieHeader(requestURL);
            }
            String response = sendPostRequest(requestURL);
            response = testPersonalPage("http://52.38.64.32/main/personal");

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
