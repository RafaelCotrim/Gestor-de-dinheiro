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
import com.example.monneymannagerapp.api.dtos.UserDto;
import com.example.monneymannagerapp.api.dtos.UserForLogin;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    private Api api;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        api = APIClient.getApi();

        checkLogin();
    }

    @Override
    protected void onResume() {
        super.onResume();
        checkLogin();
    }

    // send to Register Activity
    public void onResgiterActivity(View v){
        Intent registerActivity = new Intent(this, RegisterActivity.class);
        startActivity(registerActivity);
    }

    private void login(UserForLogin ufl){
        Context context = this;
        SharedPreferences sharedPref = this.getSharedPreferences(
                getString(R.string.preference_file),
                Context.MODE_PRIVATE);

        api.login(ufl).enqueue(new Callback<UserDto>() {
            @Override
            public void onResponse(Call<UserDto> call, Response<UserDto> response) {
                UserDto u = response.body();
                sharedPref.edit()
                        .putLong(getString(R.string.user_id_preference), u.id)
                        .putString(getString(R.string.user_name_preference), u.name)
                        .putBoolean(getString(R.string.user_admin_preference), u.isAdmin)
                        .putString(getString(R.string.user_email_preference), u.email)
                        .apply();

                Log.v("TEST", "Login saved");

                Intent dashboardActivity = new Intent(context, DashboardActivity.class);
                startActivity(dashboardActivity);
            }

            @Override
            public void onFailure(Call<UserDto> call, Throwable t) {
                Log.v("TEST", "Login Error");
            }
        });
    }

    private void checkLogin(){
        SharedPreferences sharedPref = this.getSharedPreferences(
                getString(R.string.preference_file),
                Context.MODE_PRIVATE);

        if(sharedPref.getLong(getString(R.string.user_id_preference), 0) != 0){
            Intent dashboardActivity = new Intent(this, DashboardActivity.class);
            startActivity(dashboardActivity);
        }
    }
}