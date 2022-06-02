package com.example.monneymannagerapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.monneymannagerapp.api.APIClient;
import com.example.monneymannagerapp.api.Api;
import com.example.monneymannagerapp.api.ApiCallback;
import com.example.monneymannagerapp.api.dtos.UserDto;

import java.util.ArrayList;
import java.util.List;

public class UserManagementActivity extends AppCompatActivity {

    private Api api;

    private ArrayAdapter<String> userAdapter;
    private TextView email;

    private final List<UserDto> users = new ArrayList<>();
    private final List<UserDto> activeUsers = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_management);

        api = APIClient.getApi();

        email = findViewById(R.id.email_to_find_user);

        ListView userList = findViewById(R.id.users_list);
        userAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, new ArrayList<>());
        userList.setAdapter(userAdapter);

        userList.setOnItemClickListener((adapterView, view, i, l) -> openUser(i));
        loadData();
    }


    @Override
    protected void onResume() {
        super.onResume();
        loadData();
    }

    private void loadData(){
        api.getAllUsers().enqueue(new ApiCallback<>(this, data -> {
            userAdapter.clear();
            users.clear();
            if(data == null){
                return;
            }

            for (UserDto u: data) {
                userAdapter.add(u.email);
            }

            users.addAll(data);
            activeUsers.addAll(data);
            userAdapter.notifyDataSetChanged();
        }));

    }

    public void findUserByEmail(View v){
        String emailString = email.getText().toString();

        userAdapter.clear();
        activeUsers.clear();

        if(emailString.isEmpty()){
            for (UserDto u: users) {
                userAdapter.add(u.email);
            }
        } else {
            for (UserDto u: users) {
                if(u.email.contains(emailString)){
                    userAdapter.add(u.email);
                    activeUsers.add(u);
                }
            }
        }

        userAdapter.notifyDataSetChanged();
    }

    public void openUser(int position){

        long id = activeUsers.get(position).id;

        Intent userActivity = new Intent(this, UserActivity.class);
        userActivity.putExtra(UserActivity.USER_ID_EXTRA, id);
        startActivity(userActivity);
    }
}