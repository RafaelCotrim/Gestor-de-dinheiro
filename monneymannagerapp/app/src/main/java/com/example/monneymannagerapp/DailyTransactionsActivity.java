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

import com.example.monneymannagerapp.api.APIClient;
import com.example.monneymannagerapp.api.Api;
import com.example.monneymannagerapp.api.dtos.TransactionDto;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DailyTransactionsActivity extends AppCompatActivity {

    private TextView totalAmountReceived;
    private TextView totalAmountSpent;
    private TextView totalAmount;

    private SharedPreferences sharedPref;
    private Api api;
    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");

    private List<TransactionDto> transactions;
    private double income = 0;
    private double expenses = 0;
    private Date date;

    private static final String DATE_EXTRA = "DATE";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daily_transactions);

        // Views
        TextView dateView = findViewById(R.id.transaction_date);
        totalAmountReceived = findViewById(R.id.total_amount_received);
        totalAmountSpent = findViewById(R.id.total_amount_spent);
        totalAmount = findViewById(R.id.total_amount);

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

        dateView.setText(formatter.format(date));

        api = APIClient.getApi();
        sharedPref = this.getSharedPreferences(getString(R.string.preference_file), Context.MODE_PRIVATE);

        checkLogin();
        buildTransactionsList();
    }

    public void buildTransactionsList(){

        ListView list = findViewById(R.id.transactions_list);

        api.getUserTransactions(sharedPref.getLong(getString(R.string.user_id_preference), 0), true, formatter.format(date)).enqueue(new Callback<List<TransactionDto>>() {

            @Override
            public void onResponse(Call<List<TransactionDto>> call, Response<List<TransactionDto>> response) {

                if(response.body() == null){
                    totalAmountReceived.setText("0 €");
                    totalAmountSpent.setText("0 €");
                    totalAmount.setText("0 €");
                    return;
                }

                ArrayList<String> values = new ArrayList<>();
                transactions = response.body();
                for (TransactionDto t: response.body()) {
                    values.add(t.value + "");

                    if(t.value > 0){
                        income += t.value;
                    } else {
                        expenses += t.value;
                    }
                }

                list.setAdapter(getAdapter(values));
                totalAmountReceived.setText(String.format("%s€", income));
                totalAmountSpent.setText(String.format("%s€", expenses));
                totalAmount.setText(String.format("%s€", income + expenses));
            }

            @Override
            public void onFailure(Call<List<TransactionDto>> call, Throwable t) {
                Log.v("TEST",t.toString());
            }
        });

        list.setOnItemClickListener((adapterView, view, position, id) -> {
            //Toast.makeText(getApplicationContext(), "Selected item" + transactions.get(position).toString(), Toast.LENGTH_SHORT).show();
        });
    }

    public void onClickPreviousTransactionButton(View v){
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.DATE, -1);

        Bundle b = new Bundle();
        b.putString(DATE_EXTRA, formatter.format(cal.getTime()));

        Intent intent = new Intent(this, DailyTransactionsActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        intent.putExtras(b);
        finish();
        startActivity(intent);
        overridePendingTransition(0,0);
    }

    public void onClickNextTransactionButton(View v){
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.DATE, 1);

        Bundle b = new Bundle();
        b.putString(DATE_EXTRA, formatter.format(cal.getTime()));

        Intent intent = new Intent(this, DailyTransactionsActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        intent.putExtras(b);
        finish();
        startActivity(intent);
        overridePendingTransition(0,0);
    }

    private ArrayAdapter<String> getAdapter(ArrayList<String> values){
        return new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, values);
    }

    private void checkLogin(){
        if(sharedPref.getLong(getString(R.string.user_id_preference), 0) == 0){
            startActivity(new Intent(this, MainActivity.class));
        }
    }

}