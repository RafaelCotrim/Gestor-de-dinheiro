package com.example.monneymannagerapp.Fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.monneymannagerapp.R;
import com.example.monneymannagerapp.api.APIClient;
import com.example.monneymannagerapp.api.Api;
import com.example.monneymannagerapp.api.ApiCallback;

/**
 * Class: MainFragment
 * This class is where the layout of the DashboardActivity
 * is returned, where the side menu is associated.
 */
public class MainFragment extends Fragment {

    private SharedPreferences sharedPref;
    private Api api;

    private TextView dayAmount, weekAmount, monthAmount, yearAmount;
    private TextView username;
    private TextView totalAmount;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view =inflater.inflate(R.layout.main_fragment, container, false);

        api = APIClient.getApi();
        sharedPref = getContext().getSharedPreferences(getString(R.string.preference_file), Context.MODE_PRIVATE);

        dayAmount = view.findViewById(R.id.amount_spent_in_day);
        weekAmount = view.findViewById(R.id.amount_spent_in_week);
        monthAmount = view.findViewById(R.id.amount_spent_in_month);
        yearAmount = view.findViewById(R.id.amount_spent_in_year);
        username = view.findViewById(R.id.username);
        totalAmount = view.findViewById(R.id.total_amount_view);

        username.setText(sharedPref.getString(getString(R.string.user_name_preference), ""));

        loadData();

        return view;
    }

    private void loadData(){
        api.getUserDashboard(sharedPref.getLong(getString(R.string.user_id_preference), 0))
                .enqueue(new ApiCallback<>(getContext(), data -> {
                    if(data == null){
                        return;
                    }

                    dayAmount.setText(String.format("%s", data.dayExpenses));
                    weekAmount.setText(String.format("%s", data.weekExpenses));
                    monthAmount.setText(String.format("%s", data.monthExpenses));
                    yearAmount.setText(String.format("%s", data.yearExpenses));
                    totalAmount.setText(String.format("%s", data.total));
                }));
    }
}
