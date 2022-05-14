package com.example.monneymannagerapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
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