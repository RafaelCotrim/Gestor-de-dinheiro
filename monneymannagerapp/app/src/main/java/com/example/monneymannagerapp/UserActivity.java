package com.example.monneymannagerapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.monneymannagerapp.api.Api;
import com.example.monneymannagerapp.api.ApiCallback;
import com.example.monneymannagerapp.api.dtos.UserForUpdate;

public class UserActivity extends AppCompatActivity {

    private TextView nameEditInput, emailEditInput, passEditInput, passConfEditInput;
    private SharedPreferences sharedPref;
    private Api api;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);

        nameEditInput = findViewById(R.id.edit_name_input);
        emailEditInput = findViewById(R.id.edit_email_input);
        passEditInput = findViewById(R.id.edit_pass_input);
        passConfEditInput = findViewById(R.id.edit_conf_pass_input);

        //TODO - alterar os seguintes campos pela pesquisa da API;
        nameEditInput.setText("Nome pesquisado na API");
        emailEditInput.setText("Email pesquisado na API");
    }

    public void editUser(View v){
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

    public void removeUser(View v){
        //TODO remove user
        //TODO remove user
        //TODO remove user
    }
}