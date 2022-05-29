package com.example.monneymannagerapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.monneymannagerapp.api.APIClient;
import com.example.monneymannagerapp.api.Api;
import com.example.monneymannagerapp.api.dtos.TransactionDto;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DailyTransactionsActivity extends AppCompatActivity {

    private static final String DATE_EXTRA = "DATE";

    private TextView totalAmountReceived;
    private TextView totalAmountSpent;
    private TextView totalAmount;
    private ListView transaction_list;
    private TextView dateView;

    private SharedPreferences sharedPref;
    private Api api;
    private final SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
    private ArrayAdapter<String> adapter;

    private List<TransactionDto> transactions = new ArrayList<>();
    private Date date;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daily_transactions);

        // Views
        dateView = findViewById(R.id.transaction_date);
        totalAmountReceived = findViewById(R.id.total_amount_received);
        totalAmountSpent = findViewById(R.id.total_amount_spent);
        totalAmount = findViewById(R.id.total_amount);
        transaction_list = findViewById(R.id.transactions_list);

        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1);

        transaction_list.setAdapter(adapter);
        transaction_list.setOnItemClickListener((adapterView, view, position, id) -> {
            //Toast.makeText(getApplicationContext(), "Selected item" + transactions.get(position).toString(), Toast.LENGTH_SHORT).show();
        });

        api = APIClient.getApi();
        sharedPref = this.getSharedPreferences(getString(R.string.preference_file), Context.MODE_PRIVATE);

        checkLogin();
        parseDate();
        loadData();
    }

    @Override
    protected void onResume() {
        super.onResume();
        reload();
    }

    public void onClickPreviousTransactionButton(View v){
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.DATE, -1);

        date = cal.getTime();
        reload();
    }

    public void onClickNextTransactionButton(View v){
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.DATE, 1);

        date = cal.getTime();
        reload();
    }

    public void onClickAddTransaction(View v){
        Intent addTransactionActivity = new Intent(this, AddTransactionActivity.class);
        startActivity(addTransactionActivity);
    }

    private void reload(){
        loadData();
        writeData();
    }

    private void parseDate(){
        Bundle b = getIntent().getExtras();
        if(b != null){
            try {
                date = formatter.parse(b.getString(DATE_EXTRA));
            } catch (ParseException e) {
                date = Calendar.getInstance().getTime();
            }
        } else {
            date = Calendar.getInstance().getTime();
        }
    }

    private void loadData(){
        Context c = this;
        api.getUserTransactions(sharedPref.getLong(getString(R.string.user_id_preference), 0), true, formatter.format(date)).enqueue(new Callback<List<TransactionDto>>() {

            @Override
            public void onResponse(Call<List<TransactionDto>> call, Response<List<TransactionDto>> response) {

                if (response.body() == null) {
                    transactions = new ArrayList<>();
                } else {
                    transactions = response.body();
                }
                writeData();
            }

            @Override
            public void onFailure(Call<List<TransactionDto>> call, Throwable t) {
                transactions = new ArrayList<>();
                Toast.makeText(c, "Não foi possível conectar à API", Toast.LENGTH_LONG).show();
                writeData();
            }
        });


    }

    private void writeData(){

        dateView.setText(formatter.format(date));

        adapter.clear();

        double credit = 0;
        double debit = 0;
        for (TransactionDto t: transactions) {
            adapter.add("" +t.value);
            if(t.value >= 0){
                credit += t.value;
            } else {
                debit += t.value;
            }
        }

        adapter.notifyDataSetChanged();

        totalAmountReceived.setText(String.format("%s€", credit));
        totalAmountSpent.setText(String.format("%s€", debit));
        totalAmount.setText(String.format("%s€", credit + debit));
    }

    private void checkLogin(){
        if(sharedPref.getLong(getString(R.string.user_id_preference), 0) == 0){
            startActivity(new Intent(this, MainActivity.class));
        }
    }
}