package com.example.monneymannagerapp.api;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Class: ApiCallback<T>
 *
 *     Class made to simplify the process of receiving data from the API
 *     The callback logs basic information fo the request and shows an error
 *     message if necessary before calling the response handler.
 *
 *     If, for any reason, the request fails, null is passed to the response handler.
 */
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
        Log.v(TAG, String.format("%s %s (%s)", call.request().method(), call.request().url().url(), response.code()));
        responseHandler.method(response.body());

        if(!response.isSuccessful()){
            if(call.request().method().equals("GET")){
                Toast.makeText(context, "Não foi possível adquirir dados da API", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(context, "Não foi possível executar a operação", Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    public void onFailure(@NonNull Call<T> call, @NonNull Throwable t) {
        Log.v(TAG, String.format("%s %s (%s)", call.request().method(), call.request().url().url(), t.toString()));
        responseHandler.method(null);
        if(call.request().method().equals("GET")){
            Toast.makeText(context, "Não foi possível adquirir dados da API", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(context, "Não foi possível executar a operação", Toast.LENGTH_LONG).show();
        }
    }
}
