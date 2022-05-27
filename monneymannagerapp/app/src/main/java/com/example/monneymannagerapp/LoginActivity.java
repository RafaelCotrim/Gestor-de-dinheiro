package com.example.monneymannagerapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.monneymannagerapp.api.APIClient;
import com.example.monneymannagerapp.api.Api;
import com.example.monneymannagerapp.api.dtos.UserDto;
import com.example.monneymannagerapp.api.dtos.UserForLogin;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    private Api api;
    private SharedPreferences sharedPref;
    private TextView emailView;
    private TextView passwordView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        api = APIClient.getApi();
        sharedPref = this.getSharedPreferences(getString(R.string.preference_file), Context.MODE_PRIVATE);
        emailView = findViewById(R.id.email_login_input);
        passwordView = findViewById(R.id.password_login_input);

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

    public void onSubmit(View v){
        UserForLogin ufl = new UserForLogin();
        ufl.email = emailView.getText().toString().trim();
        ufl.password = passwordView.getText().toString().trim();

        if(nullOrEmpty(ufl.email) || nullOrEmpty(ufl.password)){
            onFail("Todos os campos devem estar preenchidos");
            return;
        }

        Context context = this;

        api.login(ufl).enqueue(new Callback<UserDto>() {
            @Override
            public void onResponse(Call<UserDto> call, Response<UserDto> response) {

                if(!response.isSuccessful()){
                    onFail(getString(R.string.api_cred_error));
                    return;
                }

                UserDto u = response.body();
                sharedPref.edit()
                        .putLong(getString(R.string.user_id_preference), u.id)
                        .putString(getString(R.string.user_name_preference), u.name)
                        .putBoolean(getString(R.string.user_admin_preference), u.isAdmin)
                        .putString(getString(R.string.user_email_preference), u.email)
                        .apply();

                Intent dashboardActivity = new Intent(context, DashboardActivity.class);
                startActivity(dashboardActivity);
            }

            @Override
            public void onFailure(Call<UserDto> call, Throwable t) {
                onFail(getString(R.string.api_server_error_message));
            }
        });
    }

    private void checkLogin(){

        if(sharedPref.getLong(getString(R.string.user_id_preference), 0) != 0){
            Intent dashboardActivity = new Intent(this, DashboardActivity.class);
            startActivity(dashboardActivity);
        }
    }

    private void onFail(String message){
        passwordView.setText("");
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }

    private boolean nullOrEmpty(String s){
        return s == null || s.trim().isEmpty();
    }
}