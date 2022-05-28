package com.example.monneymannagerapp.api;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class APIClient {

    private static Retrofit retrofit = null;
    private static Api api = null;

    public static Api getApi() {

        if(api != null){
            return api;
        }

        Gson gson = new GsonBuilder()
                .setDateFormat("yyyy-MM-dd")
                .create();

        retrofit = new Retrofit.Builder()
                .baseUrl("http://192.168.0.18:8080")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        api = retrofit.create(Api.class);

        return api;
    }

}
