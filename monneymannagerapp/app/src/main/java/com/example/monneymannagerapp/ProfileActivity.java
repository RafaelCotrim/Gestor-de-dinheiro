package com.example.monneymannagerapp;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class ProfileActivity extends AppCompatActivity {

    private TextView nameEditInput, emailEditInput, passEditInput, passConfEditInput;
    private TextView editButton;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile_fragment);

        nameEditInput = (EditText) findViewById(R.id.name_edit_input);
        emailEditInput = (EditText) findViewById(R.id.email_edit_input);
        passEditInput = (EditText) findViewById(R.id.pass_edit_input);
        passConfEditInput = (EditText) findViewById(R.id.pass_conf_edit_input);

        //edit button
        editButton = (Button) findViewById(R.id.edit_button);
       // editButton.setOnClickListener(this);
//
////        //fill data profile
////        String dataProfile = "{API_REQUEST}";  //JSON
////        nameEditInput.setText(dataProfile.name);
////        emailEditInput.setText(dataProfile.email);
//
        nameEditInput.setText("Nome pesquisado na API");
        emailEditInput.setText("Email pesquisado na API");
    }

    // edit method
    public void edit(View v){
        String name = nameEditInput.getText().toString();
        String email = emailEditInput.getText().toString();
        String password = passEditInput.getText().toString().trim();
        String passwordConf = passConfEditInput.getText().toString().trim();

        if (password.isEmpty() && passwordConf.isEmpty()){
            editWithoutPassword(name, email);
        }else{
            editWithPassword(name, email, password, passwordConf);
        }
    }

    public void editWithoutPassword(String name, String email){
        //API_REQUEST - para update nome e email
        Toast.makeText(ProfileActivity.this, "TODO: Edit!\nNome:"+name+"\nEmail:"+email, Toast.LENGTH_LONG).show();
    }

    public void editWithPassword(String name, String email, String password, String passwordConf){
        //API_REQUEST - para update nome e email
        Toast.makeText(ProfileActivity.this, "TODO: Edit!\nNome:"+name+"\nEmail:"+email+"\nPass:"+password+"\nPassConf:"+passwordConf, Toast.LENGTH_LONG).show();
    }

}