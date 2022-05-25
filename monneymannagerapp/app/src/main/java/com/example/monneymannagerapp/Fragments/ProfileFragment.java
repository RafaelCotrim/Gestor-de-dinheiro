package com.example.monneymannagerapp.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.monneymannagerapp.LoginActivity;
import com.example.monneymannagerapp.R;
import com.example.monneymannagerapp.RegisterActivity;

public class ProfileFragment extends Fragment {

    private TextView nameEditInput, emailEditInput, passEditInput, passConfEditInput;
    private TextView editButton;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view =inflater.inflate(R.layout.profile_fragment, container, false);

        nameEditInput = (EditText) view.findViewById(R.id.name_edit_input);
        emailEditInput = (EditText) view.findViewById(R.id.email_edit_input);
        passEditInput = (EditText) view.findViewById(R.id.pass_edit_input);
        passConfEditInput = (EditText) view.findViewById(R.id.pass_conf_edit_input);

        //edit button
        editButton = (Button) view.findViewById(R.id.edit_button);
        editButton.setOnClickListener((View.OnClickListener) this);

//        //fill data profile
//        String dataProfile = "{API_REQUEST}";  //JSON
//        nameEditInput.setText(dataProfile.name);
//        emailEditInput.setText(dataProfile.email);

        nameEditInput.setText("Nome pesquisado na API");
        emailEditInput.setText("Email pesquisado na API");

        return view;
    }


}
