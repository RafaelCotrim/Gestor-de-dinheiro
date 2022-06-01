package com.example.monneymannagerapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.monneymannagerapp.api.Api;

public class AddUserActivity extends AppCompatActivity {

    private TextView nameEditInput, emailEditInput, passEditInput, passConfEditInput;
    private SharedPreferences sharedPref;
    private Api api;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_user);

        nameEditInput = findViewById(R.id.name_input_newuser);
        emailEditInput = findViewById(R.id.email_input_newuser);
        passEditInput = findViewById(R.id.password_input_newuser);
        passConfEditInput = findViewById(R.id.conf_pass_input_newuser);

    }

    public void addUser(View v){
        String name = nameEditInput.getText().toString();
        String email = emailEditInput.getText().toString();
        String password = passEditInput.getText().toString().trim();
        String passwordConf = passConfEditInput.getText().toString().trim();

        //TODO add user
        //TODO add user
        //TODO add user
        //TODO add user
    }
}