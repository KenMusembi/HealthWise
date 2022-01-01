package com.example.healthwise;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Url;

public interface RetrofitAPI {
    @GET
    Call<MSGModel> getMessage(@Url String url);

}
