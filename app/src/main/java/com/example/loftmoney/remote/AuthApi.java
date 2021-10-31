package com.example.loftmoney.remote;

import io.reactivex.Single;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface AuthApi {

    @GET("./auth")
    Single<AuthResponse> getAuth(@Query("social_user_id") String userId);
}
