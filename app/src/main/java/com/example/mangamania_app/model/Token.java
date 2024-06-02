package com.example.mangamania_app.model;



import java.io.Serializable;
import java.time.LocalDateTime;

public class Token implements Serializable {

    private String token;
    private String timeOut;

    public Token(String token, String timeOut) {
        super();
        this.token = token;
        this.timeOut = timeOut;
    }


    public String getToken() {
        return token;
    }


    public void setToken(String token) {
        this.token = token;
    }


    public String getTimeOut() {
        return timeOut;
    }


    public void setTimeOut(String timeOut) {
        this.timeOut = timeOut;
    }

    @Override
    public String toString() {
        return "Token{" +
                "token='" + token + '\'' +
                ", timeOut='" + timeOut + '\'' +
                '}';
    }
}