package com.example.monneymannagerapp.api;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class APIClient {

    private static Retrofit retrofit = null;
    private static Api api = null;

    public static Api getApi() {

        if(api != null){
            return api;
        }

        retrofit = new Retrofit.Builder()
                .baseUrl("http://192.168.1.69:8080")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        api = retrofit.create(Api.class);

        return api;
    }

}
