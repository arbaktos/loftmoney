package com.example.loftmoney.remote;

import com.google.gson.annotations.SerializedName;

public class AuthResponse {

    @SerializedName("id")
    String userId;

    @SerializedName("auth_token")
    String authToken;

    @SerializedName("status")
    String Status;

    public String getUserId() {
        return userId;
    }

    public String getAuthToken() {
        return authToken;
    }

    public String getStatus() {
        return Status;
    }
}
