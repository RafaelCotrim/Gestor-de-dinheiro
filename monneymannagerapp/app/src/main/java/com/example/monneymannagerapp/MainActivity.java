package com.example.monneymannagerapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.example.monneymannagerapp.api.APIClient;
import com.example.monneymannagerapp.api.Api;
import com.example.monneymannagerapp.api.dtos.TransactionDto;
import com.example.monneymannagerapp.api.dtos.UserForLogin;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private Api api;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        api = APIClient.getApi();

        UserForLogin ufl = new UserForLogin();
        ufl.email = "user@gmail.com";
        ufl.password = "user";
        
        api.getTransactions().enqueue(new Callback<List<TransactionDto>>() {

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
}