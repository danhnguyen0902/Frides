package com.app.danh.frides;

/**
 * Created by Danh on 4/30/2016.
 */
public class User {
    private String username;
    private String fName;
    private String lName;
    private String email;
    private String secretQuestion;
    private String secretAnswer;

    public User(String username, String fName, String lName, String email, String secretQuestion, String secretAnswer) {
        this.username = username;
        this.fName = fName;
        this.lName = lName;
        this.email = email;
        this.secretQuestion = secretQuestion;
        this.secretAnswer = secretAnswer;
    }

    public String getUsername() {
        return username;
    }

    public String getFName() {
        return fName;
    }

    public String getLName() {
        return lName;
    }

    public String getEmail() {
        return email;
    }

    public String getSecretQuestion() {
        return secretQuestion;
    }

    public String getSecretAnswer() {
        return secretAnswer;
    }
}
