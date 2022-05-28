package com.example.monneymannagerapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class AddTransactionActivity extends AppCompatActivity {

    private TextView transactionAmount;
    RadioGroup radioGroup;
    RadioButton radioButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_transaction);
        buildCategoriesList();
    }

    public void buildCategoriesList(){
        ListView list = (ListView) findViewById(R.id.categories_list);
        ArrayList<String> categories = getCategories();

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_single_choice, categories);
        list.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        list.setAdapter(arrayAdapter);

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                Toast.makeText(getApplicationContext(), "Item selecionado" + categories.get(position).toString(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    private ArrayList<String> getCategories(){
        ArrayList<String> values = new ArrayList<String>(); //categories list
        values.add("Category-1"); //TODO
        values.add("Category-2"); //TODO
        values.add("Category-3"); //TODO
        values.add("Category-4"); //TODO
        values.add("Category-5"); //TODO
        values.add("Category-6"); //TODO
        values.add("Category-7"); //TODO
        values.add("Category-8"); //TODO
        values.add("Category-9"); //TODO
        values.add("Category-10"); //TODO
        values.add("Category-11"); //TODO
        values.add("Category-12"); //TODO
        values.add("Category-13"); //TODO
        values.add("Category-14"); //TODO
        values.add("Category-15"); //TODO
        values.add("Category-16"); //TODO
        values.add("Category-17"); //TODO
        values.add("Category-18"); //TODO
        values.add("Category-19"); //TODO
        values.add("Category-20"); //TODO

        return values;
    }

    public void onClickAddTransaction(View v){
        transactionAmount = (TextView) findViewById(R.id.transaction_amount_input);
        int amount = Integer.parseInt(transactionAmount.getText().toString());

        if(amount<0){
            addCreditTransaction();
        }
        Intent addTransactionActivity = new Intent(this, AddTransactionActivity.class);
        startActivity(addTransactionActivity);
    }
}