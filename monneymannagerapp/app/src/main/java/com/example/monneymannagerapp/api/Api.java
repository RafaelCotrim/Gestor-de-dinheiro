package com.example.monneymannagerapp.api;

import com.example.monneymannagerapp.api.dtos.TransactionDto;
import com.example.monneymannagerapp.api.dtos.UserForLogin;
import com.example.monneymannagerapp.api.dtos.UserDto;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface Api {

    @POST("/auth/login")
    Call<UserDto> login(@Body UserForLogin ufl);

    @GET("/transactions")
    Call<List<TransactionDto>> getTransactions();
}
