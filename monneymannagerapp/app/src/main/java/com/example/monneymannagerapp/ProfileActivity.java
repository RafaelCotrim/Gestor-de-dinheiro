package com.example.monneymannagerapp;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.monneymannagerapp.api.APIClient;
import com.example.monneymannagerapp.api.Api;
import com.example.monneymannagerapp.api.ApiCallback;
import com.example.monneymannagerapp.api.dtos.UserForUpdate;

public class ProfileActivity extends AppCompatActivity {

    private TextView nameEditInput, emailEditInput, passEditInput, passConfEditInput;
    private TextView editButton;
    private SharedPreferences sharedPref;
    private Api api;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile_fragment);

        api = APIClient.getApi();
        sharedPref = this.getSharedPreferences(getString(R.string.preference_file), Context.MODE_PRIVATE);

        nameEditInput = findViewById(R.id.name_edit_input);
        emailEditInput = findViewById(R.id.email_edit_input);
        passEditInput = findViewById(R.id.pass_edit_input);
        passConfEditInput = findViewById(R.id.pass_conf_edit_input);

        //edit button
        editButton = findViewById(R.id.edit_button);

        nameEditInput.setText("Nome pesquisado na API");
        emailEditInput.setText("Email pesquisado na API");

        nameEditInput.setText(sharedPref.getString(getString(R.string.user_name_preference), ""));
        emailEditInput.setText(sharedPref.getString(getString(R.string.user_email_preference), ""));
    }

    // edit method
    public void edit(View v){
        String name = nameEditInput.getText().toString();
        String email = emailEditInput.getText().toString();
        String password = passEditInput.getText().toString().trim();
        String passwordConf = passConfEditInput.getText().toString().trim();

        if(name.isEmpty() || email.isEmpty() ){
            Toast.makeText(this, "Os campos de nome e email são obrigatórios", Toast.LENGTH_LONG).show();
        } else if (password.isEmpty() ^ passwordConf.isEmpty()){
            Toast.makeText(this, "Para mudar a senha, tanto pasword quanto sua confirmação são necessárias", Toast.LENGTH_LONG).show();
        } else {

            UserForUpdate ufu = new UserForUpdate(name, email, password, passwordConf);
            api.updateUser(sharedPref.getLong(getString(R.string.user_id_preference), 0), ufu)
                    .enqueue(new ApiCallback<>(this, data -> {
                        if(data == null){
                            return;
                        }
                        nameEditInput.setText(data.name);
                        emailEditInput.setText(data.email);
                        passEditInput.setText("");
                        passConfEditInput.setText("");

                        sharedPref.edit()
                                .putString(getString(R.string.user_name_preference), data.name)
                                .putString(getString(R.string.user_email_preference), data.email)
                                .apply();

                        Toast.makeText(this, "Dados atualizados", Toast.LENGTH_LONG).show();
                    }));
        }
    }
}