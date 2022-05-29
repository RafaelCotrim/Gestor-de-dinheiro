package com.example.monneymannagerapp.api;

import retrofit2.Call;
import retrofit2.Response;

public interface ResponseHandler<T> {
    void method(T data);
}
