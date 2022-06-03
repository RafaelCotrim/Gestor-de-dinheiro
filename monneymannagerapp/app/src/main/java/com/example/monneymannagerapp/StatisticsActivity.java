package com.example.monneymannagerapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.TextView;

import com.example.monneymannagerapp.api.APIClient;
import com.example.monneymannagerapp.api.Api;
import com.example.monneymannagerapp.api.ApiCallback;
import com.example.monneymannagerapp.api.dtos.StatisticsDto;

import org.eazegraph.lib.charts.PieChart;
import org.eazegraph.lib.models.PieModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Class: StatisticsActivity
 * This class is where the user can can see the statistics
 * of their expenses.
 *
 * In this activity, a PieChart is returned with the percentages
 * of amount spent by category.
 *
 * So, the user can check which are the main categories
 * with the highest expenses.
 */
public class StatisticsActivity extends AppCompatActivity {

    PieChart pieChart;

    List<TextView> values;
    List<TextView> displays;
    List<TextView> options;
    List<String> colors;

    private Api api;
    private SharedPreferences sharedPref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistics);

        api = APIClient.getApi();
        sharedPref = this.getSharedPreferences(getString(R.string.preference_file), Context.MODE_PRIVATE);

        colors = new ArrayList<>();

        colors.add("#FFA726");
        colors.add("#66BB6A");
        colors.add("#EF5350");
        colors.add("#29B6F6");

        values = new ArrayList<>();
        values.add(findViewById(R.id.value1));
        values.add(findViewById(R.id.value2));
        values.add(findViewById(R.id.value3));
        values.add(findViewById(R.id.value4));

        displays = new ArrayList<>();
        displays.add(findViewById(R.id.display1));
        displays.add(findViewById(R.id.display2));
        displays.add(findViewById(R.id.display3));
        displays.add(findViewById(R.id.display4));

        options = new ArrayList<>();
        options.add(findViewById(R.id.option1));
        options.add(findViewById(R.id.option2));
        options.add(findViewById(R.id.option3));
        options.add(findViewById(R.id.option4));

        pieChart = findViewById(R.id.piechart);
        loadData();
    }

    private void loadData(){
        api.getUserStatistics(sharedPref.getLong(getString(R.string.user_id_preference), 0))
                .enqueue(new ApiCallback<>(this, data -> {
                    if(data == null || data.size() == 0){
                        data = new ArrayList<>();
                    }

                    while (data.size() < 4){
                        data.add(new StatisticsDto());
                    }

                    double total = 0;

                    for (StatisticsDto s: data) {
                        total += s.value;
                    }
                    double otherTotal = 0;

                    for (int i = 0; i < data.size(); i++) {
                        if(i < 3){
                            options.get(i).setText(data.get(i).name);
                            displays.get(i).setText(data.get(i).name);
                            values.get(i).setText(String.format(Locale.US, "%.1f %%", (data.get(i).value * 100) / total));
                            pieChart.addPieSlice(
                                    new PieModel(data.get(i).name,
                                            (float) data.get(i).value,
                                            Color.parseColor(colors.get(i))
                                    )
                            );
                        } else {
                            otherTotal += data.get(i).value;
                        }
                    }

                    if(otherTotal > 0){
                        options.get(3).setText("Outros");
                        displays.get(3).setText("Outros");
                        values.get(3).setText(String.format(Locale.US,"%.1f %%", (otherTotal * 100) / total));
                        pieChart.addPieSlice(
                                new PieModel("Outros",
                                        (float) otherTotal,
                                        Color.parseColor(colors.get(3))
                                )
                        );
                    } else {
                        options.get(3).setText("-");
                        displays.get(3).setText("-");
                        values.get(3).setText(String.format(Locale.US,"%.1f %%", 0.0));
                    }

                    pieChart.startAnimation();
                }));
    }
}