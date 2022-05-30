package com.example.monneymannagerapp;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Date;
import java.util.Locale;

public class AddTransactionActivity extends AppCompatActivity {

    private TextView transactionAmount;
    private RadioButton debitButton;
    private TextView newCategoryName;

    private ArrayAdapter<String> categoryAdapter;
    private SharedPreferences sharedPref;
    private Api api;
    private DatePickerDialog datePickerDialog;
    private Button dateButton;

    private List<CategoryDto> categories = new ArrayList<>();
    private int selectedCategory = -1;
    private String date;
    private Date dateSet;
    private final SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd", Locale.UK);

    public static final String DATE_EXTRA = "DATE";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_transaction);

        ListView categoryList = findViewById(R.id.categories_list);
        newCategoryName = findViewById(R.id.new_category_input);
        transactionAmount = findViewById(R.id.transaction_amount_input);
        debitButton = findViewById(R.id.debit_option_button);

        debitButton.setChecked(true);
        categoryList.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        categoryAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_single_choice);
        categoryList.setAdapter(categoryAdapter);

        categoryList.setOnItemClickListener((adapterView, view, position, id) -> selectedCategory = position);
        api = APIClient.getApi();
        sharedPref = this.getSharedPreferences(getString(R.string.preference_file), Context.MODE_PRIVATE);

        initDatePicker();
        dateButton = findViewById(R.id.date_picker_button_add_transaction);
        dateButton.setText(getTodaysDate());

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

    private String getTodaysDate(){
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);
        return makeDateString(day, month, year);
    }


    private void initDatePicker()
    {
        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener()
        {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day)
            {
                String date = makeDateString(day, month, year);
                dateButton.setText(date);
            }
        };

        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);

        int style = AlertDialog.THEME_HOLO_LIGHT;

        datePickerDialog = new DatePickerDialog(this, style, dateSetListener, year, month, day);

    }

    private String makeDateString(int day, int month, int year)
    {
        Calendar calendar = Calendar.getInstance();
        //default_values
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.HOUR, 0);
        calendar.set(Calendar.AM_PM, Calendar.AM);

        //date
        calendar.set(Calendar.MONTH, month);
        calendar.set(Calendar.DAY_OF_MONTH, day);
        calendar.set(Calendar.YEAR, year);

        dateSet = calendar.getTime();
        String dateFormat = formatter.format(dateSet);
        return dateFormat;
    }


    public void openDatePicker(View v){
        datePickerDialog.show();
    }
}