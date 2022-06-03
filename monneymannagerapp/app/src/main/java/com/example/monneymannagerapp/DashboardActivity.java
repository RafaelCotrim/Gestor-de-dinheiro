package com.example.monneymannagerapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;

import com.example.monneymannagerapp.Fragments.MainFragment;
import com.google.android.material.navigation.NavigationView;

public class DashboardActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    private SharedPreferences sharedPref;

    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle actionBarDrawerToggle;
    private Toolbar toolbar;
    private NavigationView navigationView;

    //variaveis para carregar o fragment principal
    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;
    String userType = "Admin";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        drawerLayout = findViewById(R.id.drawer);
        navigationView = findViewById(R.id.navigationView);
        sharedPref = this.getSharedPreferences(getString(R.string.preference_file), Context.MODE_PRIVATE);
        //estabelecer evento onClick na navigationView
        if(sharedPref.getBoolean(getString(R.string.user_admin_preference), false)){
            navigationView.getMenu().clear();
            navigationView.inflateMenu(R.menu.drawer_menu_admin);
        }
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
        if(sharedPref.getBoolean(getString(R.string.user_admin_preference), false) && menuItem.getItemId() == R.id.management){
            fragmentManager = getSupportFragmentManager();
            fragmentTransaction = fragmentManager.beginTransaction();
            Intent userActivity = new Intent(this, UserManagementActivity.class);
            startActivity(userActivity);
        }
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
            Intent statisticsActivity = new Intent(this, StatisticsActivity.class);
            startActivity(statisticsActivity);
        }

        if(menuItem.getItemId() == R.id.budgets){
            //carregar fragment de orçamentos
            fragmentManager = getSupportFragmentManager();
            fragmentTransaction = fragmentManager.beginTransaction();
            Intent budgetActivity = new Intent(this, BudgetActivity.class);
            startActivity(budgetActivity);
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

    @Override
    protected void onResume() {
        super.onResume();
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.container, new MainFragment());
        fragmentTransaction.commit();
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

        finish();
        startActivity(new Intent(this, MainActivity.class));
    }
}