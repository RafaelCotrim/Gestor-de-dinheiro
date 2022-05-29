package com.example.monneymannagerapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.monneymannagerapp.api.APIClient;
import com.example.monneymannagerapp.api.Api;
import com.example.monneymannagerapp.api.dtos.CategoryDto;
import com.example.monneymannagerapp.api.dtos.CategoryForCreate;
import com.example.monneymannagerapp.api.dtos.TransactionDto;
import com.example.monneymannagerapp.api.dtos.TransactionForCreate;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddTransactionActivity extends AppCompatActivity {

    private TextView transactionAmount;
    private RadioGroup radioGroup;
    private RadioButton creditButton;
    private RadioButton debitButton;
    private ListView categoryList;
    private TextView newCategoryName;

    private ArrayAdapter<String> categoryAdapter;
    private SharedPreferences sharedPref;
    private Api api;

    private List<CategoryDto> categories = new ArrayList<>();
    private int selectedCategory = -1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_transaction);

        categoryList = findViewById(R.id.categories_list);
        newCategoryName = findViewById(R.id.new_category_input);
        transactionAmount = findViewById(R.id.transaction_amount_input);
        creditButton = findViewById(R.id.credit_option_button);
        debitButton = findViewById(R.id.debit_option_button);

        creditButton.setSelected(true);
        categoryList.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        categoryAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_single_choice);
        categoryList.setAdapter(categoryAdapter);

        categoryList.setOnItemClickListener((adapterView, view, position, id) -> selectedCategory = position);
        api = APIClient.getApi();
        sharedPref = this.getSharedPreferences(getString(R.string.preference_file), Context.MODE_PRIVATE);

        loadCategories();
    }

    public void onClickAddTransaction(View v){

        double amount = Double.parseDouble(transactionAmount.getText().toString());

        if(debitButton.isSelected()){
            amount *= -1;
        }

        Context c = this;

        TransactionForCreate tfc = new TransactionForCreate(
                amount,
                sharedPref.getLong(getString(R.string.user_id_preference), 0),
                getSelectedCategoryId());

        api.createTransaction(tfc).enqueue(new Callback<TransactionDto>() {
            @Override
            public void onResponse(Call<TransactionDto> call, Response<TransactionDto> response) {
                Intent addTransactionActivity = new Intent(c, DailyTransactionsActivity.class);
                finish();
                startActivity(addTransactionActivity);
            }

            @Override
            public void onFailure(Call<TransactionDto> call, Throwable t) {
                Toast.makeText(c, "Não foi possível conectar à API", Toast.LENGTH_LONG).show();
            }
        });
    }

    public void onAddCategory(View v){
        Context c = this;
        CategoryForCreate cfc = new CategoryForCreate(newCategoryName.getText().toString(), sharedPref.getLong(getString(R.string.user_id_preference), 0));
        api.createCategory(cfc).enqueue(new Callback<CategoryDto>() {
            @Override
            public void onResponse(Call<CategoryDto> call, Response<CategoryDto> response) {
                if(response.body() == null){
                    Toast.makeText(c, "Não foi possível conectar à API", Toast.LENGTH_LONG).show();
                } else {
                    categories.add(response.body());
                    writeCategories();
                }
            }

            @Override
            public void onFailure(Call<CategoryDto> call, Throwable t) {
                Toast.makeText(c, "Não foi possível conectar à API", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void loadCategories(){
        Context c = this;
        api.getUserCategories(sharedPref.getLong(getString(R.string.user_id_preference), 0)).enqueue(new Callback<List<CategoryDto>>() {
            @Override
            public void onResponse(Call<List<CategoryDto>> call, Response<List<CategoryDto>> response) {
                if(response.body() == null){
                    categories = new ArrayList<>();
                } else {
                    categories = response.body();
                }
                writeCategories();
            }

            @Override
            public void onFailure(Call<List<CategoryDto>> call, Throwable t) {
                categories = new ArrayList<>();
                Toast.makeText(c, "Não foi possível conectar à API", Toast.LENGTH_LONG).show();
                writeCategories();
            }
        });
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