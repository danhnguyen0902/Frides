package com.app.danh.frides;

import java.util.HashMap;

/**
 * Created by Danh on 4/27/2016.
 */
public class Data {
    private String method;
    private String requestURL;
    private HashMap<String, String> postDataParams;

    public Data(String method, String requestURL, HashMap<String, String> postDataParams) {
        this.method = method;
        this.requestURL = requestURL;
        this.postDataParams = postDataParams;
    }

    public String getMethod() {
        return method;
    }

    public String getRequestURL() {
        return requestURL;
    }

    public HashMap<String, String> getPostDataParams() {
        return postDataParams;
    }
}
