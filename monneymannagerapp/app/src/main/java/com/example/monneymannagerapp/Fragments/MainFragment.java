package com.example.monneymannagerapp.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.monneymannagerapp.R;
//Dashboard
public class MainFragment extends Fragment {

    private TextView dayAmount, weekAmount, monthAmount, yearAmount;
    private TextView username;
    private TextView totalAmount;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view =inflater.inflate(R.layout.main_fragment, container, false);

        dayAmount = view.findViewById(R.id.amount_spent_in_day);
        weekAmount = view.findViewById(R.id.amount_spent_in_week);
        monthAmount = view.findViewById(R.id.amount_spent_in_month);
        yearAmount = view.findViewById(R.id.amount_spent_in_year);
        username = view.findViewById(R.id.username);
        totalAmount = view.findViewById(R.id.total_amount_view);

        //TODO - procurar os dados na API
        dayAmount.setText("30");    //TODO dado fake - remover
        weekAmount.setText("130");  //TODO dado fake - remover
        monthAmount.setText("400"); //TODO dado fake - remover
        yearAmount.setText("5000"); //TODO dado fake - remover
        username.setText("Mariana");//TODO dado fake - remover
        totalAmount.setText("1300");//TODO dado fake - remover

        return view;
    }
}
