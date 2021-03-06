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
import com.example.monneymannagerapp.api.ApiCallback;
import com.example.monneymannagerapp.api.dtos.TransactionDto;
import com.example.monneymannagerapp.api.dtos.UserDto;
import com.example.monneymannagerapp.api.dtos.UserForLogin;
import com.example.monneymannagerapp.api.dtos.UserForRegister;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Class: MainActivity
 * This class is the first activity of this project.
 *
 * When open the application this is the activity
 * returned, and here it is checked if the login has
 * already been done or not.
 *
 * If the user already has a registered login, he is
 * redirected to the DashboardActivity.
 *
 * In case the user does not have a registered login,
 * he stays in this activity, where he can select the
 * login or registration option.
 */
public class MainActivity extends AppCompatActivity {

    private SharedPreferences sharedPref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sharedPref = this.getSharedPreferences(getString(R.string.preference_file), Context.MODE_PRIVATE);

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
        finish();
        startActivity(loginActivity);
    }

    // send to Register Activity
    public void onRegisterActivity(View v){
        Intent registerActivity = new Intent(this, RegisterActivity.class);
        finish();
        startActivity(registerActivity);
    }

    private void checkLogin(){

        if(sharedPref.getLong(getString(R.string.user_id_preference), 0) != 0){
            Intent dashboardActivity = new Intent(this, DashboardActivity.class);
            finish();
            startActivity(dashboardActivity);
        }
    }
}