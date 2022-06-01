package com.example.monneymannagerapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class AddBudgetActivity extends AppCompatActivity {

    private ArrayAdapter<String> categoryAdapter;
    private TextView budgetAmount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_budget);

        budgetAmount = (TextView) findViewById(R.id.add_budget_button);

        ListView categoryList = (ListView) findViewById(R.id.categories_budget_list);
        ArrayList<String> categories = addCategories();
        categoryList.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        categoryAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_single_choice, categories);
        categoryList.setAdapter(categoryAdapter);

    }

    private ArrayList<String> addCategories(){
        ArrayList<String> categories = new ArrayList<String>();
        categories.add("Categoria 1");
        categories.add("Categoria 2");
        categories.add("Categoria 3");
        categories.add("Categoria 4");
        categories.add("Categoria 5");
        return categories;
    }


    public void onAddBudget(View v){
        String amount = budgetAmount.getText().toString();
        //TODO getSelectedCategoryId
        //TODO add budget
        Toast.makeText(AddBudgetActivity.this, "Orçamento criado!", Toast.LENGTH_SHORT).show();
    }

}