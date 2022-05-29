package com.example.monneymannagerapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.monneymannagerapp.api.APIClient;
import com.example.monneymannagerapp.api.Api;
import com.example.monneymannagerapp.api.ApiCallback;
import com.example.monneymannagerapp.api.dtos.CategoryDto;
import com.example.monneymannagerapp.api.dtos.CategoryForCreate;
import com.example.monneymannagerapp.api.dtos.TransactionForCreate;

import java.util.ArrayList;
import java.util.List;

public class AddTransactionActivity extends AppCompatActivity {

    private TextView transactionAmount;
    private RadioButton debitButton;
    private TextView newCategoryName;

    private ArrayAdapter<String> categoryAdapter;
    private SharedPreferences sharedPref;
    private Api api;

    private List<CategoryDto> categories = new ArrayList<>();
    private int selectedCategory = -1;
    private String date;

    public static final String DATE_EXTRA = "DATE";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_transaction);

        ListView categoryList = findViewById(R.id.categories_list);
        newCategoryName = findViewById(R.id.new_category_input);
        transactionAmount = findViewById(R.id.transaction_amount_input);
        RadioButton creditButton = findViewById(R.id.credit_option_button);
        debitButton = findViewById(R.id.debit_option_button);

        creditButton.setChecked(true);
        categoryList.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        categoryAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_single_choice);
        categoryList.setAdapter(categoryAdapter);

        categoryList.setOnItemClickListener((adapterView, view, position, id) -> selectedCategory = position);
        api = APIClient.getApi();
        sharedPref = this.getSharedPreferences(getString(R.string.preference_file), Context.MODE_PRIVATE);

        loadCategories();

        date = getIntent().getExtras().getString(DATE_EXTRA);
    }

    public void onClickAddTransaction(View v){

        String val = transactionAmount.getText().toString();
        if(val.trim().isEmpty()){
            val = "0";
        }

        double amount = Double.parseDouble(val);

        if(debitButton.isChecked()){
            amount = -1 * amount;
        }

        Context c = this;

        if(amount == 0){
            Toast.makeText(this, "O montante deve ser maior que 0", Toast.LENGTH_LONG).show();
            return;
        }

        TransactionForCreate tfc = new TransactionForCreate(
                amount,
                sharedPref.getLong(getString(R.string.user_id_preference), 0),
                getSelectedCategoryId(),
                date);

        api.createTransaction(tfc).enqueue(new ApiCallback<>(this, data -> {
            if(data != null){
                Intent intent = new Intent(c, DailyTransactionsActivity.class);
                intent.putExtra(DailyTransactionsActivity.DATE_EXTRA, date);
                finish();
                startActivity(intent);
            }
        }));
    }

    public void onAddCategory(View v){
        CategoryForCreate cfc = new CategoryForCreate(newCategoryName.getText().toString(), sharedPref.getLong(getString(R.string.user_id_preference), 0));
        api.createCategory(cfc).enqueue(new ApiCallback<>(this, data -> {
            if(data != null){
                categories.add(data);
                writeCategories();
            }
        }));
    }

    private void loadCategories(){
        api.getUserCategories(sharedPref.getLong(getString(R.string.user_id_preference), 0))
                .enqueue(new ApiCallback<>(this, data -> {
                    if(data == null){
                        categories = new ArrayList<>();
                    } else {
                        categories = data;
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

    private long getSelectedCategoryId(){
        if(selectedCategory == -1){
            return 0;
        }

        return categories.get(selectedCategory).id;
    }
}