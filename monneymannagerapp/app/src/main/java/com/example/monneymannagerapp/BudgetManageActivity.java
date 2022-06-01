package com.example.monneymannagerapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class BudgetManageActivity extends AppCompatActivity {

    private ArrayAdapter<String> categoryAdapter;
    private TextView budgetAmount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_budget_manage);

        budgetAmount = (TextView) findViewById(R.id.budget_amount_edit);

        ListView categoryList = (ListView) findViewById(R.id.categories_budget_list_edit);
        ArrayList<String> categories = addCategories();
        categoryList.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        categoryAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_single_choice, categories);
        categoryList.setAdapter(categoryAdapter);
        budgetAmount.setText("200"); //TODO - procurar o amount que já esta registado para pre preencher o campo.
        //TODO - procurar a categoria que já esta registado para pre selecionar o campo.

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

    public void onSaveBudget(View v){
        String amount = budgetAmount.getText().toString();
        //TODO - getSelectedCategoryId
        //TODO - editar registo
        Toast.makeText(BudgetManageActivity.this, "As alterações foram salvas!", Toast.LENGTH_SHORT).show();
    }

    public void onRemoveBudget(View v){
        //TODO - remover
        //TODO - remover
        //TODO - remover
        Toast.makeText(BudgetManageActivity.this, "Orçamento Removido!", Toast.LENGTH_SHORT).show();
    }
}