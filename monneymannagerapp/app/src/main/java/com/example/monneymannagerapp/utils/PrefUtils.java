package com.example.monneymannagerapp.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.monneymannagerapp.R;

public class PrefUtils {

    public static SharedPreferences getPreferences(Context context){
        return context.getSharedPreferences(context.getString(R.string.preference_file), Context.MODE_PRIVATE);
    }


}
