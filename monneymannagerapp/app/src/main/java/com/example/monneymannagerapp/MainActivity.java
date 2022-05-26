package com.example.monneymannagerapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.example.monneymannagerapp.api.APIClient;
import com.example.monneymannagerapp.api.Api;
import com.example.monneymannagerapp.api.dtos.TransactionDto;
import com.example.monneymannagerapp.api.dtos.UserDto;
import com.example.monneymannagerapp.api.dtos.UserForLogin;
import com.example.monneymannagerapp.api.dtos.UserForRegister;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private Api api;
    private SharedPreferences sharedPref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        api = APIClient.getApi();
        sharedPref = this.getSharedPreferences(getString(R.string.preference_file), Context.MODE_PRIVATE);

        api.getTransactions(true, true).enqueue(new Callback<List<TransactionDto>>() {

            @Override
            public void onResponse(Call<List<TransactionDto>> call, Response<List<TransactionDto>> response) {
                for (TransactionDto t: response.body()) {
                    Log.v("TEST", "" + t.id);
                }
            }

            @Override
            public void onFailure(Call<List<TransactionDto>> call, Throwable t) {
                Log.v("TEST", t.toString());
            }
        });

        checkLogin();
    }

    @Override
    protected void onResume() {
        super.onResume();
        checkLogin();
    }

    // send to Login Activity
    public void onLoginActivity(View v){
        Intent loginActivity = new Intent(this, LoginActivity.class);
        startActivity(loginActivity);
    }

    // send to Register Activity
    public void onRegisterActivity(View v){
        Intent registerActivity = new Intent(this, RegisterActivity.class);
        startActivity(registerActivity);
    }

    // send to Register Activity
    public void onDASHActivity(View v){
        Intent dashboardActivity = new Intent(this, DashboardActivity.class);
        startActivity(dashboardActivity);
    }

    private void checkLogin(){

        if(sharedPref.getLong(getString(R.string.user_id_preference), 0) != 0){
            Intent dashboardActivity = new Intent(this, DashboardActivity.class);
            startActivity(dashboardActivity);
        }
    }
}