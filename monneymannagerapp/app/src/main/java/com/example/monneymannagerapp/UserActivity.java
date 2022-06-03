package com.example.monneymannagerapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import com.example.monneymannagerapp.api.APIClient;
import com.example.monneymannagerapp.api.Api;
import com.example.monneymannagerapp.api.ApiCallback;
import com.example.monneymannagerapp.api.dtos.UserForUpdate;

/**
 * Class: UserActivity
 * This class is where the admin user can edit or remove a user.
 *
 * If the admin change name, email or password and click on save
 * button this user will be updated.
 *
 * In this class it is also possible to create admin users
 * selecting the checkbox and click on the save button.
 */
public class UserActivity extends AppCompatActivity {

    public static final String USER_ID_EXTRA = "USER_ID";

    private CheckBox adminCheckbox;
    private TextView nameEditInput, emailEditInput, passEditInput, passConfEditInput;
    private SharedPreferences sharedPref;
    private Api api;

    private long userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);

        api = APIClient.getApi();
        sharedPref = this.getSharedPreferences(getString(R.string.preference_file), Context.MODE_PRIVATE);

        adminCheckbox = findViewById(R.id.admin_checkbox);
        nameEditInput = findViewById(R.id.edit_name_input);
        emailEditInput = findViewById(R.id.edit_email_input);
        passEditInput = findViewById(R.id.edit_pass_input);
        passConfEditInput = findViewById(R.id.edit_conf_pass_input);

        userId = getIntent().getExtras().getLong(USER_ID_EXTRA);

        loadData();
    }

    public void editUser(View v){
        String name = nameEditInput.getText().toString();
        String email = emailEditInput.getText().toString();
        String password = passEditInput.getText().toString().trim();
        String passwordConf = passConfEditInput.getText().toString().trim();
        boolean isAdmin = adminCheckbox.isChecked();

        if(name.isEmpty() || email.isEmpty() ){
            Toast.makeText(this, "Os campos de nome e email são obrigatórios", Toast.LENGTH_LONG).show();
        } else if (password.isEmpty() ^ passwordConf.isEmpty()){
            Toast.makeText(this, "Para mudar a senha, tanto pasword quanto sua confirmação são necessárias", Toast.LENGTH_LONG).show();
        } else {

            UserForUpdate ufu = new UserForUpdate(name, email, password, passwordConf, isAdmin);
            api.updateUser(userId, ufu)
                    .enqueue(new ApiCallback<>(this, data -> {
                        if(data == null){
                            return;
                        }

                        nameEditInput.setText(data.name);
                        emailEditInput.setText(data.email);
                        passEditInput.setText("");
                        passConfEditInput.setText("");
                        adminCheckbox.setChecked(data.admin);

                        if(sharedPref.getLong(getString(R.string.user_id_preference), 0) == userId){
                            sharedPref.edit()
                                    .putString(getString(R.string.user_name_preference), data.name)
                                    .putString(getString(R.string.user_email_preference), data.email)
                                    .putBoolean(getString(R.string.user_admin_preference), data.admin)
                                    .apply();
                        }

                        finish();
                    }));
        }
    }

    public void removeUser(View v){
        api.deleteUser(userId).enqueue(new ApiCallback<>(this, data -> {
            finish();
        }));
    }

    private void loadData(){
        api.getUser(userId).enqueue(new ApiCallback<>(this, data -> {
            if(data == null){
                finish();
                return;
            }

            nameEditInput.setText(data.name);
            emailEditInput.setText(data.email);
            passEditInput.setText("");
            passConfEditInput.setText("");
            adminCheckbox.setChecked(data.admin);

        }));
    }
}