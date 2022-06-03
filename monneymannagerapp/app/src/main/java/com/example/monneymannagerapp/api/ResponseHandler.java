package com.example.monneymannagerapp.api;

import retrofit2.Call;
import retrofit2.Response;

/**
 * Helper interface used to denote the kind of response handler is acceptable by the
 * ApiCallback
 * */
public interface ResponseHandler<T> {
    void method(T data);
}
