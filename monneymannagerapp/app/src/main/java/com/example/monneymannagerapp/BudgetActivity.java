package com.example.monneymannagerapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class BudgetActivity extends AppCompatActivity {

    private TextView dateView;
    ListView listView;
    String category_array[] = {"Categoria 1", "Categoria 2", "Categoria 3", "Categoria 4"};
    String amoutBudget_array[] = {"200", "150", "70", "60"};
    String amoutSpent_array[] = {"120", "155", "60", "70"};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_budget);

        dateView = (TextView) findViewById(R.id.date_budget);
        listView = findViewById(R.id.budget_list);

        MyAdapter adapter = new MyAdapter(this,category_array, amoutBudget_array, amoutSpent_array);
        listView.setAdapter(adapter);

        Calendar c = Calendar.getInstance();
        SimpleDateFormat format = new SimpleDateFormat("MMM, yyyy");
        String stringDate = format.format(c.getTime());
        dateView.setText(stringDate);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Toast.makeText(BudgetActivity.this, "Click budget: "+ i, Toast.LENGTH_SHORT).show();
                sendToBudgetManageActivity();

            }
        });

    }

    class MyAdapter extends ArrayAdapter<String>{

        Context context;
        String[] auxCategory;
        String[] auxAmountBudget;
        String[] auxAmountSpent;

        MyAdapter (Context c, String[] category, String[] amountBudget, String[] amountSpent){
            super(c, R.layout.custom_row, R.id.category_title, category);
            this.context = c;
            this.auxCategory = category;
            this.auxAmountBudget = amountBudget;
            this.auxAmountSpent = amountSpent;

        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            LayoutInflater layoutInflater =(LayoutInflater) getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            @SuppressLint("ViewHolder") View row = layoutInflater.inflate(R.layout.custom_row, parent, false);
            ImageView images = row.findViewById(R.id.image);
            TextView category = row.findViewById(R.id.category_title);
            TextView amountBudget = row.findViewById(R.id.amount_budget);
            TextView amountSpent = row.findViewById(R.id.amount_spent);

            category.setText(auxCategory[position]);
            amountBudget.setText(auxAmountBudget[position]);
            amountSpent.setText(auxAmountSpent[position]);

            if( Integer.parseInt(auxAmountBudget[position]) < Integer.parseInt(auxAmountSpent[position])){
                images.setImageResource(R.drawable.ic_no_budget);
            } else{
                images.setImageResource(R.drawable.ic__budget);
            }

            return row;
        }
    }
    public void sendToBudgetManageActivity(){
        Intent budgetManageActivity = new Intent(this, BudgetManageActivity.class);
        startActivity(budgetManageActivity);
    }

    public void onAddBudget(View v){
        Intent addBudgetActivity = new Intent(this, AddBudgetActivity.class);
        startActivity(addBudgetActivity);
    }
}