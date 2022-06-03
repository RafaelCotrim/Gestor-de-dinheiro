package com.example.monneymannagerapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.monneymannagerapp.api.APIClient;
import com.example.monneymannagerapp.api.Api;
import com.example.monneymannagerapp.api.dtos.UserDto;
import com.example.monneymannagerapp.api.dtos.UserForRegister;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Class: RegisterActivity
 * This class is where the user can register in the application.
 *
 * For registration the user needs enter a name, email and password
 * and click on the registration button.
 *
 * If the user already have a register in the application,
 * the user can select the login option that directs the
 * user to LoginActivity.
 */
public class RegisterActivity extends AppCompatActivity {

    private TextView userNameView;
    private TextView passwordView;
    private TextView confirmationView;
    private TextView emailView;

    private Api api;
    private SharedPreferences sharedPref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        api = APIClient.getApi();
        sharedPref = this.getSharedPreferences(getString(R.string.preference_file), Context.MODE_PRIVATE);
        userNameView = findViewById(R.id.name_regist_input);
        passwordView = findViewById(R.id.pass_regist_input);
        confirmationView = findViewById(R.id.conf_pass_regist_input);
        emailView = findViewById(R.id.email_regist_input);

        checkLogin();
    }

    @Override
    protected void onResume() {
        super.onResume();
        checkLogin();
    }

    public void onLoginActivity(View v){
        Intent loginActivity = new Intent(this, LoginActivity.class);
        finish();
        startActivity(loginActivity);
    }

    private void onFail(String message){
        passwordView.setText("");
        confirmationView.setText("");
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }

    public void onSubmit(View v){
        UserForRegister ufr = new UserForRegister();
        ufr.name = userNameView.getText().toString().trim();
        ufr.password = passwordView.getText().toString().trim();
        ufr.confirmation = confirmationView.getText().toString().trim();
        ufr.email = emailView.getText().toString().trim();

        if(nullOrEmpty(ufr.email) || nullOrEmpty(ufr.password) ||nullOrEmpty(ufr.confirmation) || nullOrEmpty(ufr.email)){
            onFail("Todos os campos devem estar preenchidos");
            return;
        } else if(!ufr.password.equals(ufr.confirmation)){
            onFail("Senha e senha de confirmação devem ser iguais");
            return;
        }

        Context context = this;

        api.register(ufr).enqueue(new Callback<UserDto>() {
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
                        .putBoolean(getString(R.string.user_admin_preference), u.admin)
                        .putString(getString(R.string.user_email_preference), u.email)
                        .apply();
                finish();
                startActivity(new Intent(context, DashboardActivity.class));
            }

            @Override
            public void onFailure(Call<UserDto> call, Throwable t) {
                onFail(getString(R.string.api_server_error_message));
            }
        });
    }

    private boolean nullOrEmpty(String s){
        return s == null || s.trim().isEmpty();
    }

    private void checkLogin(){
        if(sharedPref.getLong(getString(R.string.user_id_preference), 0) != 0){
            Intent dashboardActivity = new Intent(this, DashboardActivity.class);
            finish();
            startActivity(dashboardActivity);
        }
    }
}