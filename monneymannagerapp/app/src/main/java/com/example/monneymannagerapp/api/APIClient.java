package com.example.monneymannagerapp.api;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Class: APIClient
 *
 * Class responsible for creating and managing references to the API
 */
public class APIClient {

    private static Api api = null;

    public static Api getApi() {

        if(api != null){
            return api;
        }

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://192.168.0.20:8080")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        api = retrofit.create(Api.class);

        return api;
    }

}
