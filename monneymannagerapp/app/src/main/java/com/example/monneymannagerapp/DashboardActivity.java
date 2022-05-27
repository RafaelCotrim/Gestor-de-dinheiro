package com.example.monneymannagerapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.ActionBarContainer;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.monneymannagerapp.Fragments.BudgetFragment;
import com.example.monneymannagerapp.Fragments.MainFragment;
import com.example.monneymannagerapp.Fragments.ProfileFragment;
import com.example.monneymannagerapp.Fragments.StatisticFragment;
import com.google.android.material.navigation.NavigationView;

public class DashboardActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    DrawerLayout drawerLayout;
    ActionBarDrawerToggle actionBarDrawerToggle;
    Toolbar toolbar;
    NavigationView navigationView;

    //variaveis para carregar o fragment principal
    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        drawerLayout = findViewById(R.id.drawer);
        navigationView = findViewById(R.id.navigationView);

        //estabelecer evento onClick na navigationView
        navigationView.setNavigationItemSelectedListener(this);

        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open,R.string.close);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.setDrawerIndicatorEnabled(true);
        actionBarDrawerToggle.syncState();

        //carregar fragmentPrincipal
        fragmentManager = getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.container, new MainFragment());
        fragmentTransaction.commit();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        drawerLayout.closeDrawer(GravityCompat.START);
        if(menuItem.getItemId() == R.id.dashboard){
            //carregar fragment principal
            fragmentManager = getSupportFragmentManager();
            fragmentTransaction = fragmentManager.beginTransaction();
            Intent dailyTransactionsActivity = new Intent(this, DailyTransactionsActivity.class);
            startActivity(dailyTransactionsActivity);
        }

        if(menuItem.getItemId() == R.id.statistic){
            //carregar fragment das estatisticas
            fragmentManager = getSupportFragmentManager();
            fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.container, new StatisticFragment());
            fragmentTransaction.commit();
        }

        if(menuItem.getItemId() == R.id.budgets){
            //carregar fragment de orçamentos
            fragmentManager = getSupportFragmentManager();
            fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.container, new BudgetFragment());
            fragmentTransaction.commit();
        }
        if(menuItem.getItemId() == R.id.profile){
            //carregar fragment de dados pessoais
            fragmentManager = getSupportFragmentManager();
            fragmentTransaction = fragmentManager.beginTransaction();
            Intent profileActivity = new Intent(this, ProfileActivity.class);
            startActivity(profileActivity);
        }
        if(menuItem.getItemId() == R.id.logout){
            logout();
        }
        return false;
    }

    private void checkLogin(){
        if(sharedPref.getLong(getString(R.string.user_id_preference), 0) == 0){
            startActivity(new Intent(this, MainActivity.class));
        }
    }

    private void logout(){
        sharedPref.edit().remove(getString(R.string.user_id_preference))
                .remove(getString(R.string.user_admin_preference))
                .remove(getString(R.string.user_email_preference))
                .remove(getString(R.string.user_name_preference))
                .apply();

        startActivity(new Intent(this, MainActivity.class));
    }
}