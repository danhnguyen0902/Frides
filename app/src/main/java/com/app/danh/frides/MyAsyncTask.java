package com.app.danh.frides;

import android.os.AsyncTask;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;

/**
 * Created by Danh on 4/27/2016.
 */
public class MyAsyncTask extends AsyncTask<Data, Integer, String> {
    private static String cookieHeader = null;
    private HashMap<String, String> postDataParams;
    private MyAsyncListener myAsyncListener;
    private static final String getCsrfURL = "http://52.38.64.32/main/get_csrf_token";

    public MyAsyncTask(MyAsyncListener myAsyncListener) {
        super();

        this.myAsyncListener = myAsyncListener;
    }

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
                if (cookies != null) {
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
                // Get cookies and update the cookieHeader. Django gives us new cookies with the
                // session id only after we LOGGED IN
                List<String> cookies = conn.getHeaderFields().get("Set-Cookie");
                if (cookies != null) {
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

    private String sendGetRequest(String requestURL) {
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
            conn.setRequestMethod("GET");
            conn.setReadTimeout(15000);
            conn.setConnectTimeout(15000);

            int responseCode = conn.getResponseCode();
            if (responseCode == HttpsURLConnection.HTTP_OK) {
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
    protected String doInBackground(Data... params) {
        String method = params[0].getMethod();
        String requestURL = params[0].getRequestURL();
        postDataParams = params[0].getPostDataParams();

        if (cookieHeader == null) {
            getCookieHeader(getCsrfURL);
        }

        String response;
        if (method.equalsIgnoreCase("POST")) {
            response = sendPostRequest(requestURL);
        }
        else {
            response = sendGetRequest(requestURL);
        }

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
        myAsyncListener.onSuccessfulExecute(response);
    }

    protected void onProgressUpdate(Integer... progress) {
    }
}
