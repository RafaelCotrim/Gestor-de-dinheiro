package com.example.monneymannagerapp;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.monneymannagerapp.api.APIClient;
import com.example.monneymannagerapp.api.Api;
import com.example.monneymannagerapp.api.ApiCallback;
import com.example.monneymannagerapp.api.dtos.BudgetDto;
import com.example.monneymannagerapp.api.dtos.BudgetForUpdate;
import com.example.monneymannagerapp.api.dtos.CategoryDto;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Class: BudgetManageActivity
 * This class is where the user can edit or remove a budget.
 *
 * If the user changes the budget amount or a category and click on
 * save button this category will be edited and the user will be
 * redirected to BudgetActivity.
 *
 * If the user click on remove button this category is removed and
 * the user is redirected to BudgetActivity.
 */
public class BudgetManageActivity extends AppCompatActivity {

    public static final String BUDGET_ID_EXTRA = "ID";

    // Base
    private SharedPreferences sharedPref;
    private Api api;

    // Views
    private TextView budgetAmount;
    ListView categoryList;

    // View Related
    private ArrayAdapter<String> categoryAdapter;

    // Data
    long budgetId = 0;
    BudgetDto currentBudget;
    private final List<CategoryDto> categories = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_budget_manage);

        api = APIClient.getApi();
        sharedPref = this.getSharedPreferences(getString(R.string.preference_file), Context.MODE_PRIVATE);

        budgetId = getIntent().getExtras().getLong(BUDGET_ID_EXTRA);

        budgetAmount = findViewById(R.id.budget_amount_edit);
        categoryList = findViewById(R.id.categories_budget_list_edit);

        categoryList.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        categoryAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_single_choice, new ArrayList<>());
        categoryList.setAdapter(categoryAdapter);

        loadData();
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadData();
    }

    public void onSaveBudget(View v){
        double amount =  Double.parseDouble(budgetAmount.getText().toString());
        int pos = categoryList.getCheckedItemPosition();
        long categoryId = 0;

        if(pos != -1){
            categoryId = categories.get(pos).id;
        }

        BudgetForUpdate b =  new BudgetForUpdate(amount, categoryId, sharedPref.getLong(getString(R.string.user_id_preference), 0));

        api.updateBudget(budgetId, b).enqueue(new ApiCallback<>(this, data -> {
            if(data == null){
                return;
            }
            finish();
        }));
    }

    public void onRemoveBudget(View v){

        api.deleteBudget(budgetId).enqueue(new ApiCallback<>(this, data -> finish()));
    }

    private void loadData(){

        api.getUserBudgets(sharedPref.getLong(getString(R.string.user_id_preference), 0), true, null, null)
                .enqueue(new ApiCallback<>(this, data-> {
                    if(data == null){
                        return;
                    }

                    Set<Long> usedCategories = new HashSet<>();

                    for (BudgetDto b : data) {
                        if(b.id != budgetId){
                            usedCategories.add(b.category.id);
                        } else {
                            currentBudget = b;
                        }
                    }

                    loadCategories(usedCategories);
                    budgetAmount.setText(String.format("%s", currentBudget.value));
                }));
    }

    private void loadCategories(Set<Long> usedCategories){

        api.getUserCategories(sharedPref.getLong(getString(R.string.user_id_preference), 0))
                .enqueue(new ApiCallback<>(this, data -> {

                    if(data == null){
                        return;
                    }

                    categoryAdapter.clear();

                    int absIndex = 0;
                    int position = 0;
                    for (int i = 0; i < data.size(); i++) {
                        CategoryDto c = data.get(i);

                        if(usedCategories.contains(c.id)){
                            continue;
                        }

                        categoryAdapter.add(c.name);
                        categories.add(c);

                        if(currentBudget.category.id == c.id){
                            position = absIndex;
                        }

                        absIndex++;
                    }

                    categoryAdapter.notifyDataSetChanged();

                    categoryList.setItemChecked(position, true);
                }));
    }

}