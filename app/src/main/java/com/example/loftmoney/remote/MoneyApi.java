package com.example.loftmoney.remote;

import io.reactivex.Single;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface MoneyApi {

    @GET("./items")
    Single<ItemResponse> getMoneyItems(@Query("type") String type);
}
