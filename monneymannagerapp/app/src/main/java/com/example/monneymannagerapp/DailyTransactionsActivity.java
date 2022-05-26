package com.example.monneymannagerapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class DailyTransactionsActivity extends AppCompatActivity {

    private TextView date, totalAmountReceived, totalAmountSpent, totalAmount;
    private Calendar calendar;
    private SimpleDateFormat dateFormat;
    private String stringDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daily_transactions);

        date = (TextView) findViewById(R.id.transaction_date);
        totalAmountReceived = (TextView) findViewById(R.id.total_amount_received);
        totalAmountSpent = (TextView)  findViewById(R.id.total_amount_spent);
        totalAmount = (TextView)  findViewById(R.id.total_amount);

        calendar = Calendar.getInstance();
        dateFormat = new SimpleDateFormat("MM-dd-yyyy");
        stringDate = dateFormat.format(calendar.getTime());
        date.setText(stringDate);

        buildTransactionsList(stringDate);




    }

    public void buildTransactionsList(String date){
        // valor - categori
        ListView list = (ListView) findViewById(R.id.transactions_list);
        ArrayList<String> transactions = getTransactionsInfo();

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_2, transactions);
        list.setAdapter(arrayAdapter);

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                Toast.makeText(getApplicationContext(), "Item selecionado" + transactions.get(position).toString(), Toast.LENGTH_SHORT).show();
            }
        });

        //TODO soma dos recebidos e settar no TextView totalAmountReceived
        int totalAmountReceivedValue = 123; //TODO remove random value
        String totalAmountReceivedString = totalAmountReceivedValue + "€";
        totalAmountReceived.setText(totalAmountReceivedString);

        //TODO soma dos gastos e settar no TextView totalAmountSpent
        int totalAmountSpentValue = 30; //TODO remove random value
        String totalAmountSpentString = totalAmountSpentValue + "€";
        totalAmountSpent.setText(totalAmountSpentString);

        //TODO saber o total e settar no TextView totalAmount
        int totalAmountValue = 1230; //TODO remove random value
        String totalAmountString = totalAmountValue + "€";
        totalAmount.setText(totalAmountString);
    }

    private ArrayList<String> getTransactionsInfo(){
        ArrayList<String> values = new ArrayList<String>(); //transactions list
        values.add("Transaction1"); //TODO
        values.add("Transaction2"); //TODO
        values.add("Transaction3"); //TODO
        values.add("Transaction4"); //TODO
        values.add("Transaction5"); //TODO
        values.add("Transaction6"); //TODO
        values.add("Transaction7"); //TODO
        values.add("Transaction8"); //TODO
        values.add("Transaction1"); //TODO
        values.add("Transaction2"); //TODO
        values.add("Transaction3"); //TODO
        values.add("Transaction4"); //TODO
        values.add("Transaction5"); //TODO
        values.add("Transaction6"); //TODO
        values.add("Transaction7"); //TODO
        values.add("Transaction8"); //TODO
        values.add("Transaction1"); //TODO
        values.add("Transaction2"); //TODO
        values.add("Transaction3"); //TODO

        return values;
    }

    public void onClickPreviousTransactionButton(View v){
        //TODO Calcular data anterior e atualizar página com a lista correta
        //TODO newDate = X
        // TODO buildTransactionsList(String X)
    }

    public void onClickNextTransactionButton(View v){
        //TODO Calcular data seguinte e atualizar página com a lista correta
        //TODO newDate = X
        // TODO buildTransactionsList(String X)
    }

}