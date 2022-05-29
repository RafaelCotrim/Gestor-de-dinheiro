package com.example.monneymannagerapp;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.widget.TextView;

import org.eazegraph.lib.charts.PieChart;
import org.eazegraph.lib.models.PieModel;

public class StatisticsActivity extends AppCompatActivity {

    TextView option1, option2, option3, option4;
    TextView display1, display2, display3, display4;
    TextView value1, value2, value3, value4;
    PieChart pieChart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistics);

        // Link those objects with their respective
        // id's that we have given in .XML file
        value1 = findViewById(R.id.value1);
        value2 = findViewById(R.id.value2);
        value3 = findViewById(R.id.value3);
        value4 = findViewById(R.id.value4);
        option1 = findViewById(R.id.option1);
        option2 = findViewById(R.id.option2);
        option3 = findViewById(R.id.option3);
        option4 = findViewById(R.id.option4);
        display1 = findViewById(R.id.display1);
        display2 = findViewById(R.id.display2);
        display3 = findViewById(R.id.display3);
        display4 = findViewById(R.id.display4);
        pieChart = findViewById(R.id.piechart);
         setData();
    }

    private void setData(){
        //TODO - Verificar quais as 3 categorias com mais gastos e guardar o nome e gasto total por cada
        //TODO - Somar os gastos das restantes categorias
        //TODO - option 1, 2, 3 correspondem as categorias com mais gastos por ordem
        //TODO - (tal como display e value do num correspondente)
        //
        //set name category
        option1.setText("Categoria 1");
        display1.setText("Categoria 1");
        option2.setText("Categoria 2");
        display2.setText("Categoria 2");
        option3.setText("Categoria 3");
        display3.setText("Categoria 3");
        option4.setText("Outros");
        display4.setText("Outros");

        // Set the percentage of language used
        value1.setText(Integer.toString(40));
        value2.setText(Integer.toString(30));
        value3.setText(Integer.toString(25));
        value4.setText(Integer.toString(4));
        // Set the data and color to the pie chart
        pieChart.addPieSlice(
                new PieModel(
                        "R",
                        Integer.parseInt(value1.getText().toString()),
                        Color.parseColor("#FFA726")));
        pieChart.addPieSlice(
                new PieModel(
                        "Python",
                        Integer.parseInt(value2.getText().toString()),
                        Color.parseColor("#66BB6A")));
        pieChart.addPieSlice(
                new PieModel(
                        "C++",
                        Integer.parseInt(value3.getText().toString()),
                        Color.parseColor("#EF5350")));
        pieChart.addPieSlice(
                new PieModel(
                        "Java",
                        Integer.parseInt(value4.getText().toString()),
                        Color.parseColor("#29B6F6")));

        // To animate the pie chart
        pieChart.startAnimation();

    }
}