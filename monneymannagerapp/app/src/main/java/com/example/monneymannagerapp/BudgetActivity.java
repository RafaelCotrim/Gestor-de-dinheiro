package com.example.monneymannagerapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.monneymannagerapp.api.APIClient;
import com.example.monneymannagerapp.api.Api;
import com.example.monneymannagerapp.api.ApiCallback;
import com.example.monneymannagerapp.api.dtos.BudgetDto;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
/**
 * Class: BudgetActivity
 * This class is where the user can see your budgets.
 *
 * It returns a list with the defined budgets and the
 * information of each one, budget amount, spent amount
 * and category.
 * In each budget in the list it is also possible to
 * check if it has been exceeded when the image is red.
 *
 * On this page the user can choose to edit a budget
 * selecting it and can choose to add a new budget when
 * clicking on the button.
 */
public class BudgetActivity extends AppCompatActivity {

    private final SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd", Locale.US);

    private SharedPreferences sharedPref;
    private Api api;

    private TextView dateView;
    private ListView listView;
    private BudgetAdapter adapter;

    private final List<String> categoryValues = new ArrayList<>();
    private final List<Double> budgetValues = new ArrayList<>();
    private final List<Double> spentValues = new ArrayList<>();
    private final List<BudgetDto> budgets = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_budget);

        api = APIClient.getApi();
        sharedPref = this.getSharedPreferences(getString(R.string.preference_file), Context.MODE_PRIVATE);

        dateView = (TextView) findViewById(R.id.date_budget);
        listView = findViewById(R.id.budget_list);

        adapter = new BudgetAdapter(this, categoryValues, budgetValues, spentValues);
        listView.setAdapter(adapter);

        Calendar c = Calendar.getInstance();
        SimpleDateFormat format = new SimpleDateFormat("MMM, yyyy", Locale.US);
        String stringDate = format.format(c.getTime());
        dateView.setText(stringDate);

        listView.setOnItemClickListener((adapterView, view, i, l) -> {
            long id = budgets.get(i).id;
            Intent budgetManageActivity = new Intent(this, BudgetManageActivity.class);
            budgetManageActivity.putExtra(BudgetManageActivity.BUDGET_ID_EXTRA, id);
            startActivity(budgetManageActivity);
        });

        loadData();
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadData();
    }

    private void loadData(){
        Calendar cal = Calendar.getInstance();

        cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH));
        String end = formatter.format(cal.getTime());

        cal.set(Calendar.DAY_OF_MONTH, 1);
        String start = formatter.format(cal.getTime());

        api.getUserBudgets(sharedPref.getLong(getString(R.string.user_id_preference), 0), true, start, end)
                .enqueue(new ApiCallback<>(this, data -> {
                    categoryValues.clear();
                    budgetValues.clear();
                    spentValues.clear();
                    budgets.clear();

                    if(data == null){
                        return;
                    }

                    for (BudgetDto b: data) {
                        categoryValues.add(b.category.name);
                        budgetValues.add(b.value);
                        spentValues.add(b.spentValue);
                    }

                    budgets.addAll(data);
                    adapter.notifyDataSetChanged();
                }));
    }

    class BudgetAdapter extends ArrayAdapter<String>{

        Context context;
        List<String> auxCategory;
        List<Double> auxAmountBudget;
        List<Double> auxAmountSpent;

        BudgetAdapter(Context c, List<String> category, List<Double> amountBudget, List<Double> amountSpent){
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

            category.setText(auxCategory.get(position));
            amountBudget.setText(String.format("%s", auxAmountBudget.get(position)));
            amountSpent.setText(String.format("%s", auxAmountSpent.get(position)));

            if(auxAmountBudget.get(position) < auxAmountSpent.get(position)){
                images.setImageResource(R.drawable.ic_no_budget);
            } else{
                images.setImageResource(R.drawable.ic__budget);
            }

            return row;
        }
    }

    public void onAddBudget(View v){
        Intent addBudgetActivity = new Intent(this, AddBudgetActivity.class);
        startActivity(addBudgetActivity);
    }
}