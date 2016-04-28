package com.app.danh.frides;

import java.util.HashMap;

/**
 * Created by Danh on 4/27/2016.
 */
public class Data {
    private String requestURL;
    private HashMap<String, String> postDataParams;

    public Data(String requestURL, HashMap<String, String> postDataParams) {
        this.requestURL = requestURL;
        this.postDataParams = postDataParams;
    }

    public String getRequestURL() {
        return requestURL;
    }

    public HashMap<String, String> getPostDataParams() {
        return postDataParams;
    }
}
