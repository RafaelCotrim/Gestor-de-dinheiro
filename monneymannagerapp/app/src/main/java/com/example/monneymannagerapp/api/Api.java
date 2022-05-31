package com.example.monneymannagerapp.api;

import com.example.monneymannagerapp.api.dtos.CategoryDto;
import com.example.monneymannagerapp.api.dtos.CategoryForCreate;
import com.example.monneymannagerapp.api.dtos.StatisticsDto;
import com.example.monneymannagerapp.api.dtos.TransactionDto;
import com.example.monneymannagerapp.api.dtos.TransactionForCreate;
import com.example.monneymannagerapp.api.dtos.UserForLogin;
import com.example.monneymannagerapp.api.dtos.UserDto;
import com.example.monneymannagerapp.api.dtos.UserForRegister;
import com.example.monneymannagerapp.api.dtos.UserForUpdate;

import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface Api {

    // Auth methods

    @POST("/auth/login")
    Call<UserDto> login(@Body UserForLogin ufl);

    @POST("/auth/register")
    Call<UserDto> register(@Body UserForRegister ufr);

    // User methods

    @PUT("/users/{id}")
    Call<UserDto> updateUser(@Path("id") long id, @Body UserForUpdate ufu);

    @GET("/users/{id}/transactions")
    Call<List<TransactionDto>> getUserTransactions(@Path("id") long id, @Query("category-info") Boolean categoryInfo, @Query("date") String date);

    @GET("/users/{id}/categories")
    Call<List<CategoryDto>> getUserCategories(@Path("id") long id);

    @GET("/users/{id}/statistics")
    Call<List<StatisticsDto>> getUserStatistics(@Path("id") long id);

    // Transaction methods

    @GET("/transactions")
    Call<List<TransactionDto>> getTransactions(@Query("user-info") Boolean userInfo, @Query("category-info") Boolean categoryInfo);

    @POST("/transactions")
    Call<TransactionDto> createTransaction(@Body TransactionForCreate tfc);

    // Categories

    @POST("/categories")
    Call<CategoryDto> createCategory(@Body CategoryForCreate cfc);
}
