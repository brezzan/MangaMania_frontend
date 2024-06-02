package com.example.mangamania_app.model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ErrorResponse<T> {

    private String status;
    private String time;  // localtimedate
    private T data;

    public ErrorResponse(String time, String status,T data) {
        super();
        this.time = time;
        this.data = data;
        this.status = status;
    }

    public ErrorResponse(String time, String status) {
        super();
        this.time = time;
        this.status = status;

    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }


    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public String getStatus() {
        return status;
    }
    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "ErrorResponse{" +
                "status='" + status + '\'' +
                ", time='" + time + '\'' +
                ", data=" + data +
                '}';
    }
}