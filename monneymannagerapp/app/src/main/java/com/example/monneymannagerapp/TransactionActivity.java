package com.example.monneymannagerapp;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
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
import com.example.monneymannagerapp.api.dtos.TransactionForUpdate;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Class: TransactionActivity
 * This class is where the user can edit or remove a transaction.
 *
 * If the user changes the transaction amount, action, date, or
 * a category and click on save button this transaction will be
 * edited and the user will be  redirected to TransactionActivity.
 *
 * If the user click on remove button this transaction is removed and
 * the user is redirected to TransactionActivity.
 *
 * In this class it is also possible to add new categories to
 * associate with transactions.
 */
public class TransactionActivity extends AppCompatActivity {

    private TextView transactionAmount;
    private TextView newCategoryName;

    private ArrayAdapter<String> categoryAdapter;
    ListView categoryList;
    private SharedPreferences sharedPref;
    private Api api;

    private List<CategoryDto> categories = new ArrayList<>();
    private DatePickerDialog datePickerDialog;
    private Button dateButton;
    private RadioButton debitButton, creditButton;
    private final SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd", Locale.UK);

    private Long id;

    public static final String ID_EXTRA = "TRANSACTION_ID";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaction);

        api = APIClient.getApi();
        sharedPref = this.getSharedPreferences(getString(R.string.preference_file), Context.MODE_PRIVATE);

        categoryList = findViewById(R.id.categories_list_view);
        newCategoryName = findViewById(R.id.add_category);
        transactionAmount = findViewById(R.id.amount_input);
        creditButton = findViewById(R.id.credit_option);
        debitButton = findViewById(R.id.debit_option);

        initDatePicker();
        dateButton = findViewById(R.id.date_picker_button);
        dateButton.setText(formatter.format(new Date()));

        categoryAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_single_choice);
        categoryList.setAdapter(categoryAdapter);
        categoryList.setChoiceMode(ListView.CHOICE_MODE_SINGLE);

        loadCategories();

        parseId();
        loadData();
    }

    private void parseId(){
        id = getIntent().getExtras().getLong(ID_EXTRA);
    }

    protected void loadData(){
        api.getTransactionById(id, false, true)
                .enqueue(new ApiCallback<>(this, data -> {
                    if(data == null){
                        return;
                    }
                    transactionAmount.setText(String.format("%s", Math.abs(data.value)));
                    if(data.value >= 0){
                        creditButton.setChecked(true);
                    } else {
                        debitButton.setChecked(true);
                    }

                    dateButton.setText(formatter.format(data.date));

                    if(data.category == null){
                        return;
                    }

                    for (int i = 0; i < categories.size(); i++) {
                        CategoryDto c = categories.get(i);

                        if(c.id == data.category.id){
                            categoryList.setItemChecked(i, true);
                        }
                    }
                }));
    }

    public void onAddCategory(View v){

        if(newCategoryName.getText().toString().isEmpty()){
            Toast.makeText(this, "Não é possível criar uma trasação com nome vazio", Toast.LENGTH_LONG).show();
        }

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
        double value = Double.parseDouble(transactionAmount.getText().toString());
        String date = dateButton.getText().toString();

        if(debitButton.isChecked()){
            value *= -1;
        }

        int pos = categoryList.getCheckedItemPosition();

        long categoryId = 0;

        if(pos != -1){
            categoryId = categories.get(pos).id;
        }

        TransactionForUpdate tfu = new TransactionForUpdate(value, date, categoryId, sharedPref.getLong(getString(R.string.user_id_preference), 0));
        api.updateTransaction(id, tfu).enqueue(new ApiCallback<>(this, data ->{
            if(data == null){
                return;
            }

            finish();
        }));
    }

    public void removeTransaction(View v){
        api.deleteTransaction(id).enqueue(new ApiCallback<>(this, data -> finish()));
    }

    private void initDatePicker()
    {
        DatePickerDialog.OnDateSetListener dateSetListener = (datePicker, year, month, day) -> {
            String date = makeDateString(day, month, year);
            dateButton.setText(date);
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

        return formatter.format(calendar.getTime());
    }

    public void openDatePicker(View v){
        datePickerDialog.show();
    }

}