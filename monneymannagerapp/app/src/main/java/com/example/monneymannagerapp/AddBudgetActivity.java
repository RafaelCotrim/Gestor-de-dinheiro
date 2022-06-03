package com.example.monneymannagerapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.monneymannagerapp.api.APIClient;
import com.example.monneymannagerapp.api.Api;
import com.example.monneymannagerapp.api.ApiCallback;
import com.example.monneymannagerapp.api.dtos.BudgetDto;
import com.example.monneymannagerapp.api.dtos.BudgetForCreate;
import com.example.monneymannagerapp.api.dtos.CategoryDto;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Class: AddBudgetActivity
 * This class is where the new budgets are registered in database.
 *
 * When a user chooses to add a new budget, they need enter an
 * budget amount and select the category that they want.
 *
 * It's only possible to create a budget for each category.
 */
public class AddBudgetActivity extends AppCompatActivity {

    private SharedPreferences sharedPref;
    private Api api;

    private ArrayAdapter<String> categoryAdapter;
    private TextView budgetAmount;
    private TextView budgetAmount_input;
    private ListView categoryList;

    private final List<CategoryDto> categories = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_budget);

        api = APIClient.getApi();
        sharedPref = this.getSharedPreferences(getString(R.string.preference_file), Context.MODE_PRIVATE);

        budgetAmount = findViewById(R.id.add_budget_button);

        categoryList = findViewById(R.id.categories_budget_list);
        budgetAmount_input = findViewById(R.id.amount_buget_input);

        categoryList.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        categoryAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_single_choice, new ArrayList<>());
        categoryList.setAdapter(categoryAdapter);


        loadCategories();
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadCategories();
    }

    public void onAddBudget(View v){
        double amount = Double.parseDouble(budgetAmount_input.getText().toString());
        int pos = categoryList.getCheckedItemPosition();
        long categoryId = 0;

        if(pos != -1){
            categoryId = categories.get(pos).id;
        }

        long userId = sharedPref.getLong(getString(R.string.user_id_preference),0);

        api.createBudget(new BudgetForCreate(amount, categoryId, userId))
                        .enqueue(new ApiCallback<>(this, data -> {
                            if(data == null){
                                return;
                            }

                            finish();
                        }));
    }

    private void loadCategories(){

        Set<Long> usedCategories = new HashSet<>();

        api.getUserBudgets(sharedPref.getLong(getString(R.string.user_id_preference), 0), true, null, null)
                        .enqueue(new ApiCallback<>(this, data-> {
                            if(data == null){
                                return;
                            }

                            for (BudgetDto b : data) {
                                usedCategories.add(b.category.id);
                            }
                        }));

        api.getUserCategories(sharedPref.getLong(getString(R.string.user_id_preference), 0))
                .enqueue(new ApiCallback<>(this, data -> {
                    categories.clear();
                    if(data == null){
                        return;
                    }

                    for (CategoryDto c : data) {
                        if(!usedCategories.contains(c.id)){
                            categories.add(c);
                        }
                    }

                    if(categories.isEmpty()){
                        Toast.makeText(this, "Não há categorias sem orçamentos associados", Toast.LENGTH_LONG).show();
                        finish();
                    }

                    writeCategories();
                }));
    }


    private void writeCategories(){
        categoryAdapter.clear();

        for (CategoryDto c: categories) {
            categoryAdapter.add(c.name);
        }
        categoryAdapter.notifyDataSetChanged();
    }
}