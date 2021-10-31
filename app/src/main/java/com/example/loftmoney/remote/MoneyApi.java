package com.example.loftmoney.remote;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Single;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface MoneyApi {

    @GET("./items")
    Single<List<ItemRemote>> getMoneyItems(@Query("type") String type);

    @POST("items/add")
    @FormUrlEncoded
    Completable addItem(@Field("price") int price, @Field("name") String name,
                        @Field("type") String type, @Field("auth-token") String authToken);
}
