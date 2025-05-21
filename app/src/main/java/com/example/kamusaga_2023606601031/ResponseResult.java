package com.example.kamusaga_2023606601031;


import com.google.gson.annotations.SerializedName;

public class ResponseResult {
    @SerializedName("message")
    private String message;

    public String getMessage() {
        return message;
    }
}