package com.example.monneymannagerapp;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TextView;

import com.example.monneymannagerapp.api.Api;
import com.example.monneymannagerapp.api.ApiCallback;
import com.example.monneymannagerapp.api.dtos.CategoryDto;
import com.example.monneymannagerapp.api.dtos.CategoryForCreate;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class TransactionActivity extends AppCompatActivity {

    private TextView transactionAmount;
    private TextView newCategoryName;

    private ArrayAdapter<String> categoryAdapter;
    private SharedPreferences sharedPref;
    private Api api;

    private List<CategoryDto> categories = new ArrayList<>();
    private DatePickerDialog datePickerDialog;
    private Button dateButton;
    private RadioButton debitButton, creditButton;
    private final SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd", Locale.UK);
    Date date;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaction);

        ListView categoryList = findViewById(R.id.categories_list_view);
        newCategoryName = findViewById(R.id.add_category);
        transactionAmount = findViewById(R.id.amount_input);
        creditButton = findViewById(R.id.credit_option);
        debitButton = findViewById(R.id.debit_option);

        initDatePicker();
        dateButton = findViewById(R.id.date_picker_button);
        dateButton.setText(getTodaysDate());

        loadCategories();
    }

    private String getTodaysDate(){
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);
        return makeDateString(day, month, year);
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

    public void updateTransaction(View v){
        //TODO
        //TODO
        //TODO
    }

    public void removeTransaction(View v){
        //TODO
        //TODO
        //TODO
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

        date = calendar.getTime();
        String dateFormat = formatter.format(date);
        return dateFormat;
    }


    public void openDatePicker(View v){
        datePickerDialog.show();
    }

}