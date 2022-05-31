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
        Log.v(TAG, String.format("%s %s (Sucess=%b)", call.request().method(), call.request().url().url(), response.isSuccessful()));
        responseHandler.method(response.body());
        if(!response.isSuccessful()){
            Toast.makeText(context, "Não foi possível executar a operação", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onFailure(@NonNull Call<T> call, @NonNull Throwable t) {
        Log.v(TAG, String.format("%s %s (Error)", call.request().method(), call.request().url().url()));
        responseHandler.method(null);
        Toast.makeText(context, "Não foi possível executar a operação", Toast.LENGTH_LONG).show();
    }
}
