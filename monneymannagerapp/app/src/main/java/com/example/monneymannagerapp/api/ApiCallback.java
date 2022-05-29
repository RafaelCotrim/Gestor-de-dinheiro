package com.example.monneymannagerapp.api;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ApiCallback<T> implements Callback<T> {

    private final ResponseHandler<T> responseHandler;
    private final Context context;

    private static final String TAG = "API_CALL";

    public ApiCallback(Context context, ResponseHandler<T> responseHandler) {
        this.responseHandler = responseHandler;
        this.context = context;
    }

    @Override
    public void onResponse(@NonNull Call<T> call, @NonNull Response<T> response) {
        Log.v(TAG, String.format("API call to %s (Data=%b)", call.request().url().url(), response.body() != null));
        responseHandler.method(response.body());
        if(response.body() == null){
            Toast.makeText(context, "Não foi possível executar a operação", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onFailure(@NonNull Call<T> call, @NonNull Throwable t) {
        Log.w(TAG, String.format("Failed API call to %s (%s)", call.request().url().url(), t));
        responseHandler.method(null);
        Toast.makeText(context, "Não foi possível executar a operação", Toast.LENGTH_LONG).show();
    }
}
