package com.example.monneymannagerapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class UserManagementActivity extends AppCompatActivity {

    private ArrayAdapter<String> userAdapter;
    private TextView email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_management);

        email = (TextView) findViewById(R.id.email_to_find_user);

        ListView userList = (ListView) findViewById(R.id.users_list);
        ArrayList<String> users = buildUserList();
        userAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, users);
        userList.setAdapter(userAdapter);


        userList.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Toast.makeText(UserManagementActivity.this, "Click user: "+ i, Toast.LENGTH_SHORT).show();
                openUser(view);

            }
        });
    }

    public void findUserByEmail(View v){
        String emailString = email.getText().toString();
        //TODO procurar user com este email
        //TODO if user:
        openUser(v);
        //TODO else:
        Toast.makeText(UserManagementActivity.this, "Email incorreto!", Toast.LENGTH_SHORT).show();
    }

    private ArrayList<String> buildUserList(){
        ArrayList<String> users = new ArrayList<String>();
        users.add("user1@gmail.com");
        users.add("user1@gmail.com");
        users.add("user1@gmail.com");
        users.add("user1@gmail.com");
        users.add("user1@gmail.com");
        return users;
    }


    public void openUser ( View v ){ // TODO selecionar o utilizador - receber parametros
        Intent userActivity = new Intent(this, UserActivity.class);
        startActivity(userActivity);
    }
}